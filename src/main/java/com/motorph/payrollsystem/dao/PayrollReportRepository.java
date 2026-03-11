/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.dao;

import com.motorph.payrollsystem.model.payslip.PayrollPeriod;
import com.motorph.payrollsystem.model.report.PayrollReport;
import com.motorph.payrollsystem.model.report.PayrollReportInfo;
import com.motorph.payrollsystem.model.report.PayrollReportRow;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djjus
 */
public class PayrollReportRepository extends CsvRepositoryBase {
     private final Path reportsDirectory;

    public PayrollReportRepository(Path indexCsvPath, Path reportsDirectory) {
        super(indexCsvPath);
        this.reportsDirectory = reportsDirectory;
    }

    @Override
    protected String getHeader() {
        return "report_id,period_start,period_end,report_name,file_name,file_path,generated_at,generated_by_name,employee_count,total_gross_pay,total_deductions,total_net_pay";
    }

    public void ensureStorageExists() throws IOException {
        ensureFileExistsWithHeader();

        if (reportsDirectory != null) {
            Files.createDirectories(reportsDirectory);
        }
    }

    public boolean existsByPeriod(PayrollPeriod period) throws IOException {
        return findByPeriod(period) != null;
    }

    public PayrollReportInfo findByPeriod(PayrollPeriod period) throws IOException {
        validatePeriod(period);
        ensureStorageExists();

        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] cols = parseLine(line);
                if (cols.length < 12) continue;

                PayrollReportInfo info = mapInfo(cols);

