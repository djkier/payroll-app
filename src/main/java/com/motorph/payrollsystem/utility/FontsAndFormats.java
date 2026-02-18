/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;


/**
 *
 * @author djjus
 */
public class FontsAndFormats {
    
    public static int dynamicFontSize(String address) {
        int fontSize = 14;
        
        if (address.length() > 64) {
            fontSize = 12;
        }
        return fontSize;
    }
    
    public static String spaceEveryFourth(String idNumber) {
        if (idNumber == null) {
            return null;
        }
        
        String cleaned = idNumber.replace(" ", "");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < cleaned.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                result.append(" ");
            }
            result.append(cleaned.charAt(i));
        }
        
        return result.toString();
       
    }
}
