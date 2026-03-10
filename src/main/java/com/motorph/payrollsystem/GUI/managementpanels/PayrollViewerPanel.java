/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.gui.managementpanels;

import com.motorph.payrollsystem.gui.managementpanels.tools.InformationEditor;
import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.gui.managementpanels.tools.PayslipViewer;
import com.motorph.payrollsystem.gui.managementpanels.tools.SalaryEditor;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.service.EmployeeService;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.FontsAndFormats;
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
           
            fillTable(displayedEmployees);
            
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error loading employee data");
        }
        
        customizeCellColumns();
    }
    

    private void customizeCellColumns() {
        empTable.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 12));
    }
    
    private void fillTable(List<Employee> list) {
        DefaultTableModel model = (DefaultTableModel) clearTable(empTable);
        
        for (Employee emp : list) {
            model.addRow(new Object[]{
                emp.getEmployeeNo(),
                emp.getLastName(),
                emp.getFirstName(),
                emp.getDepartmentInfo().getDepartment(),
                emp.getDepartmentInfo().getPosition()
            });
        }
    }
    
    private DefaultTableModel clearTable(javax.swing.JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        return model;
    }
    
    private void hookRowDoubleClick() {
        empTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() != 2) return;
                
                int viewRow = empTable.getSelectedRow();
                if (viewRow < 0) return;
                
                int modelRow = empTable.convertRowIndexToModel(viewRow);
                if (modelRow < 0 || modelRow >= displayedEmployees.size()) return;

                showPayslipInfo(displayedEmployees.get(modelRow));
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

    private void showPayslipInfo(Employee selectedEmployee) {
        String title = "Payslip Viewer for : " + selectedEmployee.getFullName();
        PayslipViewer payslipViewer = new PayslipViewer(appContext, selectedEmployee, payslipViewerDialog);
        openInfoDialog(title, payslipViewer);
    }
    
    private void openInfoDialog(String title, PayslipViewer payslipViewer) {
        payslipViewerDialog.setTitle(title);
        payslipViewerDialog.setContentPane(payslipViewer);
        payslipViewerDialog.pack();
        payslipViewerDialog.setResizable(false);
        payslipViewerDialog.setLocationRelativeTo(null);
        
        payslipViewerDialog.setVisible(true);
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
        payslipViewerDialog = new javax.swing.JDialog(this.dialog, true);
        searchBarTextField = new javax.swing.JTextField();
        headerLabel = new javax.swing.JLabel();
        empInfoPane = new javax.swing.JScrollPane();
        empTable = new javax.swing.JTable();
        idRadio = new javax.swing.JRadioButton();
        lastNameRadio = new javax.swing.JRadioButton();
        searchByLabel = new javax.swing.JLabel();
        noteLabel = new javax.swing.JLabel();

        payslipViewerDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        payslipViewerDialog.setAlwaysOnTop(true);
        payslipViewerDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                payslipViewerDialogWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                payslipViewerDialogWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout payslipViewerDialogLayout = new javax.swing.GroupLayout(payslipViewerDialog.getContentPane());
        payslipViewerDialog.getContentPane().setLayout(payslipViewerDialogLayout);
        payslipViewerDialogLayout.setHorizontalGroup(
            payslipViewerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 544, Short.MAX_VALUE)
        );
        payslipViewerDialogLayout.setVerticalGroup(
            payslipViewerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 569, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(255, 255, 255));

        searchBarTextField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        searchBarTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        headerLabel.setText("EMPLOYEE PAYSLIP VIEWER");
        headerLabel.setFont(new java.awt.Font("Poppins", 1, 20)); // NOI18N

        empTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Employee No.", "Last Name", "First Name", "Department", "Position"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        empTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        empTable.setRowHeight(28);
        empTable.getTableHeader().setResizingAllowed(false);
        empTable.getTableHeader().setReorderingAllowed(false);
        empInfoPane.setViewportView(empTable);
        if (empTable.getColumnModel().getColumnCount() > 0) {
            empTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            empTable.getColumnModel().getColumn(0).setCellRenderer(FontsAndFormats.cellCenterRenderer());
            empTable.getColumnModel().getColumn(1).setPreferredWidth(80);
            empTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        }

        radioBtnGroup.add(idRadio);
        idRadio.setSelected(true);
        idRadio.setText("ID Number");
        idRadio.setBackground(new java.awt.Color(255, 255, 255));
        idRadio.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        idRadio.addActionListener(this::idRadioActionPerformed);

        radioBtnGroup.add(lastNameRadio);
        lastNameRadio.setText("Last Name");
        lastNameRadio.setBackground(new java.awt.Color(255, 255, 255));
        lastNameRadio.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        lastNameRadio.addActionListener(this::lastNameRadioActionPerformed);

        searchByLabel.setText("Search by :");
        searchByLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        noteLabel.setText("Double-click a row to view employee payslip records.");
        noteLabel.setFont(new java.awt.Font("Poppins", 2, 12)); // NOI18N

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
                        .addComponent(noteLabel))
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
                    .addComponent(searchByLabel)
                    .addComponent(noteLabel))
                .addGap(0, 0, 0)
                .addComponent(empInfoPane, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        idRadio.getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void payslipViewerDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_payslipViewerDialogWindowClosing
        // TODO add your handling code here:
        payslipViewerDialog.dispose();
    }//GEN-LAST:event_payslipViewerDialogWindowClosing

    private void payslipViewerDialogWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_payslipViewerDialogWindowClosed
        // TODO add your handling code here:
        resetSearchAndTable();
        isIDSelected = true;
        idRadio.setSelected(true);
        
        loadEmployees();
    }//GEN-LAST:event_payslipViewerDialogWindowClosed

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
    private javax.swing.JScrollPane empInfoPane;
    private javax.swing.JTable empTable;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JRadioButton idRadio;
    private javax.swing.JRadioButton lastNameRadio;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JDialog payslipViewerDialog;
    private javax.swing.ButtonGroup radioBtnGroup;
    private javax.swing.JTextField searchBarTextField;
    private javax.swing.JLabel searchByLabel;
    // End of variables declaration//GEN-END:variables
}
