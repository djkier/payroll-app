/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.gui.managementpanels;

import com.motorph.payrollsystem.gui.managementpanels.tools.InformationEditor;
import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.gui.managementpanels.tools.SalaryEditor;
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
public class PayrollViewerPanel extends javax.swing.JPanel {

    /**
     * Creates new form EIMPanels
     * 
     * @param appContext use to get employee contexts
     */
    public PayrollViewerPanel(
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
            
            fillStats(policy, employeeService);
            //use displayedEmployees to have a parallel ui and logic list
            fillTable(displayedEmployees);
            
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error loading employee data");
        }
        
        customizeCellColumns();
    }
    
    private void fillStats(AccessPolicy policy, EmployeeService employeeService) {
        String stats;
        
        try {
            String all = String.valueOf(employeeService.getStatusTotalStats(policy));
            String probStats = String.valueOf(employeeService.getStatusStats("Probationary", policy));
            String regStats = String.valueOf(employeeService.getStatusStats("Regular", policy));
            
            stats = "All: " + all +
                    " Regular: " + regStats +
                    " Probationary: " + probStats;
            
        } catch (Exception ex) {
            stats = "Failed to load employee stats";
            ex.printStackTrace();
        }
        
        statsLabel.setText(stats);
    }
    
    private void customizeCellColumns() {
        empSalaryInfo.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 12));
    }
    
    private void fillTable(List<Employee> list) {
        DefaultTableModel model = (DefaultTableModel) clearTable(empSalaryInfo);
        
        for (Employee emp : list) {
            model.addRow(new Object[]{
                emp.getEmployeeNo(),
                emp.getLastNameInitial(),
                emp.getGovIds().getSssNumber(),
                emp.getGovIds().getPhilHealthNumber(),
                emp.getGovIds().getPagibigNumber(),
                emp.getGovIds().getTinNumber()
            });
        }
    }
    
    private DefaultTableModel clearTable(javax.swing.JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        return model;
    }
    
    private void hookRowDoubleClick() {
        empSalaryInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() != 2) return;
                
                int viewRow = empSalaryInfo.getSelectedRow();
                if (viewRow < 0) return;
                
                int modelRow = empSalaryInfo.convertRowIndexToModel(viewRow);
                if (modelRow < 0 || modelRow >= displayedEmployees.size()) return;

                showSalaryInfo(displayedEmployees.get(modelRow));
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

    private void showSalaryInfo(Employee selectedEmployee) {
        String title = "Employee Salary Information : " + selectedEmployee.getFullName();
        SalaryEditor salaryEditor = new SalaryEditor(appContext, selectedEmployee, editSalaryDialog);
        openInfoDialog(title, salaryEditor);
    }
    
    private void openInfoDialog(String title, SalaryEditor salaryEditor) {
        editSalaryDialog.setTitle(title);
        editSalaryDialog.setContentPane(salaryEditor);
        editSalaryDialog.pack();
        editSalaryDialog.setResizable(false);
        editSalaryDialog.setLocationRelativeTo(null);
        
        editSalaryDialog.setVisible(true);
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
        editSalaryDialog = new javax.swing.JDialog(this.dialog, true);
        exitEditorDialog = new javax.swing.JDialog(editSalaryDialog, true);
        cancelConfirmPanel = new javax.swing.JPanel();
        cancelConfrimLabel = new javax.swing.JLabel();
        cancelBtnConfirm = new javax.swing.JButton();
        confirmBtnConfirm = new javax.swing.JButton();
        searchBarTextField = new javax.swing.JTextField();
        statsLabel = new javax.swing.JLabel();
        headerLabel = new javax.swing.JLabel();
        empInfoPane = new javax.swing.JScrollPane();
        empSalaryInfo = new javax.swing.JTable();
        idRadio = new javax.swing.JRadioButton();
        lastNameRadio = new javax.swing.JRadioButton();
        searchByLabel = new javax.swing.JLabel();
        noteLabel = new javax.swing.JLabel();

        editSalaryDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        editSalaryDialog.setAlwaysOnTop(true);
        editSalaryDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                editSalaryDialogWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                editSalaryDialogWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout editSalaryDialogLayout = new javax.swing.GroupLayout(editSalaryDialog.getContentPane());
        editSalaryDialog.getContentPane().setLayout(editSalaryDialogLayout);
        editSalaryDialogLayout.setHorizontalGroup(
            editSalaryDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 893, Short.MAX_VALUE)
        );
        editSalaryDialogLayout.setVerticalGroup(
            editSalaryDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
        headerLabel.setText("EMPLOYEE SALARY MANAGEMENT");

        empSalaryInfo.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        empSalaryInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Employee No.", "Name", "SSS", "PhillHealth", "Pag IBIG", "TIN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        empSalaryInfo.setRowHeight(28);
        empSalaryInfo.getTableHeader().setResizingAllowed(false);
        empSalaryInfo.getTableHeader().setReorderingAllowed(false);
        empInfoPane.setViewportView(empSalaryInfo);
        if (empSalaryInfo.getColumnModel().getColumnCount() > 0) {
            empSalaryInfo.getColumnModel().getColumn(0).setPreferredWidth(40);
            empSalaryInfo.getColumnModel().getColumn(1).setPreferredWidth(80);
            empSalaryInfo.getColumnModel().getColumn(2).setPreferredWidth(80);
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

        noteLabel.setFont(new java.awt.Font("Poppins", 2, 12)); // NOI18N
        noteLabel.setText("Double-click a row to edit employee salary details.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(noteLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(statsLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchByLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(idRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastNameRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(empInfoPane, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
                    .addComponent(headerLabel)
                    .addComponent(searchBarTextField))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(headerLabel)
                .addGap(6, 6, 6)
                .addComponent(searchBarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idRadio)
                    .addComponent(lastNameRadio)
                    .addComponent(searchByLabel))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noteLabel)
                    .addComponent(statsLabel))
                .addGap(0, 0, 0)
                .addComponent(empInfoPane, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                .addGap(24, 24, 24))
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
        editSalaryDialog.dispose();
    }//GEN-LAST:event_confirmBtnConfirmActionPerformed

    private void editSalaryDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_editSalaryDialogWindowClosing
        // TODO add your handling code here:
        exitEditorDialog.pack();
        exitEditorDialog.setResizable(false);
        exitEditorDialog.setLocationRelativeTo(null);
        exitEditorDialog.setTitle("Exiting editor");

        exitEditorDialog.setVisible(true);  
    }//GEN-LAST:event_editSalaryDialogWindowClosing

    private void editSalaryDialogWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_editSalaryDialogWindowClosed
        // TODO add your handling code here:
        resetSearchAndTable();
        isIDSelected = true;
        idRadio.setSelected(true);
        
        loadEmployees();
    }//GEN-LAST:event_editSalaryDialogWindowClosed

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
    private javax.swing.JButton cancelBtnConfirm;
    private javax.swing.JPanel cancelConfirmPanel;
    private javax.swing.JLabel cancelConfrimLabel;
    private javax.swing.JButton confirmBtnConfirm;
    private javax.swing.JDialog editSalaryDialog;
    private javax.swing.JScrollPane empInfoPane;
    private javax.swing.JTable empSalaryInfo;
    private javax.swing.JDialog exitEditorDialog;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JRadioButton idRadio;
    private javax.swing.JRadioButton lastNameRadio;
    private javax.swing.JLabel noteLabel;
    private javax.swing.ButtonGroup radioBtnGroup;
    private javax.swing.JTextField searchBarTextField;
    private javax.swing.JLabel searchByLabel;
    private javax.swing.JLabel statsLabel;
    // End of variables declaration//GEN-END:variables
}
