/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djjus
 */
public class Csv {
    
    
    //Csv paths
    public static Path appDataDir() {
        String home = System.getProperty("user.home");
        return Paths.get(home, "MotorPhPayrollFiles", "data");
        
    }
    
    public static Path reportsDirPath() {
    return appDataDir().resolve("reports");
}

    public static Path payrollReportsDirPath() {
        return reportsDirPath().resolve("payroll");
    }

    public static Path payrollReportIndexCsvPath() {
        return reportsDirPath().resolve("payroll-report-index.csv");
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
