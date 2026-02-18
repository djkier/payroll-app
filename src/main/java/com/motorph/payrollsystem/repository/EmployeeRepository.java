/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.repository;

import com.motorph.payrollsystem.domain.employee.CompProfile;
import com.motorph.payrollsystem.domain.employee.ContactInfo;
import com.motorph.payrollsystem.domain.employee.DepartmentInfo;
import com.motorph.payrollsystem.domain.employee.Employee;
import com.motorph.payrollsystem.domain.employee.GovIds;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.Money;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djjus
 */
public class EmployeeRepository {
    private final String resourcePath;
    
    public EmployeeRepository(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    
    public Employee findByEmployeeNo(String employeeNo) throws IOException {
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IOException("Path can't find: " + resourcePath);
        }
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            
            br.readLine();
             while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                //parse csv properly so splitting of "," from an address or salary can be prevented
                String[] details = parseCsvLine(line);

                if (details.length < 19) continue;
                
                //test output
//                for (String detail : details) {
//                    System.out.println(detail);
//                }
                

                //validate if the employee no is same from what is looking for
                if (details[0].equals(employeeNo)) {
                    Employee emp = new Employee();
                    ContactInfo contactInfo = new ContactInfo();
                    GovIds govIds = new GovIds();
                    DepartmentInfo departmentInfo = new DepartmentInfo();
                    CompProfile compProfile = new CompProfile();
                    
                    emp.setEmployeeNo(details[0]);
                    emp.setLastName(details[1]);
                    emp.setFirstName(details[2]);
                    emp.setBirthday(Dates.parseDate(details[3]));
                    
                    contactInfo.setAddress(details[4]);
                    contactInfo.setPhoneNumber(details[5]);
                    
                    govIds.setSssNumber(details[6]);
                    govIds.setPhilHealthNumber(details[7]);
                    govIds.setTinNumber(details[8]);
                    govIds.setPagibigNumber(details[9]);
                    
                    departmentInfo.setStatus(details[10]);
                    departmentInfo.setPosition(details[11]);
                    departmentInfo.setSupervisor(details[12]);
                    
                    compProfile.setBasicSalary(Money.parseSalary(details[15]));
                    compProfile.setRiceSubsidy(Money.parseSalary(details[16]));
                    compProfile.setPhoneAllowance(Money.parseSalary(details[17]));
                    compProfile.setClothingAllowance(Money.parseSalary(details[18]));

                    emp.setContactInfo(contactInfo);
                    emp.setGovIds(govIds);
                    emp.setDepartmentInfo(departmentInfo);
                    emp.setCompProfile(compProfile);
                    return emp;
                }
            }
        }

        return null;
    }

    //will return an array of clean strings 
    private String[] parseCsvLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
                continue;
            }

            if (c == ',' && !inQuotes) {
                out.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        out.add(sb.toString().trim());
        return out.toArray(new String[0]);
    }
    
}
