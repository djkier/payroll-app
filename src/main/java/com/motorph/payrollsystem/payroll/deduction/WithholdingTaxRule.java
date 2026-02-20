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
public class WithholdingTaxRule extends AbstractDeductionRule {
    /*
    *reference for computation
    *https://sprout.ph/articles/how-to-calculate-withholding-tax-philippines/
    *Date: January 1, 2023
    *TRAIN law
    */
    @Override
    public String getName() {
        return "Withholding Tax";
    }
    
    @Override
    public double computeMonthly(double taxablePay) {
        double tax;
        
        if (taxablePay > 666667) {
            tax = 183541.80 + ((taxablePay - 666667) * 0.35);
        } else if (taxablePay > 166667) {
            tax = 33541.80 + ((taxablePay - 166667) * 0.3);
        } else if (taxablePay > 66667) {
            tax = 8541.80 + ((taxablePay - 66667) * 0.25);
        } else if (taxablePay > 33333) {
            tax = 1875 + ((taxablePay - 33333) * 0.2);
        } else if (taxablePay > 20833) {
            tax = (taxablePay - 20833) * 0.15;
        } else {
            tax = 0;
        }
        
        return tax;
    }
    
    @Override
    public double computeSemi(double grossPay) {
        return 0;
    }
}
