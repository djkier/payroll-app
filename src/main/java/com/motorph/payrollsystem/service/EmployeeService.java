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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

        //Try to validate this if time permited
        // - ContactInfo should not be null
        // - Address and phone number required
        // - GovIds should not be null
        // - SSS / PhilHealth / TIN / Pag-IBIG required
        // - DepartmentInfo should not be null
        // - Status / Position / Supervisor required


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
        if (policy == null|| 
                            (!policy.canManageEmployees() 
                            && !policy.canUpdateEmployeeSalary() 
                            && !policy.canViewEmployeePayroll())) {
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
    
    public int getStatusStats(String statusType, AccessPolicy policy) throws IOException {
        if (statusType == null || statusType.isBlank()) {
            return 0;
        }
        
        Map<String, Integer> counts = getStatusStats(policy);
        
        return counts.getOrDefault(statusType, 0);
    } 
    
    public int getStatusTotalStats(AccessPolicy policy) throws IOException{
        int total = 0;
        List<String> statuses = getUniqueStatus(policy);
        Map<String, Integer> counts = getStatusStats(policy);
        
        for (String status : statuses) {
            total += counts.get(status);
        }
        return total;
    }
    
    public Map<String, Integer> getStatusStats(AccessPolicy policy) throws IOException {
        List<Employee> list = getEmployeeList(policy);
        List<String> statuses = getUniqueStatus(policy);
        Map<String, Integer> counts = new HashMap<>();
        
        for (String status : statuses) {
            counts.put(status, 0);
        }
        
        for (Employee employee : list) {
            String employeeStatus = employee.getDepartmentInfo().getStatus();
            counts.put(employeeStatus, counts.get(employeeStatus) + 1);
        }
        
        return counts;
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
