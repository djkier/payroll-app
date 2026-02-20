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
    /*
    *reference table
    *https://www.sss.gov.ph/wp-content/uploads/2024/12/Cir-2024-006-Employers-scaled.jpg
    *Date: January 2025
    *
    *computation is base on the mathematical behavior of the range table
    *
    */
    @Override
    public String getName() {
        return "SSS";
    }
    
    @Override
    public double computeMonthly(double grossPay) {
        double monthlyContribution = 0;
        
        if (grossPay >= 34750) {
            monthlyContribution = 1750;
        } else if (grossPay >= 5250) {
            double base = (grossPay - 5250) / 500.0;
            monthlyContribution = Math.floor(base + 1) * 25 + 250;
        } else if (grossPay > 0) {
            monthlyContribution = 250;
        } else {
            monthlyContribution = 0;
        }
        
        
        return monthlyContribution;
    }
    
}
