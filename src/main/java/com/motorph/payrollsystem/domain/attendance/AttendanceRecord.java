/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.attendance;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author djjus
 */
public class AttendanceRecord {
    private String employeeNo;
    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;
    
    public AttendanceRecord(String employeeNo, LocalDate date, LocalTime timeIn, LocalTime timeOut) {
        this.employeeNo = employeeNo;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTimeIn() {
        return timeIn;
    }

    public LocalTime getTimeOut() {
        return timeOut;
    }
    
    
    //each attendance record knows there total work hours
    public double getHoursWorked() {
        if (timeIn == null || timeOut == null) return 0;
        
        LocalTime normalizedIn = normalizeTimeIn(timeIn);
        
        long minutes = Duration.between(normalizedIn, timeOut).toMinutes();
        
        //if the timeout is earlier than time in will return 0
        if (minutes <= 0) return 0;
        
        return minutes/60.0;
    }
    
    //if time in is before 8AM or on/before 8:10 the timeIn would be 8:00AM
    private LocalTime normalizeTimeIn(LocalTime actualIn) {
        LocalTime eightAM = LocalTime.of(8,0);
        LocalTime graceEnd = LocalTime.of(8, 10);
        
        if (actualIn.isBefore(eightAM) || !(actualIn.isAfter(graceEnd))) {
            return eightAM;
        }
        
        return actualIn;
    }
    
    
}
