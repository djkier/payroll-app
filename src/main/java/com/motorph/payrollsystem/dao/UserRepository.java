/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.dao;

import com.motorph.payrollsystem.model.auth.UserAccount;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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
    
    public UserAccount findByEmployeeNo(String employeeNo) throws IOException {
        ensureFileExistsWithHeader();

        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = parseLine(line);
                if (parts.length < 3) continue;

                String fileEmpNo = parts[0].trim();
                String fileUsername = parts[1].trim();
                String filePassword = parts[2].trim();

                if (fileEmpNo.equals(employeeNo)) {
                    return new UserAccount(fileEmpNo, fileUsername, filePassword);
                }
            }
        }

        return null;
    }
    
    public UserAccount findByUsername(String username) throws IOException {
        ensureFileExistsWithHeader();

        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 3) continue;

                String fileEmpNo = parts[0].trim();
                String fileUsername = parts[1].trim();
                String filePassword = parts[2].trim();

                if (fileUsername.equalsIgnoreCase(username.trim())) {
                    return new UserAccount(fileEmpNo, fileUsername, filePassword);
                }
            }
        }

        return null;
    }
    
    public List<UserAccount> getAllAccounts() throws IOException {
        List<UserAccount> accounts = new ArrayList<>();
        ensureFileExistsWithHeader();

        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 3) continue;

                accounts.add(new UserAccount(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim()
                ));
            }
        }

        return accounts;
    }
    
    public void append(UserAccount account) throws IOException {
        appendRow(toCsvRow(account));
    }
    
    public void removeByEmployeeNo(String employeeNo) throws IOException {
        List<UserAccount> accounts = getAllAccounts();

        boolean removed = accounts.removeIf(acc ->
                acc.getEmployeeNo() != null &&
                acc.getEmployeeNo().equals(employeeNo)
        );

        if (!removed) {
            throw new IllegalStateException("User account not found: " + employeeNo);
        }

        rewriteCsvFile(accounts);
    }
    
    private void rewriteCsvFile(List<UserAccount> accounts) throws IOException {
        ensureFileExistsWithHeader();

        Path parent = csvPath.getParent();
        if (parent != null) Files.createDirectories(parent);

        Path tmp = csvPath.resolveSibling(csvPath.getFileName().toString() + ".tmp");

        try (BufferedWriter bw = Files.newBufferedWriter(
                tmp,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            bw.write(getHeader());
            bw.newLine();

            for (UserAccount account : accounts) {
                bw.write(toCsvRow(account));
                bw.newLine();
            }
        }

        Files.move(tmp, csvPath,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
    }

    private String toCsvRow(UserAccount account) {
        return csv(account.getEmployeeNo()) + "," +
               csv(account.getUsername()) + "," +
               csv(account.getPassword());
    }

    private String csv(String str) {
        if (str == null) return "";

        boolean mustQuote =
                str.contains(",") ||
                str.contains("\"") ||
                str.contains("\n") ||
                str.contains("\r");

        String clean = str.replace("\"", "\"\"");
        return mustQuote ? "\"" + clean + "\"" : clean;
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
