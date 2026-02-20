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
    
    @Override
    public String getName() {
        return "Pag-IBIG";
    }
    
    @Override
    public double computeMonthly(double grossPay) {

        //NOT OFFICIAL COMPUTATION
        double monthlyContribution = grossPay* 0.02;
        return monthlyContribution;
    }
    
    
  
}
