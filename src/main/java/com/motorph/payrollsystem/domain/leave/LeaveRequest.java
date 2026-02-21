/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.leave;

import java.time.LocalDate;

/**
 *
 * @author djjus
 */
public class LeaveRequest {
    private int requestId;
    private String employeeNo;
    
    private LocalDate filedDate;
    private LocalDate leaveStart;
    private LocalDate leaveEnd;
    
    private String subject;
    private String message;
    
    private LeaveStatus status;
    
    private String approvedBy;
    
    public LeaveRequest(
            int requestId,
            String employeeId,
            LocalDate filedDate,
            LocalDate leaveStart,
            LocalDate leaveEnd,
            String subject,
            String message,
            LeaveStatus status,
            String approvedBy){
        
        this.requestId = requestId;
        this.employeeNo = employeeId;
        this.filedDate = filedDate;
        this.leaveStart = leaveStart;
        this.leaveEnd = leaveEnd;
        this.subject = subject == null ? "" : subject;
        this.message = message == null ? "" : message;
        this.status = status == null ? LeaveStatus.PENDING : status;
        this.approvedBy = (approvedBy == null || approvedBy.isBlank()) ? null : approvedBy.trim();
    }
    
    public LeaveRequest() {}

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public LocalDate getFiledDate() {
        return filedDate;
    }

    public void setFiledDate(LocalDate filedDate) {
        this.filedDate = filedDate;
    }

    public LocalDate getLeaveStart() {
        return leaveStart;
    }

    public void setLeaveStart(LocalDate leaveStart) {
        this.leaveStart = leaveStart;
    }

    public LocalDate getLeaveEnd() {
        return leaveEnd;
    }

    public void setLeaveEnd(LocalDate leaveEnd) {
        this.leaveEnd = leaveEnd;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = (approvedBy == null || approvedBy.isBlank()) ? null : approvedBy.trim();
    }
    
    

}
