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
}
