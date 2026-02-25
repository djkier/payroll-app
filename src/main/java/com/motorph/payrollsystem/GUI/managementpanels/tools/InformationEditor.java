/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.GUI.managementpanels.tools;

import com.motorph.payrollsystem.app.AppContext;
import com.motorph.payrollsystem.domain.employee.CompProfile;
import com.motorph.payrollsystem.domain.employee.ContactInfo;
import com.motorph.payrollsystem.domain.employee.DepartmentInfo;
import com.motorph.payrollsystem.domain.employee.Employee;
import com.motorph.payrollsystem.domain.employee.GovIds;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.ThemeColor;

/**
 *
 * @author djjus
 */
public class InformationEditor extends javax.swing.JPanel {

    /**
     * Creates new form InfomationEditor
     */
    public InformationEditor(
        AppContext appContext, 
        Employee selectedEmployee,
        javax.swing.JDialog dialog) {
        
        this.appContext = appContext;
        this.selectedEmployee = selectedEmployee;
        this.parentDialog = dialog;
        this.isEditing = false;
        this.isConfirmingCancel = false;
        this.isConfirmingUpdate = false;
        
        initComponents();
        fillEmployeeInformation(selectedEmployee);
        updateFields();
        

    }
    
    private void fillEmployeeInformation(Employee emp) {
        //personal information
        employeeNoTextInput.setText(emp.getEmployeeNo());
        firstNameTextInput.setText(emp.getFirstName());
        lastNameTextInput.setText(emp.getLastName());
        birthdayTextInput.setText(Dates.fullDate(emp.getBirthday()));
        addressTextInput.setText(emp.getContactInfo().getAddress());
        phoneTextInput.setText(emp.getContactInfo().getPhoneNumber());
        
        //govt id
        sssTextInput.setText(emp.getGovIds().getSssNumber());
        philhealthTextInput.setText(emp.getGovIds().getPhilHealthNumber());
        pagibigTextInput.setText(emp.getGovIds().getPagibigNumber());
        tinTextInput.setText(emp.getGovIds().getTinNumber());
        
        //dept info
        departmentTextInput.setText(emp.getDepartmentInfo().getDepartment());
        positionTextInput.setText(emp.getDepartmentInfo().getPosition());
        supervisorTextInput.setText(emp.getDepartmentInfo().getSupervisor());
        statusTextInput.setText(emp.getDepartmentInfo().getStatus());
    }
    
    private void updateFields() {
        String view = isEditing ? "Edit" : "View";
        viewLabel.setText(view + " Employee Details");
        
        buttonsVisibility(updateBtn, !isEditing);
        buttonsVisibility(closeViewBtn, !isEditing);
        buttonsVisibility(cancelAddOrUpdateBtn,isEditing);
        buttonsVisibility(addOrUpdateBtn, isEditing);
        
        textFieldEnabler(firstNameTextInput);
        textFieldEnabler(lastNameTextInput);
        textFieldEnabler(birthdayTextInput);
        textFieldEnabler(addressTextInput);
        textFieldEnabler(phoneTextInput);
        
        textFieldEnabler(sssTextInput);
        textFieldEnabler(philhealthTextInput);
        textFieldEnabler(pagibigTextInput);
        textFieldEnabler(tinTextInput);
        
        textFieldEnabler(departmentTextInput);
        textFieldEnabler(positionTextInput);
        textFieldEnabler(supervisorTextInput);
        textFieldEnabler(statusTextInput);
        
    }
    
    private void buttonsVisibility(javax.swing.JButton btn, boolean isVisible) {
        btn.setEnabled(isVisible);
        btn.setVisible(isVisible);
    }
    
    private void textFieldEnabler(javax.swing.JTextField textField) {
        textField.setEnabled(isEditing);
        textField.setDisabledTextColor(ThemeColor.textDisabled());
    }
    
    private boolean hasChanges() {
        return !employeeNoTextInput.getText().equals(selectedEmployee.getEmployeeNo()) ||
                !firstNameTextInput.getText().equals(selectedEmployee.getFirstName()) ||
                !lastNameTextInput.getText().equals(selectedEmployee.getLastName()) ||
                !birthdayTextInput.getText().equals(Dates.fullDate(selectedEmployee.getBirthday())) ||
                !addressTextInput.getText().equals(selectedEmployee.getContactInfo().getAddress()) ||
                !phoneTextInput.getText().equals(selectedEmployee.getContactInfo().getPhoneNumber()) ||
                !sssTextInput.getText().equals(selectedEmployee.getGovIds().getSssNumber()) ||
                !philhealthTextInput.getText().equals(selectedEmployee.getGovIds().getPhilHealthNumber()) ||
                !pagibigTextInput.getText().equals(selectedEmployee.getGovIds().getPagibigNumber()) ||
                !tinTextInput.getText().equals(selectedEmployee.getGovIds().getTinNumber()) ||
                !departmentTextInput.getText().equals(selectedEmployee.getDepartmentInfo().getDepartment()) ||
                !positionTextInput.getText().equals(selectedEmployee.getDepartmentInfo().getPosition()) ||
                !supervisorTextInput.getText().equals(selectedEmployee.getDepartmentInfo().getSupervisor()) ||
                !statusTextInput.getText().equals(selectedEmployee.getDepartmentInfo().getStatus());
    }
    
