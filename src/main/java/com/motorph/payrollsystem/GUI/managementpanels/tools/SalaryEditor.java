/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.gui.managementpanels.tools;

import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.utility.Mapper;
import com.motorph.payrollsystem.utility.Money;
import com.motorph.payrollsystem.utility.ThemeColor;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author djjus
 */
public class SalaryEditor extends javax.swing.JPanel {

    /**
     * Creates new form InfomationEditor
     */
    public SalaryEditor(
        AppContext appContext, 
        Employee selectedEmployee,
        javax.swing.JDialog dialog) {
        
        this.appContext = appContext;
        this.policy = appContext.getSessionManager().getAccessPolicy();
        this.selectedEmployee = selectedEmployee;
        this.currentEmployee = appContext.getSessionManager().getCurrentEmployee();
        this.parentDialog = dialog;
        this.isEditing = false;
        
        initComponents();
        initBasicSalaryListener();
        fillEmployeeInformation(selectedEmployee);
        updateFields();
    }
    
    //Fill fields with salary related information
    private void fillEmployeeInformation(Employee emp) {
        //personal information
        fillInfo(employeeNoTextInput, emp.getEmployeeNo());
        fillInfo(firstNameTextInput, emp.getFirstName());
        fillInfo(lastNameTextInput, emp.getLastName());
        fillInfo(addressTextInput, emp.getContactInfo().getAddress());

        //govt id
        fillInfo(sssTextInput, emp.getGovIds().getSssNumber());
        fillInfo(philhealthTextInput, emp.getGovIds().getPhilHealthNumber());
        fillInfo(pagibigTextInput, emp.getGovIds().getPagibigNumber());
        fillInfo(tinTextInput, emp.getGovIds().getTinNumber());
        
        //dept info
        fillInfo(departmentTextInput, emp.getDepartmentInfo().getDepartment());
        fillInfo(positionTextInput, emp.getDepartmentInfo().getPosition());
        fillInfo(statusTextInput, emp.getDepartmentInfo().getStatus());
        
        //salary info
        fillInfo(basicTextInput, Money.displayMoney(emp.getCompProfile().getBasicSalary()));
        fillInfo(semiTextInput, Money.displayMoney(emp.getCompProfile().getSemiMonthlyRate()));
        fillInfo(hourlyTextInput, Money.displayMoney(emp.getCompProfile().getHourlyRate()));
        fillInfo(riceTextInput, Money.displayMoney(emp.getCompProfile().getRiceSubsidy()));
        fillInfo(phoneTextInput, Money.displayMoney(emp.getCompProfile().getPhoneAllowance()));
        fillInfo(clothingTextInput, Money.displayMoney(emp.getCompProfile().getClothingAllowance())); 
    }
    
    private void fillInfo(javax.swing.JTextField textField, String fieldDetails) {
        fieldDetails = (fieldDetails == null) ? "" : fieldDetails;
        textField.setText(fieldDetails);
    }

    
    //Update visibility of the field base on the current mode
    private void updateFields() {
        setEditorTitle();
        setButtonsVisibility();

        fieldEnabler(basicTextInput);
        fieldEnabler(riceTextInput);
        fieldEnabler(phoneTextInput);
        fieldEnabler(clothingTextInput);
        
    }
    
    private void setEditorTitle() {
        String preTitle = isEditing ? "Edit" : "View";    
        String viewTitle = preTitle + " Salary Details";
        viewLabel.setText(viewTitle);
    }
    
    private void setButtonsVisibility() {
        buttonVisibility(updateBtn, !isEditing);
        buttonVisibility(closeBtn, !isEditing);
        
        buttonVisibility(cancelBtn, isEditing);
        buttonVisibility(saveBtn, isEditing);
        
        String regular = "Regular";
        boolean isRegular = regular.equalsIgnoreCase(selectedEmployee.getDepartmentInfo().getStatus());
        boolean currentUser = selectedEmployee.getEmployeeNo().equals(currentEmployee.getEmployeeNo());
        updateBtn.setEnabled(!currentUser && isRegular);
        probationaryNoteLabel.setVisible(!isRegular);
           
    }
    
