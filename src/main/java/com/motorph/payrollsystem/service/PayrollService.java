/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service;

import com.motorph.payrollsystem.domain.attendance.AttendanceRecord;
import com.motorph.payrollsystem.domain.employee.Employee;
import com.motorph.payrollsystem.domain.payroll.PayrollPeriod;
import com.motorph.payrollsystem.domain.payroll.Payslip;
import com.motorph.payrollsystem.payroll.PayrollEngine;
import com.motorph.payrollsystem.repository.AttendanceRepository;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author djjus
 */
public class PayrollService {
    private final AttendanceRepository attendanceRepository;
    private final PayrollEngine payrollEngine;
    
    public PayrollService(AttendanceRepository attendanceRepository, PayrollEngine payrollEngine){
        this.attendanceRepository = attendanceRepository;
        this.payrollEngine = payrollEngine;
    }
    
    public Payslip generatePayslip(Employee employee, PayrollPeriod period) throws Exception {
        List<AttendanceRecord> records = attendanceRepository.findByEmployeeNo(employee.getEmployeeNo());
        
        return payrollEngine.computePayslip(employee, records, period);
    }
}
