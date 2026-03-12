/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.dao;

import com.motorph.payrollsystem.model.auth.UserAccount;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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

                UserAccount account = parseAccount(line);
                if (account == null) continue;

                boolean match =
                        account.getEmployeeNo().equals(employeeNo) &&
                        account.getUsername().equals(username) &&
                        account.getPassword().equals(password);

                if (match) {
                    return account;
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

                UserAccount account = parseAccount(line);
                if (account == null) continue;

                if (account.getEmployeeNo().equals(employeeNo)) {
                    return account;
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

                UserAccount account = parseAccount(line);
                if (account == null) continue;

                if (account.getUsername().equalsIgnoreCase(username.trim())) {
                    return account;
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

                UserAccount account = parseAccount(line);
                if (account != null) {
                    accounts.add(account);
                }
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
    
    public void update(UserAccount updatedAccount) throws IOException {
        if (updatedAccount == null || updatedAccount.getEmployeeNo() == null || updatedAccount.getEmployeeNo().isBlank()) {
            throw new IllegalArgumentException("Updated account is invalid.");
        }

        List<UserAccount> accounts = getAllAccounts();
        boolean updated = false;

        for (int i = 0; i < accounts.size(); i++) {
            UserAccount current = accounts.get(i);

            if (current.getEmployeeNo().equals(updatedAccount.getEmployeeNo())) {
                accounts.set(i, updatedAccount);
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new IllegalStateException("User account not found: " + updatedAccount.getEmployeeNo());
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

        Files.move(
                tmp, 
                csvPath,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
    }
    
    private UserAccount parseAccount(String line) {
        String[] parts = parseLine(line);
        if (parts.length < 3) return null;

        String employeeNo = parts[0].trim();
        String username = parts[1].trim();
        String password = parts[2].trim();

        boolean active = true;
        if (parts.length >= 4) {
            active = Boolean.parseBoolean(parts[3].trim());
        }

        return new UserAccount(employeeNo, username, password, active);
    }

    private String toCsvRow(UserAccount account) {
        return csv(account.getEmployeeNo()) + "," +
               csv(account.getUsername()) + "," +
               csv(account.getPassword()) + "," +
               csv(String.valueOf(account.isActive()));
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
        return "Employee #,Username,Password,Active";
    }

}
