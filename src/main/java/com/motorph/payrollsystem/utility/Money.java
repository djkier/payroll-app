/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

/**
 *
 * @author djjus
 */
public class Money {
    public static double parseSalary(String value) {
        String cleaned = value.replace(",", "");
        return Double.parseDouble(cleaned);
    }
    
}
