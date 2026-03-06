/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.dao;

import com.motorph.payrollsystem.model.employee.CompProfile;
import com.motorph.payrollsystem.model.employee.ContactInfo;
import com.motorph.payrollsystem.model.employee.DepartmentInfo;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.model.employee.GovIds;
import com.motorph.payrollsystem.utility.Csv;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.Money;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
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
    
    public List<Employee> getEmployeeList() throws IOException {
        List<Employee> employeeList = new ArrayList<>();
        ensureFileExistsWithHeader();
        
        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line;
            br.readLine();
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] details = Csv.parseLine(line);
                if (details.length < 19) continue;
                
                Employee emp = map(details);
                employeeList.add(emp);

            }
        }
        
        return employeeList;
    }
    
    public void update(Employee updated) throws IOException {
        List<Employee> employeeList = getEmployeeList();
        
        boolean replaced = false;
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getEmployeeNo().equals(updated.getEmployeeNo())) {
                employeeList.set(i, updated);
                replaced = true;
                break;
            }
        }
        
        if (!replaced) {
            throw new IllegalStateException("Employee not found: " + updated.getEmployeeNo());
        }
        
//        writeAllatomically(employeeList);
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
        
        Files.writeString(csvPath, getHeader()+ System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE);
    }

    private void rewriteCsvFile(List<Employee> employeeList) throws IOException {
        ensureFileExistsWithHeader();
        
        Path parent = csvPath.getParent();
        if (parent != null) Files.createDirectories(parent);
        
        //make temporary file location to avoid writing a csv file that is currently being used
        Path tmp = csvPath.resolveSibling(csvPath.getFileName().toString() + ".tmp");
        
        try (BufferedWriter bw = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            
            bw.write(getHeader());
            bw.newLine();
            
            for (Employee emp : employeeList) {
                bw.write(toCsvRow(emp));
                bw.newLine();
            }
        }
        
        //replace original
        Files.move(tmp, csvPath,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
        
    }
    
    private String getHeader() {
        return "Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,Philhealth #,TIN #,Pag-ibig #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate";
    }
    
    private String toCsvRow(Employee emp) {
        ContactInfo contactInfo = emp.getContactInfo();
        GovIds govIds = emp.getGovIds();
        DepartmentInfo deptInfo = emp.getDepartmentInfo();
        CompProfile compProf = emp.getCompProfile();
        
        
        //Personal Details
        String empNo = safe(emp.getEmployeeNo());
        String last = safe(emp.getLastName());
        String first = safe(emp.getFirstName());
        String birthday = Dates.formatDate(emp.getBirthday());
        //Contact info
        String address = (contactInfo == null) ? "" : safe(contactInfo.getAddress());
        String phone = (contactInfo == null) ? "" : safe(contactInfo.getPhoneNumber());
        //Gov IDs
        String sss = (govIds == null) ? "" : safe(govIds.getSssNumber());
        String philHealth = (govIds == null) ? "" : safe(govIds.getPhilHealthNumber());
        String tin = (govIds == null) ? "" : safe(govIds.getTinNumber());
        String pagibig = (govIds == null) ? "" : safe(govIds.getPagibigNumber());
        //Department Info
        String status = (deptInfo == null) ? "" : safe(deptInfo.getStatus());
        String position =(deptInfo == null) ? "" : safe(deptInfo.getPosition());
        String supervisor = (deptInfo == null) ? "" : safe(deptInfo.getSupervisor());
        //Compensation Profile
        String basic = (compProf == null) ? "" : Money.formatSalary(compProf.getBasicSalary());
        String rice = (compProf == null) ? "" : Money.formatSalary(compProf.getRiceSubsidy());
        String phoneAll = (compProf == null) ? "" : Money.formatSalary(compProf.getPhoneAllowance());
        String clothingAll = (compProf == null) ? "" : Money.formatSalary(compProf.getClothingAllowance());
        //Derived values
        String semiMonthly = (compProf == null) ? "" : Money.formatSalary(compProf.getSemiMonthlyRate());
        String hourlyRate = (compProf == null) ? "" : Money.formatSalary(compProf.getHourlyRate());
        
        return String.join(",", 
                csv(empNo), csv(last), csv(first), csv(birthday),
                csv(address), csv(phone),
                csv(sss), csv(philHealth), csv(tin), csv(pagibig),
                csv(status), csv(position), csv(supervisor),
                csv(basic), csv(rice), csv(phoneAll), csv(clothingAll),
                csv(semiMonthly), csv(hourlyRate));  
    }
    
    private String safe(String str) {
        return str == null ? "" : str.trim();
    }
    
    private String csv(String str) {
        if (str == null) return "";
        boolean mustQuote = str.contains(",") || 
                    str.contains("\"") ||
                    str.contains("\n") ||
                    str.contains("\r");
        
        String cleanStr = str.replace("\"", "\"\"");
        return mustQuote ? "\"" + cleanStr + "\"" : cleanStr;
    }
    
    
}
