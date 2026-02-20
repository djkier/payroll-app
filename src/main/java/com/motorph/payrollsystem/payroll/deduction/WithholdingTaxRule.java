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
    *
    *semi monthly tax table
    *https://www.payrollsolutions.ph/resources/withholding_tax
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
    public double computeSemi(double taxablePay) {
        double tax;
        
        if (taxablePay > 333333) {
            tax = 91770.70 + ((taxablePay - 333333) * 0.35);
        } else if (taxablePay > 83333) {
            tax = 16770.70 + ((taxablePay - 83333) * 0.3);
        } else if (taxablePay > 33333) {
            tax = 4270.70 + ((taxablePay - 33333) * 0.25);
        } else if (taxablePay > 16667) {
            tax = 937.50 + ((taxablePay - 16667) * 0.20);
        } else if (taxablePay > 10417) {
            tax = (taxablePay - 10417) * 0.15;
        } else {
            tax = 0;
        }
        
        return tax;
    }
}
