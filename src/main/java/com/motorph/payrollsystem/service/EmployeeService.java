/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service;

import com.motorph.payrollsystem.domain.employee.Employee;
import com.motorph.payrollsystem.repository.EmployeeRepository;
import java.io.IOException;

/**
 *
 * @author djjus
 */
public class EmployeeService {
    private final EmployeeRepository employeeRepo;
    
    public EmployeeService(EmployeeRepository employeeRepo){
        this.employeeRepo = employeeRepo;
    }
    
    public Employee findByEmployeeNo(String employeeNo) throws IOException {
        //validate employee no to prevent unknown error
        if (employeeNo == null || employeeNo.isBlank()) return null;
        return employeeRepo.findByEmployeeNo(employeeNo);
    }
}
