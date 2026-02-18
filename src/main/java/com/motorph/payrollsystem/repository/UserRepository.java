/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.repository;

import com.motorph.payrollsystem.domain.auth.UserAccount;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author djjus
 */
public class UserRepository {
    //    path of user-accounts from resource 
    private final String resourcePath; 

    public UserRepository(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public UserAccount findMatchingAccount(String employeeNo, String username, String password) throws IOException {
//        The path will be always from the resources path on the main
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IOException("Path can't find: " + resourcePath);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
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
}