    private void customScreenDialog(String title, String message) {
        cancelConfirmLabel.setText(message);
        customDialog.pack();
        customDialog.setResizable(false);
        customDialog.setLocationRelativeTo(parentDialog);
        customDialog.setTitle(title);

        customDialog.setVisible(true);  
    }
    
    private void noChangeScreenDialog() {
        noChangeDialog.pack();
        noChangeDialog.setResizable(false);
        noChangeDialog.setLocationRelativeTo(parentDialog);
        noChangeDialog.setTitle("Nothing to Save");
        
        noChangeDialog.setVisible(true);
    }
    
    private Employee buildEmployeeFromFields() {
        Employee emp = new Employee();
        ContactInfo contactInfo = new ContactInfo();
        GovIds govIds = new GovIds();
        DepartmentInfo departmentInfo = new DepartmentInfo();
        CompProfile compProfile = new CompProfile();
        
        emp.setEmployeeNo(selectedEmployee.getEmployeeNo());
        
        emp.setLastName(lastNameTextInput.getText().trim());
        emp.setFirstName(firstNameTextInput.getText().trim());
        emp.setBirthday(Dates.parseFullDate(birthdayTextInput.getText().trim()));
        
        contactInfo.setAddress(addressTextInput.getText().trim());
        contactInfo.setPhoneNumber(phoneTextInput.getText().trim());
        emp.setContactInfo(contactInfo);
        
        govIds.setSssNumber(sssTextInput.getText().trim());
        govIds.setPhilHealthNumber(philhealthTextInput.getText().trim());
        govIds.setPagibigNumber(pagibigTextInput.getText().trim());
        govIds.setTinNumber(tinTextInput.getText().trim());
        emp.setGovIds(govIds);
        
        departmentInfo.setStatus(statusTextInput.getText().trim());
        departmentInfo.setPosition(positionTextInput.getText().trim());
        departmentInfo.setSupervisor(supervisorTextInput.getText().trim());
        emp.setDepartmentInfo(departmentInfo);
        
        //TEMPORARY COMPENSATION REFACTORING ON UI IS NEEDED
        emp.setCompProfile(selectedEmployee.getCompProfile());
        
        return emp;
        
        
    }
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        customDialog = new javax.swing.JDialog(this.parentDialog, true);
        cancelConfirmPanel = new javax.swing.JPanel();
        cancelConfirmLabel = new javax.swing.JLabel();
        cancelBtnConfirm = new javax.swing.JButton();
        confirmBtnConfirm = new javax.swing.JButton();
        noChangeDialog = new javax.swing.JDialog(this.parentDialog, true);
        noChangePanel = new javax.swing.JPanel();
        noChangeLabel = new javax.swing.JLabel();
        noChangeButton = new javax.swing.JButton();
        personalInfoLabel = new javax.swing.JLabel();
        lastNameLabel = new javax.swing.JLabel();
        employeeNoLabel = new javax.swing.JLabel();
        firstNameLabel = new javax.swing.JLabel();
        birthdayLabel = new javax.swing.JLabel();
        viewLabel = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        decorLine2 = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        decorLine = new javax.swing.JPanel();
        govIdLabel = new javax.swing.JLabel();
        sssLabel = new javax.swing.JLabel();
        philHealthLabel = new javax.swing.JLabel();
        pagibigLabel = new javax.swing.JLabel();
        tinLabel = new javax.swing.JLabel();
        decorLine3 = new javax.swing.JPanel();
        departmentLabel = new javax.swing.JLabel();
        departmentNameLabel = new javax.swing.JLabel();
        positionLabel = new javax.swing.JLabel();
        supervisorLabel = new javax.swing.JLabel();
        updateBtn = new javax.swing.JButton();
        employeeNoTextInput = new javax.swing.JTextField();
        firstNameTextInput = new javax.swing.JTextField();
        lastNameTextInput = new javax.swing.JTextField();
        birthdayTextInput = new javax.swing.JTextField();
        addressTextInput = new javax.swing.JTextField();
        phoneTextInput = new javax.swing.JTextField();
        sssTextInput = new javax.swing.JTextField();
        philhealthTextInput = new javax.swing.JTextField();
        pagibigTextInput = new javax.swing.JTextField();
        tinTextInput = new javax.swing.JTextField();
        departmentTextInput = new javax.swing.JTextField();
        positionTextInput = new javax.swing.JTextField();
        supervisorTextInput = new javax.swing.JTextField();
        statusTextInput = new javax.swing.JTextField();
        closeViewBtn = new javax.swing.JButton();
        addOrUpdateBtn = new javax.swing.JButton();
        cancelAddOrUpdateBtn = new javax.swing.JButton();

        customDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        customDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                customDialogWindowClosing(evt);
            }
        });

        cancelConfirmPanel.setBackground(new java.awt.Color(255, 255, 255));

        cancelConfirmLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        cancelConfirmLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cancelConfirmLabel.setText("You have unsaved changes. Discard them?");

        cancelBtnConfirm.setBackground(ThemeColor.lightRed());
        cancelBtnConfirm.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        cancelBtnConfirm.setText("Cancel");
        cancelBtnConfirm.addActionListener(this::cancelBtnConfirmActionPerformed);

        confirmBtnConfirm.setBackground(ThemeColor.lightGreen());
        confirmBtnConfirm.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        confirmBtnConfirm.setText("Confirm");
        confirmBtnConfirm.addActionListener(this::confirmBtnConfirmActionPerformed);

        javax.swing.GroupLayout cancelConfirmPanelLayout = new javax.swing.GroupLayout(cancelConfirmPanel);
        cancelConfirmPanel.setLayout(cancelConfirmPanelLayout);
        cancelConfirmPanelLayout.setHorizontalGroup(
            cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cancelConfirmPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(cancelBtnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(confirmBtnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
            .addComponent(cancelConfirmLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cancelConfirmPanelLayout.setVerticalGroup(
            cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cancelConfirmPanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(cancelConfirmLabel)
                .addGap(18, 18, 18)
                .addGroup(cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmBtnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout customDialogLayout = new javax.swing.GroupLayout(customDialog.getContentPane());
        customDialog.getContentPane().setLayout(customDialogLayout);
        customDialogLayout.setHorizontalGroup(
            customDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cancelConfirmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        customDialogLayout.setVerticalGroup(
            customDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cancelConfirmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        noChangeDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        noChangeDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                noChangeDialogWindowClosing(evt);
            }
        });

        noChangePanel.setBackground(new java.awt.Color(255, 255, 255));

        noChangeLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        noChangeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        noChangeLabel.setText("No changes detected.");

        noChangeButton.setBackground(ThemeColor.lightGreen());
        noChangeButton.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        noChangeButton.setText("OK");
        noChangeButton.addActionListener(this::noChangeButtonActionPerformed);

        javax.swing.GroupLayout noChangePanelLayout = new javax.swing.GroupLayout(noChangePanel);
        noChangePanel.setLayout(noChangePanelLayout);
        noChangePanelLayout.setHorizontalGroup(
            noChangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(noChangeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
            .addGroup(noChangePanelLayout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(noChangeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        noChangePanelLayout.setVerticalGroup(
            noChangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, noChangePanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(noChangeLabel)
                .addGap(18, 18, 18)
                .addComponent(noChangeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout noChangeDialogLayout = new javax.swing.GroupLayout(noChangeDialog.getContentPane());
        noChangeDialog.getContentPane().setLayout(noChangeDialogLayout);
        noChangeDialogLayout.setHorizontalGroup(
            noChangeDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(noChangePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        noChangeDialogLayout.setVerticalGroup(
            noChangeDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(noChangePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setBackground(new java.awt.Color(255, 255, 255));

        personalInfoLabel.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        personalInfoLabel.setText("Personal Information");

        lastNameLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lastNameLabel.setText("Last Name :");

        employeeNoLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        employeeNoLabel.setText("Employee No :");

        firstNameLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        firstNameLabel.setText("First Name :");

        birthdayLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        birthdayLabel.setText("Birthday :");

        viewLabel.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        viewLabel.setText("View Employee Details");

        addressLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        addressLabel.setText("Address :");

        phoneLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        phoneLabel.setText("Phone Number : ");

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
            .addGap(0, 2, Short.MAX_VALUE)
        );

        statusLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        statusLabel.setText("Status :");

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

        govIdLabel.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        govIdLabel.setText("Government ID");

        sssLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        sssLabel.setText("Social Security # :");

        philHealthLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        philHealthLabel.setText("PhilHealth # :");

        pagibigLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        pagibigLabel.setText("PAG-IBIG # :");

        tinLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        tinLabel.setText("Tax Identification # :");

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
            .addGap(0, 2, Short.MAX_VALUE)
        );

        departmentLabel.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        departmentLabel.setText("Department");

        departmentNameLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        departmentNameLabel.setText("Department Name :");

        positionLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        positionLabel.setText("Position :");

        supervisorLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        supervisorLabel.setText("Supervisor :");

        updateBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        updateBtn.setText("Update Employee");
        updateBtn.addActionListener(this::updateBtnActionPerformed);

        employeeNoTextInput.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        employeeNoTextInput.setText("00000");
        employeeNoTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        employeeNoTextInput.setDoubleBuffered(true);
        employeeNoTextInput.setEnabled(false);

        firstNameTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        firstNameTextInput.setText("Don Justine");
        firstNameTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));

        lastNameTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        lastNameTextInput.setText("Fontanilla");
        lastNameTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));

        birthdayTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        birthdayTextInput.setText("Date");
        birthdayTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));

        addressTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        addressTextInput.setText("170 Fairview, Dasmarinas, Commonwealth, San Agustin, Quezon City");
        addressTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));

        phoneTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        phoneTextInput.setText("+639569978123");
        phoneTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));

        sssTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        sssTextInput.setText("+639569978123");

        philhealthTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        philhealthTextInput.setText("+639569978123");

        pagibigTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        pagibigTextInput.setText("+639569978123");

        tinTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tinTextInput.setText("+639569978123");

        departmentTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        departmentTextInput.setText("+639569978123");

        positionTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        positionTextInput.setText("+639569978123");

        supervisorTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        supervisorTextInput.setText("+639569978123");

        statusTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        statusTextInput.setText("+639569978123");

        closeViewBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        closeViewBtn.setText("Close");
        closeViewBtn.addActionListener(this::closeViewBtnActionPerformed);

        addOrUpdateBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        addOrUpdateBtn.setText("Update");
        addOrUpdateBtn.addActionListener(this::addOrUpdateBtnActionPerformed);

        cancelAddOrUpdateBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        cancelAddOrUpdateBtn.setText("Cancel");
        cancelAddOrUpdateBtn.addActionListener(this::cancelAddOrUpdateBtnActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(decorLine2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(decorLine3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(employeeNoLabel)
                            .addComponent(firstNameLabel)
                            .addComponent(phoneLabel)
                            .addComponent(addressLabel)
                            .addComponent(birthdayLabel)
                            .addComponent(lastNameLabel))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(employeeNoTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(firstNameTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastNameTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(birthdayTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phoneTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sssLabel)
                            .addComponent(philHealthLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sssTextInput, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                            .addComponent(philhealthTextInput))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tinLabel)
                            .addComponent(pagibigLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pagibigTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tinTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(govIdLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(departmentLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(departmentNameLabel)
                            .addComponent(positionLabel)
                            .addComponent(supervisorLabel)
                            .addComponent(statusLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(departmentTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(positionTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(supervisorTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(statusTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(personalInfoLabel)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(viewLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
            .addGroup(layout.createSequentialGroup()
                .addGap(304, 304, 304)
                .addComponent(closeViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelAddOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(viewLabel)
                    .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(personalInfoLabel)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employeeNoLabel)
                    .addComponent(employeeNoTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstNameLabel)
                    .addComponent(firstNameTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastNameLabel)
                    .addComponent(lastNameTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(birthdayLabel)
                    .addComponent(birthdayTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressLabel)
                    .addComponent(addressTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneLabel)
                    .addComponent(phoneTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(decorLine2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(govIdLabel)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sssLabel)
                    .addComponent(pagibigLabel)
                    .addComponent(sssTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pagibigTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(philHealthLabel)
                    .addComponent(tinLabel)
                    .addComponent(philhealthTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tinTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(decorLine3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(departmentLabel)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(departmentNameLabel)
                    .addComponent(departmentTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(positionLabel)
                    .addComponent(positionTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supervisorLabel)
                    .addComponent(supervisorTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel)
                    .addComponent(statusTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelAddOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        // TODO add your handling code here:
        isEditing = !isEditing;
        updateFields();
    }//GEN-LAST:event_updateBtnActionPerformed

    private void closeViewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeViewBtnActionPerformed
        // TODO add your handling code here:
        parentDialog.dispose();
    }//GEN-LAST:event_closeViewBtnActionPerformed

    private void addOrUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addOrUpdateBtnActionPerformed
        // TODO add your handling code here:

        if (!hasChanges()) {
            noChangeScreenDialog();
            return;
        }
        
        isConfirmingUpdate = true;
        customScreenDialog("Save Changes", "Do you want to save your changes?");
    }//GEN-LAST:event_addOrUpdateBtnActionPerformed

    private void cancelAddOrUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelAddOrUpdateBtnActionPerformed
        // TODO add your handling code here:
        if (!hasChanges()) {
            isEditing = !isEditing;
            updateFields();
            return;
        }
        
        isConfirmingCancel = true;
        customScreenDialog("Cancel Editing", "You have unsaved changes. Discard them?");
    }//GEN-LAST:event_cancelAddOrUpdateBtnActionPerformed

    private void cancelBtnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnConfirmActionPerformed
        // TODO add your handling code here:
        isConfirmingCancel = false;
        isConfirmingUpdate = false;
        customDialog.dispose();

    }//GEN-LAST:event_cancelBtnConfirmActionPerformed

    private void confirmBtnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmBtnConfirmActionPerformed
        // TODO add your handling code here:
        customDialog.dispose();
        
        if (isConfirmingUpdate) {
            this.selectedEmployee = buildEmployeeFromFields();
        }
        
        isEditing = !isEditing;
        fillEmployeeInformation(selectedEmployee);
        updateFields();
        
        isConfirmingCancel = false;
        isConfirmingUpdate = false;
    }//GEN-LAST:event_confirmBtnConfirmActionPerformed

    private void noChangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noChangeButtonActionPerformed
        // TODO add your handling code here:
        noChangeDialog.dispose();
        isEditing = !isEditing;
        updateFields();
    }//GEN-LAST:event_noChangeButtonActionPerformed

    private void noChangeDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_noChangeDialogWindowClosing
        // TODO add your handling code here:
        isEditing = !isEditing;
        updateFields();
    }//GEN-LAST:event_noChangeDialogWindowClosing

    private void customDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_customDialogWindowClosing
        // TODO add your handling code here:
        isConfirmingCancel = false;
        isConfirmingUpdate = false;
        customDialog.dispose();

    }//GEN-LAST:event_customDialogWindowClosing
    
    
    private boolean isConfirmingUpdate;
    private boolean isConfirmingCancel;
    private boolean isEditing;
    private final javax.swing.JDialog parentDialog;
    private Employee selectedEmployee;
    private AppContext appContext;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addOrUpdateBtn;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JTextField addressTextInput;
    private javax.swing.JLabel birthdayLabel;
    private javax.swing.JTextField birthdayTextInput;
    private javax.swing.JButton cancelAddOrUpdateBtn;
    private javax.swing.JButton cancelBtnConfirm;
    private javax.swing.JLabel cancelConfirmLabel;
    private javax.swing.JPanel cancelConfirmPanel;
    private javax.swing.JButton closeViewBtn;
    private javax.swing.JButton confirmBtnConfirm;
    private javax.swing.JDialog customDialog;
    private javax.swing.JPanel decorLine;
    private javax.swing.JPanel decorLine2;
    private javax.swing.JPanel decorLine3;
    private javax.swing.JLabel departmentLabel;
    private javax.swing.JLabel departmentNameLabel;
    private javax.swing.JTextField departmentTextInput;
    private javax.swing.JLabel employeeNoLabel;
    private javax.swing.JTextField employeeNoTextInput;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameTextInput;
    private javax.swing.JLabel govIdLabel;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JTextField lastNameTextInput;
    private javax.swing.JButton noChangeButton;
    private javax.swing.JDialog noChangeDialog;
    private javax.swing.JLabel noChangeLabel;
    private javax.swing.JPanel noChangePanel;
    private javax.swing.JLabel pagibigLabel;
    private javax.swing.JTextField pagibigTextInput;
    private javax.swing.JLabel personalInfoLabel;
    private javax.swing.JLabel philHealthLabel;
    private javax.swing.JTextField philhealthTextInput;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JTextField phoneTextInput;
    private javax.swing.JLabel positionLabel;
    private javax.swing.JTextField positionTextInput;
    private javax.swing.JLabel sssLabel;
    private javax.swing.JTextField sssTextInput;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField statusTextInput;
    private javax.swing.JLabel supervisorLabel;
    private javax.swing.JTextField supervisorTextInput;
    private javax.swing.JLabel tinLabel;
    private javax.swing.JTextField tinTextInput;
    private javax.swing.JButton updateBtn;
    private javax.swing.JLabel viewLabel;
    // End of variables declaration//GEN-END:variables
}
