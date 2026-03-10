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
public class LeaveRepository extends CsvRepositoryBase {
    
    public LeaveRepository(Path csvPath) {
        super(csvPath);
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
                
                String[] cols = parseLine(line);
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
                String[] cols = parseLine(line);
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
        appendRow(toCsvRow(req));
    }
    
    public List<LeaveRequest> getAllRequests() throws IOException {
        List<LeaveRequest> requests = new ArrayList<>();
        ensureFileExistsWithHeader();

        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] cols = parseLine(line);
                if (cols.length < 9) continue;

                requests.add(map(cols));
            }
        }

        return requests;
    }
    
    public LeaveRequest findByRequestId(int requestId) throws IOException {
        ensureFileExistsWithHeader();

        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] cols = parseLine(line);
                if (cols.length < 9) continue;

                LeaveRequest request = map(cols);
                if (request.getRequestId() == requestId) {
                    return request;
                }
            }
        }

        return null;
    }
    
    public List<LeaveRequest> getPendingRequestsOldestFirst() throws IOException {
        List<LeaveRequest> pending = new ArrayList<>();

        for (LeaveRequest request : getAllRequests()) {
            if (request.getStatus() == LeaveStatus.PENDING) {
                pending.add(request);
            }
        }

        pending.sort(
            Comparator.comparing(LeaveRequest::getFiledDate)
                      .thenComparingInt(LeaveRequest::getRequestId)
        );

        return pending;
    }
    
    public List<LeaveRequest> getDecidedRequestsNewestFirst() throws IOException {
        List<LeaveRequest> decided = new ArrayList<>();

        for (LeaveRequest request : getAllRequests()) {
            if (request.getStatus() != LeaveStatus.PENDING) {
                decided.add(request);
            }
        }

        decided.sort(
            Comparator.comparing(LeaveRequest::getFiledDate)
                      .reversed()
                      .thenComparing(Comparator.comparingInt(LeaveRequest::getRequestId).reversed())
        );

        return decided;
    }
    
    public void update(LeaveRequest updated) throws IOException {
        if (updated == null) {
            throw new IllegalArgumentException("Updated leave request cannot be null.");
        }

        ensureFileExistsWithHeader();

        List<LeaveRequest> allRequests = getAllRequests();
        List<String> rows = new ArrayList<>();

        boolean found = false;

        for (LeaveRequest request : allRequests) {
            if (request.getRequestId() == updated.getRequestId()) {
                rows.add(toCsvRow(updated));
                found = true;
            } else {
                rows.add(toCsvRow(request));
            }
        }

        if (!found) {
            throw new IllegalStateException("Leave request not found: " + updated.getRequestId());
        }

        rewriteAll(rows);
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
        request.setApprovedById(cols[8]);
        
        return request;
    }
    
    //Check if leave-request is existing with correct header
    @Override
    protected void ensureFileExistsWithHeader() throws IOException {
        if (Files.exists(csvPath)) return;
        
        //Create leave-request if there is none.
        Path parent = csvPath.getParent();
        if (parent != null) Files.createDirectories(parent);
        
        
        Files.writeString(csvPath, getHeader() + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE);
    }
    
    @Override
    protected String getHeader() {
        return "request_id,employee_id,filed_date,leave_start,leave_end,subject,message,status,approved_by_id";
    }
    
    
    
    //make a csv line
    private String toCsvRow(LeaveRequest req) {
        String approvedBy = req.getApprovedById() == null ? "" : req.getApprovedById();
        
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
