/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.model.report;

import com.motorph.payrollsystem.model.payslip.PayrollPeriod;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djjus
 */
public class PayrollReport {
    private PayrollReportInfo reportInfo;
    private final List<PayrollReportRow> rows;

    public PayrollReport() {
        this.reportInfo = null;
        this.rows = new ArrayList<>();
    }

    public PayrollReportInfo getReportInfo() {
        return reportInfo;
    }

    public void setReportInfo(PayrollReportInfo reportInfo) {
        this.reportInfo = reportInfo;
    }

    public List<PayrollReportRow> getRows() {
        return new ArrayList<>(rows);
    }

    public void addRow(PayrollReportRow row) {
        this.rows.add(row);
    }

    public void setRows(List<PayrollReportRow> rows) {
        this.rows.clear();

        if (rows != null) {
            this.rows.addAll(rows);
        }
    }
    

}
