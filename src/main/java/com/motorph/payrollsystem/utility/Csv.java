/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djjus
 */
public class Csv {
    public static String[] parseLine(String line) {
        List<String> output = new ArrayList<>();
        if (line == null) return output.toArray(new String[0]);
        
        StringBuilder curr = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i=0; i < line.length(); i++) {
            char ch = line.charAt(i);
            
            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    curr.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
                continue;
            }
            
            if (ch == ',' && !inQuotes) {
                output.add(curr.toString().trim());
                curr.setLength(0);
                continue;
            }
            
            curr.append(ch);
        }
        
        output.add(curr.toString().trim());
        return output.toArray(new String[0]);
    }
    

    
//    public static String[] parseLine(String line) {
//        return parseLine(line).toArray(new String[0]);
//    }
}
