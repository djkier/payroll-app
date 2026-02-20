/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.GUI.rightsidepanels;

import com.motorph.payrollsystem.app.AppContext;
import com.motorph.payrollsystem.domain.attendance.AttendanceRecord;
import com.motorph.payrollsystem.domain.employee.Employee;
import com.motorph.payrollsystem.domain.payroll.PayrollPeriod;
import com.motorph.payrollsystem.domain.payroll.Payslip;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.Money;
import com.motorph.payrollsystem.utility.PayrollPeriodFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author djjus
 */
public class SalaryPanel extends javax.swing.JPanel {

    /**
     * Creates new form HomePanel
     * @param appContext the current logged in user/employee
     */
    public SalaryPanel(AppContext appContext) {
        this.appContext = appContext;
        initComponents();
        loadPayrollPeriods();
        
        

    }
    
    private void loadPayrollPeriods() {
        try {
            //get employee no
            String employeeNo = 
                    appContext.getSessionManager()
                    .getCurrentEmployee()
                    .getEmployeeNo();
            
            //get all attendance records of the employee no
            List<AttendanceRecord> records = 
                    appContext.getAttendanceRepository()
                    .findByEmployeeNo(employeeNo);
            
            //initialize list of dates
            List<LocalDate> dates = new ArrayList<>();
            
            //get all the dates on the attendance record then store it on dates
            for (AttendanceRecord record : records) {
                dates.add(record.getDate());
            }
            
            //get the list of PayrollPeriod
            List<PayrollPeriod> periods = 
                    PayrollPeriodFactory.fromAttendanceDates(dates);
            
            //remove all items on the combo box first
            this.comboBoxPeriod.removeAllItems();
            
            //populate the combo box items with the period
            for (PayrollPeriod period : periods) {
                comboBoxPeriod.addItem(period);
            }
        } catch (Exception ex) {
            /*
            *
            *Add failed loading payroll periods
            *
            */
            ex.printStackTrace();
        }
        
        //prevent uncorrect salary data
        if (this.comboBoxPeriod.getItemCount() > 0) {
            this.comboBoxPeriod.setSelectedIndex(0);
        }
    }

