/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.access;

import com.motorph.payrollsystem.access.policies.AdminPolicy;
import com.motorph.payrollsystem.access.policies.EmployeePolicy;
import com.motorph.payrollsystem.access.policies.HrPolicy;
import com.motorph.payrollsystem.access.policies.ItPolicy;
import com.motorph.payrollsystem.access.policies.PayrollPolicy;
import com.motorph.payrollsystem.model.employee.Employee;

/**
 *
 * @author djjus
 */
public class PositionPolicyResolver {
    
    public AccessPolicy resolve(Employee employee) {
        String department = employee.getDepartmentInfo().getDepartment();
        
        switch (department) {
            case "Executive":
                return new AdminPolicy();
            
            case "Human Resources":
                return new HrPolicy();
                
            case "Accounting":
                return new PayrollPolicy();
                
            case "Information Technology":
                return new ItPolicy();
                
            default:
                return new EmployeePolicy();
        }
        
    }
}
