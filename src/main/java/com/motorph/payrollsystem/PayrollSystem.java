/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.motorph.payrollsystem;

import com.motorph.payrollsystem.GUI.MainFrame;
import com.motorph.payrollsystem.GUI.PasswordFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author djjus
 */
public class PayrollSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PasswordFrame frame = new PasswordFrame();
//            MainFrame frame = new MainFrame();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
//        MainFrame.main();
    }
    
//    NOTE: 
//    February 16 -> customize the log in page, add user account csv files, program it first to make a csv for user account, 
//    format is EmployeeNo, username, password
//    the username will be "emp" + employee no. password will be the employeeNo
//    after making the user account make a accesspolicy with this it can now integrate to make sure the ui for access policy will be followedd
            
    
    
}
