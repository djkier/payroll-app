/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service;

import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.dao.EmployeeRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
    public Employee addNewEmployee(Employee employee) throws IOException {
        if (employee == null) {
            throw new IllegalArgumentException("Employee is required.");
        }

        //In case Employee has no EmployeeNo
        if (isBlank(employee.getEmployeeNo())) {
            employee.setEmployeeNo(getNextEmployeeNo());
        }

        if (isBlank(employee.getLastName())) {
            throw new IllegalArgumentException("Last name is required.");
        }

        if (isBlank(employee.getFirstName())) {
            throw new IllegalArgumentException("First name is required.");
        }

        if (employee.getBirthday() == null) {
            throw new IllegalArgumentException("Birthday is required.");
        }

        // You said to comment out validation for later.
        // Possible next validations:
        // - ContactInfo should not be null
        // - Address and phone number required
        // - GovIds should not be null
        // - SSS / PhilHealth / TIN / Pag-IBIG required
        // - DepartmentInfo should not be null
        // - Status / Position / Supervisor required
        // - CompProfile should not be null
        // - Basic salary and allowances should be valid non-negative numbers

        //recheck if it has existed on the list
        Employee existing = findByEmployeeNo(employee.getEmployeeNo());
        if (existing != null) {
            throw new IllegalStateException("Employee already exists: " + employee.getEmployeeNo());
        }

        employeeRepo.append(employee);

        return findByEmployeeNo(employee.getEmployeeNo());
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
    
    public Employee removeEmployee(String employeeNo) throws IOException {
        if (isBlank(employeeNo)) {
            throw new IllegalArgumentException("Employee ID is required.");
        }

        Employee existing = findByEmployeeNo(employeeNo);
        if (existing == null) {
            throw new IllegalStateException("Employee not found: " + employeeNo);
        }

        employeeRepo.removeByEmployeeNo(employeeNo);

        return existing;
    }
    
    public String getNextEmployeeNo() throws IOException {
        return employeeRepo.getNextEmployeeNo();
    }
    
    public List<Employee> getEmployeeList(AccessPolicy policy) throws IOException{
        if (policy == null || !policy.canManageEmployees()) {
            throw new SecurityException("Access denied.");
        }
        
        return employeeRepo.getEmployeeList();
    }
    
    public List<String> getUniquePosition(AccessPolicy policy) throws IOException{
        List<Employee> empList= getEmployeeList(policy);
        Set<String> positions = new HashSet<>();
        
        for (Employee emp : empList) {
            String position = emp.getDepartmentInfo().getPosition();
            positions.add(position);
        }
       
        return hashSetSorter(positions);
    }
    
    public List<String> getUniqueStatus(AccessPolicy policy) throws IOException {
        List<Employee> empList = getEmployeeList(policy);
        Set<String> statuses = new HashSet<>();
        
        for (Employee emp : empList) {
            String status = emp.getDepartmentInfo().getStatus();
            statuses.add(status);
        }
       
        return hashSetSorter(statuses);
    }
    
    public List<String> getAllEmployeeNames(AccessPolicy policy) throws IOException {
        List<Employee> empList = getEmployeeList(policy);
        List<String> empNames = new ArrayList<>();
        
        for (Employee emp : empList) {
            if (emp.getLastFirstName() != null) {
                empNames.add(emp.getLastFirstName());
            }
        }
        
        Collections.sort(empNames);
        
        return empNames;
    }
    
    private List<String> hashSetSorter(Set<String> set) {
        List<String> setList = new ArrayList<>(set);
        Collections.sort(setList);
        
        return setList;
    }
    
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
