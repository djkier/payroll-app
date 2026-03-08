/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service;

import com.motorph.payrollsystem.dao.AttendanceRepository;
import com.motorph.payrollsystem.model.attendance.AttendanceRecord;
import com.motorph.payrollsystem.model.attendance.AttendanceState;
import com.motorph.payrollsystem.model.employee.Employee;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * @author djjus
 */
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public AttendanceState getAttendanceState(Employee employee) throws IOException {
        validateEmployee(employee);

        LocalDate today = LocalDate.now();
        AttendanceRecord openToday = attendanceRepository.findOpenRecordForDate(
                employee.getEmployeeNo(),
                today
        );

        if (openToday == null) {
            return new AttendanceState(true, false, false, null);
        }

        return new AttendanceState(false, true, true, openToday);
    }

    public AttendanceRecord timeIn(Employee employee) throws IOException {
        validateEmployee(employee);

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        AttendanceRecord openToday = attendanceRepository.findOpenRecordForDate(
                employee.getEmployeeNo(),
                today
        );

        if (openToday != null) {
            throw new IllegalStateException("You already have an active time-in record today.");
        }

        AttendanceRecord newRecord = new AttendanceRecord(
                employee.getEmployeeNo(),
                today,
                now,
                null
        );

        attendanceRepository.append(newRecord, employee.getLastName(), employee.getFirstName());
        return newRecord;
    }

    public AttendanceRecord timeOut(Employee employee) throws IOException {
        validateEmployee(employee);

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        AttendanceRecord openToday = attendanceRepository.findOpenRecordForDate(
                employee.getEmployeeNo(),
                today
        );

        if (openToday == null) {
            throw new IllegalStateException("No active time-in record found for today.");
        }

        if (openToday.getDate() == null || !openToday.getDate().equals(today)) {
            throw new IllegalStateException("You can only time out records from the same day.");
        }

        if (openToday.getTimeIn() != null && now.isBefore(openToday.getTimeIn())) {
            throw new IllegalStateException("Time out cannot be earlier than time in.");
        }

        attendanceRepository.timeOut(
                employee.getEmployeeNo(),
                today,
                openToday.getTimeIn(),
                now
        );

        return new AttendanceRecord(
                employee.getEmployeeNo(),
                today,
                openToday.getTimeIn(),
                now
        );
    }

    public List<AttendanceRecord> getAttendanceHistory(String employeeNo) throws IOException {
        if (employeeNo == null || employeeNo.isBlank()) {
            throw new IllegalArgumentException("Employee number is required.");
        }

        return attendanceRepository.findByEmployeeNo(employeeNo);
    }

    public List<AttendanceRecord> getAttendanceHistory(Employee employee) throws IOException {
        validateEmployee(employee);
        return attendanceRepository.findByEmployeeNo(employee.getEmployeeNo());
    }

    public List<AttendanceRecord> getAttendanceByDate(Employee employee, LocalDate date) throws IOException {
        validateEmployee(employee);

        if (date == null) {
            throw new IllegalArgumentException("Date is required.");
        }

        return attendanceRepository.findByEmployeeNoAndDate(employee.getEmployeeNo(), date);
    }

    private void validateEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee is required.");
        }

        if (employee.getEmployeeNo() == null || employee.getEmployeeNo().isBlank()) {
            throw new IllegalArgumentException("Employee number is required.");
        }
    }
}
