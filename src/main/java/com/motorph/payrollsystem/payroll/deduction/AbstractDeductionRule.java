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
public abstract class AbstractDeductionRule implements DeductionRule {
    protected double getMonthlyBasic(Employee employee) {
        return employee.getCompProfile().getBasicSalary();
    }
    
    protected double getTotalAllowanceesPerPeriod(Employee employee) {
        return employee.getCompProfile().getRiceSubsidy() +
                employee.getCompProfile().getPhoneAllowance() +
                employee.getCompProfile().getClothingAllowance();
    }
    
    protected double round2(double x) {
        return Math.round(x*100.0) / 100.0;
    }
}
