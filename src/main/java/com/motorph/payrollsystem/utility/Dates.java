/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author djjus
 */
public class Dates {
    
    //01/01/1990
    public static LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }
    
    //22:20
    public static LocalTime parseMilitaryTime(String timeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        return LocalTime.parse(timeStr, formatter);
    }
    
    //10:20 PM
    public static String formattedTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("hh:mm a");
        return dateTime.format(formatter);
    }
    
    //Wednesday
    public static String dayOfWeek(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE");
        return dateTime.format(formatter);
    }
    
    //January 1, 1990
    public static String fullDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        return dateTime.format(formatter);
    }
    
    //January 1, 1990
    public static String fullDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        return date.format(formatter);
    }
    
    //Jan 1, 1990
    public static String shortFullDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        return date.format(formatter);
    }
    
    
}
