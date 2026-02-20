/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.payroll;

import com.motorph.payrollsystem.domain.employee.Employee;
import com.motorph.payrollsystem.payroll.deduction.DeductionRule;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djjus
 */
public class Payslip {
    private final Employee employee;
    private final PayrollPeriod period;
    
    private double totalHours;
    private double grossPay;
    private double totalDeductions;
    private double netPay;
    
    private final List<PayslipLine> lines = new ArrayList<>();
    
    public Payslip(Employee employee, PayrollPeriod period) {
        this.employee = employee;
        this.period = period;
    }

    public Employee getEmployee() {
        return employee;
    }

    public PayrollPeriod getPeriod() {
        return period;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public double getNetPay() {
        return netPay;
    }

    public List<PayslipLine> getLines() {
        return new ArrayList<>(lines);
    }
    
    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }
    
    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }
    
    public void addLine(PayslipLine line) {
        lines.add(line);
    }
    
    public void computeTotals(){
        double earnings = 0;
        double deductions = 0;
        
        for (PayslipLine line : lines) {
            if (line.getType() == PayslipLine.LineType.EARNING) {
                earnings += line.getAmount();
            } else {
                deductions += line.getAmount();
            }
        }
        
        this.grossPay = earnings;
        this.totalDeductions = deductions;
        this.netPay = earnings - deductions;
    }
    //Earnings getters
    public double getBasicPayAmount() {
        return getOtherAmount("Basic", PayslipLine.LineType.EARNING);
    }
    public double getRiceAmount() {
        return getOtherAmount("Rice Subsidy", PayslipLine.LineType.EARNING);
    }
    
    public double getPhoneAmount() {
        return getOtherAmount("Phone Allowance", PayslipLine.LineType.EARNING);
    }
    
    public double getClothingAmount() {
        return getOtherAmount("Clothing Allowance", PayslipLine.LineType.EARNING);
    }
    
    
    //Deductions getters
    public double getSssAmount() {
        return getOtherAmount("SSS", PayslipLine.LineType.DEDUCTION);
    }
    
    public double getPhilHealthAmount() {
        return getOtherAmount("PhilHealth", PayslipLine.LineType.DEDUCTION);
    }
    
    public double getPagibigAmount() {
        return getOtherAmount("Pag-IBIG", PayslipLine.LineType.DEDUCTION);
    }
    
    public double getTaxAmount() {
        return getOtherAmount("Withholding Tax", PayslipLine.LineType.DEDUCTION);
    }
    
    //Get totals
    public double getGovtDeduction() {
        return getSssAmount() + getPhilHealthAmount() + getPagibigAmount();
    }
    
    public double getTaxableIncome() {
//        double total = getGrossPay() - govtDeduction();
//        System.out.println(getGrossPay());
//        System.out.println(govtDeduction());
        return getGrossPay() - getGovtDeduction();
    }
    
    public double getDeductionAmount(DeductionRule rule) {
        return getOtherAmount(rule.getName(), PayslipLine.LineType.DEDUCTION);
    }
    
    private double getOtherAmount(String label, PayslipLine.LineType type) {
        for (PayslipLine line : lines) {
            if (line.getType() == type &&
                    line.getLabel().equalsIgnoreCase(label)) {
                return line.getAmount();
            }
        }
        return 0;
    }
    
    
    
    
}
