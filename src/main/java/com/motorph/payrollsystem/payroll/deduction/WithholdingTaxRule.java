/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.payroll.deduction;

import com.motorph.payrollsystem.domain.employee.Employee;

/**
 *
 * @author djjus
 */
public class WithholdingTaxRule extends AbstractDeductionRule {
    
    @Override
    public String getName() {
        return "Withholding Tax";
    }
    
    @Override
    public double compute(Employee employee, double taxablePayPeriod) {
        double tax;
        
        if (taxablePayPeriod <= 10000) tax = 0;
        else if (taxablePayPeriod <= 20000) tax = (taxablePayPeriod - 10000) *0.10;
        else tax = 1000 + (taxablePayPeriod - 20000) * 0.15;
        
        return round2(Math.max(0, tax));
    }
}
