/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.auth;

/**
 *
 * @author djjus
 */
public class UserAccount {
    private final String employeeNo;
    private final String username;
    private final String password;
    
    public UserAccount(String employeeNo, String username, String password) {
        this.employeeNo = employeeNo;
        this.username = username;
        this.password = password;
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
    
    
}
