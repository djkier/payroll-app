/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.gui.managementpanels.tools;

import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.DepartmentResolver;
import com.motorph.payrollsystem.utility.Mapper;
import com.motorph.payrollsystem.utility.RegexPattern;
import com.motorph.payrollsystem.utility.ThemeColor;
import java.awt.Image;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.ImageIcon;

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
        Boolean viewingMode,
        javax.swing.JDialog dialog) {
        
        this.appContext = appContext;
        this.policy = appContext.getSessionManager().getAccessPolicy();
        this.selectedEmployee = selectedEmployee;
        this.currentEmployee = appContext.getSessionManager().getCurrentEmployee();
        this.parentDialog = dialog;
        this.isEditing = !viewingMode;
        this.isViewing = viewingMode;
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

        fillInfo(employeeNoTextInput, emp.getEmployeeNo());
        fillInfo(firstNameTextInput, emp.getFirstName());
        fillInfo(lastNameTextInput, emp.getLastName());
        //make bday change into today or emp.getBirthday();
        fillInfo(bdayPicker, emp.getBirthday());
        fillInfo(addressTextInput, emp.getContactInfo().getAddress());
        fillInfo(phoneTextInput, emp.getContactInfo().getPhoneNumber());

        //govt id
        fillInfo(sssTextInput, emp.getGovIds().getSssNumber());
        fillInfo(philhealthTextInput, emp.getGovIds().getPhilHealthNumber());
        fillInfo(pagibigTextInput, emp.getGovIds().getPagibigNumber());
        fillInfo(tinTextInput, emp.getGovIds().getTinNumber());
        
        //dept info
        fillInfo(departmentTextInput, emp.getDepartmentInfo().getDepartment());
        fillInfo(departmentTextFieldTemp, emp.getDepartmentInfo().getDepartment());
        
        updatePosition(emp.getDepartmentInfo().getPosition());
        updateSupervisor(emp.getDepartmentInfo().getSupervisor());
        updateStatus(emp.getDepartmentInfo().getStatus());
    }
    
    private void fillInfo(javax.swing.JTextField textField, String fieldDetails) {
        fieldDetails = (fieldDetails == null) ? "" : fieldDetails;
        textField.setText(fieldDetails);
    }
    
    private void fillInfo(com.github.lgooddatepicker.components.DatePicker datePicker, LocalDate date) {
        date = (date == null) ? LocalDate.now() : date;
        datePicker.setDate(date);
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
        if (value == null) {
            comboBox.setSelectedIndex(0);
            textField.setText((String) comboBox.getSelectedItem());
        } else {
            comboBox.setSelectedItem(value);
            textField.setText(value);
        }
    }
    
    //Update visibility of the field base on the current mode
    private void updateFields() {
        setEditorTitle();
        setButtonsVisibility();

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
    
    private void setEditorTitle() {
        String viewTitle;
        
        if (!isViewing) {
            viewTitle = "Add Employee";
        } else {
            String view = isEditing ? "Edit" : "View";
            viewTitle = view + " Employee Details";
        }
        
        viewLabel.setText(viewTitle);
    }
    
    private void setButtonsVisibility() {
        boolean isAddMode = !isViewing;
        boolean isEditMode = isViewing && isEditing;
        boolean isViewMode = isViewing && !isEditing;
        
        //update and close btn should be visible when viewing
        buttonVisibility(updateBtn, isViewMode);
        buttonVisibility(closeViewBtn, isViewMode);
        
        buttonVisibility(removeBtn, isEditMode);
        buttonVisibility(cancelAddOrUpdateBtn, isEditing);
        buttonVisibility(addOrUpdateBtn, isEditing);
        
        if (isViewMode) {
            updateBtn.setEnabled(
                !selectedEmployee.getEmployeeNo().equals(currentEmployee.getEmployeeNo())
            );
        }
        
        addOrUpdateBtn.setText(isAddMode ? "Add" : "Update");
        
    }
    
    private void buttonVisibility(javax.swing.JButton btn, boolean isVisible) {
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
        return !Objects.equals(employeeNoTextInput.getText(), selectedEmployee.getEmployeeNo()) ||
               !Objects.equals(firstNameTextInput.getText(), selectedEmployee.getFirstName()) ||
               !Objects.equals(lastNameTextInput.getText(), selectedEmployee.getLastName()) ||
               !Objects.equals(bdayPicker.getDate(), selectedEmployee.getBirthday()) ||
               !Objects.equals(addressTextInput.getText(), selectedEmployee.getContactInfo().getAddress()) ||
               !Objects.equals(phoneTextInput.getText(), selectedEmployee.getContactInfo().getPhoneNumber()) ||
               !Objects.equals(sssTextInput.getText(), selectedEmployee.getGovIds().getSssNumber()) ||
               !Objects.equals(philhealthTextInput.getText(), selectedEmployee.getGovIds().getPhilHealthNumber()) ||
               !Objects.equals(pagibigTextInput.getText(), selectedEmployee.getGovIds().getPagibigNumber()) ||
               !Objects.equals(tinTextInput.getText(), selectedEmployee.getGovIds().getTinNumber()) ||
               !Objects.equals(departmentTextInput.getText(), selectedEmployee.getDepartmentInfo().getDepartment()) ||
               !Objects.equals(positionTextInput.getText(), selectedEmployee.getDepartmentInfo().getPosition()) ||
               !Objects.equals(supervisorTextInput.getText(), selectedEmployee.getDepartmentInfo().getSupervisor()) ||
               !Objects.equals(statusTextInput.getText(), selectedEmployee.getDepartmentInfo().getStatus());
    }
    
    private void dialogOpener(javax.swing.JDialog dialog, String title) {
        dialog.setResizable(false);
        dialog.setTitle(title);
        dialog.pack();
        dialog.setLocationRelativeTo(parentDialog);
        
        dialog.setVisible(true); 
    }
    
    private void openSuccessDialog(String title, String message, Employee emp) {
        successLabel.setText(message);
        successEmployeeLabel.setText(emp.getLastFirstName());
        dialogOpener(successDialog, title);
    }
    
    private List<String> validateForm() {
        List<String> errors = new ArrayList<>();
        
        validateNames("First Name", firstNameTextInput, RegexPattern.namePattern(), errors);
        validateNames("Last Name", lastNameTextInput, RegexPattern.namePattern(), errors);
        validateBirthday(errors);
        validateAddress(errors);
        validatePhoneNumber(errors);
        validateSSS(errors);
        validatePhilhealth(errors);
        validatePagIbig(errors);
        validateTIN(errors);
        validateNames("Department Name", departmentTextInput, RegexPattern.deptPattern(), errors);
        validateNames("Position", positionTextInput, RegexPattern.deptPattern(), errors);
        validateNames("Supervisor Name", supervisorTextInput, RegexPattern.deptPattern(), errors);
        validateNames("Status", statusTextInput, RegexPattern.deptPattern(), errors);

        return errors;
    }
    
    private void validateNames(String fieldName, 
            javax.swing.JTextField textField,
            String pattern,
            List<String> errors) {
        String name = textField.getText().trim();
        if(name == null || name.isEmpty()) {
            errors.add(fieldName + " is required.");
        } else if (!name.matches(pattern)) {
            errors.add(fieldName + " contains invalid characters.");
        }
    }
    private void validateBirthday(List<String> errors) {
        LocalDate bday = bdayPicker.getDate();
        if (bday == null) {
            errors.add("Birthday is required.");
        } else {
            LocalDate today = LocalDate.now();
            LocalDate youngestAllowed = today.minusYears(18);
            LocalDate oldestAllowed = today.minusYears(100); 
            if (bday.isAfter(youngestAllowed)) {
                errors.add("Employee must be born on or before " + Dates.monthYear(youngestAllowed) + ".");
            } else if (bday.isBefore(oldestAllowed)) {
                errors.add("Employee must be born on or after " + Dates.monthYear(oldestAllowed) + ".");
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

    private String formatErrorsForLabel(List<String> errors) {
        List<String> formatted = new ArrayList<>();
        for (String e : errors) {
            formatted.add("- " + e);
        }
        return "<html>" + String.join("<br>", formatted) + "</html>";
    }
    
    private void handleUpdateEmployee(Employee employee) {
        try {
            this.selectedEmployee = appContext.getEmployeeService().updateEmployee(employee);

            isEditing = false;
            fillEmployeeInformation(selectedEmployee);
            
            updateFields();
            updateDialog.dispose();
            openSuccessDialog("Successfully Updated", "You successfully updated:", selectedEmployee);
        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to update employee.",
                    "Update Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
    
    
    private void handleAddEmployee(Employee employee) {
        try {
            //Add employee
            this.selectedEmployee = appContext.getEmployeeService().addNewEmployee(employee);
            //add new account
            boolean accountCreated = addUserAccount(this.selectedEmployee);
            if (!accountCreated) {
                return;
            }
            
            updateDialog.dispose();
            parentDialog.dispose();
            openSuccessDialog("Successfully Added", "You succesfully added:", this.selectedEmployee);
            
            } catch (IllegalStateException e) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        e.getMessage(),
                        "Duplicate Employee",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                );

            } catch (IOException e) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Failed to add new employee.",
                        "Adding New Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
    }
    
    private boolean addUserAccount(Employee emp) {
        if (emp == null || emp.getEmployeeNo() == null || emp.getEmployeeNo().isBlank()) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Cannot create account because employee data is incomplete.",
                    "Add Employee Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        try {
            appContext.getUserAccountService().createDefaultAccount(emp.getEmployeeNo());
            return true;

        } catch (IllegalStateException | IOException ex) {
            // rollback employee if account creation fails
            try {
                appContext.getEmployeeService().removeEmployee(emp.getEmployeeNo());
            } catch (Exception rollbackEx) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Employee was added, but account creation failed and rollback also failed.\n"
                                + "Manual fix may be needed.\n\nReason: " + ex.getMessage(),
                        "Add Employee Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
                rollbackEx.printStackTrace();
                return false;
            }

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Employee creation was rolled back because account creation failed.\n\nReason: " + ex.getMessage(),
                    "Add Employee Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
   }
    
    private boolean removeUserAccount(Employee emp) {
        if (emp == null || emp.getEmployeeNo() == null || emp.getEmployeeNo().isBlank()) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Cannot remove user account because employee data is incomplete.",
                    "Remove Employee Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        try {
            appContext.getUserAccountService().deleteAccountByEmployeeNo(emp.getEmployeeNo());
            return true;

        } catch (IllegalArgumentException | IllegalStateException | IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to remove user account.\n\nReason: " + ex.getMessage(),
                    "Remove Employee Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
            return false;
        }
    }
    
    private Image salaryLogo() {
        ImageIcon newIcon = new ImageIcon(getClass().getResource("/images/tab-icon/module-icon/eim.png"));
        return newIcon.getImage();
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
        validationErrorDialog = new javax.swing.JDialog(this.parentDialog, true);
        validationErrorPanel = new javax.swing.JPanel();
        validationErrorBtn = new javax.swing.JButton();
        validationErrorHeader = new javax.swing.JLabel();
        validationErrorLabel = new javax.swing.JLabel();
        removeDialog = new javax.swing.JDialog(this.parentDialog, true);
        removeDialogPanel = new javax.swing.JPanel();
        removeDialogLabel = new javax.swing.JLabel();
        removeDialogCancelBtn = new javax.swing.JButton();
        removeDialogConfirmBtn = new javax.swing.JButton();
        removeDialogNumberLabel = new javax.swing.JLabel();
        removeDialogNameLabel = new javax.swing.JLabel();
        removeDialogNumberField = new javax.swing.JLabel();
        removeDialogNameField = new javax.swing.JLabel();
        removeDialogLabel1 = new javax.swing.JLabel();
        successDialog = new javax.swing.JDialog(this.parentDialog, true);
        successsPanel = new javax.swing.JPanel();
        successLabel = new javax.swing.JLabel();
        successBtn = new javax.swing.JButton();
        successEmployeeLabel = new javax.swing.JLabel();
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
        cancelDialog.setIconImage(salaryLogo());
        cancelDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                cancelDialogWindowClosing(evt);
            }
        });

        cancelConfirmPanel.setBackground(new java.awt.Color(255, 255, 255));

        cancelDialogLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cancelDialogLabel.setText("You have unsaved changes. Discard them?");
        cancelDialogLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        cancelDialogCancelBtn.setText("CANCEL");
        cancelDialogCancelBtn.setFocusPainted(false);
        cancelDialogCancelBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        cancelDialogCancelBtn.addActionListener(this::cancelDialogCancelBtnActionPerformed);

        cancelDialogConfirmBtn.setText("CONFIRM");
        cancelDialogConfirmBtn.setFocusPainted(false);
        cancelDialogConfirmBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        cancelDialogConfirmBtn.addActionListener(this::cancelDialogConfirmBtnActionPerformed);

        javax.swing.GroupLayout cancelConfirmPanelLayout = new javax.swing.GroupLayout(cancelConfirmPanel);
        cancelConfirmPanel.setLayout(cancelConfirmPanelLayout);
        cancelConfirmPanelLayout.setHorizontalGroup(
            cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cancelConfirmPanelLayout.createSequentialGroup()
                .addGroup(cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cancelConfirmPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(cancelDialogLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cancelConfirmPanelLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(cancelDialogCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(cancelDialogConfirmBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
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
        noChangeDialog.setIconImage(salaryLogo());
        noChangeDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                noChangeDialogWindowClosing(evt);
            }
        });

        noChangePanel.setBackground(new java.awt.Color(255, 255, 255));

        noChangeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        noChangeLabel.setText("No changes made.");
        noChangeLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        noChangeButton.setText("OK");
        noChangeButton.setFocusPainted(false);
        noChangeButton.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        noChangeButton.addActionListener(this::noChangeButtonActionPerformed);

        javax.swing.GroupLayout noChangePanelLayout = new javax.swing.GroupLayout(noChangePanel);
        noChangePanel.setLayout(noChangePanelLayout);
        noChangePanelLayout.setHorizontalGroup(
            noChangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(noChangePanelLayout.createSequentialGroup()
                .addGroup(noChangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(noChangePanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(noChangeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(noChangePanelLayout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(noChangeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
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
        updateDialog.setIconImage(salaryLogo());
        updateDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                updateDialogWindowClosing(evt);
            }
        });

        updateDialogPanel.setBackground(new java.awt.Color(255, 255, 255));

        updateDialogLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateDialogLabel.setText("Do you want to save your changes?");
        updateDialogLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        updateDialogCancelBtn.setText("CANCEL");
        updateDialogCancelBtn.setFocusPainted(false);
        updateDialogCancelBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        updateDialogCancelBtn.addActionListener(this::updateDialogCancelBtnActionPerformed);

        updateDialogSaveBtn.setText("SAVE");
        updateDialogSaveBtn.setFocusPainted(false);
        updateDialogSaveBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        updateDialogSaveBtn.addActionListener(this::updateDialogSaveBtnActionPerformed);

        javax.swing.GroupLayout updateDialogPanelLayout = new javax.swing.GroupLayout(updateDialogPanel);
        updateDialogPanel.setLayout(updateDialogPanelLayout);
        updateDialogPanelLayout.setHorizontalGroup(
            updateDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateDialogPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(updateDialogCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
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
            .addComponent(updateDialogPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        validationErrorDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        validationErrorDialog.setIconImage(salaryLogo());
        validationErrorDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                validationErrorDialogWindowClosing(evt);
            }
        });

        validationErrorPanel.setBackground(new java.awt.Color(255, 255, 255));

        validationErrorBtn.setText("OK");
        validationErrorBtn.setFocusPainted(false);
        validationErrorBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        validationErrorBtn.addActionListener(this::validationErrorBtnActionPerformed);

        validationErrorHeader.setText("The following field/s contain errors :");
        validationErrorHeader.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        validationErrorLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        validationErrorLabel.setText("- Last name contains invalid characters.");
        validationErrorLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        validationErrorLabel.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N

        javax.swing.GroupLayout validationErrorPanelLayout = new javax.swing.GroupLayout(validationErrorPanel);
        validationErrorPanel.setLayout(validationErrorPanelLayout);
        validationErrorPanelLayout.setHorizontalGroup(
            validationErrorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(validationErrorPanelLayout.createSequentialGroup()
                .addGroup(validationErrorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(validationErrorPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(validationErrorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(validationErrorPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(validationErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(validationErrorHeader)))
                    .addGroup(validationErrorPanelLayout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(validationErrorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        validationErrorPanelLayout.setVerticalGroup(
            validationErrorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, validationErrorPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(validationErrorHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(validationErrorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(validationErrorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout validationErrorDialogLayout = new javax.swing.GroupLayout(validationErrorDialog.getContentPane());
        validationErrorDialog.getContentPane().setLayout(validationErrorDialogLayout);
        validationErrorDialogLayout.setHorizontalGroup(
            validationErrorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(validationErrorDialogLayout.createSequentialGroup()
                .addComponent(validationErrorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        validationErrorDialogLayout.setVerticalGroup(
            validationErrorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(validationErrorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        removeDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        removeDialog.setIconImage(salaryLogo());
        removeDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                removeDialogWindowClosing(evt);
            }
        });

        removeDialogPanel.setBackground(new java.awt.Color(255, 255, 255));

        removeDialogLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        removeDialogLabel.setText("Are you sure you want to remove this employee?");
        removeDialogLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        removeDialogCancelBtn.setText("CANCEL");
        removeDialogCancelBtn.setFocusPainted(false);
        removeDialogCancelBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        removeDialogCancelBtn.addActionListener(this::removeDialogCancelBtnActionPerformed);

        removeDialogConfirmBtn.setText("REMOVE");
        removeDialogConfirmBtn.setFocusPainted(false);
        removeDialogConfirmBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        removeDialogConfirmBtn.addActionListener(this::removeDialogConfirmBtnActionPerformed);

        removeDialogNumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        removeDialogNumberLabel.setText("Employee No. :");
        removeDialogNumberLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        removeDialogNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        removeDialogNameLabel.setText("Employee Name :");
        removeDialogNameLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        removeDialogNumberField.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        removeDialogNumberField.setText("10100");
        removeDialogNumberField.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        removeDialogNameField.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        removeDialogNameField.setText("Fontanilla, Don Justine xyzaqe");
        removeDialogNameField.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        removeDialogLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        removeDialogLabel1.setText("This action cannot be undone.");
        removeDialogLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        javax.swing.GroupLayout removeDialogPanelLayout = new javax.swing.GroupLayout(removeDialogPanel);
        removeDialogPanel.setLayout(removeDialogPanelLayout);
        removeDialogPanelLayout.setHorizontalGroup(
            removeDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(removeDialogLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(removeDialogPanelLayout.createSequentialGroup()
                .addGroup(removeDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(removeDialogPanelLayout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(removeDialogCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(removeDialogConfirmBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(removeDialogPanelLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(removeDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(removeDialogPanelLayout.createSequentialGroup()
                                .addComponent(removeDialogNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeDialogNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(removeDialogPanelLayout.createSequentialGroup()
                                .addComponent(removeDialogNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeDialogNumberField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(removeDialogPanelLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(removeDialogLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        removeDialogPanelLayout.setVerticalGroup(
            removeDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, removeDialogPanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(removeDialogLabel)
                .addGap(12, 12, 12)
                .addGroup(removeDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeDialogNumberLabel)
                    .addComponent(removeDialogNumberField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(removeDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeDialogNameLabel)
                    .addComponent(removeDialogNameField))
                .addGap(12, 12, 12)
                .addComponent(removeDialogLabel1)
                .addGap(18, 18, 18)
                .addGroup(removeDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeDialogCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeDialogConfirmBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout removeDialogLayout = new javax.swing.GroupLayout(removeDialog.getContentPane());
        removeDialog.getContentPane().setLayout(removeDialogLayout);
        removeDialogLayout.setHorizontalGroup(
            removeDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(removeDialogPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        removeDialogLayout.setVerticalGroup(
            removeDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(removeDialogPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        successDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        successDialog.setIconImage(salaryLogo());
        successDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                successDialogWindowClosing(evt);
            }
        });

        successsPanel.setBackground(new java.awt.Color(255, 255, 255));

        successLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        successLabel.setText("You succesfully removed :");
        successLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        successBtn.setText("OK");
        successBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        successBtn.addActionListener(this::successBtnActionPerformed);

        successEmployeeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        successEmployeeLabel.setText("Fontanilla, Don Justine T.");
        successEmployeeLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        javax.swing.GroupLayout successsPanelLayout = new javax.swing.GroupLayout(successsPanel);
        successsPanel.setLayout(successsPanelLayout);
        successsPanelLayout.setHorizontalGroup(
            successsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(successsPanelLayout.createSequentialGroup()
                .addGroup(successsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(successsPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(successsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(successLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(successEmployeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(successsPanelLayout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(successBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        successsPanelLayout.setVerticalGroup(
            successsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, successsPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(successLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(successEmployeeLabel)
                .addGap(18, 18, 18)
                .addComponent(successBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout successDialogLayout = new javax.swing.GroupLayout(successDialog.getContentPane());
        successDialog.getContentPane().setLayout(successDialogLayout);
        successDialogLayout.setHorizontalGroup(
            successDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(successsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        successDialogLayout.setVerticalGroup(
            successDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(successsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addGap(0, 0, Short.MAX_VALUE)
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
        updateBtn.setFocusPainted(false);
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
        pagibigTextInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pagibigTextInputKeyTyped(evt);
            }
        });

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
        addOrUpdateBtn.setFocusPainted(false);
        addOrUpdateBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        addOrUpdateBtn.addActionListener(this::addOrUpdateBtnActionPerformed);

        cancelAddOrUpdateBtn.setText("Cancel");
        cancelAddOrUpdateBtn.setFocusPainted(false);
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
        removeBtn.setFocusPainted(false);
        removeBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        removeBtn.addActionListener(this::removeBtnActionPerformed);

        closeViewBtn.setText("Close");
        closeViewBtn.setFocusPainted(false);
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
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closeViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(cancelAddOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(addOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(viewLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
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
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(bdayPicker, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lastNameTextInput, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE))
                            .addComponent(addressTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
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
                        .addGap(25, 25, 25)
                        .addComponent(govIdLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(personalInfoLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(193, 193, 193)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(supervisorTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(positionTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(statusTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(supervisorComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(statusComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(positionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 30, Short.MAX_VALUE))
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
                            .addComponent(statusLabel)
                            .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(decorLine3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(decorLine2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(decorLine, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(viewLabel)
                    .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addGap(12, 12, 12)
                .addComponent(decorLine2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addGap(12, 12, 12)
                .addComponent(decorLine3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
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
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelAddOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(closeViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
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
            dialogOpener(noChangeDialog, "Nothing to Save");
            return;
        }
        
        //validity check
        List<String> validate = validateForm();
        if (!validate.isEmpty()) {
            //errorDialog
            validationErrorLabel.setText(formatErrorsForLabel(validate));
            dialogOpener(validationErrorDialog, "Data Validation Check");
            return;
        }
        
        dialogOpener(updateDialog, "Save Changes");
    }//GEN-LAST:event_addOrUpdateBtnActionPerformed

    private void cancelAddOrUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelAddOrUpdateBtnActionPerformed
        // TODO add your handling code here:
        if (isViewing) {
            if (!hasChanges()) {
                isEditing = !isEditing;
                updateFields();
                return;
            }
        } 
        //No changes made when adding new Employee
        else {
            if(!hasChanges()) {
                parentDialog.dispose();
                return;
            }    
        }

        dialogOpener(cancelDialog, "Cancel Editing");
    }//GEN-LAST:event_cancelAddOrUpdateBtnActionPerformed

    private void cancelDialogCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDialogCancelBtnActionPerformed
        // TODO add your handling code here:
        cancelDialog.dispose();
    }//GEN-LAST:event_cancelDialogCancelBtnActionPerformed

    private void cancelDialogConfirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDialogConfirmBtnActionPerformed
        // TODO add your handling code here:
        if (isViewing) {
            isEditing = !isEditing;
            fillEmployeeInformation(selectedEmployee);
            updateFields();
        } else {
            parentDialog.dispose();
        }
        cancelDialog.dispose();
    }//GEN-LAST:event_cancelDialogConfirmBtnActionPerformed

    private void noChangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noChangeButtonActionPerformed
        // TODO add your handling code here:
        if (isViewing) {
            isEditing = !isEditing;
            updateFields();
        }
        
        noChangeDialog.dispose();
    }//GEN-LAST:event_noChangeButtonActionPerformed

    private void noChangeDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_noChangeDialogWindowClosing
        // TODO add your handling code here:
        isEditing = !isEditing;
        updateFields();
        noChangeDialog.dispose();
    }//GEN-LAST:event_noChangeDialogWindowClosing

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
            // TODO add your handling code here:
        if (!isViewing) {
            return;
        }

        //If none is selected, show a warning dialog and stop the process.
        if (this.selectedEmployee == null) {
            //Error no selected employee
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Employee is not existing",
                    "Invalid Employee",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        removeDialogNumberField.setText(this.selectedEmployee.getEmployeeNo());
        removeDialogNameField.setText(this.selectedEmployee.getLastFirstName());
        dialogOpener(removeDialog, "Confirm Employee Removal");
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
        Employee employee = Mapper.buildEmployee(
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
            positionTextInput.getText().trim(),
            supervisorTextInput.getText().trim()
        );
        
        //Update existing employee    
        if (isViewing) {
            handleUpdateEmployee(employee);
            return;
        } 
        
        handleAddEmployee(employee);
    }//GEN-LAST:event_updateDialogSaveBtnActionPerformed

    private void updateDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_updateDialogWindowClosing
        // TODO add your handling code here:
        updateDialog.dispose();
    }//GEN-LAST:event_updateDialogWindowClosing

    private void statusComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusComboBoxActionPerformed
        // TODO add your handling code here:
        String selected = (String) statusComboBox.getSelectedItem();
        statusTextInput.setText(selected);
    }//GEN-LAST:event_statusComboBoxActionPerformed

    private void validationErrorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validationErrorBtnActionPerformed
        // TODO add your handling code here:
        validationErrorDialog.dispose();
    }//GEN-LAST:event_validationErrorBtnActionPerformed

    private void pagibigTextInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pagibigTextInputKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();

        // allow digits only
        if (!Character.isDigit(c)) {
            evt.consume();
            return;
        }

        // limit to 12 digits
        if (pagibigTextInput.getText().length() >= 12) {
            evt.consume();
        }
    }//GEN-LAST:event_pagibigTextInputKeyTyped

    private void removeDialogCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeDialogCancelBtnActionPerformed
        // TODO add your handling code here:
        removeDialog.dispose();
    }//GEN-LAST:event_removeDialogCancelBtnActionPerformed

    private void removeDialogConfirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeDialogConfirmBtnActionPerformed
        // TODO add your handling code here:
        //successfully removed dialog
        try {
            Employee removed = appContext.getEmployeeService().removeEmployee(selectedEmployee.getEmployeeNo());
            //refactor this when IT has add or delete feat
            boolean accountRemoved = removeUserAccount(selectedEmployee);
            if (!accountRemoved) {
                return;
            }

            parentDialog.dispose();
            removeDialog.dispose();
            openSuccessDialog("Successfully Removed", "You successfully removed:", removed);
            
        } catch (IllegalArgumentException e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid Employee",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            e.printStackTrace();

        } catch (IllegalStateException e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Employee Removal Error",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            e.printStackTrace();

        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to remove employee.",
                    "Delete Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }

    }//GEN-LAST:event_removeDialogConfirmBtnActionPerformed

    private void successBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_successBtnActionPerformed
        // TODO add your handling code here:
        successDialog.dispose();
    }//GEN-LAST:event_successBtnActionPerformed

    private void successDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_successDialogWindowClosing
        // TODO add your handling code here:
        successDialog.dispose();
    }//GEN-LAST:event_successDialogWindowClosing

    private void removeDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_removeDialogWindowClosing
        // TODO add your handling code here:
        removeDialog.dispose();
    }//GEN-LAST:event_removeDialogWindowClosing

    private void cancelDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_cancelDialogWindowClosing
        // TODO add your handling code here:
        cancelDialog.dispose();
    }//GEN-LAST:event_cancelDialogWindowClosing

    private void validationErrorDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_validationErrorDialogWindowClosing
        // TODO add your handling code here:
        validationErrorDialog.dispose();
    }//GEN-LAST:event_validationErrorDialogWindowClosing
    
    
    
    private boolean isViewing;
    private boolean isConfirmingUpdate;
    private boolean isConfirmingCancel;
    private boolean isEditing;
    private AccessPolicy policy;
    private final javax.swing.JDialog parentDialog;
    private Employee currentEmployee;
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
    private javax.swing.JDialog removeDialog;
    private javax.swing.JButton removeDialogCancelBtn;
    private javax.swing.JButton removeDialogConfirmBtn;
    private javax.swing.JLabel removeDialogLabel;
    private javax.swing.JLabel removeDialogLabel1;
    private javax.swing.JLabel removeDialogNameField;
    private javax.swing.JLabel removeDialogNameLabel;
    private javax.swing.JLabel removeDialogNumberField;
    private javax.swing.JLabel removeDialogNumberLabel;
    private javax.swing.JPanel removeDialogPanel;
    private javax.swing.JLabel sssLabel;
    private javax.swing.JTextField sssTextInput;
    private javax.swing.JComboBox<String> statusComboBox;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField statusTextInput;
    private javax.swing.JButton successBtn;
    private javax.swing.JDialog successDialog;
    private javax.swing.JLabel successEmployeeLabel;
    private javax.swing.JLabel successLabel;
    private javax.swing.JPanel successsPanel;
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
    private javax.swing.JButton validationErrorBtn;
    private javax.swing.JDialog validationErrorDialog;
    private javax.swing.JLabel validationErrorHeader;
    private javax.swing.JLabel validationErrorLabel;
    private javax.swing.JPanel validationErrorPanel;
    private javax.swing.JLabel viewLabel;
    // End of variables declaration//GEN-END:variables
}
