/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.dao;

import com.motorph.payrollsystem.model.attendance.AttendanceRecord;
import com.motorph.payrollsystem.utility.Dates;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djjus
 */
public class AttendanceRepository extends CsvRepositoryBase {
    
    public AttendanceRepository(Path csvPath) {
        super(csvPath);
    }
    
    public List<AttendanceRecord> findByEmployeeNo(String employeeNo) throws IOException {
        ensureFileExistsWithHeader();
        
        List<AttendanceRecord> attendanceRecords = new ArrayList<>();
        
        //read each line from attendance record csv
        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            //skip header
            br.readLine(); 
            
            String line;
            while((line = br.readLine()) != null) {
                //if the line has no string skip
                if(line.trim().isEmpty()) continue;
                
                //the line should have 6 column if not skip
                String[] columnData = line.split(",");
                if (columnData.length < 6) continue;
                
                //skip records that dont belong to the employee
                String fileEmpNo = columnData[0].trim();
                if (!fileEmpNo.equals(employeeNo)) continue;
                
                attendanceRecords.add(map(columnData));
                
            }
        }

        return attendanceRecords;
    }
    
    public List<AttendanceRecord> findByEmployeeNoAndDate(String employeeNo, LocalDate date) throws IOException {
        List<AttendanceRecord> all = findByEmployeeNo(employeeNo);
        List<AttendanceRecord> filtered = new ArrayList<>();

        for (AttendanceRecord record : all) {
            if (record.getDate() != null && record.getDate().equals(date)) {
                filtered.add(record);
            }
        }

        return filtered;
    }
    
    public AttendanceRecord findOpenRecordForDate(String employeeNo, LocalDate date) throws IOException {
        List<AttendanceRecord> records = findByEmployeeNoAndDate(employeeNo, date);

        AttendanceRecord latestOpen = null;
        for (AttendanceRecord record : records) {
            if (record.getTimeOut() == null) {
                if (latestOpen == null) {
                    latestOpen = record;
                } else if (record.getTimeIn() != null && latestOpen.getTimeIn() != null
                        && record.getTimeIn().isAfter(latestOpen.getTimeIn())) {
                    latestOpen = record;
                }
            }
        }

        return latestOpen;
    }
    
    public void append(AttendanceRecord record, String lastName, String firstName) throws IOException {
        if (record == null) {
            throw new IllegalArgumentException("Attendance record is required.");
        }

        String row = String.join(",",
                safe(record.getEmployeeNo()),
                safe(lastName),
                safe(firstName),
                safe(Dates.formatDate(record.getDate())),
                safe(formatTime(record.getTimeIn())),
                safe(formatTime(record.getTimeOut()))
        );

        appendRow(row);
    }
    
    public void timeOut(String employeeNo, LocalDate date, LocalTime timeIn, LocalTime timeOut) throws IOException {
        ensureFileExistsWithHeader();

        List<String> rows = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        List<String> rewritten = new ArrayList<>();

        boolean updated = false;

        for (int i = 1; i < rows.size(); i++) {
            
            //skip line without data
            String line = rows.get(i);
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] columnData = line.split(",", -1);
            //skip line that dont follow the format
            if (columnData.length < 6) {
                continue;
            }

            String fileEmpNo = columnData[0].trim();
            LocalDate fileDate = Dates.parseDate(columnData[3].trim());
            LocalTime fileTimeIn = parseTimeOrNull(columnData[4].trim());
            LocalTime fileTimeOut = parseTimeOrNull(columnData[5].trim());

            boolean isTarget
                    = fileEmpNo.equals(employeeNo)
                    && fileDate.equals(date)
                    && equalsTime(fileTimeIn, timeIn)
                    && fileTimeOut == null
                    && !updated;

            if (isTarget) {
                columnData[5] = formatTime(timeOut);
                updated = true;
            }

            rewritten.add(toCsvRow(columnData));
        }

        if (!updated) {
            throw new IllegalStateException("Open attendance record not found.");
        }

        rewriteAll(rewritten);
    }

    private AttendanceRecord map(String[] columnData) {
        String employeeNo = columnData[0].trim();
        LocalDate date = Dates.parseDate(columnData[3].trim());
        LocalTime timeIn = parseTimeOrNull(columnData[4].trim());
        LocalTime timeOut = parseTimeOrNull(columnData[5].trim());

        return new AttendanceRecord(employeeNo, date, timeIn, timeOut);
    }

    private LocalTime parseTimeOrNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Dates.parseMilitaryTime(value.trim());
    }

    private String formatTime(LocalTime time) {
        if (time == null) {
            return "";
        }
        return time.toString();
    }

    private boolean equalsTime(LocalTime a, LocalTime b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    private String toCsvRow(String[] columnData) {
        return String.join(",",
                safe(getValue(columnData, 0)),
                safe(getValue(columnData, 1)),
                safe(getValue(columnData, 2)),
                safe(getValue(columnData, 3)),
                safe(getValue(columnData, 4)),
                safe(getValue(columnData, 5))
        );
    }

    private String getValue(String[] arr, int index) {
        return index < arr.length ? arr[index].trim() : "";
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    @Override
    protected void ensureFileExistsWithHeader() throws IOException {
        if (Files.exists(csvPath)) {
            return;
        }

        Path parent = csvPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        String header = "Employee #,Last Name,First Name,Date,Log In,Log Out";
        Files.writeString(csvPath, header + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE);
    }

    @Override
    protected String getHeader() {
        return "Employee #,Last Name,First Name,Date,Log In,Log Out";
    }
}
