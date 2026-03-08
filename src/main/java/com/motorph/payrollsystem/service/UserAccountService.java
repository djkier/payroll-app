/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service;


import com.motorph.payrollsystem.model.auth.UserAccount;
import com.motorph.payrollsystem.dao.UserRepository;
import java.io.IOException;

/**
 *
 * @author djjus
 */
public class UserAccountService {
    private final UserRepository userRepo;

    public UserAccountService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    
    public UserAccount login(String employeeNo, String username, String password) throws IOException {
        //if there is no entry it should return null right away
        if (employeeNo == null || employeeNo.isBlank()) return null;
        if (username == null || username.isBlank()) return null;
        if (password == null || password.isBlank()) return null;
        
        return userRepo.findMatchingAccount(employeeNo.trim(), username.trim(), password.trim());
    }
    
    public UserAccount createDefaultAccount(String employeeNo) throws IOException {
        if (isBlank(employeeNo)) {
            throw new IllegalArgumentException("Employee ID is required.");
        }

        String cleanEmployeeNo = employeeNo.trim();
        String username = "emp" + cleanEmployeeNo;
        String password = cleanEmployeeNo;

        UserAccount existingByEmpNo = userRepo.findByEmployeeNo(cleanEmployeeNo);
        if (existingByEmpNo != null) {
            throw new IllegalStateException("User account already exists for employee: " + cleanEmployeeNo);
        }

        UserAccount existingByUsername = userRepo.findByUsername(username);
        if (existingByUsername != null) {
            throw new IllegalStateException("Username already exists: " + username);
        }

        UserAccount account = new UserAccount(cleanEmployeeNo, username, password);
        userRepo.append(account);
        return account;
    }

    public UserAccount deleteAccountByEmployeeNo(String employeeNo) throws IOException {
        if (isBlank(employeeNo)) {
            throw new IllegalArgumentException("Employee ID is required.");
        }

        String cleanEmployeeNo = employeeNo.trim();
        UserAccount existing = userRepo.findByEmployeeNo(cleanEmployeeNo);

        if (existing == null) {
            throw new IllegalStateException("User account not found: " + cleanEmployeeNo);
        }

        userRepo.removeByEmployeeNo(cleanEmployeeNo);
        return existing;
    }

    public UserAccount findAccountByEmployeeNo(String employeeNo) throws IOException {
        if (isBlank(employeeNo)) {
            return null;
        }
        return userRepo.findByEmployeeNo(employeeNo.trim());
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

