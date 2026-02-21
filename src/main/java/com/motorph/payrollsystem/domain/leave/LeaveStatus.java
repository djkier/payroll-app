/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.leave;

/**
 *
 * @author djjus
 */
public enum LeaveStatus {
    PENDING,
    APPROVED,
    REJECTED;
    
    public static LeaveStatus fromCsv(String raw) {
        if ((raw == null) || raw.isBlank()) return PENDING;
        return LeaveStatus.valueOf(raw.trim().toUpperCase());
    }
    
    
}
