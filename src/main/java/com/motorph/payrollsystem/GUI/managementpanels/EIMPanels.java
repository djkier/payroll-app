/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.GUI.managementpanels;

import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.app.AppContext;
import com.motorph.payrollsystem.domain.employee.Employee;
import com.motorph.payrollsystem.service.EmployeeService;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.ThemeColor;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author djjus
 */
public class EIMPanels extends javax.swing.JPanel {

    /**
     * Creates new form EIMPanels
     * 
     * @param appContext use to get employee contexts
     */
    public EIMPanels(
            AppContext appContext,
            javax.swing.JDialog dialog) {
        this.appContext = appContext;
        this.dialog = dialog;
        this.employeeList = List.of();
        this.isEditing = false;
        
        initComponents();
        loadEmployees();
        hookRowDoubleClick();
    }
    
    private void loadEmployees() {
        EmployeeService employeeService = appContext.getEmployeeService();
        AccessPolicy policy = appContext.getSessionManager().getAccessPolicy();
        
        try {
            List<Employee> employeeList = employeeService.getEmployeeList(policy.canManageEmployees());
            this.employeeList = employeeList;
            
            fillTable(employeeList);
            
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error loading employee data");
        }
        
        customizeCellColumns();
    }
    
    private void customizeCellColumns() {
        empInfoTable.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 12));
    }
    
    private void fillTable(List<Employee> list) {
        DefaultTableModel model = (DefaultTableModel) clearTable(empInfoTable);
        
        for (Employee emp : list) {
            model.addRow(new Object[]{
                emp.getEmployeeNo(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getDepartmentInfo().getDepartment(),
                emp.getDepartmentInfo().getPosition(),
                emp.getDepartmentInfo().getStatus()
            });
        }
    }
    
    private DefaultTableModel clearTable(javax.swing.JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        return model;
    }
    
    private void hookRowDoubleClick() {
        empInfoTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() != 2) return;
                
                int row = empInfoTable.getSelectedRow();
                if (row < 0 || row >= employeeList.size()) return;
                
                Employee selectedEmployee = employeeList.get(row);
                showEmployeeDetails(selectedEmployee);
//                System.out.println("Employee : " + selectedEmployee.getFullName());
            }
        }) ;
    }
    
    private void showEmployeeDetails(Employee emp) {
        fillEmployeeInformation(emp);
        
        editEmployeeDialog.pack();
        editEmployeeDialog.setResizable(false);
        editEmployeeDialog.setLocationRelativeTo(null);
        editEmployeeDialog.setTitle("Employee Information : " + emp.getFullName() );
        
        updateFields();
        
        editEmployeeDialog.setVisible(true);

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
        String view = isEditing ? "Updating" : "View";
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radioBtnGroup = new javax.swing.ButtonGroup();
        editEmployeeDialog = new javax.swing.JDialog(this.dialog, true);
        editEmployeePanel = new javax.swing.JPanel();
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
        searchBarTextField = new javax.swing.JTextField();
        statsLabel = new javax.swing.JLabel();
        headerLabel = new javax.swing.JLabel();
        addNewBtn = new javax.swing.JButton();
        empInfoPane = new javax.swing.JScrollPane();
        empInfoTable = new javax.swing.JTable();
        idRadio = new javax.swing.JRadioButton();
        lastNameRadio = new javax.swing.JRadioButton();
        searchByLabel = new javax.swing.JLabel();

        editEmployeeDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        editEmployeeDialog.setAlwaysOnTop(true);

        editEmployeePanel.setBackground(new java.awt.Color(255, 255, 255));

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

        javax.swing.GroupLayout editEmployeePanelLayout = new javax.swing.GroupLayout(editEmployeePanel);
        editEmployeePanel.setLayout(editEmployeePanelLayout);
        editEmployeePanelLayout.setHorizontalGroup(
            editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(decorLine2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(decorLine3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(editEmployeePanelLayout.createSequentialGroup()
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editEmployeePanelLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(employeeNoLabel)
                            .addComponent(firstNameLabel)
                            .addComponent(phoneLabel)
                            .addComponent(addressLabel)
                            .addComponent(birthdayLabel)
                            .addComponent(lastNameLabel))
                        .addGap(6, 6, 6)
                        .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(employeeNoTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(firstNameTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastNameTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(birthdayTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phoneTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(editEmployeePanelLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sssLabel)
                            .addComponent(philHealthLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sssTextInput, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                            .addComponent(philhealthTextInput))
                        .addGap(18, 18, 18)
                        .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tinLabel)
                            .addComponent(pagibigLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pagibigTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tinTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(editEmployeePanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(govIdLabel))
                    .addGroup(editEmployeePanelLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(departmentLabel))
                    .addGroup(editEmployeePanelLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(departmentNameLabel)
                            .addComponent(positionLabel)
                            .addComponent(supervisorLabel)
                            .addComponent(statusLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(departmentTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(positionTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(supervisorTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(statusTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(editEmployeePanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(personalInfoLabel)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(editEmployeePanelLayout.createSequentialGroup()
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editEmployeePanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(viewLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editEmployeePanelLayout.createSequentialGroup()
                        .addGap(293, 293, 293)
                        .addComponent(closeViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelAddOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36))
        );
        editEmployeePanelLayout.setVerticalGroup(
            editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editEmployeePanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(viewLabel)
                    .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(personalInfoLabel)
                .addGap(6, 6, 6)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employeeNoLabel)
                    .addComponent(employeeNoTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstNameLabel)
                    .addComponent(firstNameTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastNameLabel)
                    .addComponent(lastNameTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(birthdayLabel)
                    .addComponent(birthdayTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressLabel)
                    .addComponent(addressTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneLabel)
                    .addComponent(phoneTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(decorLine2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(govIdLabel)
                .addGap(7, 7, 7)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sssLabel)
                    .addComponent(pagibigLabel)
                    .addComponent(sssTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pagibigTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(philHealthLabel)
                    .addComponent(tinLabel)
                    .addComponent(philhealthTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tinTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(decorLine3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(departmentLabel)
                .addGap(7, 7, 7)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(departmentNameLabel)
                    .addComponent(departmentTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(positionLabel)
                    .addComponent(positionTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supervisorLabel)
                    .addComponent(supervisorTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel)
                    .addComponent(statusTextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(editEmployeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeViewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelAddOrUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout editEmployeeDialogLayout = new javax.swing.GroupLayout(editEmployeeDialog.getContentPane());
        editEmployeeDialog.getContentPane().setLayout(editEmployeeDialogLayout);
        editEmployeeDialogLayout.setHorizontalGroup(
            editEmployeeDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(editEmployeePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 761, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        editEmployeeDialogLayout.setVerticalGroup(
            editEmployeeDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(editEmployeePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setBackground(new java.awt.Color(255, 255, 255));

        searchBarTextField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        searchBarTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        statsLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        statsLabel.setText("Total : 36  Regular : 12  Probationary : 24");

        headerLabel.setFont(new java.awt.Font("Poppins", 1, 20)); // NOI18N
        headerLabel.setText("EMPLOYEE INFORMATION MANAGEMENT");

        addNewBtn.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        addNewBtn.setText("Add new employee");

        empInfoTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        empInfoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Employee No.", "Last Name", "First Name", "Department", "Position", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        empInfoTable.setRowHeight(28);
        empInfoTable.getTableHeader().setResizingAllowed(false);
        empInfoTable.getTableHeader().setReorderingAllowed(false);
        empInfoPane.setViewportView(empInfoTable);
        if (empInfoTable.getColumnModel().getColumnCount() > 0) {
            empInfoTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            empInfoTable.getColumnModel().getColumn(1).setPreferredWidth(80);
            empInfoTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        }

        idRadio.setBackground(new java.awt.Color(255, 255, 255));
        radioBtnGroup.add(idRadio);
        idRadio.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        idRadio.setText("ID Number");

        lastNameRadio.setBackground(new java.awt.Color(255, 255, 255));
        radioBtnGroup.add(lastNameRadio);
        lastNameRadio.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        lastNameRadio.setText("Last Name");

        searchByLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        searchByLabel.setText("Search by :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchByLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(idRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastNameRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(statsLabel))
                    .addComponent(empInfoPane)
                    .addComponent(headerLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchBarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 577, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(addNewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(headerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addNewBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(searchBarTextField))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idRadio)
                    .addComponent(lastNameRadio)
                    .addComponent(searchByLabel)
                    .addComponent(statsLabel))
                .addGap(12, 12, 12)
                .addComponent(empInfoPane, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        idRadio.getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        // TODO add your handling code here:
        isEditing = !isEditing;
        updateFields();
    }//GEN-LAST:event_updateBtnActionPerformed

    private void closeViewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeViewBtnActionPerformed
        // TODO add your handling code here:
        this.editEmployeeDialog.dispose();
    }//GEN-LAST:event_closeViewBtnActionPerformed

    private void addOrUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addOrUpdateBtnActionPerformed
        // TODO add your handling code here:
        isEditing = !isEditing;
        updateFields();
    }//GEN-LAST:event_addOrUpdateBtnActionPerformed

    private void cancelAddOrUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelAddOrUpdateBtnActionPerformed
        // TODO add your handling code here:
        isEditing = !isEditing;
        updateFields();
    }//GEN-LAST:event_cancelAddOrUpdateBtnActionPerformed

    private String addOrUpdate;
    private boolean isEditing;
    private javax.swing.JDialog dialog;
    private List<Employee> employeeList;
    private AppContext appContext;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewBtn;
    private javax.swing.JButton addOrUpdateBtn;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JTextField addressTextInput;
    private javax.swing.JLabel birthdayLabel;
    private javax.swing.JTextField birthdayTextInput;
    private javax.swing.JButton cancelAddOrUpdateBtn;
    private javax.swing.JButton closeViewBtn;
    private javax.swing.JPanel decorLine;
    private javax.swing.JPanel decorLine1;
    private javax.swing.JPanel decorLine2;
    private javax.swing.JPanel decorLine3;
    private javax.swing.JLabel departmentLabel;
    private javax.swing.JLabel departmentNameLabel;
    private javax.swing.JTextField departmentTextInput;
    private javax.swing.JDialog editEmployeeDialog;
    private javax.swing.JPanel editEmployeePanel;
    private javax.swing.JScrollPane empInfoPane;
    private javax.swing.JTable empInfoTable;
    private javax.swing.JLabel employeeNoLabel;
    private javax.swing.JTextField employeeNoTextInput;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameTextInput;
    private javax.swing.JLabel govIdLabel;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JRadioButton idRadio;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JRadioButton lastNameRadio;
    private javax.swing.JTextField lastNameTextInput;
    private javax.swing.JLabel pagibigLabel;
    private javax.swing.JTextField pagibigTextInput;
    private javax.swing.JLabel personalInfoLabel;
    private javax.swing.JLabel philHealthLabel;
    private javax.swing.JTextField philhealthTextInput;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JTextField phoneTextInput;
    private javax.swing.JLabel positionLabel;
    private javax.swing.JTextField positionTextInput;
    private javax.swing.ButtonGroup radioBtnGroup;
    private javax.swing.JTextField searchBarTextField;
    private javax.swing.JLabel searchByLabel;
    private javax.swing.JLabel sssLabel;
    private javax.swing.JTextField sssTextInput;
    private javax.swing.JLabel statsLabel;
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
