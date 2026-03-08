/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.model.attendance;

/**
 *
 * @author djjus
 */
public class AttendanceState {
    private final boolean canTimeIn;
    private final boolean canTimeOut;
    private final boolean currentlyTimedIn;
    private final AttendanceRecord openRecordToday;

    public AttendanceState(
            boolean canTimeIn,
            boolean canTimeOut,
            boolean currentlyTimedIn,
            AttendanceRecord openRecordToday
    ) {
        this.canTimeIn = canTimeIn;
        this.canTimeOut = canTimeOut;
        this.currentlyTimedIn = currentlyTimedIn;
        this.openRecordToday = openRecordToday;
    }

    public boolean canTimeIn() {
        return canTimeIn;
    }

    public boolean canTimeOut() {
        return canTimeOut;
    }

    public boolean isCurrentlyTimedIn() {
        return currentlyTimedIn;
    }

    public AttendanceRecord getOpenRecordToday() {
        return openRecordToday;
    }
}