    private void changeSalaryUI(Payslip payslip) {
        //For testing incorrect data
//        System.out.println("Period: " + payslip.getPeriod().toString());
//        System.out.println("Basic Salary: " + payslip.getEmployee().getCompProfile().getBasicSalary());
//        System.out.println("Total hours: " + payslip.getTotalHours());
//        System.out.println("Hourly rate: " + payslip.getEmployee().getCompProfile().getHourlyRate());
//        System.out.println("Gross pay: " + payslip.getGrossPay()); 
//        
        //MAIN
        //Main summary
        mainGrossText.setText(Money.displayMoney(payslip.getGrossPay()));
        mainSssText.setText(Money.displayMoney(payslip.getSssAmount()));
        System.out.println("SSS amount " + payslip.getSssAmount());
        mainPhilHealthText.setText(Money.displayMoney(payslip.getPhilHealthAmount()));
        mainPagIbigText.setText(Money.displayMoney(payslip.getPagibigAmount()));
        //Main tax
        mainTaxableText.setText(Money.displayMoney(payslip.getTaxableIncome()));
        mainTaxText.setText(Money.displayMoney(payslip.getTaxAmount()));
        //Main Net
        mainNetPayText.setText(Money.displayMoney(payslip.getNetPay()));
        
        //PREVIEW
        //Employee Information
        Employee employee = payslip.getEmployee();
        employeeNoText.setText(employee.getEmployeeNo());
        nameText.setText(employee.getFullName());
        positionText.setText(employee.getDepartmentInfo().getPosition());
        //Period Information
        startText.setText(Dates.fullDate(payslip.getPeriod().getStartDate()));
        endText.setText(Dates.fullDate(payslip.getPeriod().getEndDate()));
        //Earnings
        basicText.setText(Money.displayMoney(payslip.getGrossPay()));
        riceText.setText(Money.displayMoney(payslip.getRiceAmount()));
        phoneText.setText(Money.displayMoney(payslip.getPhoneAmount()));
        clothingText.setText(Money.displayMoney(payslip.getClothingAmount()));
        grossText.setText(Money.displayMoney(payslip.getGrossPay()));
        //Deductions
        sssText.setText(Money.displayMoney(payslip.getSssAmount()));
        philHealthText.setText(Money.displayMoney(payslip.getPhilHealthAmount()));
        pagIbigText.setText(Money.displayMoney(payslip.getPagibigAmount()));
        taxText.setText(Money.displayMoney(payslip.getTaxAmount()));
        totalDeductionText.setText(Money.displayMoney(payslip.getTotalDeductions()));
        //Summary
        gross2Text.setText(Money.displayMoney(payslip.getGrossPay()));
        totalDeduction2Text.setText(Money.displayMoney(payslip.getTotalDeductions()));
        
        previewNetPayText.setText(Money.displayMoney(payslip.getNetPay()));
        
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        payslipViewScrollPane = new javax.swing.JScrollPane();
        payslipView = new javax.swing.JPanel();
        payslipLayer = new javax.swing.JPanel();
        decorLine1 = new javax.swing.JPanel();
        motorPhIcon = new javax.swing.JLabel();
        companyName = new javax.swing.JLabel();
        address2 = new javax.swing.JLabel();
        address1 = new javax.swing.JLabel();
        empLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        positionLabel = new javax.swing.JLabel();
        startLabel = new javax.swing.JLabel();
        endLabel = new javax.swing.JLabel();
        decorLine2 = new javax.swing.JPanel();
        earningsLabel = new javax.swing.JLabel();
        basicLabel = new javax.swing.JLabel();
        riceLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        clothingLabel = new javax.swing.JLabel();
        basicText = new javax.swing.JLabel();
        riceText = new javax.swing.JLabel();
        phoneText = new javax.swing.JLabel();
        clothingText = new javax.swing.JLabel();
        grossLabel = new javax.swing.JLabel();
        grossText = new javax.swing.JLabel();
        decorLine5 = new javax.swing.JPanel();
        deductionsLabel = new javax.swing.JLabel();
        sssLabel = new javax.swing.JLabel();
        philhealthLabel = new javax.swing.JLabel();
        pagibigLabel = new javax.swing.JLabel();
        taxLabel = new javax.swing.JLabel();
        sssText = new javax.swing.JLabel();
        philHealthText = new javax.swing.JLabel();
        pagIbigText = new javax.swing.JLabel();
        taxText = new javax.swing.JLabel();
        totalDeductionLabel = new javax.swing.JLabel();
        totalDeductionText = new javax.swing.JLabel();
        decorLine6 = new javax.swing.JPanel();
        summaryLabel = new javax.swing.JLabel();
        gross2Label = new javax.swing.JLabel();
        deduction2Label = new javax.swing.JLabel();
        gross2Text = new javax.swing.JLabel();
        totalDeduction2Text = new javax.swing.JLabel();
        netPanel = new javax.swing.JPanel();
        previewNetPayLabel = new javax.swing.JLabel();
        previewNetPayText = new javax.swing.JLabel();
        employeeNoText = new javax.swing.JLabel();
        nameText = new javax.swing.JLabel();
        positionText = new javax.swing.JLabel();
        startText = new javax.swing.JLabel();
        endText = new javax.swing.JLabel();
        dashboardLabel = new javax.swing.JLabel();
        decorLine = new javax.swing.JPanel();
        printPayBtn = new javax.swing.JButton();
        comboBoxPeriod = new javax.swing.JComboBox<>();
        periodCoveredLabel = new javax.swing.JLabel();
        summary2Label = new javax.swing.JLabel();
        gross3Label = new javax.swing.JLabel();
        taxableIncomeLabel = new javax.swing.JLabel();
        netPay2Label = new javax.swing.JLabel();
        mainPhilHealthText = new javax.swing.JLabel();
        mainTaxableText = new javax.swing.JLabel();
        mainNetPayText = new javax.swing.JLabel();
        sss2Label = new javax.swing.JLabel();
        philHealth2Label = new javax.swing.JLabel();
        pagibig2Label = new javax.swing.JLabel();
        tax2Label = new javax.swing.JLabel();
        decorLine7 = new javax.swing.JPanel();
        decorLine8 = new javax.swing.JPanel();
        decorLine9 = new javax.swing.JPanel();
        mainPagIbigText = new javax.swing.JLabel();
        mainSssText = new javax.swing.JLabel();
        mainGrossText = new javax.swing.JLabel();
        mainTaxText = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(760, 640));

