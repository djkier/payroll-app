/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.model.auth;

/**
 *
 * @author djjus
 */
public class UserAccount {
    private final String employeeNo;
    private final String username;
    private final String password;
    private final boolean active;
    
    public UserAccount(String employeeNo, String username, String password) {
        this(employeeNo, username, password, true);
        
    }
    
    public UserAccount(String employeeNo, String username, String password, boolean active) {
        this.employeeNo = employeeNo;
        this.username = username;
        this.password = password;
        this.active = active;
    }


    public String getEmployeeNo() {
        return employeeNo;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public UserAccount withUsername(String newUsername) {
        return new UserAccount(employeeNo, newUsername, password, active);
    }

    public UserAccount withPassword(String newPassword) {
        return new UserAccount(employeeNo, username, newPassword, active);
    }

    public UserAccount withActive(boolean newActive) {
        return new UserAccount(employeeNo, username, password, newActive);
    }
    
    
}
