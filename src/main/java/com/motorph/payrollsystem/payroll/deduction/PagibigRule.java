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
public class PagibigRule extends AbstractDeductionRule {
    /*
    *reference for computation
    *https://www.tripleiconsulting.com/pag-ibig-contribution-table-2025-how-compute-for-your-employees/
    *Date: June 25, 2025
    *
    */
    @Override
    public String getName() {
        return "Pag-IBIG";
    }
    
    
    
    @Override
    public double computeMonthly(double grossPay) {
   
        double monthlyContribution;
        if (grossPay >= 10000) {
            monthlyContribution = 200;
        } else if (grossPay > 1500) {
            monthlyContribution = grossPay * 0.02;
        } else if (grossPay > 0) {
            monthlyContribution = grossPay * 0.01;
        } else {
            monthlyContribution = 0;
        }

        return monthlyContribution;
    }
    
    
    
    
  
}
