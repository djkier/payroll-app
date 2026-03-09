/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 *
 * @author djjus
 */
public class FontsAndFormats {
    
    public static int dynamicFontSize(String address) {
        int fontSize = 14;
        
        if (address.length() > 64) {
            fontSize = 12;
        }
        return fontSize;
    }
    
    public static String spaceEveryFourth(String idNumber) {
        if (idNumber == null) {
            return null;
        }
        
        String cleaned = idNumber.replace(" ", "");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < cleaned.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                result.append(" ");
            }
            result.append(cleaned.charAt(i));
        }
        
        return result.toString();
       
    }
    
    //table cellrenderer for leave requests
    public static DefaultTableCellRenderer statusCellRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
                setHorizontalAlignment(javax.swing.JLabel.CENTER);
                Component cell = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Reset default
                cell.setBackground(Color.WHITE);

                if (value != null) {
                    String status = value.toString();

                    switch (status) {
                        case "APPROVED":
                            cell.setBackground(ThemeColor.lightGreen());
                            break;
                        case "PENDING":
                            cell.setBackground(ThemeColor.lightYellow());
                            break;
                        case "REJECTED":
                            cell.setBackground(ThemeColor.lightRed());
                            break;
                        default:
                            cell.setBackground(Color.WHITE);
                    }
                }
                return cell;
            }
        };
    }
}
