/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.motorph.payrollsystem.utility;

import com.motorph.payrollsystem.model.employee.CompProfile;
import com.motorph.payrollsystem.model.employee.ContactInfo;
import com.motorph.payrollsystem.model.employee.DepartmentInfo;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.model.employee.GovIds;

/**
 *
 * @author djjus
 */
public class Mapper {
    
    //Mapper for GUI textfields into a Employee class 
    //---- Updating employee information----
    public static Employee buildEmployee(
            Employee selectedEmployee,
            String lastName,
            String firstName,
            String birthday,
            String address,
            String phone,
            String sss,
            String philhealth,
            String tin,
            String pagibig,
            String status,
            String position,
            String supervisor
    ) {
        return buildEmployee(
                selectedEmployee.getEmployeeNo(),
                lastName,
                firstName,
                birthday,
                address,
                phone,
                sss,
                philhealth,
                tin,
                pagibig,
                status,
                position,
                supervisor,
                String.valueOf(selectedEmployee.getCompProfile().getBasicSalary()),
                String.valueOf(selectedEmployee.getCompProfile().getRiceSubsidy()),
                String.valueOf(selectedEmployee.getCompProfile().getPhoneAllowance()),
                String.valueOf(selectedEmployee.getCompProfile().getClothingAllowance())
        );
    }
    
    //---updating employee salary----
    public static Employee buildEmployee(
            Employee selectedEmployee,
            String basicSalary,
            String rice,
            String phoneAllowance,
            String clothingAllowance
    ) {
        CompProfile compProfile = new CompProfile();
        
        compProfile.setBasicSalary(Money.parseSalary(basicSalary));
        compProfile.setRiceSubsidy(Money.parseSalary(rice));
        compProfile.setPhoneAllowance(Money.parseSalary(phoneAllowance));
        compProfile.setClothingAllowance(Money.parseSalary(clothingAllowance));
        
        selectedEmployee.setCompProfile(compProfile);
        return selectedEmployee;
    }
    
    //Base employee mapper
    public static Employee buildEmployee(
            String employeeNo,
            String lastName,
            String firstName,
            String birthday,
            String address,
            String phone,
            String sss,
            String philhealth,
            String tin,
            String pagibig,
            String status,
            String position,
            String supervisor,
            String basicSalary,
            String riceSubsidy,
            String phoneAllowance,
            String clothingAllowance
    ) {
        Employee emp = new Employee();
        ContactInfo contactInfo = new ContactInfo();
        GovIds govIds = new GovIds();
        DepartmentInfo departmentInfo = new DepartmentInfo();
        CompProfile compProfile = new CompProfile();

        emp.setEmployeeNo(employeeNo);
        emp.setLastName(lastName);
        emp.setFirstName(firstName);
        emp.setBirthday(Dates.parseDate(birthday));

        contactInfo.setAddress(address);
        contactInfo.setPhoneNumber(phone);

        govIds.setSssNumber(sss);
        govIds.setPhilHealthNumber(philhealth);
        govIds.setTinNumber(tin);
        govIds.setPagibigNumber(pagibig);

        departmentInfo.setStatus(status);
        departmentInfo.setPosition(position);
        departmentInfo.setSupervisor(supervisor);

        compProfile.setBasicSalary(Money.parseSalary(basicSalary));
        compProfile.setRiceSubsidy(Money.parseSalary(riceSubsidy));
        compProfile.setPhoneAllowance(Money.parseSalary(phoneAllowance));
        compProfile.setClothingAllowance(Money.parseSalary(clothingAllowance));

        emp.setContactInfo(contactInfo);
        emp.setGovIds(govIds);
        emp.setDepartmentInfo(departmentInfo);
        emp.setCompProfile(compProfile);
        return emp;
    }
}
