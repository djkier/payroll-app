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
import java.util.List;

/**
 *
 * @author djjus
 */
public class PayrollEngine {
    
    private final List<DeductionRule> deductionRules;
    private Employee employee;
    private List<AttendanceRecord> records;
    
    public PayrollEngine() {
        this.deductionRules = List.of(
                new SssRule(), 
                new PhilHealthRule(), 
                new PagibigRule());
    }
    
    //payroll engine should not store the employee, records or period its function is to compute the payslip
    //excluding the deduction since deduction rules is universal for every payslip
    public Payslip computePayslip (Employee employee, List<AttendanceRecord> records, PayrollPeriod period) {
        this.employee = employee;
        this.records = records;
        //get total hour
        double totalHours = computeTotalHours(records, period);
        Payslip payslip = new Payslip(employee, period);
        payslip.setTotalHours(totalHours);
        
        //Payslip EARNINGS
        //basic pay = totalHours * employee hourly rate
        double basicPay = computeBasicPay(employee, totalHours);
        double riceSubsidy = computeAllowance(employee.getCompProfile().getRiceSubsidy(), period);
        double phoneAllowance = computeAllowance(employee.getCompProfile().getPhoneAllowance(), period);
        double clothingAllowance = computeAllowance(employee.getCompProfile().getClothingAllowance(), period);
        //add payslip line for EARNINGS
        addEarningPayslipLine(payslip, "Basic", basicPay);
        addEarningPayslipLine(payslip, "Rice Subsidy", riceSubsidy);
        addEarningPayslipLine(payslip, "Phone Allowance", phoneAllowance);
        addEarningPayslipLine(payslip, "Clothing Allowance", clothingAllowance);
        
        
        //Payslip govt DEDUCTION (tax excluded)
        for (DeductionRule rule : deductionRules) {
            //change rule.compute
            double deduction = computeDeduction(rule, payslip);
            payslip.addLine(new PayslipLine(rule.getName(),
                                            deduction,
                                            PayslipLine.LineType.DEDUCTION));
        }
        
        //withholding tax
        DeductionRule taxRule = new WithholdingTaxRule();
        double tax = taxRule.computeMonthly(payslip.getTaxableIncome());
        payslip.addLine(new PayslipLine(taxRule.getName(),
                                        tax,
                                        PayslipLine.LineType.DEDUCTION));
           
        payslip.computeTotals();
        
        return payslip;
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
    
    private double computeBasicPay(Employee employee, double totalHours) {
        double hourlyRate = employee.getCompProfile().getHourlyRate();
        return hourlyRate * totalHours;
    }
    
    private double computeAllowance(double amount, PayrollPeriod period) {
        double divisor = 1.0;
        if (period.getPeriodType() == PayrollPeriod.PeriodType.SEMI_MONTHLY) {
            divisor = 2.0;
        }
        
        return amount / divisor;
    }
    
    private double computeDeduction(DeductionRule rule, Payslip payslip) {
        //monthly
        if (payslip.getPeriod().isMonthly()) {
            return rule.computeMonthly(payslip.getGrossPay());
        }
        
        //semi monthly
        //first cut off
        if (payslip.getPeriod().isFirstCutoff()) {
            return rule.computeSemi(payslip.getGrossPay());
        }
        
        //second cutoff
        PayrollPeriod firstCutoffPeriod = PayrollPeriodFactory.firstCutoffOf(payslip.getPeriod().getMonthYear());
        Payslip firstCutOffPayslip = computePayslip(employee, records, firstCutoffPeriod);
        double fullMonthGrossPay = payslip.getGrossPay() + firstCutOffPayslip.getGrossPay();
        double fullMonthDeduction = rule.computeMonthly(fullMonthGrossPay);
        double secondCutoffDeduction = fullMonthDeduction - firstCutOffPayslip.getDeductionAmount(rule);

        return secondCutoffDeduction;
    }
    
    private void addEarningPayslipLine(Payslip payslip, String name, double amount) {
        if (amount >= 0) {
            payslip.addLine(new PayslipLine(name, amount, PayslipLine.LineType.EARNING));
        }
    }
}
