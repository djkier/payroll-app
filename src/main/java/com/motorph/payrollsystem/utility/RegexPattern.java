/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

/**
 *
 * @author djjus
 */
public class RegexPattern {
    public static String namePattern() {
        return "^[\\p{L}][\\p{L}\\s'-]*$";
    }
    
    public static String addressPattern() {
        return ".*[\\p{L}\\d].*";
    }
    
    public static String phonePattern() {
        return "^\\d{3}-\\d{3}-\\d{3}$";
    }
    
    public static String sssPattern() {
        return "^\\d{2}-\\d{7}-\\d{1}$";
    }
    
    
}
