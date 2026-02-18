/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author djjus
 */
public class Dates {
    public static LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }
    
    public static String formattedTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("hh:mm a");
        return dateTime.format(formatter);
    }
    
    public static String dayOfWeek(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE");
        return dateTime.format(formatter);
    }
    
    public static String fullDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        return dateTime.format(formatter);
    }
}
