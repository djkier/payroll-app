/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.attendance;

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
        return 0;
    }
    
    
}
