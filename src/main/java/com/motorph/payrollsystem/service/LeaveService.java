/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service;

import com.motorph.payrollsystem.model.leave.LeaveRequest;
import com.motorph.payrollsystem.model.leave.LeaveStatus;
import com.motorph.payrollsystem.dao.LeaveRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author djjus
 */
public class LeaveService {
    private final LeaveRepository repo;
    
    public LeaveService(LeaveRepository repo) {
        this.repo = repo;
    }
    
    public LeaveRequest addNewRequest(
        String employeeNo,
        String subject,
        LocalDate startDate,
        LocalDate endDate,
        String message
    ) throws IOException {
        
        //Validate fields
        if (employeeNo == null || employeeNo.isBlank())
            throw new IllegalArgumentException("Employee No is required.");
            
        if (subject == null || subject.isBlank())
            throw new IllegalArgumentException("Subject is required.");
        
        if (startDate == null)
            throw new IllegalArgumentException("Start date is required.");
        
        if (endDate == null)
            throw new IllegalArgumentException("End date is required.");
        
        if (message == null || message.isBlank())
            throw new IllegalArgumentException("Message is required.");
        
        //Validate date range
        if (endDate.isBefore(startDate)) 
            throw new IllegalArgumentException("End date cannot be before start date.");
        
        //Create request
        LeaveRequest req = new LeaveRequest();
        req.setRequestId(repo.getNextRequestId());
        req.setEmployeeNo(employeeNo);
        req.setFiledDate(LocalDate.now());
        req.setLeaveStart(startDate);
        req.setLeaveEnd(endDate);
        req.setSubject(subject.trim());
        req.setMessage(message.trim());
        req.setStatus(LeaveStatus.PENDING);
        req.setApprovedBy(null);
        
        //save
        repo.append(req);
        
        return req;
        
    }
    
    public List<LeaveRequest> getLeaveHistory(String employeeNo) throws IOException {
        return repo.findByEmployeeNo(employeeNo);
    }
    
    public Map<LeaveStatus, Integer> getStatusCounts(String employeeNo) throws IOException {
        List<LeaveRequest> list = getLeaveHistory(employeeNo);
        
        Map<LeaveStatus, Integer> counts = new EnumMap<>(LeaveStatus.class);
        for (LeaveStatus status : LeaveStatus.values()) counts.put(status, 0);
        
        for (LeaveRequest request : list) {
            counts.put(request.getStatus(), counts.get(request.getStatus()) + 1);
        }
        
        return counts;
    }
    
}
