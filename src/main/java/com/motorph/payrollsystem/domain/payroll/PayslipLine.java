/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.payroll;

/**
 *
 * @author djjus
 */
public class PayslipLine {
    
    public enum LineType {
        EARNING,
        DEDUCTION
    }
    
    private final String label;
    private final double amount;
    private LineType type;
    
    public PayslipLine(String label, double amount, LineType type) {
        this.label = label;
        this.amount = amount;
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public double getAmount() {
        return amount;
    }

    public LineType getType() {
        return type;
    }
    
    
    
}
