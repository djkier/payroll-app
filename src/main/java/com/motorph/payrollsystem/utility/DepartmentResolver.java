/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

/**
 *
 * @author djjus
 */
public class DepartmentResolver {
    public static String getDepartmentName(String position) {
        switch(position) {
            case "Chief Executive Officer":
            case "Chief Operating Officer":
            case "Chief Finance Officer":
            case "Chief Marketing Officer":
                return "Executive";
            
            case "HR Manager":
            case "HR Team Leader":
            case "HR Rank and File":
                return "Human Resources";
                
            case "Accounting Head":
            case "Payroll Manager":
            case "Payroll Team Leader":
            case "Payroll Rank and File":
                return "Accounting";
                
            case "Account Manager":
            case "Account Team Leader":
            case "Account Rank and File":
                return "Accounts";
                
            case "IT Operations and Systems":
                return "Information Technology";

            case "Sales & Marketing":
                return "Commercial";

            case "Supply Chain and Logistics":
                return "Operations";

            case "Customer Service and Relations":
                return "Customer Experience";

            case null:
            default:
                return "Unknown Department";
        }
    }
}
