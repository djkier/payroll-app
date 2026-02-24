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
import com.motorph.payrollsystem.utility.Csv;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.Money;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
/**
 *
 * @author djjus
 */
public class EmployeeRepository {
    private final Path csvPath;
    
    public EmployeeRepository(Path csvPath) {
        this.csvPath = csvPath;
    }
    
    public Employee findByEmployeeNo(String employeeNo) throws IOException {
        ensureFileExistsWithHeader();
        
        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            
            br.readLine();
             while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                //parse csv properly so splitting of "," from an address or salary can be prevented
                String[] details = Csv.parseLine(line);

                if (details.length < 19) continue;
                
                //test output
//                for (String detail : details) {
//                    System.out.println(detail);
//                }

                //validate if the employee no is same from what is looking for
                if (details[0].trim().equals(employeeNo)) {
                    return map(details);
                }
            }
        }

        return null;
    }
    
    private Employee map(String[] details) {
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

        compProfile.setBasicSalary(Money.parseSalary(details[13]));
        compProfile.setRiceSubsidy(Money.parseSalary(details[14]));
        compProfile.setPhoneAllowance(Money.parseSalary(details[15]));
        compProfile.setClothingAllowance(Money.parseSalary(details[16]));

        emp.setContactInfo(contactInfo);
        emp.setGovIds(govIds);
        emp.setDepartmentInfo(departmentInfo);
        emp.setCompProfile(compProfile);
        return emp;
    }
    
    private void ensureFileExistsWithHeader() throws IOException {
        if (Files.exists(csvPath)) return;
        
        //Create leave-request if there is none.
        Path parent = csvPath.getParent();
        if (parent != null) Files.createDirectories(parent);
        
        String header = "Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,Philhealth #,TIN #,Pag-ibig #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate";
        Files.writeString(csvPath, header + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE);
    }

    
    
}
