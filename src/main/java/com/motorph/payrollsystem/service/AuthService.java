/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service;

import com.motorph.payrollsystem.domain.auth.ReadCsvUserAccount;
import com.motorph.payrollsystem.domain.auth.UserAccount;
import java.io.IOException;

/**
 *
 * @author djjus
 */
public class AuthService {
    private final ReadCsvUserAccount finder;
    
    public AuthService(ReadCsvUserAccount finder) {
        this.finder = finder;
    }
    
    public UserAccount login(String employeeNo, String username, String password) throws IOException {
        //if there is no entry it should return null right away
        if (employeeNo == null || employeeNo.isBlank()) return null;
        if (username == null || username.isBlank()) return null;
        if (password == null || password.isBlank()) return null;
        
        return finder.findMatchingAccount(employeeNo.trim(), username.trim(), password.trim());
        
    }
}
