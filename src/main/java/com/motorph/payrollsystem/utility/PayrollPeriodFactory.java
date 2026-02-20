/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

import com.motorph.payrollsystem.domain.payroll.PayrollPeriod;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author djjus
 */
public class PayrollPeriodFactory {
    public static List<PayrollPeriod> fromAttendanceDatesSemiMonthly(List<LocalDate> dates) {
        Set<PayrollPeriod> unique = new HashSet<>();
        PayrollPeriod.Cutoff cutoff;
        
        for(LocalDate date : dates) {
            YearMonth ym = YearMonth.from(date);
            
            LocalDate start;
            LocalDate end;
            
            if (date.getDayOfMonth() <= 15) {
                start = ym.atDay(1);
                end = ym.atDay(15);
                cutoff = PayrollPeriod.Cutoff.FIRST;
            } else {
                start = ym.atDay(16);
                end = ym.atEndOfMonth();
                cutoff = PayrollPeriod.Cutoff.SECOND;
            }
            
            PayrollPeriod payrollPeriod = new PayrollPeriod(start, end);
            payrollPeriod.setPeriodType(PayrollPeriod.PeriodType.SEMI_MONTHLY);
            payrollPeriod.setCutoff(cutoff);
            unique.add(payrollPeriod);
        }
        
        List<PayrollPeriod> list = new ArrayList<>(unique);
        list.sort(Comparator.comparing(PayrollPeriod::getStartDate).reversed());
        return list;
        
    }
    
    //get the payroll period to for the first cutoff to be use by 2nd cutoff reconciliation
    public static PayrollPeriod firstCutoffOf(YearMonth ym) {
        PayrollPeriod firstCutoff = new PayrollPeriod(ym.atDay(1), ym.atDay(15));
        firstCutoff.setPeriodType(PayrollPeriod.PeriodType.SEMI_MONTHLY);
        firstCutoff.setCutoff(PayrollPeriod.Cutoff.FIRST);
        return firstCutoff;
    }
    
    public static List<PayrollPeriod> fromAttendanceDatesMonthly(List<LocalDate> dates) {
        //EDIT FOR FUTURE INTEGRATION WHEN MONTHLY SALARY IS NEEDED
        List<PayrollPeriod> list = new ArrayList<>();
        return list;
    }
}
