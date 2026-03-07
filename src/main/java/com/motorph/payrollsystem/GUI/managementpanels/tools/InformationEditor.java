/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.gui.managementpanels.tools;

import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.model.employee.CompProfile;
import com.motorph.payrollsystem.model.employee.ContactInfo;
import com.motorph.payrollsystem.model.employee.DepartmentInfo;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.model.employee.GovIds;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.DepartmentResolver;
import com.motorph.payrollsystem.utility.Mapper;
import com.motorph.payrollsystem.utility.RegexPattern;
import com.motorph.payrollsystem.utility.ThemeColor;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

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
        this.policy = appContext.getSessionManager().getAccessPolicy();
        this.selectedEmployee = selectedEmployee;
        this.parentDialog = dialog;
        this.isEditing = false;
        this.isConfirmingCancel = false;
        this.isConfirmingUpdate = false;
        
        initComponents();
        fillEmployeeInformation(selectedEmployee);
        setDatePicker();
        updateFields();
    }
    
    //Fill fields with information
    private void fillEmployeeInformation(Employee emp) {
        //personal information
        employeeNoTextInput.setText(emp.getEmployeeNo());
        firstNameTextInput.setText(emp.getFirstName());
        lastNameTextInput.setText(emp.getLastName());
        bdayPicker.setDate(emp.getBirthday());
        addressTextInput.setText(emp.getContactInfo().getAddress());
        phoneTextInput.setText(emp.getContactInfo().getPhoneNumber());
        
        //govt id
        sssTextInput.setText(emp.getGovIds().getSssNumber());
        philhealthTextInput.setText(emp.getGovIds().getPhilHealthNumber());
        pagibigTextInput.setText(emp.getGovIds().getPagibigNumber());
        tinTextInput.setText(emp.getGovIds().getTinNumber());
        
        //dept info
        departmentTextInput.setText(emp.getDepartmentInfo().getDepartment());
        departmentTextFieldTemp.setText(emp.getDepartmentInfo().getDepartment());
        
        updatePosition(emp.getDepartmentInfo().getPosition());
        updateSupervisor(emp.getDepartmentInfo().getSupervisor());
        updateStatus(emp.getDepartmentInfo().getStatus());
    }
    
    
    private void setDatePicker() {
        updateBdayBorder();
        bdayPicker.getComponentDateTextField().setEditable(false);
        bdayPicker.getComponentDateTextField().setFocusable(false);
        bdayPicker.getComponentDateTextField().setOpaque(true);
    }
    
    private void updatePosition(String empPosition) {
        try {
            List<String> positions = appContext.getEmployeeService().getUniquePosition(policy);
            loadComboBoxItems(positionComboBox, positions);
            setSelectedValue(positionComboBox, positionTextInput, empPosition);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateSupervisor(String immSupervisor) {
        try {
            List<String> supervisors = appContext.getEmployeeService().getAllEmployeeNames(policy);
            loadComboBoxItems(supervisorComboBox, supervisors);
            supervisorComboBox.addItem("N/A");
            setSelectedValue(supervisorComboBox, supervisorTextInput, immSupervisor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void updateStatus(String empStatus) {
        try {
            List<String> statuses = appContext.getEmployeeService().getUniqueStatus(policy);
            loadComboBoxItems(statusComboBox, statuses);
            setSelectedValue(statusComboBox, statusTextInput, empStatus);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void loadComboBoxItems(javax.swing.JComboBox<String> comboBox, List<String> items) {
        comboBox.removeAllItems();

        for (String item : items) {
            comboBox.addItem(item);
        }
    }

    private void setSelectedValue(javax.swing.JComboBox<String> comboBox, 
            javax.swing.JTextField textField, 
            String value) {
        comboBox.setSelectedItem(value);
        textField.setText(value);
    }
    
    //Update visibility of the field base on the current mode
    private void updateFields() {
        String view = isEditing ? "Edit" : "View";
        viewLabel.setText(view + " Employee Details");
        
        buttonsVisibility(updateBtn, !isEditing);
        buttonsVisibility(closeViewBtn, !isEditing);
        buttonsVisibility(removeBtn, isEditing);
        buttonsVisibility(cancelAddOrUpdateBtn,isEditing);
        buttonsVisibility(addOrUpdateBtn, isEditing);
        
        fieldEnabler(firstNameTextInput);
        fieldEnabler(lastNameTextInput);
        fieldEnabler(bdayPicker);
        fieldEnabler(addressTextInput);
        fieldEnabler(phoneTextInput);
        
        fieldEnabler(sssTextInput);
        fieldEnabler(philhealthTextInput);
        fieldEnabler(pagibigTextInput);
        fieldEnabler(tinTextInput);
        
        fieldEnabler(departmentTextInput, departmentTextFieldTemp);
        fieldEnabler(positionTextInput, positionComboBox);
        fieldEnabler(supervisorTextInput, supervisorComboBox);
        fieldEnabler(statusTextInput, statusComboBox);
        
    }
    
    private void buttonsVisibility(javax.swing.JButton btn, boolean isVisible) {
        btn.setEnabled(isVisible);
        btn.setVisible(isVisible);
    }
    
    private void fieldEnabler(com.github.lgooddatepicker.components.DatePicker datePicker) {
        //datepicker toggle
        datePicker.getComponentDateTextField().setEnabled(isEditing);
        datePicker.getComponentToggleCalendarButton().setEnabled(isEditing);
        
        //datepicker textfield
        updateBdayBorder();
        java.awt.Color fieldColor = isEditing ? ThemeColor.activeText() : ThemeColor.textDisabled();
        datePicker.getComponentDateTextField().setForeground(fieldColor);
        datePicker.setEnabled(isEditing);
        datePicker.getComponentDateTextField().setBackground(ThemeColor.white());
        
    }
    
    private void fieldEnabler(javax.swing.JTextField textfield) {
        fieldEnabler(textfield, isEditing);
    }
    
    private void fieldEnabler(javax.swing.JTextField textField, boolean isEnable) {
        textField.setEnabled(isEnable);
        textField.setDisabledTextColor(ThemeColor.textDisabled());
    }
    
    private void fieldEnabler(javax.swing.JTextField textField, javax.swing.JTextField tempField) {
        fieldEnabler(textField, false); 
        textField.setVisible(!isEditing);
        
        fieldEnabler(tempField, false);
        tempField.setVisible(isEditing);
    }
    
    private void fieldEnabler(javax.swing.JTextField textField, javax.swing.JComboBox comboBox) {
        textField.setEnabled(isEditing);
        textField.setVisible(!isEditing);
        textField.setDisabledTextColor(ThemeColor.textDisabled());
        
        comboBox.setEnabled(isEditing);
        comboBox.setVisible(isEditing);
        comboBox.setBackground(ThemeColor.white());
        
    }
    
    private void updateBdayBorder() {
        javax.swing.border.LineBorder activeBorder = new javax.swing.border.LineBorder(ThemeColor.activeBorder(), 1);
        javax.swing.border.Border disabledBorder = firstNameTextInput.getBorder();
        
        bdayPicker.getComponentDateTextField().setBorder(
                isEditing ? 
                        activeBorder :
                        disabledBorder
                        );
    }
    
    //Check if theres changes
    private boolean hasChanges() {
        return !employeeNoTextInput.getText().equals(selectedEmployee.getEmployeeNo()) ||
                !firstNameTextInput.getText().equals(selectedEmployee.getFirstName()) ||
                !lastNameTextInput.getText().equals(selectedEmployee.getLastName()) ||
                !bdayPicker.getDate().equals(selectedEmployee.getBirthday()) ||
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
    
    private void dialogOpener(javax.swing.JDialog dialog, String title) {
        
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentDialog);
        dialog.setTitle(title);

        dialog.setVisible(true);  
    }
    
    private void noChangeScreenDialog() {
        noChangeDialog.pack();
        noChangeDialog.setResizable(false);
        noChangeDialog.setLocationRelativeTo(parentDialog);
        noChangeDialog.setTitle("Nothing to Save");
        
        noChangeDialog.setVisible(true);
    }
    
    private List<String> validateForm() {
        List<String> errors = new ArrayList<>();
        
        validateFirstName(errors);
        validateLastName(errors);
        validateBirthday(errors);
        validateAddress(errors);
        validatePhoneNumber(errors);
        validateSSS(errors);
        validatePhilhealth(errors);
        validatePagIbig(errors);
        validateTIN(errors);

        return errors;
    }
    
    private void validateFirstName(List<String> errors) {
        String firstName = firstNameTextInput.getText().trim();
        if(firstName.isEmpty()) {
            errors.add("First name is required.");
        } else if (!firstName.matches(RegexPattern.namePattern())) {
            errors.add("First name contains invalid characters.");
        }
    }
    private void validateLastName(List<String> errors) {
        String lastName = lastNameTextInput.getText().trim();
        if(lastName.isEmpty()) {
            errors.add("Last name is required");
        } else if (!lastName.matches(RegexPattern.namePattern())) {
            errors.add("Last name contains invalid characters.");
        }
    }
    private void validateBirthday(List<String> errors) {
        LocalDate bday = bdayPicker.getDate();
        if (bday == null) {
            errors.add("Birthday is required.");
        } else {
            LocalDate today = LocalDate.now();
            LocalDate youngestAllowed = today.minusYears(18);
            LocalDate oldestAllowed = today.minusYears(65); 
            if (bday.isAfter(youngestAllowed)) {
                errors.add("Employee must be born on or before " + youngestAllowed + ".");
            } else if (bday.isBefore(oldestAllowed)) {
                errors.add("Employee must be born on or after " + oldestAllowed + ".");
            }
        }
    }
    private void validateAddress(List<String> errors) {
        String address = addressTextInput.getText().trim();
        if (address.isEmpty()) {
            errors.add("Address is required.");
        } else if (!address.matches(RegexPattern.addressPattern())) {
            errors.add("Address must contain letters or numbers.");
        }
    }
    private void validatePhoneNumber(List<String> errors) {
        String phone = phoneTextInput.getText().trim();

        if (phone.isEmpty()) {
            errors.add("Phone number is required.");
        } else if (!phone.matches("^\\d{3}-\\d{3}-\\d{3}$")) {
            errors.add("Phone number must follow the format 123-456-789.");
        }
    }
    private void validateSSS(List<String> errors) {
        String sss = sssTextInput.getText().trim();

        if (sss.isEmpty()) {
            errors.add("SSS number is required.");
        } else if (!sss.matches(RegexPattern.sssPattern())) {
            errors.add("SSS number must follow the format 12-3456789-0.");
        }
    }
    private void validatePhilhealth(List<String> errors) {
        String philHealth = philhealthTextInput.getText().trim();

        if (philHealth.isEmpty()) {
            errors.add("PhilHealth number is required.");
        } else if (!philHealth.matches(RegexPattern.philhealthPattern())) {
            errors.add("PhilHealth number must contain exactly 12 digits.");
        }
    }
    private void validatePagIbig(List<String> errors) {
        String pagIbig = pagibigTextInput.getText().trim();

        if (pagIbig.isEmpty()) {
            errors.add("Pag-IBIG number is required.");
        } else if (!pagIbig.matches(RegexPattern.pagibigPattern())) {
            errors.add("Pag-IBIG number must contain exactly 12 digits.");
        }
    }
    private void validateTIN(List<String> errors) {
        String tin = tinTextInput.getText().trim();

        if (tin.isEmpty()) {
            errors.add("TIN is required.");
        } else if (!tin.matches(RegexPattern.tinPattern())) {
            errors.add("TIN must follow the format ###-###-###-###.");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cancelDialog = new javax.swing.JDialog(this.parentDialog, true);
        cancelConfirmPanel = new javax.swing.JPanel();
        cancelDialogLabel = new javax.swing.JLabel();
        cancelDialogCancelBtn = new javax.swing.JButton();
        cancelDialogConfirmBtn = new javax.swing.JButton();
        noChangeDialog = new javax.swing.JDialog(this.parentDialog, true);
        noChangePanel = new javax.swing.JPanel();
        noChangeLabel = new javax.swing.JLabel();
        noChangeButton = new javax.swing.JButton();
        updateDialog = new javax.swing.JDialog(this.parentDialog, true);
        updateDialogPanel = new javax.swing.JPanel();
        updateDialogLabel = new javax.swing.JLabel();
        updateDialogCancelBtn = new javax.swing.JButton();
        updateDialogSaveBtn = new javax.swing.JButton();
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
        addressTextInput = new javax.swing.JTextField();
        phoneTextInput = new javax.swing.JTextField();
        sssTextInput = new javax.swing.JTextField();
        philhealthTextInput = new javax.swing.JTextField();
        pagibigTextInput = new javax.swing.JTextField();
        tinTextInput = new javax.swing.JTextField();
        departmentTextInput = new javax.swing.JTextField();
        positionTextInput = new javax.swing.JTextField();
        statusTextInput = new javax.swing.JTextField();
        addOrUpdateBtn = new javax.swing.JButton();
        cancelAddOrUpdateBtn = new javax.swing.JButton();
        bdayPicker = new com.github.lgooddatepicker.components.DatePicker();
        supervisorTextInput = new javax.swing.JTextField();
        positionComboBox = new javax.swing.JComboBox<>();
        supervisorComboBox = new javax.swing.JComboBox<>();
        statusComboBox = new javax.swing.JComboBox<>();
        removeBtn = new javax.swing.JButton();
        closeViewBtn = new javax.swing.JButton();
        departmentTextFieldTemp = new javax.swing.JTextField();

        cancelDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        cancelDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                cancelDialogWindowClosing(evt);
            }
        });

        cancelConfirmPanel.setBackground(new java.awt.Color(255, 255, 255));

        cancelDialogLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cancelDialogLabel.setText("You have unsaved changes. Discard them?");
        cancelDialogLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        cancelDialogCancelBtn.setText("Cancel");
        cancelDialogCancelBtn.setBackground(ThemeColor.lightRed());
        cancelDialogCancelBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        cancelDialogCancelBtn.addActionListener(this::cancelDialogCancelBtnActionPerformed);

        cancelDialogConfirmBtn.setText("Confirm");
        cancelDialogConfirmBtn.setBackground(ThemeColor.lightGreen());
        cancelDialogConfirmBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        cancelDialogConfirmBtn.addActionListener(this::cancelDialogConfirmBtnActionPerformed);

        javax.swing.GroupLayout cancelConfirmPanelLayout = new javax.swing.GroupLayout(cancelConfirmPanel);
        cancelConfirmPanel.setLayout(cancelConfirmPanelLayout);
        cancelConfirmPanelLayout.setHorizontalGroup(
            cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cancelConfirmPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(cancelDialogCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(cancelDialogConfirmBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
            .addComponent(cancelDialogLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cancelConfirmPanelLayout.setVerticalGroup(
            cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cancelConfirmPanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(cancelDialogLabel)
                .addGap(18, 18, 18)
                .addGroup(cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelDialogCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelDialogConfirmBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout cancelDialogLayout = new javax.swing.GroupLayout(cancelDialog.getContentPane());
        cancelDialog.getContentPane().setLayout(cancelDialogLayout);
        cancelDialogLayout.setHorizontalGroup(
            cancelDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cancelConfirmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        cancelDialogLayout.setVerticalGroup(
            cancelDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cancelConfirmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        noChangeDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        noChangeDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                noChangeDialogWindowClosing(evt);
            }
        });

        noChangePanel.setBackground(new java.awt.Color(255, 255, 255));

        noChangeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        noChangeLabel.setText("No changes detected.");
        noChangeLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        noChangeButton.setText("OK");
        noChangeButton.setBackground(ThemeColor.lightGreen());
        noChangeButton.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
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

        updateDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        updateDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                updateDialogWindowClosing(evt);
            }
        });

        updateDialogPanel.setBackground(new java.awt.Color(255, 255, 255));

        updateDialogLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        updateDialogLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateDialogLabel.setText("Do you want to save your changes?");

        updateDialogCancelBtn.setBackground(ThemeColor.lightRed());
        updateDialogCancelBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        updateDialogCancelBtn.setText("Cancel");
        updateDialogCancelBtn.addActionListener(this::updateDialogCancelBtnActionPerformed);

        updateDialogSaveBtn.setBackground(ThemeColor.lightGreen());
        updateDialogSaveBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        updateDialogSaveBtn.setText("Save");
        updateDialogSaveBtn.addActionListener(this::updateDialogSaveBtnActionPerformed);

        javax.swing.GroupLayout updateDialogPanelLayout = new javax.swing.GroupLayout(updateDialogPanel);
        updateDialogPanel.setLayout(updateDialogPanelLayout);
        updateDialogPanelLayout.setHorizontalGroup(
            updateDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateDialogPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(updateDialogCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(updateDialogSaveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
            .addComponent(updateDialogLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        updateDialogPanelLayout.setVerticalGroup(
            updateDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updateDialogPanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(updateDialogLabel)
                .addGap(18, 18, 18)
                .addGroup(updateDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateDialogCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateDialogSaveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout updateDialogLayout = new javax.swing.GroupLayout(updateDialog.getContentPane());
        updateDialog.getContentPane().setLayout(updateDialogLayout);
        updateDialogLayout.setHorizontalGroup(
            updateDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(updateDialogPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        updateDialogLayout.setVerticalGroup(
            updateDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(updateDialogPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setBackground(new java.awt.Color(255, 255, 255));

        personalInfoLabel.setText("Personal Information");
        personalInfoLabel.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N

        lastNameLabel.setText("Last Name :");
        lastNameLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        employeeNoLabel.setText("Employee No :");
        employeeNoLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        firstNameLabel.setText("First Name :");
        firstNameLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        birthdayLabel.setText("Birthday :");
        birthdayLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        viewLabel.setText("View Employee Details");
        viewLabel.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N

        addressLabel.setText("Address :");
        addressLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        phoneLabel.setText("Phone Number : ");
        phoneLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

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

        statusLabel.setText("Status :");
        statusLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

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

        govIdLabel.setText("Government ID");
        govIdLabel.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N

        sssLabel.setText("Social Security # :");
        sssLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        philHealthLabel.setText("PhilHealth # :");
        philHealthLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        pagibigLabel.setText("PAG-IBIG # :");
        pagibigLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        tinLabel.setText("Tax Identification # :");
        tinLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

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

        departmentLabel.setText("Department");
        departmentLabel.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N

        departmentNameLabel.setText("Department Name :");
        departmentNameLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        positionLabel.setText("Position :");
        positionLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        supervisorLabel.setText("Supervisor :");
        supervisorLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        updateBtn.setText("Update Employee");
        updateBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
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

        addressTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        addressTextInput.setText("170 Fairview, Dasmarinas, Commonwealth, San Agustin, Quezon City");
        addressTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));

        phoneTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        phoneTextInput.setText("+639569978123");
        phoneTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        phoneTextInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                phoneTextInputKeyTyped(evt);
            }
        });

        sssTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        sssTextInput.setText("+639569978123");
        sssTextInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                sssTextInputKeyTyped(evt);
            }
        });

        philhealthTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        philhealthTextInput.setText("+639569978123");
        philhealthTextInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                philhealthTextInputKeyTyped(evt);
            }
        });

        pagibigTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        pagibigTextInput.setText("+639569978123");

        tinTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tinTextInput.setText("+639569978123");
        tinTextInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tinTextInputKeyTyped(evt);
            }
        });

        departmentTextInput.setEditable(false);
        departmentTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        departmentTextInput.setText("+639569978123");
        departmentTextInput.setBackground(new java.awt.Color(255, 255, 255));

        positionTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        positionTextInput.setText("+639569978123");

        statusTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        statusTextInput.setText("+639569978123");

        addOrUpdateBtn.setText("Update");
        addOrUpdateBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        addOrUpdateBtn.addActionListener(this::addOrUpdateBtnActionPerformed);

        cancelAddOrUpdateBtn.setText("Cancel");
        cancelAddOrUpdateBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        cancelAddOrUpdateBtn.addActionListener(this::cancelAddOrUpdateBtnActionPerformed);

        bdayPicker.setBackground(new java.awt.Color(255, 255, 255));
        bdayPicker.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        bdayPicker.setForeground(new java.awt.Color(255, 255, 255));

        supervisorTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        supervisorTextInput.setText("+639569978123");
        supervisorTextInput.addActionListener(this::supervisorTextInputActionPerformed);

        positionComboBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        positionComboBox.addActionListener(this::positionComboBoxActionPerformed);

        supervisorComboBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        supervisorComboBox.addActionListener(this::supervisorComboBoxActionPerformed);

        statusComboBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        statusComboBox.addActionListener(this::statusComboBoxActionPerformed);

        removeBtn.setText("Remove");
        removeBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        removeBtn.addActionListener(this::removeBtnActionPerformed);

        closeViewBtn.setText("Close");
        closeViewBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        closeViewBtn.addActionListener(this::closeViewBtnActionPerformed);

        departmentTextFieldTemp.setEditable(false);
        departmentTextFieldTemp.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        departmentTextFieldTemp.setText("+639569978123");
        departmentTextFieldTemp.setBackground(new java.awt.Color(255, 255, 255));
        departmentTextFieldTemp.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(decorLine2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(decorLine3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelAddOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(viewLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
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
                            .addComponent(phoneTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(bdayPicker, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lastNameTextInput, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE))))
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
                        .addGap(24, 24, 24)
                        .addComponent(personalInfoLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(192, 192, 192)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(supervisorTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(positionTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(statusTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(supervisorComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(statusComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(positionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(departmentLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(positionLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(departmentNameLabel)
                                .addGap(18, 18, 18)
                                .addComponent(departmentTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(departmentTextFieldTemp, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(supervisorLabel)
                            .addComponent(statusLabel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
                    .addComponent(bdayPicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(17, 17, 17)
                .addComponent(departmentLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(departmentTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(departmentNameLabel)
                    .addComponent(departmentTextFieldTemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(positionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(positionLabel)
                        .addComponent(positionTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supervisorTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supervisorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supervisorLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelAddOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(closeViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
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
        //check for changes
        if (!hasChanges()) {
            noChangeScreenDialog();
            return;
        }
        
        //validity check
        List<String> validate = validateForm();
        if (!validate.isEmpty()) {
            System.out.println("error");
            //errorDialog
            return;
        }
        
        dialogOpener(updateDialog, "Save Changes");
    }//GEN-LAST:event_addOrUpdateBtnActionPerformed

    private void cancelAddOrUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelAddOrUpdateBtnActionPerformed
        // TODO add your handling code here:
        if (!hasChanges()) {
            isEditing = !isEditing;
            updateFields();
            return;
        }
        
        dialogOpener(cancelDialog, "Cancel Editing");
    }//GEN-LAST:event_cancelAddOrUpdateBtnActionPerformed

    private void cancelDialogCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDialogCancelBtnActionPerformed
        // TODO add your handling code here:
        cancelDialog.dispose();
    }//GEN-LAST:event_cancelDialogCancelBtnActionPerformed

    private void cancelDialogConfirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDialogConfirmBtnActionPerformed
        // TODO add your handling code here:
        //when confirming update
//        positionTextInput.getText().trim(),

        isEditing = !isEditing;
        fillEmployeeInformation(selectedEmployee);
        updateFields();
        cancelDialog.dispose();
    }//GEN-LAST:event_cancelDialogConfirmBtnActionPerformed

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

    private void cancelDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_cancelDialogWindowClosing
        // TODO add your handling code here:
        isConfirmingCancel = false;
        isConfirmingUpdate = false;
        cancelDialog.dispose();

    }//GEN-LAST:event_cancelDialogWindowClosing

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_removeBtnActionPerformed

    private void phoneTextInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phoneTextInputKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();

        // allow only digits
        if (!Character.isDigit(c)) {
            evt.consume();
            return;
        }

        String text = phoneTextInput.getText().replace("-", "");

        // limit to 9 digits
        if (text.length() >= 9) {
            evt.consume();
            return;
        }

        // add the new digit
        text += c;

        // rebuild formatted text
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (i > 0 && i % 3 == 0) {
                formatted.append("-");
            }
            formatted.append(text.charAt(i));
        }

        phoneTextInput.setText(formatted.toString());

        evt.consume();
    }//GEN-LAST:event_phoneTextInputKeyTyped

    private void sssTextInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sssTextInputKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();

        // allow digits only
        if (!Character.isDigit(c)) {
            evt.consume();
            return;
        }

        String text = sssTextInput.getText().replace("-", "");
        // limit to 10 digits total
        if (text.length() >= 10) {
            evt.consume();
            return;
        }

        text += c;

        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (i == 2 || i == 9) {
                formatted.append("-");
            }
            formatted.append(text.charAt(i));
        }
        
        sssTextInput.setText(formatted.toString());
        evt.consume();
    }//GEN-LAST:event_sssTextInputKeyTyped

    private void philhealthTextInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_philhealthTextInputKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();

        // allow digits only
        if (!Character.isDigit(c)) {
            evt.consume();
            return;
        }

        // limit to 12 digits
        if (philhealthTextInput.getText().length() >= 12) {
            evt.consume();
        }
    }//GEN-LAST:event_philhealthTextInputKeyTyped

    private void tinTextInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tinTextInputKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();

        // digits only
        if (!Character.isDigit(c)) {
            evt.consume();
            return;
        }

        String text = tinTextInput.getText().replace("-", "");

        // limit to 12 digits
        if (text.length() >= 12) {
            evt.consume();
            return;
        }

        text += c;

        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (i == 3 || i == 6 || i == 9) {
                formatted.append("-");
            }
            formatted.append(text.charAt(i));
        }

        tinTextInput.setText(formatted.toString());

        evt.consume();
    }//GEN-LAST:event_tinTextInputKeyTyped

    private void supervisorTextInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supervisorTextInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supervisorTextInputActionPerformed

    private void supervisorComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supervisorComboBoxActionPerformed
        // TODO add your handling code here:
        String selected = (String) supervisorComboBox.getSelectedItem();
        supervisorTextInput.setText(selected);
    }//GEN-LAST:event_supervisorComboBoxActionPerformed

    private void positionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionComboBoxActionPerformed
        // TODO add your handling code here:
        String selected = (String) positionComboBox.getSelectedItem();
        selected = (selected == null) ? "" : selected;
        positionTextInput.setText(selected);
        
        departmentTextFieldTemp.setText(DepartmentResolver.getDepartmentName(selected));
        departmentTextInput.setText(DepartmentResolver.getDepartmentName(selected));
    }//GEN-LAST:event_positionComboBoxActionPerformed

    private void updateDialogCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateDialogCancelBtnActionPerformed
        // TODO add your handling code here:
        updateDialog.dispose();
    }//GEN-LAST:event_updateDialogCancelBtnActionPerformed

    private void updateDialogSaveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateDialogSaveBtnActionPerformed
        // TODO add your handling code here:
        //save editing
        Employee updateEmployee = Mapper.buildEmployee(
            selectedEmployee,
            lastNameTextInput.getText().trim(),
            firstNameTextInput.getText().trim(),
            Dates.formatDate(bdayPicker.getDate()),
            addressTextInput.getText().trim(),
            phoneTextInput.getText().trim(),
            sssTextInput.getText().trim(),
            philhealthTextInput.getText().trim(),
            tinTextInput.getText().trim(),
            pagibigTextInput.getText().trim(),
            statusTextInput.getText().trim(),
            "position-replacer",
            supervisorTextInput.getText().trim()
        );
            

        try {
            this.selectedEmployee = appContext.getEmployeeService().updateEmployee(updateEmployee);
                
        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to update employee.",
                    "Update Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
        
        
        isEditing = !isEditing;
        fillEmployeeInformation(selectedEmployee);
        updateFields();
        updateDialog.dispose();
    }//GEN-LAST:event_updateDialogSaveBtnActionPerformed

    private void updateDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_updateDialogWindowClosing
        // TODO add your handling code here:
    }//GEN-LAST:event_updateDialogWindowClosing

    private void statusComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusComboBoxActionPerformed
        // TODO add your handling code here:
        String selected = (String) statusComboBox.getSelectedItem();
        statusTextInput.setText(selected);
    }//GEN-LAST:event_statusComboBoxActionPerformed
    
    public void setViewingMode(boolean viewingMode) {
        isViewing = viewingMode;
    }
    
    private boolean isViewing;
    private boolean isConfirmingUpdate;
    private boolean isConfirmingCancel;
    private boolean isEditing;
    private AccessPolicy policy;
    private final javax.swing.JDialog parentDialog;
    private Employee selectedEmployee;
    private AppContext appContext;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addOrUpdateBtn;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JTextField addressTextInput;
    private com.github.lgooddatepicker.components.DatePicker bdayPicker;
    private javax.swing.JLabel birthdayLabel;
    private javax.swing.JButton cancelAddOrUpdateBtn;
    private javax.swing.JPanel cancelConfirmPanel;
    private javax.swing.JDialog cancelDialog;
    private javax.swing.JButton cancelDialogCancelBtn;
    private javax.swing.JButton cancelDialogConfirmBtn;
    private javax.swing.JLabel cancelDialogLabel;
    private javax.swing.JButton closeViewBtn;
    private javax.swing.JPanel decorLine;
    private javax.swing.JPanel decorLine2;
    private javax.swing.JPanel decorLine3;
    private javax.swing.JLabel departmentLabel;
    private javax.swing.JLabel departmentNameLabel;
    private javax.swing.JTextField departmentTextFieldTemp;
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
    private javax.swing.JComboBox<String> positionComboBox;
    private javax.swing.JLabel positionLabel;
    private javax.swing.JTextField positionTextInput;
    private javax.swing.JButton removeBtn;
    private javax.swing.JLabel sssLabel;
    private javax.swing.JTextField sssTextInput;
    private javax.swing.JComboBox<String> statusComboBox;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField statusTextInput;
    private javax.swing.JComboBox<String> supervisorComboBox;
    private javax.swing.JLabel supervisorLabel;
    private javax.swing.JTextField supervisorTextInput;
    private javax.swing.JLabel tinLabel;
    private javax.swing.JTextField tinTextInput;
    private javax.swing.JButton updateBtn;
    private javax.swing.JDialog updateDialog;
    private javax.swing.JButton updateDialogCancelBtn;
    private javax.swing.JLabel updateDialogLabel;
    private javax.swing.JPanel updateDialogPanel;
    private javax.swing.JButton updateDialogSaveBtn;
    private javax.swing.JLabel viewLabel;
    // End of variables declaration//GEN-END:variables
}
