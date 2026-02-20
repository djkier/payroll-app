/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.payroll;

import com.motorph.payrollsystem.utility.Dates;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

/**
 *
 * @author djjus
 */
public class PayrollPeriod {
    public enum PeriodType {
        MONTHLY, SEMI_MONTHLY
    }
    
    public enum Cutoff {
        FIRST, SECOND, NONE
    }
    
    private final LocalDate startDate;
    private final LocalDate endDate;
    private PeriodType periodType;
    private Cutoff cutoff;
    private final YearMonth month;
    
    public PayrollPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodType = null;
        this.cutoff = null;
        this.month = YearMonth.from(startDate);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public Cutoff getCutoff() {
        return cutoff;
    }

    public YearMonth getMonth() {
        return month;
    }
    
    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }
    
    public void setCutoff(Cutoff cutoff) {
        this.cutoff = cutoff;
    }
    
    public boolean isFirstCutoff() {
        return periodType == PeriodType.SEMI_MONTHLY && cutoff == Cutoff.FIRST;
    }
    
    public boolean isSecondCutoff() {
        return periodType == PeriodType.SEMI_MONTHLY && cutoff == Cutoff.SECOND;
    }
    
    public boolean isMonthly() {
        return periodType == PeriodType.MONTHLY;
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
                Objects.equals(endDate, that.endDate) &&
                periodType == that.periodType &&
                cutoff == that.cutoff;
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
