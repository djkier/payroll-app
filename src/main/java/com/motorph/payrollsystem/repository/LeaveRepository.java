/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.repository;

import com.motorph.payrollsystem.domain.leave.LeaveRequest;
import com.motorph.payrollsystem.domain.leave.LeaveStatus;
import com.motorph.payrollsystem.utility.Csv;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author djjus
 */
public class LeaveRepository {
    private final String resourcePath;
    
    public LeaveRepository(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    
    public List<LeaveRequest> findByEmployeeNo(String employeeNo) throws IOException {
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IOException("Path can't find: " + resourcePath);
        }
        
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] cols = Csv.parseLine(line);
                if (cols.length < 9) continue;
                
                if (!cols[1].equals(employeeNo)) continue;
                
                LeaveRequest req = map(cols);
                leaveRequests.add(req);
            }
        }
        
        leaveRequests.sort(Comparator.comparing(LeaveRequest::getFiledDate).reversed());
        return leaveRequests;
    }
    
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
}
