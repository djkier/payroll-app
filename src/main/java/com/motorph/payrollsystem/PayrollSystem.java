/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.motorph.payrollsystem;

import com.motorph.payrollsystem.GUI.MainFrame;
import com.motorph.payrollsystem.GUI.PasswordFrame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.SwingUtilities;

/**
 *
 * @author djjus
 */
public class PayrollSystem {

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            PasswordFrame frame = new PasswordFrame();
////            MainFrame frame = new MainFrame();
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//        MainFrame.main();

        String inputFile = "src/main/resources/csv-files/employee-details.csv";
        String outputFile = "src/main/resources/csv-files/user-accounts.csv";

        generateUserAccounts(inputFile, outputFile);

    }
    
    public static void generateUserAccounts(String inputPath, String outputPath) {
        try (
            BufferedReader reader = Files.newBufferedReader(Paths.get(inputPath));
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))
        ) {
            String line;

            // skip header
            reader.readLine();

            // write new header
            writer.write("Employee #,Username,Password");
            writer.newLine();

            while ((line = reader.readLine()) != null) {
                // split only first column (employee number)
                String[] parts = line.split(",", 2);

                if (parts.length > 0) {
                    String employeeNo = parts[0].trim();

                    String username = "emp" + employeeNo;
                    String password = employeeNo;

                    writer.write(employeeNo + "," + username + "," + password);
                    writer.newLine();
                }
            }

            System.out.println("user-accounts.csv generated successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
//    NOTE: 
//    February 16 -> customize the log in page, add user account csv files, program it first to make a csv for user account, 
//    format is EmployeeNo, username, password
//    the username will be "emp" + employee no. password will be the employeeNo
//    after making the user account make a accesspolicy with this it can now integrate to make sure the ui for access policy will be followedd
            
    
    
}
