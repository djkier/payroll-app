/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.payroll.deduction;

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
        
        
    }
}
