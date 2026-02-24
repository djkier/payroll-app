/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djjus
 */
public class Csv {
    
    //Csv line parser
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
    
    
    //Csv paths
    public static Path appDataDir() {
        String home = System.getProperty("user.home");
        return Paths.get(home, "MotorPhPayrollFiles", "data");
        
    }
    
    public static Path leavesCsvPath() {
        return appDataDir().resolve("leave-requests.csv");
    }
    
    public static Path employeeCsvPath() {
        return appDataDir().resolve("employee-details.csv");
    }
    
    public static Path attendanceCsvPath() {
        return appDataDir().resolve("employee-attendance.csv");
    }
    
    public static Path userCsvPath() {
        return appDataDir().resolve("user-accounts.csv");
    }
    
    private static String resourcePath(String path) {
        return "/csv-files/" + path;
    }
    
    public static String leavesResourcePath() {
        return resourcePath("leave-requests.csv");
    }
    
    public static String employeeResourcePath() {
        return resourcePath("employee-details.csv");
    }
    
    public static String attendanceResourcePath() {
        return resourcePath("employee-attendance.csv");
    }
    
    public static String userResourcePath() {
        return resourcePath("user-accounts.csv");
    }
    
    

    

}
