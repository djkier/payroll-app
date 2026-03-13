/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.motorph.payrollsystem;

import com.motorph.payrollsystem.gui.PasswordFrame;
import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.utility.Csv;
import com.motorph.payrollsystem.utility.DataBootstrap;
import javax.swing.SwingUtilities;


/**
 *
 * @author djjus
 */
public class PayrollSystem {
    

    public static void main(String[] args) {
        //MAKE A NEW COPY OF THE TEMPLATE FILES IF NOT EXISTING
        try {
            DataBootstrap.ensureCsvExported(Csv.employeeResourcePath(), Csv.employeeCsvPath());
            DataBootstrap.ensureCsvExported(Csv.leavesResourcePath(), Csv.leavesCsvPath());
            DataBootstrap.ensureCsvExported(Csv.attendanceResourcePath(), Csv.attendanceCsvPath());
            DataBootstrap.ensureCsvExported(Csv.userResourcePath(), Csv.userCsvPath());
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, 
                    "Failed to initialize data files.\n" + e.getMessage(),
                    "Startup Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            AppContext context = new AppContext();
            
            PasswordFrame frame = new PasswordFrame(context);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
        


    }
    
    
}
