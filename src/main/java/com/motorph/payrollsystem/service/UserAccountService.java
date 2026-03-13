/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service;


import com.motorph.payrollsystem.dao.EmployeeRepository;
import com.motorph.payrollsystem.model.auth.UserAccount;
import com.motorph.payrollsystem.dao.UserRepository;
import com.motorph.payrollsystem.model.employee.Employee;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author djjus
 */
public class UserAccountService {
    private final UserRepository userRepo;
    private final EmployeeRepository employeeRepo;

    public UserAccountService(UserRepository userRepo, EmployeeRepository employeeRepo) {
        this.userRepo = userRepo;
        this.employeeRepo = employeeRepo;
    }
    
    public UserAccount login(String employeeNo, String username, String password) throws IOException {
        //if there is no entry it should return null right away
        if (isBlank(employeeNo) || isBlank(username) || isBlank(password)) {
            return null;
        }

        UserAccount account = userRepo.findMatchingAccount(
                employeeNo.trim(),
                username.trim(),
                password.trim()
        );

        if (account == null) {
            return null;
        }

        if (!account.isActive()) {
            return null;
        }

        Employee employee = employeeRepo.findByEmployeeNo(account.getEmployeeNo());
        if (employee == null) {
            return null;
        }

        return account;
    }
    
    public List<UserAccount> getAllAccounts() throws IOException {
        return userRepo.getAllAccounts();
    }

    
    public UserAccount createDefaultAccount(String employeeNo) throws IOException {
        if (isBlank(employeeNo)) {
            throw new IllegalArgumentException("Employee No is required.");
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

        UserAccount account = new UserAccount(cleanEmployeeNo, username, password, true);
        userRepo.append(account);
        return account;
    }
    
    public UserAccount findAccountByEmployeeNo(String employeeNo) throws IOException {
        if (isBlank(employeeNo)) {
            return null;
        }
        return userRepo.findByEmployeeNo(employeeNo.trim());
    }

    public boolean hasEmployeeRecord(String employeeNo) throws IOException {
        if (isBlank(employeeNo)) {
            return false;
        }
        return employeeRepo.findByEmployeeNo(employeeNo.trim()) != null;
    }
    
    public UserAccount updateUsername(String employeeNo, String newUsername) throws IOException {
        if (isBlank(employeeNo)) {
            throw new IllegalArgumentException("Employee No is required.");
        }
        if (isBlank(newUsername)) {
            throw new IllegalArgumentException("Username is required.");
        }

        String cleanEmployeeNo = employeeNo.trim();
        String cleanUsername = newUsername.trim();

        UserAccount existing = userRepo.findByEmployeeNo(cleanEmployeeNo);
        if (existing == null) {
            throw new IllegalStateException("User account not found: " + cleanEmployeeNo);
        }

        UserAccount existingByUsername = userRepo.findByUsername(cleanUsername);
        if (existingByUsername != null &&
                !existingByUsername.getEmployeeNo().equals(cleanEmployeeNo)) {
            throw new IllegalStateException("Username already exists.");
        }

        UserAccount updated = existing.withUsername(cleanUsername);
        userRepo.update(updated);
        return updated;
    }
    
    public UserAccount resetPassword(String employeeNo, String newPassword) throws IOException {
        if (isBlank(employeeNo)) {
            throw new IllegalArgumentException("Employee ID is required.");
        }
        if (isBlank(newPassword)) {
            throw new IllegalArgumentException("Password is required.");
        }

        String cleanEmployeeNo = employeeNo.trim();
        String cleanPassword = newPassword.trim();

        UserAccount existing = userRepo.findByEmployeeNo(cleanEmployeeNo);
        if (existing == null) {
            throw new IllegalStateException("User account not found: " + cleanEmployeeNo);
        }

        UserAccount updated = existing.withPassword(cleanPassword);
        userRepo.update(updated);
        return updated;
    }
    
    public UserAccount setAccountActive(String employeeNo, boolean active) throws IOException {
        if (isBlank(employeeNo)) {
            throw new IllegalArgumentException("Employee ID is required.");
        }

        String cleanEmployeeNo = employeeNo.trim();
        UserAccount existing = userRepo.findByEmployeeNo(cleanEmployeeNo);

        if (existing == null) {
            throw new IllegalStateException("User account not found: " + cleanEmployeeNo);
        }

        UserAccount updated = existing.withActive(active);
        userRepo.update(updated);
        return updated;
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
        
        Employee employee = employeeRepo.findByEmployeeNo(cleanEmployeeNo);
        if (employee != null) {
            throw new IllegalStateException("Cannot delete account while employee information still exists.");
        }

        userRepo.removeByEmployeeNo(cleanEmployeeNo);
        return existing;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}

