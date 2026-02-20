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
    public double compute(Employee employee, double grossPayPeriod) {
        double monthlyBasic = getMonthlyBasic(employee);
        
        //NOT OFFICIAL COMPUTATION
        double monthlyContribution = monthlyBasic * 0.02;
        return round2(monthlyContribution);
    }
}
