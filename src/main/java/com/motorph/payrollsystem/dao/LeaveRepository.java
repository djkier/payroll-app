/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.dao;

import com.motorph.payrollsystem.model.leave.LeaveRequest;
import com.motorph.payrollsystem.model.leave.LeaveStatus;
import com.motorph.payrollsystem.utility.Csv;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author djjus
 */
public class LeaveRepository {
    private final Path csvPath;
    
    public LeaveRepository(Path csvPath) {
        this.csvPath = csvPath;
    }
    
    public List<LeaveRequest> getEmployeeLeaveHistory(String employeeNo) throws IOException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        ensureFileExistsWithHeader();

        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)){
            String line;
            //skip header
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] cols = Csv.parseLine(line);
                if (cols.length < 9) continue;
                if (!cols[1].trim().equals(employeeNo)) continue;

                leaveRequests.add(map(cols));
            }
        }
        
        leaveRequests.sort(Comparator.comparing(LeaveRequest::getFiledDate).reversed());
        
        return leaveRequests;
    }
    
    //get the last request_id + 1
    public int getNextRequestId() throws IOException {
        ensureFileExistsWithHeader();
        int max = 0;
        
        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null){
                if (line.trim().isEmpty()) continue;
                String[] cols = Csv.parseLine(line);
                if (cols.length < 1) continue;
                
                try {
                    int id = Integer.parseInt(cols[0].trim());
                    if (id > max) max = id;
                } catch (NumberFormatException ignore) {}
            }
        }
        return max + 1;
    }
    
    //add new entry on leave
    public void append(LeaveRequest req) throws IOException {
        ensureFileExistsWithHeader();

        String row = toCsvRow(req);
        
        boolean needsNewLine = false;
        
        if (Files.size(csvPath) > 0) {
            byte[] lastByte = new byte[1];
            try (RandomAccessFile raf = new RandomAccessFile(csvPath.toFile(), "r")) {
                raf.seek(raf.length() - 1);
                raf.read(lastByte);
            }
            char lastChar = (char) lastByte[0];
            if (lastChar != '\n') {
                needsNewLine = true;
            }
        }
        
        String contentToAppend = (needsNewLine ? System.lineSeparator() : "") + row + System.lineSeparator();

        Files.writeString(csvPath, contentToAppend,
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND);

    }
    
    //Convert a line (cols) into a LeaveRequest object
    private LeaveRequest map(String[] cols) {
        LeaveRequest request = new LeaveRequest();
        request.setRequestId(Integer.parseInt(cols[0]));
        request.setEmployeeNo(cols[1]);
        
        request.setFiledDate(LocalDate.parse(cols[2]));
        request.setLeaveStart(LocalDate.parse(cols[3]));
        request.setLeaveEnd(LocalDate.parse(cols[4]));
        
        request.setSubject(cols[5]);
        request.setMessage(cols[6]);
        
        request.setStatus(LeaveStatus.fromCsv(cols[7]));
        request.setApprovedBy(cols[8]);
        
        return request;
    }
    
    //Check if leave-request is existing with correct header
    private void ensureFileExistsWithHeader() throws IOException {
        if (Files.exists(csvPath)) return;
        
        //Create leave-request if there is none.
        Path parent = csvPath.getParent();
        if (parent != null) Files.createDirectories(parent);
        
        String header = "request_id,employee_id,filed_date,leave_start,leave_end,subject,message,status,approved_by";
        Files.writeString(csvPath, header + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE);
    }
    
    
    
    //make a csv line
    private String toCsvRow(LeaveRequest req) {
        String approvedBy = req.getApprovedBy() == null ? "" : req.getApprovedBy();
        
        return req.getRequestId() + "," +
                req.getEmployeeNo() + "," +
                req.getFiledDate() + "," +
                req.getLeaveStart() + "," +
                req.getLeaveEnd() + "," +
                csvQuote(req.getSubject()) + "," +
                csvQuote(req.getMessage()) + "," +
                req.getStatus() + "," +
                approvedBy;
    }
    
    private String csvQuote(String str) {
        if (str == null) str = "";
        //escape internal quote
        String escaped = str.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
