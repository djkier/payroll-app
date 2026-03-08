/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.gui.managementpanels;

import com.motorph.payrollsystem.gui.managementpanels.tools.InformationEditor;
import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.service.EmployeeService;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.ThemeColor;
import java.io.IOException;
import java.util.ArrayList;
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
        this.employeeList = new ArrayList<>();
        this.displayedEmployees = new ArrayList<>();
        this.isIDSelected = true;
        
        initComponents();
        
        loadEmployees();
        hookRowDoubleClick();
        hookSearch();
    }
    
    private void loadEmployees() {
        EmployeeService employeeService = appContext.getEmployeeService();
        AccessPolicy policy = appContext.getSessionManager().getAccessPolicy();
        
        try {
            this.employeeList = employeeService.getEmployeeList(policy);
            displayedEmployees = new ArrayList<>(employeeList);
            
            //should use tableEmployeeList
            fillTable(displayedEmployees);
            
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
                
                int viewRow = empInfoTable.getSelectedRow();
                if (viewRow < 0) return;
                
                int modelRow = empInfoTable.convertRowIndexToModel(viewRow);
                if (modelRow < 0 || modelRow >= displayedEmployees.size()) return;

                showEmployeeInfo(displayedEmployees.get(modelRow));
            }
        });
    }
    
    private void hookSearch() {
        searchBarTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applySearch();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applySearch();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applySearch();
            }
        });
    }
    
    private void applySearch() {
        String keyword = searchBarTextField.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            displayedEmployees = new ArrayList<>(employeeList);
            fillTable(displayedEmployees);
            return;
        }

        List<Employee> filtered = new ArrayList<>();

        for (Employee emp : employeeList) {
            if (idRadio.isSelected()) {
                String empNo = String.valueOf(emp.getEmployeeNo()).toLowerCase();
                if (empNo.contains(keyword)) {
                    filtered.add(emp);
                }
            } else if (lastNameRadio.isSelected()) {
                String lastName = emp.getLastName() == null ? "" : emp.getLastName().toLowerCase();
                if (lastName.contains(keyword)) {
                    filtered.add(emp);
                }
            }
        }

        displayedEmployees = filtered;
        fillTable(displayedEmployees);
    }
    
    private void resetSearchAndTable() {
        searchBarTextField.setText("");
        searchBarTextField.requestFocus();
        applySearch();
    }

    private void showEmployeeInfo(Employee selectedEmployee) {
        String title = "Employee Information : " + selectedEmployee.getFullName();
        showEmployeeEditor(selectedEmployee, title, true);
    }
    
    private void showEmployeeEditor(Employee employee, String title, boolean isViewing) {
        InformationEditor infoEditor = new InformationEditor(appContext, employee, isViewing, editEmployeeDialog);
        openInfoDialog(title, infoEditor);
    }
    
    private void openInfoDialog(String title, InformationEditor infoEditor) {
        editEmployeeDialog.setTitle(title);
        editEmployeeDialog.setContentPane(infoEditor);
        editEmployeeDialog.pack();
        editEmployeeDialog.setResizable(false);
        editEmployeeDialog.setLocationRelativeTo(null);
        
        editEmployeeDialog.setVisible(true);
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
        exitEditorDialog = new javax.swing.JDialog(editEmployeeDialog, true);
        cancelConfirmPanel = new javax.swing.JPanel();
        cancelConfrimLabel = new javax.swing.JLabel();
        cancelBtnConfirm = new javax.swing.JButton();
        confirmBtnConfirm = new javax.swing.JButton();
        searchBarTextField = new javax.swing.JTextField();
        statsLabel = new javax.swing.JLabel();
        headerLabel = new javax.swing.JLabel();
        addNewBtn = new javax.swing.JButton();
        empInfoPane = new javax.swing.JScrollPane();
        empInfoTable = new javax.swing.JTable();
        idRadio = new javax.swing.JRadioButton();
        lastNameRadio = new javax.swing.JRadioButton();
        searchByLabel = new javax.swing.JLabel();

        editEmployeeDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        editEmployeeDialog.setAlwaysOnTop(true);
        editEmployeeDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                editEmployeeDialogWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                editEmployeeDialogWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout editEmployeeDialogLayout = new javax.swing.GroupLayout(editEmployeeDialog.getContentPane());
        editEmployeeDialog.getContentPane().setLayout(editEmployeeDialogLayout);
        editEmployeeDialogLayout.setHorizontalGroup(
            editEmployeeDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 893, Short.MAX_VALUE)
        );
        editEmployeeDialogLayout.setVerticalGroup(
            editEmployeeDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 653, Short.MAX_VALUE)
        );

        cancelConfirmPanel.setBackground(new java.awt.Color(255, 255, 255));

        cancelConfrimLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        cancelConfrimLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cancelConfrimLabel.setText("Are you sure you want to exit?");

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
            .addComponent(cancelConfrimLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cancelConfirmPanelLayout.setVerticalGroup(
            cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cancelConfirmPanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(cancelConfrimLabel)
                .addGap(18, 18, 18)
                .addGroup(cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmBtnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout exitEditorDialogLayout = new javax.swing.GroupLayout(exitEditorDialog.getContentPane());
        exitEditorDialog.getContentPane().setLayout(exitEditorDialogLayout);
        exitEditorDialogLayout.setHorizontalGroup(
            exitEditorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cancelConfirmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        exitEditorDialogLayout.setVerticalGroup(
            exitEditorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cancelConfirmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setBackground(new java.awt.Color(255, 255, 255));

        searchBarTextField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        searchBarTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        statsLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        statsLabel.setText("Total : 36  Regular : 12  Probationary : 24");

        headerLabel.setFont(new java.awt.Font("Poppins", 1, 20)); // NOI18N
        headerLabel.setText("EMPLOYEE INFORMATION MANAGEMENT");

        addNewBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        addNewBtn.setText("Add new employee");
        addNewBtn.addActionListener(this::addNewBtnActionPerformed);

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
        idRadio.setSelected(true);
        idRadio.setText("ID Number");
        idRadio.addActionListener(this::idRadioActionPerformed);

        lastNameRadio.setBackground(new java.awt.Color(255, 255, 255));
        radioBtnGroup.add(lastNameRadio);
        lastNameRadio.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        lastNameRadio.setText("Last Name");
        lastNameRadio.addActionListener(this::lastNameRadioActionPerformed);

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

    private void cancelBtnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnConfirmActionPerformed
        // TODO add your handling code here:
        exitEditorDialog.dispose();
    }//GEN-LAST:event_cancelBtnConfirmActionPerformed

    private void confirmBtnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmBtnConfirmActionPerformed
        // TODO add your handling code here:
        exitEditorDialog.dispose();
        editEmployeeDialog.dispose();
    }//GEN-LAST:event_confirmBtnConfirmActionPerformed

    private void editEmployeeDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_editEmployeeDialogWindowClosing
        // TODO add your handling code here:
        exitEditorDialog.pack();
        exitEditorDialog.setResizable(false);
        exitEditorDialog.setLocationRelativeTo(null);
        exitEditorDialog.setTitle("Exiting editor");

        exitEditorDialog.setVisible(true);  
    }//GEN-LAST:event_editEmployeeDialogWindowClosing

    private void editEmployeeDialogWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_editEmployeeDialogWindowClosed
        // TODO add your handling code here:
        resetSearchAndTable();
        isIDSelected = true;
        idRadio.setSelected(true);
        
        loadEmployees();
    }//GEN-LAST:event_editEmployeeDialogWindowClosed

    private void addNewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewBtnActionPerformed
        try {
            Employee employee = new Employee();
            employee.setEmployeeNo(appContext.getEmployeeService().getNextEmployeeNo());

            String title = "Adding Employee";
            showEmployeeEditor(employee, title, false);

        } catch (IOException e) {
            // Replace this with your custom dialog opener
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to generate employee number.",
                    "Add Employee Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }//GEN-LAST:event_addNewBtnActionPerformed

    private void idRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idRadioActionPerformed
        // TODO add your handling code here:
        if (isIDSelected) return;
        
        isIDSelected = true;
        resetSearchAndTable();
    }//GEN-LAST:event_idRadioActionPerformed

    private void lastNameRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastNameRadioActionPerformed
        // TODO add your handling code here:
        if (!isIDSelected) return;
        
        isIDSelected = false;
        resetSearchAndTable();
    }//GEN-LAST:event_lastNameRadioActionPerformed

    private javax.swing.JDialog dialog;
    private boolean isIDSelected;
    private List<Employee> employeeList;
    private List<Employee> displayedEmployees;
    private AppContext appContext;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewBtn;
    private javax.swing.JButton cancelBtnConfirm;
    private javax.swing.JPanel cancelConfirmPanel;
    private javax.swing.JLabel cancelConfrimLabel;
    private javax.swing.JButton confirmBtnConfirm;
    private javax.swing.JDialog editEmployeeDialog;
    private javax.swing.JScrollPane empInfoPane;
    private javax.swing.JTable empInfoTable;
    private javax.swing.JDialog exitEditorDialog;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JRadioButton idRadio;
    private javax.swing.JRadioButton lastNameRadio;
    private javax.swing.ButtonGroup radioBtnGroup;
    private javax.swing.JTextField searchBarTextField;
    private javax.swing.JLabel searchByLabel;
    private javax.swing.JLabel statsLabel;
    // End of variables declaration//GEN-END:variables
}
