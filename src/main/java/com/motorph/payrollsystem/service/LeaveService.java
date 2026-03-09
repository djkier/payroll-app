/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.service;

import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.model.leave.LeaveRequest;
import com.motorph.payrollsystem.model.leave.LeaveStatus;
import com.motorph.payrollsystem.dao.LeaveRepository;
import com.motorph.payrollsystem.model.employee.Employee;
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
        req.setApprovedById(null);
        
        //save
        repo.append(req);
        
        return req;
        
    }
    
    public List<LeaveRequest> getEmployeeLeaveHistory(String employeeNo) throws IOException {
        return repo.getEmployeeLeaveHistory(employeeNo);
    }
    
    public Map<LeaveStatus, Integer> getStatusCounts(String employeeNo) throws IOException {
        List<LeaveRequest> list = getEmployeeLeaveHistory(employeeNo);
        
        Map<LeaveStatus, Integer> counts = new EnumMap<>(LeaveStatus.class);
        for (LeaveStatus status : LeaveStatus.values()) {
            counts.put(status, 0);
        }

        for (LeaveRequest request : list) {
            counts.put(request.getStatus(), counts.get(request.getStatus()) + 1);
        }
        
        return counts;
    }
    
    public List<LeaveRequest> getPendingRequestsForReview(AccessPolicy policy) throws IOException {
        validateReviewPermission(policy);
        return repo.getPendingRequestsOldestFirst();
    }

    public List<LeaveRequest> getDecisionHistory(AccessPolicy policy) throws IOException {
        validateReviewPermission(policy);
        return repo.getDecidedRequestsNewestFirst();
    }

    public LeaveRequest findRequestById(int requestId, AccessPolicy policy) throws IOException {
        validateReviewPermission(policy);

        if (requestId <= 0) {
            throw new IllegalArgumentException("Invalid request ID.");
        }

        LeaveRequest request = repo.findByRequestId(requestId);
        if (request == null) {
            throw new IllegalStateException("Leave request not found.");
        }

        return request;
    }

    public LeaveRequest approveRequest(int requestId, Employee reviewer, AccessPolicy policy) throws IOException {
        return decideRequest(requestId, reviewer, policy, LeaveStatus.APPROVED);
    }

    public LeaveRequest rejectRequest(int requestId, Employee reviewer, AccessPolicy policy) throws IOException {
        return decideRequest(requestId, reviewer, policy, LeaveStatus.REJECTED);
    }

    public boolean canReviewerDecide(LeaveRequest request, Employee reviewer, AccessPolicy policy) {
        if (request == null) return false;
        if (reviewer == null) return false;
        if (policy == null || !policy.canReviewLeaveRequests()) return false;
        if (reviewer.getEmployeeNo() == null || reviewer.getEmployeeNo().isBlank()) return false;
        if (request.getEmployeeNo() != null && request.getEmployeeNo().equals(reviewer.getEmployeeNo())) return false;

        return request.getStatus() == LeaveStatus.PENDING;
    }

    private LeaveRequest decideRequest(
            int requestId,
            Employee reviewer,
            AccessPolicy policy,
            LeaveStatus decision
    ) throws IOException {

        validateReviewPermission(policy);

        if (reviewer == null) {
            throw new IllegalArgumentException("Reviewer is required.");
        }

        if (reviewer.getEmployeeNo() == null || reviewer.getEmployeeNo().isBlank()) {
            throw new IllegalArgumentException("Reviewer employee ID is required.");
        }

        if (requestId <= 0) {
            throw new IllegalArgumentException("Invalid request ID.");
        }

        if (decision != LeaveStatus.APPROVED && decision != LeaveStatus.REJECTED) {
            throw new IllegalArgumentException("Invalid leave decision.");
        }

        LeaveRequest request = repo.findByRequestId(requestId);
        if (request == null) {
            throw new IllegalStateException("Leave request not found.");
        }

        if (request.getEmployeeNo() != null && request.getEmployeeNo().equals(reviewer.getEmployeeNo())) {
            throw new IllegalStateException("You cannot review your own leave request.");
        }

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("This leave request has already been decided.");
        }

        request.setStatus(decision);
        request.setApprovedById(reviewer.getEmployeeNo());

        repo.update(request);

        return repo.findByRequestId(requestId);
    }

    private void validateReviewPermission(AccessPolicy policy) {
        if (policy == null || !policy.canReviewLeaveRequests()) {
            throw new SecurityException("Access denied. You are not allowed to review leave requests.");
        }
    }
}
    

