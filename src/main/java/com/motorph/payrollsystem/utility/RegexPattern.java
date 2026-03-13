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
        return "^[\\p{L}][\\p{L}\\s'.,-]*[\\p{L}.]$";
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
    
    public static String philhealthPattern() {
        return "^\\d{12}$";
    }
    
    public static String pagibigPattern() {
        return philhealthPattern();
    }
    
    public static String tinPattern() {
        return "^\\d{3}-\\d{3}-\\d{3}-\\d{3}$";
    }
    
    public static String deptPattern() {
        return "^(N/A|[\\p{L}0-9][\\p{L}0-9\\s&/().,'-]*[\\p{L}0-9)])$";
    }
    
    public static String usernamePatter() {
        return "^[a-zA-Z0-9._@-]+$";
    }
    
}
