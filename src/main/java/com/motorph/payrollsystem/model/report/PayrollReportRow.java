/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.model.report;

/**
 *
 * @author djjus
 */
public class PayrollReportRow {
    private String employeeNo;
    private String employeeName;
    private String position;
    private double hourlyRate;
    private double totalHoursWorked;
    private double grossPay;
    private double sssDeduction;
    private double philHealthDeduction;
    private double pagIbigDeduction;
    private double withholdingTax;
    private double totalDeductions;
    private double netPay;

    public PayrollReportRow() {
        this.employeeNo = null;
        this.employeeName = null;
        this.position = null;
        this.hourlyRate = 0.0;
        this.totalHoursWorked = 0.0;
        this.grossPay = 0.0;
        this.sssDeduction = 0.0;
        this.philHealthDeduction = 0.0;
        this.pagIbigDeduction = 0.0;
        this.withholdingTax = 0.0;
        this.totalDeductions = 0.0;
        this.netPay = 0.0;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getTotalHoursWorked() {
        return totalHoursWorked;
    }

    public void setTotalHoursWorked(double totalHoursWorked) {
        this.totalHoursWorked = totalHoursWorked;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }

    public double getSssDeduction() {
        return sssDeduction;
    }

    public void setSssDeduction(double sssDeduction) {
        this.sssDeduction = sssDeduction;
    }

    public double getPhilHealthDeduction() {
        return philHealthDeduction;
    }

    public void setPhilHealthDeduction(double philHealthDeduction) {
        this.philHealthDeduction = philHealthDeduction;
    }

    public double getPagIbigDeduction() {
        return pagIbigDeduction;
    }

    public void setPagIbigDeduction(double pagIbigDeduction) {
        this.pagIbigDeduction = pagIbigDeduction;
    }

    public double getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(double withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(double totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }
}
