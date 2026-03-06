/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.config;

import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.access.PositionPolicyResolver;
import com.motorph.payrollsystem.model.auth.UserAccount;
import com.motorph.payrollsystem.model.employee.Employee;
import java.time.LocalDateTime;

/**
 *
 * @author djjus
 */
public class SessionManager {
    private UserAccount currentUser;
    private Employee currentEmployee;
    private LocalDateTime loginTime;
    private AccessPolicy accessPolicy;
    
    
    //store the current log in user and the time login
    public void startSession(UserAccount user, Employee employee, AccessPolicy policy) {
        this.currentUser = user;
        this.loginTime = LocalDateTime.now();
        this.currentEmployee = employee;
        this.accessPolicy = policy;
    }
    
    //remove attributes when the user log out
    public void endSession(){
        this.currentUser = null;
        this.currentEmployee = null;
        this.loginTime = null;
        this.accessPolicy = null;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public UserAccount getCurrentUser() {
        return currentUser;
    }
    
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }
    
    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public AccessPolicy getAccessPolicy() {
        return accessPolicy;
    }
}
