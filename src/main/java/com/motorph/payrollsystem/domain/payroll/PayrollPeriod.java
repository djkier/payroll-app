/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.payroll;

import com.motorph.payrollsystem.utility.Dates;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author djjus
 */
public class PayrollPeriod {
    private final LocalDate startDate;
    private final LocalDate endDate;
    
    public PayrollPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    
    //override equal comparison of this class
    @Override
    public boolean equals(Object other) {
        //if it is the same object and instance then return true
        if (this == other) return true;
        
        //if the other object is not an instance of PayrollPeriod return false
        if (!(other instanceof PayrollPeriod)) return false;
        
        //Convert it to PayrollPeriod instance then check for equality
        PayrollPeriod that = (PayrollPeriod) other;
        return Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }
    
    //Override hashcode so it can be use for hashset
    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
    
    //Override toString() so it can be use on the combobox 
    @Override
    public String toString() {
        return Dates.shortFullDate(startDate) + " - " + Dates.shortFullDate(endDate);
    }
}
