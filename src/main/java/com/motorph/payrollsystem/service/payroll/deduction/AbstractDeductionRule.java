/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service.payroll.deduction;

import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.model.payslip.PayrollPeriod;

/**
 *
 * @author djjus
 */
public abstract class AbstractDeductionRule implements DeductionRule {

    @Override
    public double computeSemi(double semiPay) {
        double estimatedMonthly = semiPay * 2.0;
        return computeMonthly(estimatedMonthly) / 2.0;
    }
    
    @Override
    public Basis getBasis() {
        return Basis.GROSS_PAY;
    }

}
