/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.employee;

import java.time.LocalDate;

/**
 *
 * @author djjus
 */
public class Employee {
    private String employeeNo;
    private String lastName;
    private String firstName;
    private LocalDate birthday;
    private String status;
    private String position;
    private String supervisor;
    private ContactInfo contactInfo;
    private GovIds govIds;
    private CompProfile compProfile;
    
    public Employee() {
        this.employeeNo = null;
        this.lastName = null;
        this.firstName = null;
        this.birthday = null;
        this.status = null;
        this.position = null;
        this.supervisor = null;
        this.contactInfo = new ContactInfo();
        this.govIds = new GovIds();
        this.compProfile = new CompProfile();
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public GovIds getGovIds() {
        return govIds;
    }

    public void setGovIds(GovIds govIds) {
        this.govIds = govIds;
    }

    public CompProfile getCompProfile() {
        return compProfile;
    }

    public void setCompProfile(CompProfile compProfile) {
        this.compProfile = compProfile;
    }
    
    
}
