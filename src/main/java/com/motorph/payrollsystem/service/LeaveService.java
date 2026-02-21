/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service;

import com.motorph.payrollsystem.domain.leave.LeaveRequest;
import com.motorph.payrollsystem.domain.leave.LeaveStatus;
import com.motorph.payrollsystem.repository.LeaveRepository;
import java.io.IOException;
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
