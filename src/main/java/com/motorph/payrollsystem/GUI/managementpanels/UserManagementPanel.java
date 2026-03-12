/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.gui.managementpanels;

import com.motorph.payrollsystem.gui.managementpanels.*;
import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.gui.managementpanels.tools.PayslipViewer;
import com.motorph.payrollsystem.gui.managementpanels.tools.PayslipViewer;
import com.motorph.payrollsystem.model.auth.UserAccount;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.service.EmployeeService;
import com.motorph.payrollsystem.service.UserAccountService;
import com.motorph.payrollsystem.utility.FontsAndFormats;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author djjus
 */
public class UserManagementPanel extends javax.swing.JPanel {

    /**
     * Creates new form EIMPanels
     * 
     * @param appContext use to get employee contexts
     */
    public UserManagementPanel(
            AppContext appContext,
            javax.swing.JDialog dialog) {
        this.appContext = appContext;
        this.dialog = dialog;
        this.userList = new ArrayList<>();
        this.displayedUser = new ArrayList<>();
        this.isIDSelected = true;
        
        initComponents();
        
        loadUserAccounts();
//        hookRowDoubleClick();
//        hookSearch();
    }
    
    private void loadUserAccounts() {
        UserAccountService userAccountService = appContext.getUserAccountService();

        try {
            this.userList = userAccountService.getAllAccounts();
            displayedUser = new ArrayList<>(userList);
           
            fillTable(displayedUser);
            
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error loading employee data");
        }
        
        customizeCellColumns();
    }
    

    private void customizeCellColumns() {
        userTable.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 12));
    }
    
    private void fillTable(List<UserAccount> list) {
        DefaultTableModel model = (DefaultTableModel) clearTable(userTable);
        
        
        for (UserAccount user : list) {
            model.addRow(new Object[]{
                user.getEmployeeNo(),
                user.getUsername(),
                user.isActive(),
                getUserEmployeeName(user.getEmployeeNo())
            });
        }
    }
    
    private String getUserEmployeeName(String employeeNo) {
        EmployeeService employeeService = appContext.getEmployeeService();
        String name;
        try {
            name = employeeService.findByEmployeeNo(employeeNo).getLastFirstName();
        } catch (Exception ex) {
            name = "Name can't found";
            ex.printStackTrace();
        }
        
        return name;
    }
    
    private DefaultTableModel clearTable(javax.swing.JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        return model;
    }
    
