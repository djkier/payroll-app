/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.payroll;

import com.motorph.payrollsystem.domain.attendance.AttendanceRecord;
import com.motorph.payrollsystem.domain.employee.Employee;
import com.motorph.payrollsystem.domain.payroll.PayrollPeriod;
import com.motorph.payrollsystem.domain.payroll.Payslip;
import com.motorph.payrollsystem.domain.payroll.PayslipLine;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author djjus
 */
public class PayrollEngine {
    
    public Payslip computePayslip (Employee employee, List<AttendanceRecord> records, PayrollPeriod period) {
        //get total hour
        double totalHours = computeTotalHours(records, period);
        
        //get gross pay 
        double grossPay = computeGrossPay(employee, totalHours);
        
        Payslip payslip = new Payslip(employee, period);
        payslip.setTotalHours(totalHours);
        
        payslip.addLine(new PayslipLine("Basic", grossPay, PayslipLine.LineType.EARNING));
        
        payslip.computeTotals();
        
        return payslip;
    }
    
    private double computeTotalHours(List<AttendanceRecord> records, PayrollPeriod period) {
        double total = 0;
        
        for (AttendanceRecord record : records) {
            LocalDate date = record.getDate();
            
            boolean inRange = 
               !date.isBefore(period.getStartDate()) &&
               !date.isAfter(period.getEndDate());
            
            if (!inRange) continue;
            
            total += record.getHoursWorked();
        }
        
        return total;
    }
    
    private double computeGrossPay(Employee employee, double totalHours) {
        double hourlyRate = employee.getCompProfile().getHourlyRate();
        return hourlyRate * totalHours;
    }
}
