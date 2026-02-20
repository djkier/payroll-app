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
public class PhilHealthRule extends AbstractDeductionRule{
    /*
    *reference for computation
    *https://www.tripleiconsulting.com/philhealth-contribution-table-2025-new-rate-how-compute-contributions/
    *Date: June 23, 2025
    *
    */
    @Override
    public String getName() {
        return "PhilHealth";
    }
    
    //Employee and employer will share evenly on the contribution
    @Override
    public double computeMonthly(double grossPay) {
        double monthlyContribution;
        
        if (grossPay >= 100000) {
            monthlyContribution = 2500.0;
        } else if (grossPay > 10000) {
            monthlyContribution = grossPay * 0.05 / 2;
        } else if (grossPay > 0) {
            monthlyContribution = 250.0;
        } else {
            monthlyContribution  = 0;
        }
        
        return monthlyContribution;
    }
    

}