    private void buttonVisibility(javax.swing.JButton btn, boolean isVisible) {
        btn.setEnabled(isVisible);
        btn.setVisible(isVisible);
    }
    
    private void fieldEnabler(javax.swing.JTextField textfield) {
        String currentValue = textfield.getText();
        if (isEditing) {
            textfield.setText(Money.parseStringSalary(currentValue));
        } else {
            textfield.setText(Money.displayMoney(currentValue));
        }
        
        fieldEnabler(textfield, isEditing);
    }
    
    private void fieldEnabler(javax.swing.JTextField textField, boolean isEnable) {
        textField.setEnabled(isEnable);
        textField.setDisabledTextColor(ThemeColor.textDisabled());
    }
       
    //Check if theres changes
    private boolean hasChanges() {
        
        return isChanged(basicTextInput, selectedEmployee.getCompProfile().getBasicSalary()) 
                || isChanged(riceTextInput, selectedEmployee.getCompProfile().getRiceSubsidy()) 
                || isChanged(phoneTextInput, selectedEmployee.getCompProfile().getPhoneAllowance()) 
                || isChanged(clothingTextInput, selectedEmployee.getCompProfile().getClothingAllowance());
    }
    
    private boolean isChanged (javax.swing.JTextField input, double modelValue) {
        try {
            double inputValue = Money.parseSalary(input.getText());
            return Math.round(inputValue * 100) != Math.round(modelValue * 100);
        } catch (NumberFormatException e) {
            return true;
        }
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
        
        validateMoney(basicTextInput, errors);
        validateMoney(riceTextInput, errors);
        validateMoney(phoneTextInput, errors);
        validateMoney(clothingTextInput, errors);

        return errors;
    }
    
    private void validateMoney(javax.swing.JTextField textField, List<String> errors) {
        String label = textField.getName();

        if (label == null || label.trim().isEmpty()) {
            label = "Amount";
        }

        String value = textField.getText();

        if (value == null || value.trim().isEmpty()) {
            errors.add(label + " is required.");
            return;
        }

        try {
            double amount = Money.parseSalary(value);

            if (amount < 0) {
                errors.add(label + " cannot be negative");
                return;
            }

            if ("Basic Salary".equalsIgnoreCase(label) && amount <= 0) {
                errors.add(label + " must be greater than 0.");
            }

        } catch (NumberFormatException e) {
            errors.add(label + " must be a valid amount.");
        }
    }
    


    private String formatErrorsForLabel(List<String> errors) {
        List<String> formatted = new ArrayList<>();
        for (String e : errors) {
            formatted.add("- " + e);
        }
        return "<html>" + String.join("<br>", formatted) + "</html>";
    }
    
