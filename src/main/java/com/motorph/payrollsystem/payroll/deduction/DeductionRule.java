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
public interface DeductionRule {
    //deduction name
    String getName();
    
    //compute() compute deduction for this payroll period
    //grossPayPeriod is the gross pay for the selected cutoff
    double computeMonthly(double grossPay);
    double computeSemi(double grossPay);

    
}
