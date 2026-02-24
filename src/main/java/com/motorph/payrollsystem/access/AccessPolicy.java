/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.access;

/**
 *
 * @author djjus
 */
public interface AccessPolicy {
    boolean canViewServiceMenu();
    
    boolean canManageEmployees();
    boolean canReviewLeaveRequests();
    boolean canRunPayroll();
    boolean canViewPayrollReports();
    
    String roleName();
}
