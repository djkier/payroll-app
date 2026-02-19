/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.domain.employee;

/**
 *
 * @author djjus
 */
public class CompProfile {
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    
    public CompProfile() {
        this.basicSalary = 0;
        this.riceSubsidy = 0;
        this.phoneAllowance = 0;
        this.clothingAllowance = 0;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getRiceSubsidy() {
        return riceSubsidy;
    }

    public void setRiceSubsidy(double riceSubsidy) {
        this.riceSubsidy = riceSubsidy;
    }

    public double getPhoneAllowance() {
        return phoneAllowance;
    }

    public void setPhoneAllowance(double phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public double getClothingAllowance() {
        return clothingAllowance;
    }

    public void setClothingAllowance(double clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }
    
    public double getSemiMonthlyRate() {
        return this.basicSalary / 2;
    }
    
    public double getHourlyRate() {
        return (this.basicSalary / 21.0) / 8.0;
    }
    
    public double getTotalAllowance() {
        return this.riceSubsidy +
                this.phoneAllowance +
                this.clothingAllowance;
    }
}
