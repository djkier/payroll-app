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
public class SssRule extends AbstractDeductionRule {
    
    @Override
    public String getName() {
        return "SSS";
    }
    
    @Override
    public double computeMonthly(double grossPayPeriod) {
        //placeholder
        //NOT OFFICIAL COMPUTATION
        double monthlyContribution = grossPayPeriod* 0.02;
        return monthlyContribution;
    }
    
}
