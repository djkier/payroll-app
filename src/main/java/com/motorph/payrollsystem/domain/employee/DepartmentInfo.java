/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.employee;

import com.motorph.payrollsystem.utility.DepartmentResolver;

/**
 *
 * @author djjus
 */
public class DepartmentInfo {
    private String status;
    private String supervisor;
    private String position;
    
    
    public DepartmentInfo() {
        this.status = null;
        this.supervisor = null;
        this.position = null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getDepartment() {
        return DepartmentResolver.getDepartmentName(this.position);
    }
    
    
}
