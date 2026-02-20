/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.payroll;

import com.motorph.payrollsystem.domain.attendance.AttendanceRecord;
import com.motorph.payrollsystem.domain.employee.Employee;
import com.motorph.payrollsystem.domain.payroll.PayrollPeriod;
import com.motorph.payrollsystem.domain.payroll.Payslip;
import com.motorph.payrollsystem.domain.payroll.PayslipLine;
import com.motorph.payrollsystem.payroll.deduction.DeductionRule;
import com.motorph.payrollsystem.payroll.deduction.PagibigRule;
import com.motorph.payrollsystem.payroll.deduction.PhilHealthRule;
import com.motorph.payrollsystem.payroll.deduction.SssRule;
import com.motorph.payrollsystem.payroll.deduction.WithholdingTaxRule;
import com.motorph.payrollsystem.utility.PayrollPeriodFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djjus
 */
public class PayrollEngine {
    
    private final List<DeductionRule> govtRules;
    private final DeductionRule taxRule;
    
    public PayrollEngine() {
        this.govtRules = List.of(
                new SssRule(), 
                new PhilHealthRule(), 
                new PagibigRule());
        this.taxRule = new WithholdingTaxRule();
    }
    
    //payroll engine should not store the employee, records or period 
    //its function is to generate payslip base on the period
    //IT SHOULD BE STATELESS ALWAYS
    //except storing deduction rules since deduction rules is universal for every payslip
    public Payslip computePayslip (Employee employee, List<AttendanceRecord> records, PayrollPeriod period) {
        //Generate payslip and add Earnings
        Payslip payslip = createPayslipWithEarnings(employee, records, period);
        
        //Add govt deductions
        addDeductions(payslip, employee, records, govtRules);
        
        //Add tax
        addDeductions(payslip, employee, records, List.of(taxRule));
        
        payslip.computeTotals();
        return payslip;
    }
    
    private Payslip createPayslipWithEarnings(Employee employee, List<AttendanceRecord> records, PayrollPeriod period) {
        double totalHours = computeTotalHours(records, period);
        
        Payslip payslip = new Payslip(employee, period);
        payslip.setTotalHours(totalHours);
        
        //Payslip EARNINGS
        double basicPay = employee.getCompProfile().getHourlyRate() * totalHours;
        double riceSubsidy = computeAllowance(employee.getCompProfile().getRiceSubsidy(), period);
        double phoneAllowance = computeAllowance(employee.getCompProfile().getPhoneAllowance(), period);
        double clothingAllowance = computeAllowance(employee.getCompProfile().getClothingAllowance(), period);
        //add payslip line for EARNINGS
        addEarningPayslipLine(payslip, "Basic", basicPay);
        addEarningPayslipLine(payslip, "Rice Subsidy", riceSubsidy);
        addEarningPayslipLine(payslip, "Phone Allowance", phoneAllowance);
        addEarningPayslipLine(payslip, "Clothing Allowance", clothingAllowance);
        
        //Refresh Payslip attrib gross pay
        payslip.computeTotals();

        return payslip;
    }
    
    private void addEarningPayslipLine(Payslip payslip, String name, double amount) {
        if (amount >= 0) {
            payslip.addLine(new PayslipLine(name, amount, PayslipLine.LineType.EARNING));
        }
    }
    
    private void addDeductions(
            Payslip payslip, 
            Employee employee,
            List<AttendanceRecord> records, 
            List<DeductionRule> rules) {
        
        for (DeductionRule rule : rules) {
            double amount = computeDeduction(rule, payslip, employee, records);
            payslip.addLine(new PayslipLine(rule.getName(), amount, PayslipLine.LineType.DEDUCTION));
            payslip.computeTotals();
        }
    }
    
    private double computeDeduction(DeductionRule rule, Payslip currentPayslip, Employee employee, List<AttendanceRecord> records) {
        double currentBase = baseAmount(rule, currentPayslip);
        
        //Check if the payroll period is monthly basis
        if (currentPayslip.getPeriod().isMonthly()) {
            return rule.computeMonthly(currentBase);
        }
        
        //Check if the payroll is for the first cut off
        if (currentPayslip.getPeriod().isFirstCutoff()) {
            return rule.computeSemi(currentBase);
        }
        
        //Second cutoff deduction = fullmonth deduction - first cutoff deduction
        //get the firstperiod for this cutoff
        PayrollPeriod firstPeriod = PayrollPeriodFactory.firstCutoffOf(currentPayslip.getPeriod().getMonthYear());
        //compute the first payslip gross for the first cutoff
        Payslip firstPayslip = computePayslip(employee, records, firstPeriod);
        
        //compute the govt deduction base on the firstPayslip earning
        double firstBase = baseAmount(rule, firstPayslip);
        double firstDeduction = rule.computeSemi(firstBase);
        
        //compute the govt deduction base on the full month earning 
        double fullMonthBase = currentBase + firstBase;
        double fullMonthDeduction = rule.computeMonthly(fullMonthBase);
        
        return fullMonthDeduction - firstDeduction;
    }
    
    private double baseAmount(DeductionRule rule, Payslip payslip) {
        return (rule.getBasis() == DeductionRule.Basis.TAXABLE_INCOME) ?
                payslip.getTaxableIncome() :
                payslip.getGrossPay();
    }
    
    private double computeTotalHours(List<AttendanceRecord> records, PayrollPeriod period) {
        double total = 0;
        
        for (AttendanceRecord record : records) {
            LocalDate date = record.getDate();
            
            boolean inRange = 
               !date.isBefore(period.getStartDate()) &&
               !date.isAfter(period.getEndDate());
            
            if (!inRange) continue;
            
            total += record.getHoursWorked();
        }
        
        return total;
    }
    
    private double computeAllowance(double monthlyAmount, PayrollPeriod period) {
        return period.getPeriodType() == PayrollPeriod.PeriodType.SEMI_MONTHLY ?
                monthlyAmount/2.0 :
                monthlyAmount;
    }
    
}
