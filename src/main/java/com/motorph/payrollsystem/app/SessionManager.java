/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.app;

import com.motorph.payrollsystem.domain.auth.UserAccount;
import com.motorph.payrollsystem.domain.employee.Employee;
import java.time.LocalDateTime;

/**
 *
 * @author djjus
 */
public class SessionManager {
    private UserAccount currentUser;
    private Employee currentEmployee;
    private LocalDateTime loginTime;
    
    //store the current log in user and the time login
    public void startSession(UserAccount user, Employee employee) {
        this.currentUser = user;
        this.currentEmployee = employee;
        this.loginTime = LocalDateTime.now();
    }
    
    //remove attributes when the user log out
    public void endSession(){
        this.currentUser = null;
        this.currentEmployee = null;
        this.loginTime = null;
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
}
