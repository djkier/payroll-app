/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.motorph.payrollsystem;

import com.motorph.payrollsystem.GUI.MainFrame;
import com.motorph.payrollsystem.GUI.PasswordFrame;
import com.motorph.payrollsystem.app.AppContext;
import com.motorph.payrollsystem.repository.EmployeeRepository;
import java.io.IOException;
import javax.swing.SwingUtilities;


/**
 *
 * @author djjus
 */
public class PayrollSystem {

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            AppContext context = new AppContext();
            
            PasswordFrame frame = new PasswordFrame(context);
//            MainFrame frame = new MainFrame();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
//        MainFrame.main();


    }
    
//    NOTE: 
//    Make a "Note" flowchart.
//    do salary tomorrow decide if you will make a monthly or bi-monthly
            
    
    
}
