/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service.payroll;

import com.motorph.payrollsystem.model.attendance.AttendanceRecord;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.model.payslip.PayrollPeriod;
import com.motorph.payrollsystem.model.payslip.Payslip;
import com.motorph.payrollsystem.service.payroll.PayrollEngine;
import com.motorph.payrollsystem.dao.AttendanceRepository;
import com.motorph.payrollsystem.service.AttendanceService;
import com.motorph.payrollsystem.utility.PayrollPeriodFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djjus
 */
public class PayrollService {
    private final AttendanceService attendanceService;
    private final PayrollEngine payrollEngine;

    public PayrollService(AttendanceService attendanceService, PayrollEngine payrollEngine) {
        this.attendanceService = attendanceService;
        this.payrollEngine = payrollEngine;
    }

    public Payslip generatePayslip(Employee employee, PayrollPeriod period) throws Exception {
        List<AttendanceRecord> records = attendanceService.getAttendanceHistory(employee);
        return payrollEngine.computePayslip(employee, records, period);
    }
    
    public List<PayrollPeriod> getAvailablePayrollPeriods(Employee employee) throws IOException {
        if (employee == null) {
            throw new IllegalArgumentException("No logged-in employee found.");
        }

        List<AttendanceRecord> records = attendanceService.getAttendanceHistory(employee);
        List<LocalDate> dates = new ArrayList<>();
        
        for (AttendanceRecord record : records) {
            if (record != null && record.getDate() != null) {
                dates.add(record.getDate());
            }
        }

        return PayrollPeriodFactory.fromAttendanceDatesSemiMonthly(dates);
    }
}
