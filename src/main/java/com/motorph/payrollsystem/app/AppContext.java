/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.app;

import com.motorph.payrollsystem.payroll.PayrollEngine;
import com.motorph.payrollsystem.repository.AttendanceRepository;
import com.motorph.payrollsystem.repository.EmployeeRepository;
import com.motorph.payrollsystem.repository.LeaveRepository;
import com.motorph.payrollsystem.repository.UserRepository;
import com.motorph.payrollsystem.service.AuthService;
import com.motorph.payrollsystem.service.EmployeeService;
import com.motorph.payrollsystem.service.LeaveService;
import com.motorph.payrollsystem.service.PayrollService;

/**
 *
 * @author djjus
 */
public class AppContext {
    private final SessionManager sessionManager;
    private final AuthService authService;
    private final EmployeeService employeeService;
    private final PayrollService payrollService;
    private final LeaveService leaveService;
    
        private final AttendanceRepository attendanceRepository;
    
    
    public AppContext() {
        this.sessionManager = new SessionManager();
        
        UserRepository finder = new UserRepository("/csv-files/user-accounts.csv");
        this.authService = new AuthService(finder);
        
        
        EmployeeRepository employeeRepo = new EmployeeRepository("/csv-files/employee-details.csv");
        this.employeeService = new EmployeeService(employeeRepo);
        
        this.attendanceRepository = new AttendanceRepository("/csv-files/employee-attendance.csv");
        PayrollEngine payrollEngine = new PayrollEngine();
        
        this.payrollService = new PayrollService(attendanceRepository, payrollEngine);
        
        LeaveRepository leaveRepository = new LeaveRepository("/csv-files/leave-requests.csv");
        this.leaveService = new LeaveService(leaveRepository);
        
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public AuthService getAuthService(){
        return authService;
    }
    
    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public AttendanceRepository getAttendanceRepository() {
        return attendanceRepository;
    }

    public PayrollService getPayrollService() {
        return payrollService;
    }
    
    public LeaveService getLeaveService() {
        return leaveService;
    }
    
    
    
    
    
    
}
