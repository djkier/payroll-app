/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service;


import com.motorph.payrollsystem.dao.PayrollReportRepository;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.model.payslip.PayrollPeriod;
import com.motorph.payrollsystem.model.payslip.Payslip;
import com.motorph.payrollsystem.model.report.PayrollReport;
import com.motorph.payrollsystem.model.report.PayrollReportInfo;
import com.motorph.payrollsystem.model.report.PayrollReportRow;
import com.motorph.payrollsystem.service.payroll.PayrollService;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.PayrollPeriodFactory;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author djjus
 */
public class PayrollReportService {
    private final EmployeeService employeeService;
    private final PayrollService payrollService;
    private final PayrollReportRepository payrollReportRepo;

    public PayrollReportService(
            EmployeeService employeeService,
            PayrollService payrollService,
            PayrollReportRepository payrollReportRepository
    ) {
        this.employeeService = employeeService;
        this.payrollService = payrollService;
        this.payrollReportRepo = payrollReportRepository;
    }

    public List<PayrollPeriod> getAllAvailablePayrollPeriods() throws Exception {
        List<Employee> employees = employeeService.getAllEmployees();
        Set<PayrollPeriod> uniquePeriods = new LinkedHashSet<>();

        for (Employee employee : employees) {
            if (employee == null) continue;

            List<PayrollPeriod> employeePeriods = payrollService.getAvailablePayrollPeriods(employee);
            uniquePeriods.addAll(employeePeriods);
        }

        return PayrollPeriodFactory.reverseList(uniquePeriods);
    }
    
    public List<Integer> getAllAvailablePayrollYears() throws Exception {
        List<PayrollPeriod> periods = getAllAvailablePayrollPeriods();
        Set<Integer> uniqueYears = new LinkedHashSet<>();

        for (PayrollPeriod period : periods) {
            if (period == null || period.getStartDate() == null) {
                continue;
            }

            uniqueYears.add(period.getStartDate().getYear());
        }

        return new ArrayList<>(uniqueYears);
    }

    public boolean hasExistingReport(PayrollPeriod period) throws IOException {
        validatePeriod(period);
        return payrollReportRepo.existsByPeriod(period);
    }

    public PayrollReportInfo getExistingReportInfo(PayrollPeriod period) throws IOException {
        validatePeriod(period);
        return payrollReportRepo.findByPeriod(period);
    }

    public List<PayrollReportInfo> getAllGeneratedReports() throws IOException {
        return payrollReportRepo.findAllReportInfos();
    }
    
    public PayrollReport loadPayrollReport(PayrollPeriod period) throws IOException {
        return loadPayrollReport(getExistingReportInfo(period));
    }

    public PayrollReport loadPayrollReport(PayrollReportInfo reportInfo) throws IOException {
        if (reportInfo == null) {
            throw new IllegalArgumentException("Payroll report info is required.");
        }

        return payrollReportRepo.readReport(reportInfo);
    }

    public PayrollReport generatePayrollReport(PayrollPeriod period, String generatedByName) throws Exception {
        validatePeriod(period);
        validateGeneratedByName(generatedByName);

        PayrollReport report = buildPayrollReport(period, generatedByName);
        payrollReportRepo.saveReport(report);

        return report;
    }

    private PayrollReport buildPayrollReport(
            PayrollPeriod period, 
            String generatedByName) throws Exception {
        
        List<Employee> employees = employeeService.getAllEmployees();
        List<PayrollReportRow> reportRows = new ArrayList<>();

        double totalGrossPay = 0.0;
        double totalDeductions = 0.0;
        double totalNetPay = 0.0;

        for (Employee employee : employees) {
            if (employee == null) continue;

            Payslip payslip = payrollService.generatePayslip(employee, period);

            
            if (payslip == null) continue;

            if (payslip.getTotalHours() <= 0) {
                continue;
            }

            PayrollReportRow row = toReportRow(employee, payslip);
            reportRows.add(row);
            
            totalGrossPay += row.getGrossPay();
            totalDeductions += row.getTotalDeductions();
            totalNetPay += row.getNetPay();
        }

        PayrollReportInfo info = buildReportInfo(
                period,
                generatedByName,
                reportRows.size(),
                totalGrossPay,
                totalDeductions,
                totalNetPay
        );

        PayrollReport report = new PayrollReport();
        report.setReportInfo(info);
        report.setRows(reportRows);
        
        return report;
    }
    


    private PayrollReportInfo buildReportInfo(
            PayrollPeriod period,
            String generatedByName,
            int employeeCount,
            double totalGrossPay,
            double totalDeductions,
            double totalNetPay
    ) {
        PayrollReportInfo info = new PayrollReportInfo();

        String reportName = buildReportName(period);
        String fileName = payrollReportRepo.buildReportFileName(period);
        Path filePath = payrollReportRepo.buildReportFilePath(period);

        info.setReportId(payrollReportRepo.buildReportId(period));
        info.setPayrollPeriod(period);
        info.setReportName(reportName);
        info.setFileName(fileName);
        info.setFilePath(filePath.toString());
        info.setGeneratedAt(LocalDateTime.now());
        info.setGeneratedByName(generatedByName);
        info.setEmployeeCount(employeeCount);
        info.setTotalGrossPay(totalGrossPay);
        info.setTotalDeductions(totalDeductions);
        info.setTotalNetPay(totalNetPay);

        return info;
    }

    private PayrollReportRow toReportRow(Employee employee, Payslip payslip) {
        PayrollReportRow row = new PayrollReportRow();

        row.setEmployeeNo(employee.getEmployeeNo());
        row.setEmployeeName(employee.getLastFirstName());
        row.setPosition(employee.getDepartmentInfo().getPosition());
        row.setHourlyRate(employee.getCompProfile().getHourlyRate());
        row.setTotalHoursWorked(payslip.getTotalHours());
        row.setGrossPay(payslip.getGrossPay());
        row.setSssDeduction(payslip.getSssAmount());
        row.setPhilHealthDeduction(payslip.getPhilHealthAmount());
        row.setPagIbigDeduction(payslip.getPagibigAmount());
        row.setWithholdingTax(payslip.getTaxAmount());
        row.setTotalDeductions(payslip.getTotalDeductions());
        row.setNetPay(payslip.getNetPay());

        return row;
    }

    private String buildReportName(PayrollPeriod period) {
        return "Payroll Report - "
                + Dates.shortFullExtraDate(period.getStartDate())
                + " to "
                + Dates.shortFullExtraDate(period.getEndDate());
    }

    private void validatePeriod(PayrollPeriod period) {
        if (period == null) {
            throw new IllegalArgumentException("Payroll period is required.");
        }

        if (period.getStartDate() == null || period.getEndDate() == null) {
            throw new IllegalArgumentException("Payroll period dates are required.");
        }
    }

    private void validateGeneratedByName(String generatedByName) {
        if (generatedByName == null || generatedByName.isBlank()) {
            throw new IllegalArgumentException("Generated by name is required.");
        }
    }
}
