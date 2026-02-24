/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.access.policies;

import com.motorph.payrollsystem.access.AccessPolicy;

/**
 *
 * @author djjus
 */
public class EmployeePolicy implements AccessPolicy {
    @Override
    public boolean canViewServiceMenu() {
        return false;
    }
    
    @Override
    public boolean canManageEmployees() {
        return false;
    };
    
    @Override
    public boolean canReviewLeaveRequests() {
        return false;
    };
    
    @Override
    public boolean canRunPayroll() {
        return false;
    };
    
    @Override
    public boolean canViewPayrollReports() {
        return false;
    };
    
    @Override
    public String roleName() {
        return "EMPLOYEE";
    }
}