//    private void hookRowDoubleClick() {
//        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
//            @Override
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                if (evt.getClickCount() != 2) return;
//                
//                int viewRow = userTable.getSelectedRow();
//                if (viewRow < 0) return;
//                
//                int modelRow = userTable.convertRowIndexToModel(viewRow);
//                if (modelRow < 0 || modelRow >= displayedUser.size()) return;
//
//                showPayslipInfo(displayedUser.get(modelRow));
//            }
//        });
//    }
//    
//    private void hookSearch() {
//        searchBarTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
//            @Override
//            public void insertUpdate(javax.swing.event.DocumentEvent e) {
//                applySearch();
//            }
//
//            @Override
//            public void removeUpdate(javax.swing.event.DocumentEvent e) {
//                applySearch();
//            }
//
//            @Override
//            public void changedUpdate(javax.swing.event.DocumentEvent e) {
//                applySearch();
//            }
//        });
//    }
//    
//    private void applySearch() {
//        String keyword = searchBarTextField.getText().trim().toLowerCase();
//
//        if (keyword.isEmpty()) {
//            displayedUser = new ArrayList<>(userList);
//            fillTable(displayedUser);
//            return;
//        }
//
//        List<Employee> filtered = new ArrayList<>();
//
//        for (Employee emp : userList) {
//            if (idRadio.isSelected()) {
//                String empNo = String.valueOf(emp.getEmployeeNo()).toLowerCase();
//                if (empNo.contains(keyword)) {
//                    filtered.add(emp);
//                }
//            } else if (lastNameRadio.isSelected()) {
//                String lastName = emp.getLastName() == null ? "" : emp.getLastName().toLowerCase();
//                if (lastName.contains(keyword)) {
//                    filtered.add(emp);
//                }
//            }
//        }
//
//        displayedUser = filtered;
//        fillTable(displayedUser);
//    }
//    
//    private void resetSearchAndTable() {
//        searchBarTextField.setText("");
//        searchBarTextField.requestFocus();
//        applySearch();
//    }
//
//    private void showPayslipInfo(Employee selectedEmployee) {
//        String title = "Payslip Viewer for : " + selectedEmployee.getFullName();
//        PayslipViewer payslipViewer = new PayslipViewer(appContext, selectedEmployee, payslipViewerDialog);
//        openInfoDialog(title, payslipViewer);
//    }
//    
//    private void openInfoDialog(String title, PayslipViewer payslipViewer) {
//        payslipViewerDialog.setTitle(title);
//        payslipViewerDialog.setContentPane(payslipViewer);
//        payslipViewerDialog.pack();
//        payslipViewerDialog.setResizable(false);
//        payslipViewerDialog.setLocationRelativeTo(null);
//        
//        payslipViewerDialog.setVisible(true);
//    }
    


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
        userPane = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        idRadio = new javax.swing.JRadioButton();
        lastNameRadio = new javax.swing.JRadioButton();
        searchByLabel = new javax.swing.JLabel();
        noteLabel = new javax.swing.JLabel();
        resetBtn = new javax.swing.JButton();
        userNameBtn = new javax.swing.JButton();
        activateBtn = new javax.swing.JButton();
        deactivateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        decorLine = new javax.swing.JPanel();

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

        headerLabel.setFont(new java.awt.Font("Poppins", 1, 20)); // NOI18N
        headerLabel.setText("User Account Management");

        userTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Employee No", "UserName", "Status", "Employee Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        userTable.setRowHeight(28);
        userTable.getTableHeader().setResizingAllowed(false);
        userTable.getTableHeader().setReorderingAllowed(false);
        userPane.setViewportView(userTable);
        if (userTable.getColumnModel().getColumnCount() > 0) {
            userTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            userTable.getColumnModel().getColumn(0).setHeaderValue("Employee No");
            userTable.getColumnModel().getColumn(0).setCellRenderer(FontsAndFormats.cellCenterRenderer());
            userTable.getColumnModel().getColumn(1).setPreferredWidth(80);
            userTable.getColumnModel().getColumn(1).setHeaderValue("UserName");
            userTable.getColumnModel().getColumn(2).setPreferredWidth(80);
            userTable.getColumnModel().getColumn(2).setHeaderValue("Status");
            userTable.getColumnModel().getColumn(3).setPreferredWidth(200);
            userTable.getColumnModel().getColumn(3).setHeaderValue("Employee Name");
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
        noteLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        noteLabel.setText("Select a row to manage the user account.");

        resetBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        resetBtn.setText("Reset Password");
        resetBtn.setFocusPainted(false);
        resetBtn.addActionListener(this::resetBtnActionPerformed);

        userNameBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        userNameBtn.setText("Change Username");
        userNameBtn.setFocusPainted(false);

        activateBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        activateBtn.setText("Activate");
        activateBtn.setFocusPainted(false);

        deactivateBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        deactivateBtn.setText("Deactivate");
        deactivateBtn.setFocusPainted(false);

        deleteBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        deleteBtn.setText("Delete Account");
        deleteBtn.setFocusPainted(false);

        refreshBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        refreshBtn.setText("Refresh");
        refreshBtn.setFocusPainted(false);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(noteLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(userPane, javax.swing.GroupLayout.PREFERRED_SIZE, 706, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(resetBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(userNameBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(activateBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deactivateBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(refreshBtn))
                            .addComponent(headerLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(searchByLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(idRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lastNameRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(searchBarTextField)
                            .addComponent(decorLine, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(24, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(headerLabel)
                .addGap(6, 6, 6)
                .addComponent(searchBarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idRadio)
                    .addComponent(lastNameRadio)
                    .addComponent(searchByLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userPane, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(noteLabel)
                .addGap(2, 2, 2)
                .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resetBtn)
                    .addComponent(userNameBtn)
                    .addComponent(activateBtn)
                    .addComponent(deactivateBtn)
                    .addComponent(deleteBtn)
                    .addComponent(refreshBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        idRadio.getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void payslipViewerDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_payslipViewerDialogWindowClosing
        // TODO add your handling code here:
        payslipViewerDialog.dispose();
    }//GEN-LAST:event_payslipViewerDialogWindowClosing

    private void payslipViewerDialogWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_payslipViewerDialogWindowClosed
        // TODO add your handling code here:
//        resetSearchAndTable();
//        isIDSelected = true;
//        idRadio.setSelected(true);
//        
//        loadEmployees();
    }//GEN-LAST:event_payslipViewerDialogWindowClosed

    private void idRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idRadioActionPerformed
        // TODO add your handling code here:
        if (isIDSelected) return;
        
        isIDSelected = true;
//        resetSearchAndTable();
    }//GEN-LAST:event_idRadioActionPerformed

    private void lastNameRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastNameRadioActionPerformed
        // TODO add your handling code here:
        if (!isIDSelected) return;
        
        isIDSelected = false;
//        resetSearchAndTable();
    }//GEN-LAST:event_lastNameRadioActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resetBtnActionPerformed

    private javax.swing.JDialog dialog;
    private boolean isIDSelected;
    private List<UserAccount> userList;
    private List<UserAccount> displayedUser;
    private AppContext appContext;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton activateBtn;
    private javax.swing.JButton deactivateBtn;
    private javax.swing.JPanel decorLine;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JRadioButton idRadio;
    private javax.swing.JRadioButton lastNameRadio;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JDialog payslipViewerDialog;
    private javax.swing.ButtonGroup radioBtnGroup;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton resetBtn;
    private javax.swing.JTextField searchBarTextField;
    private javax.swing.JLabel searchByLabel;
    private javax.swing.JButton userNameBtn;
    private javax.swing.JScrollPane userPane;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
