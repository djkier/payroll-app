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
    public static List<PayrollPeriod> fromAttendanceDates(List<LocalDate> dates) {
        Set<PayrollPeriod> unique = new HashSet<>();
        
        for(LocalDate date : dates) {
            YearMonth ym = YearMonth.from(date);
            
            LocalDate start;
            LocalDate end;
            
            if (date.getDayOfMonth() <= 15) {
                start = ym.atDay(1);
                end = ym.atDay(15);
            } else {
                start = ym.atDay(16);
                end = ym.atEndOfMonth();
            }
            
            unique.add(new PayrollPeriod(start, end));
        }
        
        List<PayrollPeriod> list = new ArrayList<>(unique);
        list.sort(Comparator.comparing(PayrollPeriod::getStartDate).reversed());
        return list;
        
    }
}
