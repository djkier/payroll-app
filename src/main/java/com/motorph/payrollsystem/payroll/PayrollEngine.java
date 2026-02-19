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
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author djjus
 */
public class PayrollEngine {
    
    private final List<DeductionRule> deductionRules;
    
    public PayrollEngine() {
        this.deductionRules = List.of(
                new SssRule(), 
                new PhilHealthRule(), 
                new PagibigRule());
    }
    
    public Payslip computePayslip (Employee employee, List<AttendanceRecord> records, PayrollPeriod period) {
        //get total hour
        double totalHours = computeTotalHours(records, period);
        
        //get gross pay 
        double grossPay = computeGrossPay(employee, totalHours);
        
        Payslip payslip = new Payslip(employee, period);
        payslip.setTotalHours(totalHours);
        
        //Payslip EARNING
        payslip.addLine(new PayslipLine("Basic", grossPay, PayslipLine.LineType.EARNING));
        
        //Payslip govt DEDUCTION (tax excluded)
        double govTotal = 0;
        for (DeductionRule rule : deductionRules) {
            double deduction = rule.compute(employee, grossPay);
            govTotal += deduction;
            
            payslip.addLine(new PayslipLine(rule.getName(),
                                            deduction,
                                            PayslipLine.LineType.DEDUCTION));
        }
        
        //withholding tax
        double taxablePay = grossPay - govTotal;
        DeductionRule taxRule = new WithholdingTaxRule();
        double tax = taxRule.compute(employee, taxablePay);
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
    
    private double computeGrossPay(Employee employee, double totalHours) {
        double hourlyRate = employee.getCompProfile().getHourlyRate();
        return hourlyRate * totalHours;
    }
}