        payslipViewScrollPane.setBackground(new java.awt.Color(255, 255, 255));
        payslipViewScrollPane.setHorizontalScrollBar(null);

        payslipView.setBackground(new java.awt.Color(255, 255, 255));
        payslipView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));

        payslipLayer.setBackground(new java.awt.Color(255, 255, 255));
        payslipLayer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        decorLine1.setBackground(new java.awt.Color(240, 240, 240));
        decorLine1.setDoubleBuffered(false);

        javax.swing.GroupLayout decorLine1Layout = new javax.swing.GroupLayout(decorLine1);
        decorLine1.setLayout(decorLine1Layout);
        decorLine1Layout.setHorizontalGroup(
            decorLine1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        decorLine1Layout.setVerticalGroup(
            decorLine1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        motorPhIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tab-icon/attendance.png"))); // NOI18N

        companyName.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        companyName.setText("MotorPH");

        address2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        address2.setText("Bagong Nayon, Quezon City");

        address1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        address1.setText("7 Jupiter Avenue Cor F. Sandoval Jr.");

        empLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        empLabel.setText("Employee No :");

        nameLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        nameLabel.setText("Name :");

        positionLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        positionLabel.setText("Position :");

        startLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        startLabel.setText("Period Start :");

        endLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        endLabel.setText("Period End :");

        decorLine2.setBackground(new java.awt.Color(240, 240, 240));
        decorLine2.setDoubleBuffered(false);

        javax.swing.GroupLayout decorLine2Layout = new javax.swing.GroupLayout(decorLine2);
        decorLine2.setLayout(decorLine2Layout);
        decorLine2Layout.setHorizontalGroup(
            decorLine2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        decorLine2Layout.setVerticalGroup(
            decorLine2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        earningsLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        earningsLabel.setText("Earnings");

        basicLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        basicLabel.setText("Basic Salary");

        riceLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        riceLabel.setText("Rice Subsidy");

        phoneLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        phoneLabel.setText("Phone Allowance");

        clothingLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        clothingLabel.setText("Clothing Allowance");

        basicText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        basicText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        basicText.setText("0.00");

        riceText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        riceText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        riceText.setText("0.00");

        phoneText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        phoneText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        phoneText.setText("180, 000.00");

        clothingText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        clothingText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        clothingText.setText("16,180,000.00");

        grossLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        grossLabel.setText("Gross Pay");

        grossText.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        grossText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grossText.setText("16,180,000.00");

        decorLine5.setBackground(new java.awt.Color(240, 240, 240));
        decorLine5.setDoubleBuffered(false);

        javax.swing.GroupLayout decorLine5Layout = new javax.swing.GroupLayout(decorLine5);
        decorLine5.setLayout(decorLine5Layout);
        decorLine5Layout.setHorizontalGroup(
            decorLine5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        decorLine5Layout.setVerticalGroup(
            decorLine5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        deductionsLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        deductionsLabel.setText("Deductions");

        sssLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        sssLabel.setText("SSS");

        philhealthLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        philhealthLabel.setText("PhilHealth");

        pagibigLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        pagibigLabel.setText("Pag-IBIG");

        taxLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        taxLabel.setText("Withholding Tax");

        sssText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        sssText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        sssText.setText("180, 000.00");

        philHealthText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        philHealthText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        philHealthText.setText("180, 000.00");

        pagIbigText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        pagIbigText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pagIbigText.setText("180, 000.00");

        taxText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        taxText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        taxText.setText("0.0");

        totalDeductionLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        totalDeductionLabel.setText("Total Deduction");

        totalDeductionText.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        totalDeductionText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalDeductionText.setText("0.00");

        decorLine6.setBackground(new java.awt.Color(240, 240, 240));
        decorLine6.setDoubleBuffered(false);

        javax.swing.GroupLayout decorLine6Layout = new javax.swing.GroupLayout(decorLine6);
        decorLine6.setLayout(decorLine6Layout);
        decorLine6Layout.setHorizontalGroup(
            decorLine6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        decorLine6Layout.setVerticalGroup(
            decorLine6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        summaryLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        summaryLabel.setText("Summary");

        gross2Label.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        gross2Label.setText("Gross Pay");

        deduction2Label.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        deduction2Label.setText("Deduction");

        gross2Text.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        gross2Text.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gross2Text.setText("180, 000.00");

        totalDeduction2Text.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        totalDeduction2Text.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalDeduction2Text.setText("16,180,000.00");

        netPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        previewNetPayLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        previewNetPayLabel.setText("Net Pay");

        previewNetPayText.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        previewNetPayText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        previewNetPayText.setText("16,180,000.00");

        javax.swing.GroupLayout netPanelLayout = new javax.swing.GroupLayout(netPanel);
        netPanel.setLayout(netPanelLayout);
        netPanelLayout.setHorizontalGroup(
            netPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, netPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(previewNetPayLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(previewNetPayText, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        netPanelLayout.setVerticalGroup(
            netPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(netPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(netPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(previewNetPayLabel)
                    .addComponent(previewNetPayText, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        employeeNoText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        employeeNoText.setText("10010");

        nameText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        nameText.setText("Don Justine Fontanilla");

        positionText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        positionText.setText("Chief Operating Executive");

        startText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        startText.setText("2026/01/15");

        endText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        endText.setText("2026/01/31");

        javax.swing.GroupLayout payslipLayerLayout = new javax.swing.GroupLayout(payslipLayer);
        payslipLayer.setLayout(payslipLayerLayout);
        payslipLayerLayout.setHorizontalGroup(
            payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(payslipLayerLayout.createSequentialGroup()
                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(payslipLayerLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(motorPhIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(payslipLayerLayout.createSequentialGroup()
                                .addGap(101, 101, 101)
                                .addComponent(companyName))
                            .addGroup(payslipLayerLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(address1)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(payslipLayerLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(decorLine1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(decorLine2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(decorLine5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(decorLine6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(netPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(payslipLayerLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(payslipLayerLayout.createSequentialGroup()
                                        .addComponent(summaryLabel)
                                        .addGap(217, 271, Short.MAX_VALUE))
                                    .addGroup(payslipLayerLayout.createSequentialGroup()
                                        .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(payslipLayerLayout.createSequentialGroup()
                                                .addComponent(endLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(endText))
                                            .addComponent(earningsLabel)
                                            .addGroup(payslipLayerLayout.createSequentialGroup()
                                                .addComponent(empLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(employeeNoText))
                                            .addGroup(payslipLayerLayout.createSequentialGroup()
                                                .addComponent(nameLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(nameText))
                                            .addGroup(payslipLayerLayout.createSequentialGroup()
                                                .addComponent(positionLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(positionText))
                                            .addGroup(payslipLayerLayout.createSequentialGroup()
                                                .addComponent(startLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(startText)))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(payslipLayerLayout.createSequentialGroup()
                                        .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(payslipLayerLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(payslipLayerLayout.createSequentialGroup()
                                                        .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(basicLabel)
                                                            .addComponent(riceLabel)
                                                            .addComponent(phoneLabel)
                                                            .addComponent(clothingLabel)
                                                            .addComponent(grossLabel))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(clothingText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(riceText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(basicText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(phoneText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(grossText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                    .addGroup(payslipLayerLayout.createSequentialGroup()
                                                        .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(gross2Label)
                                                            .addComponent(deduction2Label))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(totalDeduction2Text, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                                            .addComponent(gross2Text, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                                            .addGroup(payslipLayerLayout.createSequentialGroup()
                                                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(deductionsLabel)
                                                    .addGroup(payslipLayerLayout.createSequentialGroup()
                                                        .addGap(6, 6, 6)
                                                        .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(sssLabel)
                                                            .addComponent(philhealthLabel)
                                                            .addComponent(pagibigLabel)
                                                            .addComponent(taxLabel)
                                                            .addComponent(totalDeductionLabel))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(taxText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(philHealthText, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                                        .addComponent(pagIbigText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .addComponent(sssText, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(totalDeductionText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                        .addGap(8, 8, 8)))))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, payslipLayerLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(address2)
                .addGap(92, 92, 92))
        );
        payslipLayerLayout.setVerticalGroup(
            payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(payslipLayerLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(payslipLayerLayout.createSequentialGroup()
                        .addComponent(companyName)
                        .addGap(0, 0, 0)
                        .addComponent(address1))
                    .addComponent(motorPhIcon))
                .addGap(2, 2, 2)
                .addComponent(address2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decorLine1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(empLabel)
                    .addComponent(employeeNoText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(positionLabel)
                    .addComponent(positionText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startLabel)
                    .addComponent(startText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(endLabel)
                    .addComponent(endText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decorLine2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(payslipLayerLayout.createSequentialGroup()
                        .addComponent(earningsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(basicLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(riceLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phoneLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clothingLabel))
                    .addGroup(payslipLayerLayout.createSequentialGroup()
                        .addComponent(basicText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(riceText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phoneText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clothingText)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(grossLabel)
                    .addComponent(grossText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decorLine5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deductionsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(payslipLayerLayout.createSequentialGroup()
                        .addComponent(sssLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(philhealthLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pagibigLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(taxLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalDeductionLabel)
                            .addComponent(totalDeductionText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(payslipLayerLayout.createSequentialGroup()
                        .addComponent(sssText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(philHealthText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pagIbigText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(taxText)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decorLine6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(summaryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(payslipLayerLayout.createSequentialGroup()
                        .addComponent(gross2Text)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalDeduction2Text))
                    .addGroup(payslipLayerLayout.createSequentialGroup()
                        .addComponent(gross2Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deduction2Label)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(netPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout payslipViewLayout = new javax.swing.GroupLayout(payslipView);
        payslipView.setLayout(payslipViewLayout);
        payslipViewLayout.setHorizontalGroup(
            payslipViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(payslipViewLayout.createSequentialGroup()
                .addComponent(payslipLayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 38, Short.MAX_VALUE))
        );
        payslipViewLayout.setVerticalGroup(
            payslipViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(payslipLayer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        payslipViewScrollPane.setViewportView(payslipView);

        dashboardLabel.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        dashboardLabel.setText("SALARY DETAILS");

        decorLine.setBackground(new java.awt.Color(240, 240, 240));
        decorLine.setDoubleBuffered(false);

        javax.swing.GroupLayout decorLineLayout = new javax.swing.GroupLayout(decorLine);
        decorLine.setLayout(decorLineLayout);
        decorLineLayout.setHorizontalGroup(
            decorLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 769, Short.MAX_VALUE)
        );
        decorLineLayout.setVerticalGroup(
            decorLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );

        printPayBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        printPayBtn.setText("PRINT PAYSLIP");
        printPayBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printPayBtn.addActionListener(this::printPayBtnActionPerformed);

        comboBoxPeriod.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        comboBoxPeriod.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        comboBoxPeriod.addActionListener(this::comboBoxPeriodActionPerformed);

        periodCoveredLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        periodCoveredLabel.setText("Period Covered");

        summary2Label.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        summary2Label.setText("Summary");

        gross3Label.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        gross3Label.setText("Gross Pay");

        taxableIncomeLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        taxableIncomeLabel.setText("Taxable Income");

        netPay2Label.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        netPay2Label.setText("Net Pay");

        mainPhilHealthText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        mainPhilHealthText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mainPhilHealthText.setText("0.00");

        mainTaxableText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        mainTaxableText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mainTaxableText.setText("0.00");

        mainNetPayText.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        mainNetPayText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mainNetPayText.setText("180,000.00");

        sss2Label.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        sss2Label.setText("SSS Contribution");

        philHealth2Label.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        philHealth2Label.setText("PhilHealth");

        pagibig2Label.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        pagibig2Label.setText("Pag-IBIG");

        tax2Label.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        tax2Label.setText("Withholding Tax");

        decorLine7.setBackground(new java.awt.Color(240, 240, 240));
        decorLine7.setDoubleBuffered(false);

        javax.swing.GroupLayout decorLine7Layout = new javax.swing.GroupLayout(decorLine7);
        decorLine7.setLayout(decorLine7Layout);
        decorLine7Layout.setHorizontalGroup(
            decorLine7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );
        decorLine7Layout.setVerticalGroup(
            decorLine7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        decorLine8.setBackground(new java.awt.Color(240, 240, 240));
        decorLine8.setDoubleBuffered(false);

        javax.swing.GroupLayout decorLine8Layout = new javax.swing.GroupLayout(decorLine8);
        decorLine8.setLayout(decorLine8Layout);
        decorLine8Layout.setHorizontalGroup(
            decorLine8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );
        decorLine8Layout.setVerticalGroup(
            decorLine8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        decorLine9.setBackground(new java.awt.Color(240, 240, 240));
        decorLine9.setDoubleBuffered(false);

        javax.swing.GroupLayout decorLine9Layout = new javax.swing.GroupLayout(decorLine9);
        decorLine9.setLayout(decorLine9Layout);
        decorLine9Layout.setHorizontalGroup(
            decorLine9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );
        decorLine9Layout.setVerticalGroup(
            decorLine9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        mainPagIbigText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        mainPagIbigText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mainPagIbigText.setText("0.00");

        mainSssText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        mainSssText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mainSssText.setText("0.00");

        mainGrossText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        mainGrossText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mainGrossText.setText("0.00");

        mainTaxText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        mainTaxText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mainTaxText.setText("0.00");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(comboBoxPeriod, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(netPay2Label)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(mainNetPayText)
                                        .addGap(9, 9, 9))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(periodCoveredLabel)
                                        .addComponent(summary2Label)
                                        .addComponent(taxableIncomeLabel)
                                        .addComponent(decorLine8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(decorLine9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(decorLine7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(15, 15, 15)
                                            .addComponent(tax2Label)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(45, 45, 45)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(sss2Label)
                                            .addComponent(gross3Label)
                                            .addComponent(philHealth2Label)
                                            .addComponent(pagibig2Label))
                                        .addGap(12, 12, 12)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(mainSssText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(mainPhilHealthText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(mainPagIbigText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(mainGrossText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(mainTaxableText, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                            .addComponent(mainTaxText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(8, 8, 8)))
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(printPayBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84)))
                .addComponent(payslipViewScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(dashboardLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(dashboardLabel)
                .addGap(6, 6, 6)
                .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(payslipViewScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(58, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(periodCoveredLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addComponent(summary2Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(decorLine7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(gross3Label)
                            .addComponent(mainGrossText))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sss2Label)
                            .addComponent(mainSssText))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mainPhilHealthText)
                            .addComponent(philHealth2Label))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pagibig2Label)
                            .addComponent(mainPagIbigText))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(decorLine8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(taxableIncomeLabel)
                            .addComponent(mainTaxableText))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tax2Label)
                            .addComponent(mainTaxText))
                        .addGap(8, 8, 8)
                        .addComponent(decorLine9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(netPay2Label)
                            .addComponent(mainNetPayText))
                        .addGap(73, 73, 73)
                        .addComponent(printPayBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void printPayBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printPayBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_printPayBtnActionPerformed

    private void comboBoxPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxPeriodActionPerformed
        // TODO add your handling code here:
        PayrollPeriod selected = (PayrollPeriod) this.comboBoxPeriod.getSelectedItem();
        
        if (selected == null) return;
        
        try {
            Employee employee = this.appContext.getSessionManager().getCurrentEmployee();
            
            Payslip payslip = this.appContext.getPayrollService().generatePayslip(employee, selected);
            
            changeSalaryUI(payslip);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_comboBoxPeriodActionPerformed

    private AppContext appContext;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel address1;
    private javax.swing.JLabel address2;
    private javax.swing.JLabel basicLabel;
    private javax.swing.JLabel basicText;
    private javax.swing.JLabel clothingLabel;
    private javax.swing.JLabel clothingText;
    private javax.swing.JComboBox<PayrollPeriod> comboBoxPeriod;
    private javax.swing.JLabel companyName;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JPanel decorLine;
    private javax.swing.JPanel decorLine1;
    private javax.swing.JPanel decorLine2;
    private javax.swing.JPanel decorLine3;
    private javax.swing.JPanel decorLine4;
    private javax.swing.JPanel decorLine5;
    private javax.swing.JPanel decorLine6;
    private javax.swing.JPanel decorLine7;
    private javax.swing.JPanel decorLine8;
    private javax.swing.JPanel decorLine9;
    private javax.swing.JLabel deduction2Label;
    private javax.swing.JLabel deductionsLabel;
    private javax.swing.JLabel earningsLabel;
    private javax.swing.JLabel empLabel;
    private javax.swing.JLabel employeeNoText;
    private javax.swing.JLabel endLabel;
    private javax.swing.JLabel endText;
    private javax.swing.JLabel gross2Label;
    private javax.swing.JLabel gross2Text;
    private javax.swing.JLabel gross3Label;
    private javax.swing.JLabel grossLabel;
    private javax.swing.JLabel grossText;
    private javax.swing.JLabel mainGrossText;
    private javax.swing.JLabel mainNetPayText;
    private javax.swing.JLabel mainPagIbigText;
    private javax.swing.JLabel mainPhilHealthText;
    private javax.swing.JLabel mainSssText;
    private javax.swing.JLabel mainTaxText;
    private javax.swing.JLabel mainTaxableText;
    private javax.swing.JLabel motorPhIcon;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel nameText;
    private javax.swing.JPanel netPanel;
    private javax.swing.JLabel netPay2Label;
    private javax.swing.JLabel pagIbigText;
    private javax.swing.JLabel pagibig2Label;
    private javax.swing.JLabel pagibigLabel;
    private javax.swing.JPanel payslipLayer;
    private javax.swing.JPanel payslipView;
    private javax.swing.JScrollPane payslipViewScrollPane;
    private javax.swing.JLabel periodCoveredLabel;
    private javax.swing.JLabel philHealth2Label;
    private javax.swing.JLabel philHealthText;
    private javax.swing.JLabel philhealthLabel;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JLabel phoneText;
    private javax.swing.JLabel positionLabel;
    private javax.swing.JLabel positionText;
    private javax.swing.JLabel previewNetPayLabel;
    private javax.swing.JLabel previewNetPayText;
    private javax.swing.JButton printPayBtn;
    private javax.swing.JLabel riceLabel;
    private javax.swing.JLabel riceText;
    private javax.swing.JLabel sss2Label;
    private javax.swing.JLabel sssLabel;
    private javax.swing.JLabel sssText;
    private javax.swing.JLabel startLabel;
    private javax.swing.JLabel startText;
    private javax.swing.JLabel summary2Label;
    private javax.swing.JLabel summaryLabel;
    private javax.swing.JLabel tax2Label;
    private javax.swing.JLabel taxLabel;
    private javax.swing.JLabel taxText;
    private javax.swing.JLabel taxableIncomeLabel;
    private javax.swing.JLabel totalDeduction2Text;
    private javax.swing.JLabel totalDeductionLabel;
    private javax.swing.JLabel totalDeductionText;
    // End of variables declaration//GEN-END:variables
}
