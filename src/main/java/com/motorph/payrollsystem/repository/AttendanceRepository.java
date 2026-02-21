/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.repository;

import com.motorph.payrollsystem.domain.attendance.AttendanceRecord;
import com.motorph.payrollsystem.utility.Dates;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djjus
 */
public class AttendanceRepository {
    private final String resourcePath;
    
    public AttendanceRepository(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    
    public List<AttendanceRecord> findByEmployeeNo(String employeeNo) throws IOException {
        InputStream is = getClass().getResourceAsStream(resourcePath);
        
//        if the path doesnt exist throw an error
        if (is == null) {
            throw new IOException("Path can't find: " + resourcePath);
        }
        
        List<AttendanceRecord> attendanceRecords = new ArrayList<>();
        
        //read each line from attendance record csv
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            //skip header
            br.readLine(); 
            
            String line;
            while((line = br.readLine()) != null) {
                //if the line has no string skip
                if(line.trim().isEmpty()) continue;
                
                //the line should have 6 column if not skip
                String[] columnData = line.split(",");
                if (columnData.length < 6) continue;
                
                //skip records that dont belong to the employee
                String fileEmpNo = columnData[0].trim();
                if (!fileEmpNo.equals(employeeNo)) continue;
                
                attendanceRecords.add(map(columnData));
                
            }
        }

        return attendanceRecords;
    }
    
    private AttendanceRecord map(String[] columnData) {
        String employeeNo = columnData[0].trim();
        LocalDate date = Dates.parseDate(columnData[3].trim());
        LocalTime timeIn = Dates.parseMilitaryTime(columnData[4].trim());
        LocalTime timeOut = Dates.parseMilitaryTime(columnData[5].trim());
        
        return new AttendanceRecord(employeeNo, date, timeIn, timeOut);
    }
}
