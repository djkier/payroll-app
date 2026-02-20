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
    
    //SSS, PhilHealth, and Pagibig use the gross pay as the basis of computation
    //withhodling tax use Taxable income  = gross - sss - philhealth - pagibig as that basis of computation
    enum Basis {
        GROSS_PAY, TAXABLE_INCOME
    }
    
    //deduction name
    String getName();
    
    Basis getBasis();
    
    //compute() compute deduction for this payroll period
    //grossPayPeriod is the gross pay for the selected cutoff
    double computeMonthly(double grossPay);
    double computeSemi(double grossPay);

    
}
