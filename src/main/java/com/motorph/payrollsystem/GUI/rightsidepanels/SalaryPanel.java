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
import java.awt.Image;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
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
        loadPreviewIcon();
        initComponents();
        loadPayrollPeriods();
    }
    
    private void loadPreviewIcon(){
        ImageIcon newIcon = new ImageIcon(getClass().getResource("/images/tab-icon/motorPhIcon.png"));
        
        Image img = newIcon.getImage();
        Image scaledImg = img.getScaledInstance(67, 56, Image.SCALE_SMOOTH);
        
        this.icon = new ImageIcon(scaledImg);
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
                    PayrollPeriodFactory.fromAttendanceDatesSemiMonthly(dates);
            
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

    private void changeSalaryUI() {
        //For testing incorrect data
//        System.out.println("Period: " + payslip.getPeriod().toString());
//        System.out.println("Basic Salary: " + payslip.getEmployee().getCompProfile().getBasicSalary());
//        System.out.println("Total hours: " + payslip.getTotalHours());
//        System.out.println("Hourly rate: " + payslip.getEmployee().getCompProfile().getHourlyRate());
//        System.out.println("Gross pay: " + payslip.getGrossPay()); 
//        
        //MAIN
        //Main summary
        mainGrossPayText.setText(Money.displayMoney(payslip.getGrossPay()));
        mainSssText.setText(Money.displayMoney(payslip.getSssAmount()));
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
        basicText.setText(Money.displayMoney(payslip.getBasicPayAmount()));
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
    
    private void showPrintDialog() {
        printDialog.setSize(450, 880);
        printDialog.setResizable(false);
        printDialog.setLocationRelativeTo(this);
        printDialog.setTitle("Printing...");
        
        printDialog.setVisible(true);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        printDialog = new javax.swing.JDialog();
        printView = new javax.swing.JPanel();
        printLayer = new javax.swing.JPanel();
        decorLine3 = new javax.swing.JPanel();
        motorPhIcon1 = new javax.swing.JLabel();
        companyName1 = new javax.swing.JLabel();
        address3 = new javax.swing.JLabel();
        address4 = new javax.swing.JLabel();
        empLabel1 = new javax.swing.JLabel();
        nameLabel1 = new javax.swing.JLabel();
        positionLabel1 = new javax.swing.JLabel();
        startLabel1 = new javax.swing.JLabel();
        endLabel1 = new javax.swing.JLabel();
        decorLine4 = new javax.swing.JPanel();
        earningsLabel1 = new javax.swing.JLabel();
        basicLabel1 = new javax.swing.JLabel();
        riceLabel1 = new javax.swing.JLabel();
        phoneLabel1 = new javax.swing.JLabel();
        clothingLabel1 = new javax.swing.JLabel();
        basicText1 = new javax.swing.JLabel();
        riceText1 = new javax.swing.JLabel();
        phoneText1 = new javax.swing.JLabel();
        clothingText1 = new javax.swing.JLabel();
        grossLabel1 = new javax.swing.JLabel();
        grossText1 = new javax.swing.JLabel();
        decorLine10 = new javax.swing.JPanel();
        deductionsLabel1 = new javax.swing.JLabel();
        sssLabel1 = new javax.swing.JLabel();
        philhealthLabel1 = new javax.swing.JLabel();
        pagibigLabel1 = new javax.swing.JLabel();
        taxLabel1 = new javax.swing.JLabel();
        sssText1 = new javax.swing.JLabel();
        philHealthText1 = new javax.swing.JLabel();
        pagIbigText1 = new javax.swing.JLabel();
        taxText1 = new javax.swing.JLabel();
        totalDeductionLabel1 = new javax.swing.JLabel();
        totalDeductionText1 = new javax.swing.JLabel();
        decorLine11 = new javax.swing.JPanel();
        summaryLabel1 = new javax.swing.JLabel();
        gross2Label1 = new javax.swing.JLabel();
        deduction2Label1 = new javax.swing.JLabel();
        gross2Text1 = new javax.swing.JLabel();
        totalDeduction2Text1 = new javax.swing.JLabel();
        netPanel1 = new javax.swing.JPanel();
        previewNetPayLabel1 = new javax.swing.JLabel();
        previewNetPayText1 = new javax.swing.JLabel();
        employeeNoText1 = new javax.swing.JLabel();
        nameText1 = new javax.swing.JLabel();
        positionText1 = new javax.swing.JLabel();
        startText1 = new javax.swing.JLabel();
        endText1 = new javax.swing.JLabel();
        printNote = new javax.swing.JLabel();
        printBtn = new javax.swing.JButton();
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
        mainGrossPayLabel = new javax.swing.JLabel();
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
        mainGrossPayText = new javax.swing.JLabel();
        mainTaxText = new javax.swing.JLabel();

        printDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        printDialog.setAlwaysOnTop(true);
        printDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        printDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        printDialog.setName("printingDialog"); // NOI18N

        printView.setBackground(new java.awt.Color(255, 255, 255));
        printView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));

        printLayer.setBackground(new java.awt.Color(255, 255, 255));
        printLayer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        decorLine3.setBackground(new java.awt.Color(240, 240, 240));
        decorLine3.setDoubleBuffered(false);

        javax.swing.GroupLayout decorLine3Layout = new javax.swing.GroupLayout(decorLine3);
        decorLine3.setLayout(decorLine3Layout);
        decorLine3Layout.setHorizontalGroup(
            decorLine3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        decorLine3Layout.setVerticalGroup(
            decorLine3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        motorPhIcon1.setIcon(this.icon);

        companyName1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        companyName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        companyName1.setText("MotorPH");

        address3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        address3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        address3.setText("Bagong Nayon, Quezon City");

        address4.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        address4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        address4.setText("7 Jupiter Avenue Cor F. Sandoval Jr.");

        empLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        empLabel1.setText("Employee No :");

        nameLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        nameLabel1.setText("Name :");

        positionLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        positionLabel1.setText("Position :");

        startLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        startLabel1.setText("Period Start :");

        endLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        endLabel1.setText("Period End :");

        decorLine4.setBackground(new java.awt.Color(240, 240, 240));
        decorLine4.setDoubleBuffered(false);

        javax.swing.GroupLayout decorLine4Layout = new javax.swing.GroupLayout(decorLine4);
        decorLine4.setLayout(decorLine4Layout);
        decorLine4Layout.setHorizontalGroup(
            decorLine4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        decorLine4Layout.setVerticalGroup(
            decorLine4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        earningsLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        earningsLabel1.setText("Earnings");

        basicLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        basicLabel1.setText("Basic Salary");

        riceLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        riceLabel1.setText("Rice Subsidy");

        phoneLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        phoneLabel1.setText("Phone Allowance");

        clothingLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        clothingLabel1.setText("Clothing Allowance");

        basicText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        basicText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        basicText1.setText("0.00");

        riceText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        riceText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        riceText1.setText("0.00");

        phoneText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        phoneText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        phoneText1.setText("180, 000.00");

        clothingText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        clothingText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        clothingText1.setText("16,180,000.00");

        grossLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        grossLabel1.setText("Gross Pay");

        grossText1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        grossText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grossText1.setText("16,180,000.00");

        decorLine10.setBackground(new java.awt.Color(240, 240, 240));
        decorLine10.setDoubleBuffered(false);

        javax.swing.GroupLayout decorLine10Layout = new javax.swing.GroupLayout(decorLine10);
        decorLine10.setLayout(decorLine10Layout);
        decorLine10Layout.setHorizontalGroup(
            decorLine10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        decorLine10Layout.setVerticalGroup(
            decorLine10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        deductionsLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        deductionsLabel1.setText("Deductions");

        sssLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        sssLabel1.setText("SSS");

        philhealthLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        philhealthLabel1.setText("PhilHealth");

        pagibigLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        pagibigLabel1.setText("Pag-IBIG");

        taxLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        taxLabel1.setText("Withholding Tax");

        sssText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        sssText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        sssText1.setText("180, 000.00");

        philHealthText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        philHealthText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        philHealthText1.setText("180, 000.00");

        pagIbigText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        pagIbigText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pagIbigText1.setText("180, 000.00");

        taxText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        taxText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        taxText1.setText("0.0");

        totalDeductionLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        totalDeductionLabel1.setText("Total Deduction");

        totalDeductionText1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        totalDeductionText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalDeductionText1.setText("0.00");

        decorLine11.setBackground(new java.awt.Color(240, 240, 240));
        decorLine11.setDoubleBuffered(false);

        javax.swing.GroupLayout decorLine11Layout = new javax.swing.GroupLayout(decorLine11);
        decorLine11.setLayout(decorLine11Layout);
        decorLine11Layout.setHorizontalGroup(
            decorLine11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        decorLine11Layout.setVerticalGroup(
            decorLine11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        summaryLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        summaryLabel1.setText("Summary");

        gross2Label1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        gross2Label1.setText("Gross Pay");

        deduction2Label1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        deduction2Label1.setText("Deduction");

        gross2Text1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        gross2Text1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        gross2Text1.setText("180, 000.00");

        totalDeduction2Text1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        totalDeduction2Text1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalDeduction2Text1.setText("16,180,000.00");

        netPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        previewNetPayLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        previewNetPayLabel1.setText("Net Pay");

        previewNetPayText1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        previewNetPayText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        previewNetPayText1.setText("16,180,000.00");

        javax.swing.GroupLayout netPanel1Layout = new javax.swing.GroupLayout(netPanel1);
        netPanel1.setLayout(netPanel1Layout);
        netPanel1Layout.setHorizontalGroup(
            netPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, netPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(previewNetPayLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(previewNetPayText1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        netPanel1Layout.setVerticalGroup(
            netPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(netPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(netPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(previewNetPayLabel1)
                    .addComponent(previewNetPayText1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        employeeNoText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        employeeNoText1.setText("10010");

        nameText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        nameText1.setText("Don Justine Fontanilla");

        positionText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        positionText1.setText("Chief Operating Executive");

        startText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        startText1.setText("2026/01/15");

        endText1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        endText1.setText("2026/01/31");

        javax.swing.GroupLayout printLayerLayout = new javax.swing.GroupLayout(printLayer);
        printLayer.setLayout(printLayerLayout);
        printLayerLayout.setHorizontalGroup(
            printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printLayerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(decorLine3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(decorLine4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(decorLine10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(decorLine11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(netPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(printLayerLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(printLayerLayout.createSequentialGroup()
                                .addComponent(summaryLabel1)
                                .addGap(217, 271, Short.MAX_VALUE))
                            .addGroup(printLayerLayout.createSequentialGroup()
                                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(printLayerLayout.createSequentialGroup()
                                        .addComponent(endLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(endText1))
                                    .addComponent(earningsLabel1)
                                    .addGroup(printLayerLayout.createSequentialGroup()
                                        .addComponent(empLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(employeeNoText1))
                                    .addGroup(printLayerLayout.createSequentialGroup()
                                        .addComponent(nameLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(nameText1))
                                    .addGroup(printLayerLayout.createSequentialGroup()
                                        .addComponent(positionLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(positionText1))
                                    .addGroup(printLayerLayout.createSequentialGroup()
                                        .addComponent(startLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(startText1)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(printLayerLayout.createSequentialGroup()
                                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(printLayerLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(printLayerLayout.createSequentialGroup()
                                                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(basicLabel1)
                                                    .addComponent(riceLabel1)
                                                    .addComponent(phoneLabel1)
                                                    .addComponent(clothingLabel1)
                                                    .addComponent(grossLabel1))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(clothingText1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(riceText1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(basicText1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(phoneText1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(grossText1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                            .addGroup(printLayerLayout.createSequentialGroup()
                                                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(gross2Label1)
                                                    .addComponent(deduction2Label1))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(totalDeduction2Text1, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                                    .addComponent(gross2Text1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                                    .addGroup(printLayerLayout.createSequentialGroup()
                                        .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(deductionsLabel1)
                                            .addGroup(printLayerLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(sssLabel1)
                                                    .addComponent(philhealthLabel1)
                                                    .addComponent(pagibigLabel1)
                                                    .addComponent(taxLabel1)
                                                    .addComponent(totalDeductionLabel1))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(taxText1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(philHealthText1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(pagIbigText1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(sssText1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(totalDeductionText1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(8, 8, 8))))
                    .addGroup(printLayerLayout.createSequentialGroup()
                        .addComponent(motorPhIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(address3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(address4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(companyName1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        printLayerLayout.setVerticalGroup(
            printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printLayerLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(printLayerLayout.createSequentialGroup()
                        .addComponent(companyName1)
                        .addGap(0, 0, 0)
                        .addComponent(address4)
                        .addGap(2, 2, 2)
                        .addComponent(address3))
                    .addComponent(motorPhIcon1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decorLine3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(empLabel1)
                    .addComponent(employeeNoText1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel1)
                    .addComponent(nameText1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(positionLabel1)
                    .addComponent(positionText1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startLabel1)
                    .addComponent(startText1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(endLabel1)
                    .addComponent(endText1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decorLine4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(printLayerLayout.createSequentialGroup()
                        .addComponent(earningsLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(basicLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(riceLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phoneLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clothingLabel1))
                    .addGroup(printLayerLayout.createSequentialGroup()
                        .addComponent(basicText1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(riceText1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phoneText1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clothingText1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(grossLabel1)
                    .addComponent(grossText1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decorLine10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deductionsLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(printLayerLayout.createSequentialGroup()
                        .addComponent(sssLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(philhealthLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pagibigLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(taxLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalDeductionLabel1)
                            .addComponent(totalDeductionText1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(printLayerLayout.createSequentialGroup()
                        .addComponent(sssText1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(philHealthText1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pagIbigText1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(taxText1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decorLine11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(summaryLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(printLayerLayout.createSequentialGroup()
                        .addComponent(gross2Text1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalDeduction2Text1))
                    .addGroup(printLayerLayout.createSequentialGroup()
                        .addComponent(gross2Label1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deduction2Label1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(netPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout printViewLayout = new javax.swing.GroupLayout(printView);
        printView.setLayout(printViewLayout);
        printViewLayout.setHorizontalGroup(
            printViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(printLayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        printViewLayout.setVerticalGroup(
            printViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(printLayer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        printNote.setFont(new java.awt.Font("Poppins", 2, 14)); // NOI18N
        printNote.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        printNote.setText("This element is for simulation only");

        printBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        printBtn.setText("CLOSE");
        printBtn.addActionListener(this::printBtnActionPerformed);

        javax.swing.GroupLayout printDialogLayout = new javax.swing.GroupLayout(printDialog.getContentPane());
        printDialog.getContentPane().setLayout(printDialogLayout);
        printDialogLayout.setHorizontalGroup(
            printDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printDialogLayout.createSequentialGroup()
                .addGroup(printDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(printDialogLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(printDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(printView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(printNote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(printDialogLayout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        printDialogLayout.setVerticalGroup(
            printDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printDialogLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(printView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(printNote)
                .addGap(16, 16, 16)
                .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

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

        motorPhIcon.setIcon(this.icon);

        companyName.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        companyName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        companyName.setText("MotorPH");

        address2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        address2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        address2.setText("Bagong Nayon, Quezon City");

        address1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        address1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
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
                                            .addComponent(taxText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(philHealthText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(pagIbigText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(sssText, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(totalDeductionText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(8, 8, 8))))
                    .addGroup(payslipLayerLayout.createSequentialGroup()
                        .addComponent(motorPhIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(address2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(address1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(companyName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        payslipLayerLayout.setVerticalGroup(
            payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(payslipLayerLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(payslipLayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(payslipLayerLayout.createSequentialGroup()
                        .addComponent(companyName)
                        .addGap(0, 0, 0)
                        .addComponent(address1)
                        .addGap(2, 2, 2)
                        .addComponent(address2))
                    .addComponent(motorPhIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

        mainGrossPayLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        mainGrossPayLabel.setText("Gross Pay");

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

        mainGrossPayText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        mainGrossPayText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mainGrossPayText.setText("0.00");

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
                                            .addComponent(mainGrossPayLabel)
                                            .addComponent(philHealth2Label)
                                            .addComponent(pagibig2Label))
                                        .addGap(12, 12, 12)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(mainSssText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(mainPhilHealthText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(mainPagIbigText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(mainGrossPayText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                            .addComponent(mainGrossPayLabel)
                            .addComponent(mainGrossPayText))
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
        showPrintDialog();
        
    }//GEN-LAST:event_printPayBtnActionPerformed

    private void comboBoxPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxPeriodActionPerformed
        // TODO add your handling code here:
        PayrollPeriod selected = (PayrollPeriod) this.comboBoxPeriod.getSelectedItem();
        
        if (selected == null) return;
        
        try {
            Employee employee = this.appContext.getSessionManager().getCurrentEmployee();
            
            Payslip payslip = this.appContext.getPayrollService().generatePayslip(employee, selected);
            
            this.payslip = payslip;
            changeSalaryUI();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_comboBoxPeriodActionPerformed

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
        // TODO add your handling code here:
        this.printDialog.dispose();
    }//GEN-LAST:event_printBtnActionPerformed
    
    private Payslip payslip;
    private ImageIcon icon;
    private AppContext appContext;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel address1;
    private javax.swing.JLabel address2;
    private javax.swing.JLabel address3;
    private javax.swing.JLabel address4;
    private javax.swing.JLabel basicLabel;
    private javax.swing.JLabel basicLabel1;
    private javax.swing.JLabel basicText;
    private javax.swing.JLabel basicText1;
    private javax.swing.JLabel clothingLabel;
    private javax.swing.JLabel clothingLabel1;
    private javax.swing.JLabel clothingText;
    private javax.swing.JLabel clothingText1;
    private javax.swing.JComboBox<PayrollPeriod> comboBoxPeriod;
    private javax.swing.JLabel companyName;
    private javax.swing.JLabel companyName1;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JPanel decorLine;
    private javax.swing.JPanel decorLine1;
    private javax.swing.JPanel decorLine10;
    private javax.swing.JPanel decorLine11;
    private javax.swing.JPanel decorLine2;
    private javax.swing.JPanel decorLine3;
    private javax.swing.JPanel decorLine4;
    private javax.swing.JPanel decorLine5;
    private javax.swing.JPanel decorLine6;
    private javax.swing.JPanel decorLine7;
    private javax.swing.JPanel decorLine8;
    private javax.swing.JPanel decorLine9;
    private javax.swing.JLabel deduction2Label;
    private javax.swing.JLabel deduction2Label1;
    private javax.swing.JLabel deductionsLabel;
    private javax.swing.JLabel deductionsLabel1;
    private javax.swing.JLabel earningsLabel;
    private javax.swing.JLabel earningsLabel1;
    private javax.swing.JLabel empLabel;
    private javax.swing.JLabel empLabel1;
    private javax.swing.JLabel employeeNoText;
    private javax.swing.JLabel employeeNoText1;
    private javax.swing.JLabel endLabel;
    private javax.swing.JLabel endLabel1;
    private javax.swing.JLabel endText;
    private javax.swing.JLabel endText1;
    private javax.swing.JLabel gross2Label;
    private javax.swing.JLabel gross2Label1;
    private javax.swing.JLabel gross2Text;
    private javax.swing.JLabel gross2Text1;
    private javax.swing.JLabel grossLabel;
    private javax.swing.JLabel grossLabel1;
    private javax.swing.JLabel grossText;
    private javax.swing.JLabel grossText1;
    private javax.swing.JLabel mainGrossPayLabel;
    private javax.swing.JLabel mainGrossPayText;
    private javax.swing.JLabel mainNetPayText;
    private javax.swing.JLabel mainPagIbigText;
    private javax.swing.JLabel mainPhilHealthText;
    private javax.swing.JLabel mainSssText;
    private javax.swing.JLabel mainTaxText;
    private javax.swing.JLabel mainTaxableText;
    private javax.swing.JLabel motorPhIcon;
    private javax.swing.JLabel motorPhIcon1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel nameLabel1;
    private javax.swing.JLabel nameText;
    private javax.swing.JLabel nameText1;
    private javax.swing.JPanel netPanel;
    private javax.swing.JPanel netPanel1;
    private javax.swing.JLabel netPay2Label;
    private javax.swing.JLabel pagIbigText;
    private javax.swing.JLabel pagIbigText1;
    private javax.swing.JLabel pagibig2Label;
    private javax.swing.JLabel pagibigLabel;
    private javax.swing.JLabel pagibigLabel1;
    private javax.swing.JPanel payslipLayer;
    private javax.swing.JPanel payslipView;
    private javax.swing.JScrollPane payslipViewScrollPane;
    private javax.swing.JLabel periodCoveredLabel;
    private javax.swing.JLabel philHealth2Label;
    private javax.swing.JLabel philHealthText;
    private javax.swing.JLabel philHealthText1;
    private javax.swing.JLabel philhealthLabel;
    private javax.swing.JLabel philhealthLabel1;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JLabel phoneLabel1;
    private javax.swing.JLabel phoneText;
    private javax.swing.JLabel phoneText1;
    private javax.swing.JLabel positionLabel;
    private javax.swing.JLabel positionLabel1;
    private javax.swing.JLabel positionText;
    private javax.swing.JLabel positionText1;
    private javax.swing.JLabel previewNetPayLabel;
    private javax.swing.JLabel previewNetPayLabel1;
    private javax.swing.JLabel previewNetPayText;
    private javax.swing.JLabel previewNetPayText1;
    private javax.swing.JButton printBtn;
    private javax.swing.JDialog printDialog;
    private javax.swing.JPanel printLayer;
    private javax.swing.JLabel printNote;
    private javax.swing.JButton printPayBtn;
    private javax.swing.JPanel printView;
    private javax.swing.JLabel riceLabel;
    private javax.swing.JLabel riceLabel1;
    private javax.swing.JLabel riceText;
    private javax.swing.JLabel riceText1;
    private javax.swing.JLabel sss2Label;
    private javax.swing.JLabel sssLabel;
    private javax.swing.JLabel sssLabel1;
    private javax.swing.JLabel sssText;
    private javax.swing.JLabel sssText1;
    private javax.swing.JLabel startLabel;
    private javax.swing.JLabel startLabel1;
    private javax.swing.JLabel startText;
    private javax.swing.JLabel startText1;
    private javax.swing.JLabel summary2Label;
    private javax.swing.JLabel summaryLabel;
    private javax.swing.JLabel summaryLabel1;
    private javax.swing.JLabel tax2Label;
    private javax.swing.JLabel taxLabel;
    private javax.swing.JLabel taxLabel1;
    private javax.swing.JLabel taxText;
    private javax.swing.JLabel taxText1;
    private javax.swing.JLabel taxableIncomeLabel;
    private javax.swing.JLabel totalDeduction2Text;
    private javax.swing.JLabel totalDeduction2Text1;
    private javax.swing.JLabel totalDeductionLabel;
    private javax.swing.JLabel totalDeductionLabel1;
    private javax.swing.JLabel totalDeductionText;
    private javax.swing.JLabel totalDeductionText1;
    // End of variables declaration//GEN-END:variables
}