    private void handleUpdateEmployeeSalary(Employee employee) {
        try {
            this.selectedEmployee = appContext.getEmployeeService().updateEmployee(employee);

            isEditing = false;
            fillEmployeeInformation(selectedEmployee);
            updateFields();
            updateDialog.dispose();
            openSuccessDialog("Successfully Updated", "Successfully updated salary of:", selectedEmployee);
        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to update employee salary.",
                    "Update Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
    
    private void handleMoneyInputKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        javax.swing.JTextField textField = (javax.swing.JTextField) evt.getSource();

        // allow backspace and delete
        if (c == java.awt.event.KeyEvent.VK_BACK_SPACE || c == java.awt.event.KeyEvent.VK_DELETE) {
            return;
        }

        // only digits and decimal point
        if (!Character.isDigit(c) && c != '.') {
            evt.consume();
            return;
        }

        String currentText = textField.getText();
        String nextText = currentText.substring(0, textField.getSelectionStart())
                + c
                + currentText.substring(textField.getSelectionEnd());

        // max 12 whole digits, optional decimal, max 2 decimal digits
        if (!nextText.matches("^\\d{0,12}(\\.\\d{0,2})?$")) {
            evt.consume();
        }
    }
    
    private void handleDependentField() {
         String basicInput = basicTextInput.getText();
        
        try {
            double basicSalary = Money.parseSalary(basicInput);
            
            double semiMonthly = basicSalary / 2.0;
            double hourlyRate = basicSalary / 21.0 / 8.0;
            
            semiTextInput.setText(Money.displayMoney(semiMonthly));
            hourlyTextInput.setText(Money.displayMoney(hourlyRate));
        } catch (NumberFormatException e) {
            semiTextInput.setText("");
            hourlyTextInput.setText("");
        }
    
    }
    
    private void initBasicSalaryListener() {
        basicTextInput.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                handleDependentField();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                handleDependentField();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                handleDependentField();
            }
        });
    }
    
    private Image updateSalaryLogo() {
        ImageIcon newIcon = new ImageIcon(getClass().getResource("/images/tab-icon/module-icon/update-salary.png"));
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
        successDialog = new javax.swing.JDialog(this.parentDialog, true);
        successsPanel = new javax.swing.JPanel();
        successLabel = new javax.swing.JLabel();
        successBtn = new javax.swing.JButton();
        successEmployeeLabel = new javax.swing.JLabel();
        personalInfoLabel = new javax.swing.JLabel();
        lastNameLabel = new javax.swing.JLabel();
        employeeNoLabel = new javax.swing.JLabel();
        firstNameLabel = new javax.swing.JLabel();
        viewLabel = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
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
        updateBtn = new javax.swing.JButton();
        employeeNoTextInput = new javax.swing.JTextField();
        firstNameTextInput = new javax.swing.JTextField();
        lastNameTextInput = new javax.swing.JTextField();
        addressTextInput = new javax.swing.JTextField();
        sssTextInput = new javax.swing.JTextField();
        philhealthTextInput = new javax.swing.JTextField();
        pagibigTextInput = new javax.swing.JTextField();
        tinTextInput = new javax.swing.JTextField();
        positionTextInput = new javax.swing.JTextField();
        statusTextInput = new javax.swing.JTextField();
        saveBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        closeBtn = new javax.swing.JButton();
        decorLine4 = new javax.swing.JPanel();
        salaryLabel = new javax.swing.JLabel();
        basicLabel = new javax.swing.JLabel();
        departmentTextInput = new javax.swing.JTextField();
        semiMonthlyLabel = new javax.swing.JLabel();
        hourlyLabel = new javax.swing.JLabel();
        basicTextInput = new javax.swing.JTextField();
        semiTextInput = new javax.swing.JTextField();
        hourlyTextInput = new javax.swing.JTextField();
        riceLabel = new javax.swing.JLabel();
        riceTextInput = new javax.swing.JTextField();
        phoneLabel = new javax.swing.JLabel();
        phoneTextInput = new javax.swing.JTextField();
        clotihingLabel = new javax.swing.JLabel();
        clothingTextInput = new javax.swing.JTextField();
        probationaryNoteLabel = new javax.swing.JLabel();

        cancelDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        cancelDialog.setIconImage(updateSalaryLogo());
        cancelDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                cancelDialogWindowClosing(evt);
            }
        });

        cancelConfirmPanel.setBackground(new java.awt.Color(255, 255, 255));

        cancelDialogLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        cancelDialogLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cancelDialogLabel.setText("You have unsaved changes. Discard them?");

        cancelDialogCancelBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        cancelDialogCancelBtn.setText("CANCEL");
        cancelDialogCancelBtn.setFocusPainted(false);
        cancelDialogCancelBtn.addActionListener(this::cancelDialogCancelBtnActionPerformed);

        cancelDialogConfirmBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        cancelDialogConfirmBtn.setText("CONFIRM");
        cancelDialogConfirmBtn.setFocusPainted(false);
        cancelDialogConfirmBtn.addActionListener(this::cancelDialogConfirmBtnActionPerformed);

        javax.swing.GroupLayout cancelConfirmPanelLayout = new javax.swing.GroupLayout(cancelConfirmPanel);
        cancelConfirmPanel.setLayout(cancelConfirmPanelLayout);
        cancelConfirmPanelLayout.setHorizontalGroup(
            cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cancelConfirmPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(cancelDialogCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
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
        noChangeDialog.setIconImage(updateSalaryLogo());
        noChangeDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                noChangeDialogWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                noChangeDialogWindowClosing(evt);
            }
        });

        noChangePanel.setBackground(new java.awt.Color(255, 255, 255));

        noChangeLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        noChangeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        noChangeLabel.setText("No changes made.");

        noChangeButton.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        noChangeButton.setText("OK");
        noChangeButton.setFocusPainted(false);
        noChangeButton.addActionListener(this::noChangeButtonActionPerformed);

        javax.swing.GroupLayout noChangePanelLayout = new javax.swing.GroupLayout(noChangePanel);
        noChangePanel.setLayout(noChangePanelLayout);
        noChangePanelLayout.setHorizontalGroup(
            noChangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(noChangePanelLayout.createSequentialGroup()
                .addComponent(noChangeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, noChangePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(noChangeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
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

        updateDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        updateDialog.setIconImage(updateSalaryLogo());

        updateDialogPanel.setBackground(new java.awt.Color(255, 255, 255));

        updateDialogLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        updateDialogLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        updateDialogLabel.setText("Do you want to save your changes?");

        updateDialogCancelBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        updateDialogCancelBtn.setText("CANCEL");
        updateDialogCancelBtn.setFocusPainted(false);
        updateDialogCancelBtn.addActionListener(this::updateDialogCancelBtnActionPerformed);

        updateDialogSaveBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        updateDialogSaveBtn.setText("SAVE");
        updateDialogSaveBtn.setFocusPainted(false);
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
            .addComponent(updateDialogPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        validationErrorDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        validationErrorDialog.setIconImage(updateSalaryLogo());
        validationErrorDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                validationErrorDialogWindowClosing(evt);
            }
        });

        validationErrorPanel.setBackground(new java.awt.Color(255, 255, 255));

        validationErrorBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        validationErrorBtn.setText("OK");
        validationErrorBtn.setFocusPainted(false);
        validationErrorBtn.addActionListener(this::validationErrorBtnActionPerformed);

        validationErrorHeader.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        validationErrorHeader.setText("The following field/s contain errors :");

        validationErrorLabel.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        validationErrorLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        validationErrorLabel.setText("- Last name contains invalid characters.");
        validationErrorLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

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
                        .addGap(134, 134, 134)
                        .addComponent(validationErrorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        validationErrorPanelLayout.setVerticalGroup(
            validationErrorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, validationErrorPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(validationErrorHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(validationErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
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

        successDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        successDialog.setIconImage(updateSalaryLogo());
        successDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                successDialogWindowClosing(evt);
            }
        });

        successsPanel.setBackground(new java.awt.Color(255, 255, 255));

        successLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        successLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        successLabel.setText("You succesfully removed :");

        successBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        successBtn.setText("OK");
        successBtn.setFocusPainted(false);
        successBtn.addActionListener(this::successBtnActionPerformed);

        successEmployeeLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        successEmployeeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        successEmployeeLabel.setText("Fontanilla, Don Justine T.");

        javax.swing.GroupLayout successsPanelLayout = new javax.swing.GroupLayout(successsPanel);
        successsPanel.setLayout(successsPanelLayout);
        successsPanelLayout.setHorizontalGroup(
            successsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(successsPanelLayout.createSequentialGroup()
                .addGroup(successsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(successsPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(successsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(successLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                            .addComponent(successEmployeeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(successsPanelLayout.createSequentialGroup()
                        .addGap(110, 110, 110)
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

        personalInfoLabel.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        personalInfoLabel.setText("Personal Information");

        lastNameLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lastNameLabel.setText("Last Name :");

        employeeNoLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        employeeNoLabel.setText("Employee No :");

        firstNameLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        firstNameLabel.setText("First Name :");

        viewLabel.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        viewLabel.setText("View Salary Details");

        addressLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        addressLabel.setText("Address :");

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
            .addGap(0, 0, Short.MAX_VALUE)
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

        updateBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        updateBtn.setText("UPDATE SALARY");
        updateBtn.setFocusPainted(false);
        updateBtn.addActionListener(this::updateBtnActionPerformed);

        employeeNoTextInput.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        employeeNoTextInput.setText("00000");
        employeeNoTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        employeeNoTextInput.setDoubleBuffered(true);
        employeeNoTextInput.setEnabled(false);

        firstNameTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        firstNameTextInput.setText("Don Justine");
        firstNameTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        firstNameTextInput.setEnabled(false);

        lastNameTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        lastNameTextInput.setText("Fontanilla");
        lastNameTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        lastNameTextInput.setEnabled(false);

        addressTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        addressTextInput.setText("170 Fairview, Dasmarinas, Commonwealth, San Agustin, Quezon City");
        addressTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        addressTextInput.setEnabled(false);

        sssTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        sssTextInput.setText("+639569978123");
        sssTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        sssTextInput.setEnabled(false);

        philhealthTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        philhealthTextInput.setText("+639569978123");
        philhealthTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        philhealthTextInput.setEnabled(false);

        pagibigTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        pagibigTextInput.setText("+639569978123");
        pagibigTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        pagibigTextInput.setEnabled(false);

        tinTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tinTextInput.setText("+639569978123");
        tinTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        tinTextInput.setEnabled(false);

        positionTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        positionTextInput.setText("+639569978123");
        positionTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        positionTextInput.setEnabled(false);

        statusTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        statusTextInput.setText("+639569978123");
        statusTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        statusTextInput.setEnabled(false);

        saveBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        saveBtn.setText("SAVE");
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(this::saveBtnActionPerformed);

        cancelBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        cancelBtn.setText("CANCEL");
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);

        closeBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        closeBtn.setText("CLOSE");
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(this::closeBtnActionPerformed);

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
            .addGap(0, 2, Short.MAX_VALUE)
        );

        salaryLabel.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        salaryLabel.setText("Salary Details");

        basicLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        basicLabel.setText("Basic Salary :");

        departmentTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        departmentTextInput.setText("+639569978123");
        departmentTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        departmentTextInput.setEnabled(false);

        semiMonthlyLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        semiMonthlyLabel.setText("Semi-Monthly :");

        hourlyLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        hourlyLabel.setText("Hourly Rate :");

        basicTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        basicTextInput.setText("+639569978123");
        basicTextInput.setName("Basic Salary"); // NOI18N
        basicTextInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                basicTextInputKeyTyped(evt);
            }
        });

        semiTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        semiTextInput.setText("+639569978123");
        semiTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        semiTextInput.setEnabled(false);

        hourlyTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        hourlyTextInput.setText("+639569978123");
        hourlyTextInput.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        hourlyTextInput.setEnabled(false);

        riceLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        riceLabel.setText("Rice Subsidy :");

        riceTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        riceTextInput.setText("+639569978123");
        riceTextInput.setName("Rice Subsidy"); // NOI18N
        riceTextInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                riceTextInputKeyTyped(evt);
            }
        });

        phoneLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        phoneLabel.setText("Phone Allowance :");

        phoneTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        phoneTextInput.setText("+639569978123");
        phoneTextInput.setName("Phone Allowance"); // NOI18N
        phoneTextInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                phoneTextInputKeyTyped(evt);
            }
        });

        clotihingLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        clotihingLabel.setText("Clothing Allowance :");

        clothingTextInput.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        clothingTextInput.setText("+639569978123");
        clothingTextInput.setName("Clothing Allowance"); // NOI18N
        clothingTextInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                clothingTextInputKeyTyped(evt);
            }
        });

        probationaryNoteLabel.setBackground(new java.awt.Color(255, 255, 255));
        probationaryNoteLabel.setFont(new java.awt.Font("Poppins", 2, 12)); // NOI18N
        probationaryNoteLabel.setForeground(new java.awt.Color(0, 0, 0));
        probationaryNoteLabel.setText("(Only Regular employees can receive salary updates)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(employeeNoLabel)
                            .addComponent(firstNameLabel)
                            .addComponent(addressLabel)
                            .addComponent(lastNameLabel))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(employeeNoTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(firstNameTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastNameTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(departmentNameLabel)
                            .addComponent(positionLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(departmentTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(statusLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(statusTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(positionTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(decorLine3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(decorLine4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(decorLine, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(personalInfoLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(probationaryNoteLabel))
                    .addComponent(decorLine2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(viewLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(semiMonthlyLabel)
                                                .addComponent(basicLabel)
                                                .addComponent(hourlyLabel))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(hourlyTextInput)
                                                .addComponent(basicTextInput, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                                .addComponent(semiTextInput))
                                            .addGap(64, 64, 64)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(phoneLabel)
                                                .addComponent(riceLabel)
                                                .addComponent(clotihingLabel))
                                            .addGap(10, 10, 10)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(clothingTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(phoneTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(riceTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(salaryLabel)
                                        .addGap(594, 594, 594))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(departmentLabel)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewLabel)
                    .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(personalInfoLabel)
                    .addComponent(probationaryNoteLabel))
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
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressLabel)
                    .addComponent(addressTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(departmentLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(departmentNameLabel)
                    .addComponent(statusLabel)
                    .addComponent(statusTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(departmentTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(positionLabel)
                    .addComponent(positionTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(decorLine4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(salaryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(basicLabel)
                    .addComponent(basicTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(riceLabel)
                    .addComponent(riceTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(semiMonthlyLabel)
                    .addComponent(semiTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneLabel)
                    .addComponent(phoneTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hourlyLabel)
                    .addComponent(hourlyTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clotihingLabel)
                    .addComponent(clothingTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        // TODO add your handling code here:
        isEditing = true;
        updateFields();
    }//GEN-LAST:event_updateBtnActionPerformed

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        // TODO add your handling code here:
        parentDialog.dispose();
    }//GEN-LAST:event_closeBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        // TODO add your handling code here:
        //validity check
        List<String> validate = validateForm();
        if (!validate.isEmpty()) {
            //errorDialog
            validationErrorLabel.setText(formatErrorsForLabel(validate));
            dialogOpener(validationErrorDialog, "Data Validation Check");
            return;
        }
        
        //check for changes
        if (!hasChanges()) {
            dialogOpener(noChangeDialog, "Nothing to Save");
            return;
        }

        dialogOpener(updateDialog, "Save Changes");
    }//GEN-LAST:event_saveBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        // TODO add your handling code here:
        //no changes return
        if (!hasChanges()) {
            isEditing = false;
            updateFields();
            return;
        }

        dialogOpener(cancelDialog, "Cancel Editing");
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void cancelDialogCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDialogCancelBtnActionPerformed
        // TODO add your handling code here:
        cancelDialog.dispose();
    }//GEN-LAST:event_cancelDialogCancelBtnActionPerformed

    private void cancelDialogConfirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDialogConfirmBtnActionPerformed
        // TODO add your handling code here:
        isEditing = false;
        fillEmployeeInformation(selectedEmployee);
        updateFields();

        cancelDialog.dispose();
    }//GEN-LAST:event_cancelDialogConfirmBtnActionPerformed

    private void noChangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noChangeButtonActionPerformed
        // TODO add your handling code here:
        noChangeDialog.dispose();
    }//GEN-LAST:event_noChangeButtonActionPerformed

    private void noChangeDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_noChangeDialogWindowClosing
        // TODO add your handling code here:
        
        noChangeDialog.dispose();
    }//GEN-LAST:event_noChangeDialogWindowClosing

    private void updateDialogCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateDialogCancelBtnActionPerformed
        // TODO add your handling code here:
        updateDialog.dispose();
    }//GEN-LAST:event_updateDialogCancelBtnActionPerformed

    private void updateDialogSaveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateDialogSaveBtnActionPerformed
        // TODO add your handling code here:
        Employee employee = Mapper.buildEmployee(
            selectedEmployee,
            basicTextInput.getText().trim(),
            riceTextInput.getText().trim(),
            phoneTextInput.getText().trim(),
            clothingTextInput.getText().trim()
        );
        
        //Update existing employee    
        handleUpdateEmployeeSalary(employee);
    }//GEN-LAST:event_updateDialogSaveBtnActionPerformed

    private void validationErrorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validationErrorBtnActionPerformed
        // TODO add your handling code here:
        validationErrorDialog.dispose();
    }//GEN-LAST:event_validationErrorBtnActionPerformed

    private void successBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_successBtnActionPerformed
        // TODO add your handling code here:
        successDialog.dispose();
    }//GEN-LAST:event_successBtnActionPerformed

    private void successDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_successDialogWindowClosing
        // TODO add your handling code here:
        successDialog.dispose();
    }//GEN-LAST:event_successDialogWindowClosing

    private void cancelDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_cancelDialogWindowClosing
        // TODO add your handling code here:
        cancelDialog.dispose();
    }//GEN-LAST:event_cancelDialogWindowClosing

    private void validationErrorDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_validationErrorDialogWindowClosing
        // TODO add your handling code here:
        validationErrorDialog.dispose();
    }//GEN-LAST:event_validationErrorDialogWindowClosing

    private void noChangeDialogWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_noChangeDialogWindowClosed
        // TODO add your handling code here:
        isEditing = false;
        updateFields();
    }//GEN-LAST:event_noChangeDialogWindowClosed

    private void basicTextInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_basicTextInputKeyTyped
        // TODO add your handling code here:
        handleMoneyInputKeyTyped(evt);
    }//GEN-LAST:event_basicTextInputKeyTyped

    private void riceTextInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_riceTextInputKeyTyped
        // TODO add your handling code here:
        handleMoneyInputKeyTyped(evt);
    }//GEN-LAST:event_riceTextInputKeyTyped

    private void phoneTextInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phoneTextInputKeyTyped
        // TODO add your handling code here:
        handleMoneyInputKeyTyped(evt);
    }//GEN-LAST:event_phoneTextInputKeyTyped

    private void clothingTextInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clothingTextInputKeyTyped
        // TODO add your handling code here:
        handleMoneyInputKeyTyped(evt);
    }//GEN-LAST:event_clothingTextInputKeyTyped
    
    
    
    private boolean isEditing;
    private AccessPolicy policy;
    private final javax.swing.JDialog parentDialog;
    private Employee currentEmployee;
    private Employee selectedEmployee;
    private AppContext appContext;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addressLabel;
    private javax.swing.JTextField addressTextInput;
    private javax.swing.JLabel basicLabel;
    private javax.swing.JTextField basicTextInput;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JPanel cancelConfirmPanel;
    private javax.swing.JDialog cancelDialog;
    private javax.swing.JButton cancelDialogCancelBtn;
    private javax.swing.JButton cancelDialogConfirmBtn;
    private javax.swing.JLabel cancelDialogLabel;
    private javax.swing.JButton closeBtn;
    private javax.swing.JTextField clothingTextInput;
    private javax.swing.JLabel clotihingLabel;
    private javax.swing.JPanel decorLine;
    private javax.swing.JPanel decorLine2;
    private javax.swing.JPanel decorLine3;
    private javax.swing.JPanel decorLine4;
    private javax.swing.JLabel departmentLabel;
    private javax.swing.JLabel departmentNameLabel;
    private javax.swing.JTextField departmentTextInput;
    private javax.swing.JLabel employeeNoLabel;
    private javax.swing.JTextField employeeNoTextInput;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameTextInput;
    private javax.swing.JLabel govIdLabel;
    private javax.swing.JLabel hourlyLabel;
    private javax.swing.JTextField hourlyTextInput;
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
    private javax.swing.JLabel probationaryNoteLabel;
    private javax.swing.JLabel riceLabel;
    private javax.swing.JTextField riceTextInput;
    private javax.swing.JLabel salaryLabel;
    private javax.swing.JButton saveBtn;
    private javax.swing.JLabel semiMonthlyLabel;
    private javax.swing.JTextField semiTextInput;
    private javax.swing.JLabel sssLabel;
    private javax.swing.JTextField sssTextInput;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField statusTextInput;
    private javax.swing.JButton successBtn;
    private javax.swing.JDialog successDialog;
    private javax.swing.JLabel successEmployeeLabel;
    private javax.swing.JLabel successLabel;
    private javax.swing.JPanel successsPanel;
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
