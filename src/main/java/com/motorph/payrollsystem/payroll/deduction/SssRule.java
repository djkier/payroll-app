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
    public double compute(Employee employee, double grossPayPeriod) {
        //placeholder
        double monthlyBasic = getMonthlyBasic(employee);
        
        
        //NOT OFFICIAL COMPUTATION
        double monthlyContribution = grossPayPeriod * 0.045;
        return round2(monthlyContribution/2.0);
    }
}
