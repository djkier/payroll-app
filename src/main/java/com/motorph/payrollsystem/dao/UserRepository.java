/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.dao;

import com.motorph.payrollsystem.model.auth.UserAccount;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author djjus
 */
public class UserRepository extends CsvRepositoryBase {
    //    path of user-accounts from resource 
    

    public UserRepository(Path csvPath) {
        super(csvPath);
    }

    public UserAccount findMatchingAccount(String employeeNo, String username, String password) throws IOException {
        ensureFileExistsWithHeader();

        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;

            // skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                //employeeNo,username,password
                String[] parts = line.split(",", -1);
                if (parts.length < 3) continue;

                String fileEmpNo = parts[0].trim();
                String fileUsername = parts[1].trim();
                String filePassword = parts[2].trim();

                boolean match =
                        fileEmpNo.equals(employeeNo) &&
                        fileUsername.equals(username) &&
                        filePassword.equals(password);

                if (match) {
                    return new UserAccount(fileEmpNo, fileUsername, filePassword);
                }
            }
        }

        return null; 
    }
    
    @Override
    protected void ensureFileExistsWithHeader() throws IOException {
        if (Files.exists(csvPath)) return;
        
        //Create leave-request if there is none.
        Path parent = csvPath.getParent();
        if (parent != null) Files.createDirectories(parent);
        
        Files.writeString(csvPath, getHeader() + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE);
    }
    
    @Override
    protected String getHeader() {
        return "Employee #,Username,Password";
    }

}
