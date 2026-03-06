/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service;

import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.dao.EmployeeRepository;
import java.io.IOException;
import java.util.List;

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
    
    public List<Employee> getEmployeeList(AccessPolicy policy) throws IOException{
        if (policy == null || !policy.canManageEmployees()) {
            throw new SecurityException("Access denied.");
        }
        
        return employeeRepo.getEmployeeList();
    }
    
    public Employee updateEmployee(Employee updated) throws IOException {
        if (updated == null) throw new IllegalArgumentException("Updated employee is null");
        
        if (isBlank(updated.getEmployeeNo())) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        if (isBlank(updated.getLastName()) || isBlank(updated.getFirstName())) {
            throw new IllegalArgumentException("First name and last name are required");
        }
        
        Employee existing = findByEmployeeNo(updated.getEmployeeNo());
        if (existing == null) {
            throw new IllegalStateException("Employee not found: " + updated.getEmployeeNo());
        }

        
        employeeRepo.update(updated);
        
        return findByEmployeeNo(updated.getEmployeeNo());
    }
    
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
