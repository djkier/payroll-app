/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *
 * @author djjus
 */
public class Money {
    public static double parseSalary(String value) {
        String cleaned = value.replace(",", "");
        return Double.parseDouble(cleaned);
    }
    
    public static String displayMoney(double amount) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        
        BigDecimal value = BigDecimal.valueOf(amount);
        return df.format(value);
    }
    
}