                if (samePeriod(info.getPayrollPeriod(), period)) {
                    return info;
                }
            }
        }

        return null;
    }

    public List<PayrollReportInfo> findAllReportInfos() throws IOException {
        ensureStorageExists();

        List<PayrollReportInfo> reports = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] cols = parseLine(line);
                if (cols.length < 12) continue;

                reports.add(mapInfo(cols));
            }
        }

        return reports;
    }

    public void saveReport(PayrollReport report) throws IOException {
        if (report == null) {
            throw new IllegalArgumentException("No report found");
        }

        if (report.getReportInfo() == null) {
            throw new IllegalArgumentException("Report information is requried");
        }

        ensureStorageExists();

        writeReportFile(report);
        upsertReportInfo(report.getReportInfo());
    }

    //validate info -> open csv -> parse into a payroll report row -> return PayrollReport
    public PayrollReport readReport(PayrollReportInfo info) throws IOException {
        if (info == null) {
            throw new IllegalArgumentException("Payroll report info is required");
        }

        if (info.getFilePath() == null || info.getFilePath().isBlank()) {
            throw new IllegalArgumentException("File path not found");
        }

        Path reportPath = Path.of(info.getFilePath());

        if (!Files.exists(reportPath)) {
            throw new IllegalStateException("Payroll report file not found: " + info.getFileName());
        }

        PayrollReport report = new PayrollReport();
        report.setReportInfo(info);

        List<PayrollReportRow> rows = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(reportPath, StandardCharsets.UTF_8)) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] cols = parseLine(line);
                if (cols.length < 12) continue;

                rows.add(mapRow(cols));
            }
        }

        report.setRows(rows);
        return report;
    }

    
    public Path buildReportFilePath(PayrollPeriod period) {
        validatePeriod(period);
        String fileName = buildReportFileName(period);
        return reportsDirectory.resolve(fileName);
    }

    public String buildReportFileName(PayrollPeriod period) {
        validatePeriod(period);
        return "PR_" + period.getStartDate() + "_" + period.getEndDate() + ".csv";
    }

    public String buildReportId(PayrollPeriod period) {
        validatePeriod(period);
        return "PR-" + period.getStartDate() + "-" + period.getEndDate();
    }

    //rewrite existing payroll period
    private void upsertReportInfo(PayrollReportInfo info) throws IOException {
        List<PayrollReportInfo> existing = findAllReportInfos();
        List<String> rows = new ArrayList<>();

        boolean replaced = false;

        for (PayrollReportInfo current : existing) {
            if (samePeriod(current.getPayrollPeriod(), info.getPayrollPeriod())) {
                rows.add(toInfoCsvRow(info));
                replaced = true;
            } else {
                rows.add(toInfoCsvRow(current));
            }
        }

        if (!replaced) {
            rows.add(toInfoCsvRow(info));
        }

        rewriteAll(rows);
    }

    private void writeReportFile(PayrollReport report) throws IOException {
        PayrollReportInfo info = report.getReportInfo();

        if (info.getFilePath() == null || info.getFilePath().isBlank()) {
            throw new IllegalArgumentException("Report file path is required.");
        }

        Path reportPath = Path.of(info.getFilePath());

        Path parent = reportPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        List<String> lines = new ArrayList<>();
        lines.add(getReportHeader());

        for (PayrollReportRow row : report.getRows()) {
            lines.add(toReportRowCsv(row));
        }

        Files.write(reportPath, lines, StandardCharsets.UTF_8);
    }

    private String getReportHeader() {
        return "Employee No,Employee Name,Position,Hourly Rate,Total Hours Worked,Gross Pay,SSS,PhilHealth,Pag-IBIG,Withholding Tax,Total Deductions,Net Pay";
    }

    private PayrollReportInfo mapInfo(String[] cols) {
        PayrollReportInfo info = new PayrollReportInfo();

        PayrollPeriod period = new PayrollPeriod(
                LocalDate.parse(cols[1].trim()),
                LocalDate.parse(cols[2].trim())
        );

        info.setReportId(cols[0].trim());
        info.setPayrollPeriod(period);
        info.setReportName(cols[3].trim());
        info.setFileName(cols[4].trim());
        info.setFilePath(cols[5].trim());

        String generatedAt = cols[6].trim();
        if (!generatedAt.isBlank()) {
            info.setGeneratedAt(LocalDateTime.parse(generatedAt));
        }

        info.setGeneratedByName(cols[7].trim());
        info.setEmployeeCount(parseInt(cols[8]));
        info.setTotalGrossPay(parseDouble(cols[9]));
        info.setTotalDeductions(parseDouble(cols[10]));
        info.setTotalNetPay(parseDouble(cols[11]));

        return info;
    }

    private PayrollReportRow mapRow(String[] cols) {
        PayrollReportRow row = new PayrollReportRow();

        row.setEmployeeNo(cols[0].trim());
        row.setEmployeeName(cols[1].trim());
        row.setPosition(cols[2].trim());
        row.setHourlyRate(parseDouble(cols[3]));
        row.setTotalHoursWorked(parseDouble(cols[4]));
        row.setGrossPay(parseDouble(cols[5]));
        row.setSssDeduction(parseDouble(cols[6]));
        row.setPhilHealthDeduction(parseDouble(cols[7]));
        row.setPagIbigDeduction(parseDouble(cols[8]));
        row.setWithholdingTax(parseDouble(cols[9]));
        row.setTotalDeductions(parseDouble(cols[10]));
        row.setNetPay(parseDouble(cols[11]));

        return row;
    }

    private String toInfoCsvRow(PayrollReportInfo info) {
        return String.join(",",
                csv(info.getReportId()),
                csv(info.getPayrollPeriod().getStartDate().toString()),
                csv(info.getPayrollPeriod().getEndDate().toString()),
                csv(info.getReportName()),
                csv(info.getFileName()),
                csv(info.getFilePath()),
                csv(info.getGeneratedAt() == null ? "" : info.getGeneratedAt().toString()),
                csv(info.getGeneratedByName()),
                csv(String.valueOf(info.getEmployeeCount())),
                csv(formatMoney(info.getTotalGrossPay())),
                csv(formatMoney(info.getTotalDeductions())),
                csv(formatMoney(info.getTotalNetPay()))
        );
    }

    private String toReportRowCsv(PayrollReportRow row) {
        return String.join(",",
                csv(row.getEmployeeNo()),
                csv(row.getEmployeeName()),
                csv(row.getPosition()),
                csv(formatMoney(row.getHourlyRate())),
                csv(formatMoney(row.getTotalHoursWorked())),
                csv(formatMoney(row.getGrossPay())),
                csv(formatMoney(row.getSssDeduction())),
                csv(formatMoney(row.getPhilHealthDeduction())),
                csv(formatMoney(row.getPagIbigDeduction())),
                csv(formatMoney(row.getWithholdingTax())),
                csv(formatMoney(row.getTotalDeductions())),
                csv(formatMoney(row.getNetPay()))
        );
    }

    private boolean samePeriod(PayrollPeriod a, PayrollPeriod b) {
        if (a == null || b == null) return false;

        return a.getStartDate().equals(b.getStartDate())
                && a.getEndDate().equals(b.getEndDate());
    }

    private void validatePeriod(PayrollPeriod period) {
        if (period == null) {
            throw new IllegalArgumentException("Payroll period is required.");
        }

        if (period.getStartDate() == null || period.getEndDate() == null) {
            throw new IllegalArgumentException("Payroll period dates are required.");
        }
    }

    private double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int parseInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String formatMoney(double value) {
        return String.format("%.2f", value);
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }
}
