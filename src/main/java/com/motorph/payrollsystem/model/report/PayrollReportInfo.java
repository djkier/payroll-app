/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.model.report;

import com.motorph.payrollsystem.model.payslip.PayrollPeriod;
import java.time.LocalDateTime;

/**
 *
 * @author djjus
 */
public class PayrollReportInfo {
    private String reportId;
    private PayrollPeriod payrollPeriod;
    private String reportName;
    private String fileName;
    private String filePath;
    private LocalDateTime generatedAt;
    private String generatedByName;
    private int employeeCount;
    private double totalGrossPay;
    private double totalDeductions;
    private double totalNetPay;

    public PayrollReportInfo() {
        this.reportId = null;
        this.payrollPeriod = null;
        this.reportName = null;
        this.fileName = null;
        this.filePath = null;
        this.generatedAt = null;
        this.generatedByName = null;
        this.employeeCount = 0;
        this.totalGrossPay = 0.0;
        this.totalDeductions = 0.0;
        this.totalNetPay = 0.0;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public PayrollPeriod getPayrollPeriod() {
        return payrollPeriod;
    }

    public void setPayrollPeriod(PayrollPeriod payrollPeriod) {
        this.payrollPeriod = payrollPeriod;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getGeneratedByName() {
        return generatedByName;
    }

    public void setGeneratedByName(String generatedByName) {
        this.generatedByName = generatedByName;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }

    public double getTotalGrossPay() {
        return totalGrossPay;
    }

    public void setTotalGrossPay(double totalGrossPay) {
        this.totalGrossPay = totalGrossPay;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(double totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public double getTotalNetPay() {
        return totalNetPay;
    }

    public void setTotalNetPay(double totalNetPay) {
        this.totalNetPay = totalNetPay;
    }
}
