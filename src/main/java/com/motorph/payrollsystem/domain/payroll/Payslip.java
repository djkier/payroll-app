/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.payroll;

import com.motorph.payrollsystem.domain.employee.Employee;
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
    
    public double getSssAmount() {
        return getDeductionAmount("SSS");
    }
    
    public double getPhilHealthAmount() {
        return getDeductionAmount("PhilHealth");
    }
    
    public double getPagibigAmount() {
        return getDeductionAmount("Pag-IBIG");
    }
    
    public double getTaxAmount() {
        return getDeductionAmount("Withholding Tax");
    }
    
    private double getDeductionAmount(String label) {
        for (PayslipLine line : lines) {
            if (line.getType() == PayslipLine.LineType.DEDUCTION &&
                    line.getLabel().equalsIgnoreCase(label)) {
                return line.getAmount();
            }
        }
        return 0;
    }
    
    
}
