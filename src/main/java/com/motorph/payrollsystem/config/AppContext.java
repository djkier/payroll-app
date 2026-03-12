/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.config;

import com.motorph.payrollsystem.access.PositionPolicyResolver;
import com.motorph.payrollsystem.service.payroll.PayrollEngine;
import com.motorph.payrollsystem.dao.AttendanceRepository;
import com.motorph.payrollsystem.dao.EmployeeRepository;
import com.motorph.payrollsystem.dao.LeaveRepository;
import com.motorph.payrollsystem.dao.PayrollReportRepository;
import com.motorph.payrollsystem.dao.UserRepository;
import com.motorph.payrollsystem.service.AttendanceService;
import com.motorph.payrollsystem.service.UserAccountService;
import com.motorph.payrollsystem.service.EmployeeService;
import com.motorph.payrollsystem.service.LeaveService;
import com.motorph.payrollsystem.service.PayrollReportService;
import com.motorph.payrollsystem.service.payroll.PayrollService;
import com.motorph.payrollsystem.utility.Csv;

/**
 *
 * @author djjus
 */
public class AppContext {
    private final SessionManager sessionManager;
    private final PositionPolicyResolver positionPolicyResolver;
    
    private final UserAccountService userAccountService;
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    private final PayrollService payrollService;
    private final LeaveService leaveService;
    private final PayrollReportService payrollReportService;
    
    
    
    public AppContext() {
        this.sessionManager = new SessionManager();
        this.positionPolicyResolver = new PositionPolicyResolver();
 
        EmployeeRepository employeeRepo = new EmployeeRepository(Csv.employeeCsvPath());
        this.employeeService = new EmployeeService(employeeRepo);
        
        UserRepository userRepo = new UserRepository(Csv.userCsvPath());
        this.userAccountService = new UserAccountService(userRepo, employeeRepo);
        
        AttendanceRepository attendanceRepository = new AttendanceRepository(Csv.attendanceCsvPath());
        this.attendanceService = new AttendanceService(attendanceRepository);
        
        LeaveRepository leaveRepository = new LeaveRepository(Csv.leavesCsvPath());
        this.leaveService = new LeaveService(leaveRepository);
        
        PayrollEngine payrollEngine = new PayrollEngine();
        this.payrollService = new PayrollService(attendanceService, payrollEngine);
        
        PayrollReportRepository payrollReportRepository = 
                new PayrollReportRepository(
                        Csv.payrollReportIndexCsvPath(),
                        Csv.payrollReportsDirPath()
                );
        this.payrollReportService = new PayrollReportService(
                employeeService,
                payrollService,
                payrollReportRepository
        );
        
        
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public UserAccountService getUserAccountService(){
        return userAccountService;
    }
    
    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public AttendanceService getAttendanceService() {
        return attendanceService;
    }

    public PayrollService getPayrollService() {
        return payrollService;
    }

    public PayrollReportService getPayrollReportService() {
        return payrollReportService;
    }
    
    public LeaveService getLeaveService() {
        return leaveService;
    }

    public PositionPolicyResolver getPositionPolicyResolver() {
        return positionPolicyResolver;
    }
    
    
    
    
    
    
    
    
}
