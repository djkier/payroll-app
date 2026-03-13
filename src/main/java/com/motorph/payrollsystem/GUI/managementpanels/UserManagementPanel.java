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
import com.motorph.payrollsystem.gui.managementpanels.tools.useraccountpanel.ChangeUsernamePanel;
import com.motorph.payrollsystem.gui.managementpanels.tools.useraccountpanel.ResetPasswordPanel;
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
        this.parentDialog = dialog;
        this.userList = new ArrayList<>();
        this.displayedUser = new ArrayList<>();
        this.selectedUser = null;
        this.currentUser = appContext.getSessionManager().getCurrentEmployee();
        
        this.isIDSelected = true;
        
        initComponents();
        
        loadUserAccounts();
        initButtonState();
        hookTableSelection();
        hookSearch();
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
                isActiveEquiv(user.isActive()),
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
    
    private String isActiveEquiv(boolean isActive) {
        return isActive ? "ACTIVE" : "INACTIVE";
    }
    
    private DefaultTableModel clearTable(javax.swing.JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        return model;
    }
    
    private void initButtonState() {
        resetBtn.setEnabled(false);
        userNameBtn.setEnabled(false);
        activateBtn.setEnabled(false);
        deactivateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
    }
    
    private void hookTableSelection() {
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            updateButtonState();
        });
    }
      
    private void updateButtonState() {
        int viewRow = userTable.getSelectedRow();

        if (viewRow < 0) {
            initButtonState();
            return;
        }

        int modelRow = userTable.convertRowIndexToModel(viewRow);
        if (modelRow < 0 || modelRow >= displayedUser.size()) {
            initButtonState();
            return;
        }

        this.selectedUser = displayedUser.get(modelRow);
        String employeeNo = selectedUser.getEmployeeNo();
        initButtonState();
        

        try {
            Employee employee = appContext.getEmployeeService().findByEmployeeNo(employeeNo);
            String currentEmployeeNo = this.currentUser.getEmployeeNo();

            boolean isCurrentUser = employeeNo.equals(currentEmployeeNo);
            boolean hasEmployeeRecord = employee != null;
            boolean isActive = selectedUser.isActive();

            if (!hasEmployeeRecord) {
                deleteBtn.setEnabled(!isCurrentUser);
                return;
            }

            resetBtn.setEnabled(true);
            userNameBtn.setEnabled(true);

            if (isActive) {
                deactivateBtn.setEnabled(!isCurrentUser);
            } else {
                activateBtn.setEnabled(true);
            }

        } catch (Exception ex) {
            initButtonState();
            ex.printStackTrace();
        }
        
        System.out.println(selectedUser.getUsername());
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
            displayedUser = new ArrayList<>(userList);
            fillTable(displayedUser);
            return;
        }

        List<UserAccount> filtered = new ArrayList<>();

        for (UserAccount user : userList) {
            String empNo = String.valueOf(user.getEmployeeNo()).toLowerCase();
            if (idRadio.isSelected()) {
                if (empNo.contains(keyword)) {
                    filtered.add(user);
                }
            } else if (empNameRadioBtn.isSelected()) {
                try {
                    Employee emp = appContext.getEmployeeService().findByEmployeeNo(empNo);

                    if (emp == null) {
                        continue;
                    }

                    if (emp.getLastFirstName().toLowerCase().contains(keyword)) {
                        filtered.add(user);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace(); 
                }
            }
        }

        displayedUser = filtered;
        fillTable(displayedUser);
    }
    
    private void resetSearchAndTable() {
        searchBarTextField.setText("");
        searchBarTextField.requestFocus();
        applySearch();
    }

    private void openActionDialog(String title, javax.swing.JPanel panel) {
        btnActionDialog.setTitle(title);
        btnActionDialog.setContentPane(panel);
        btnActionDialog.pack();
        btnActionDialog.setResizable(false);
        btnActionDialog.setLocationRelativeTo(parentDialog);
        
        btnActionDialog.setVisible(true);
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
        btnActionDialog = new javax.swing.JDialog(this.parentDialog, true);
        searchBarTextField = new javax.swing.JTextField();
        headerLabel = new javax.swing.JLabel();
        userPane = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        idRadio = new javax.swing.JRadioButton();
        empNameRadioBtn = new javax.swing.JRadioButton();
        searchByLabel = new javax.swing.JLabel();
        noteLabel = new javax.swing.JLabel();
        resetBtn = new javax.swing.JButton();
        userNameBtn = new javax.swing.JButton();
        activateBtn = new javax.swing.JButton();
        deactivateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        decorLine = new javax.swing.JPanel();

        btnActionDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        btnActionDialog.setAlwaysOnTop(true);
        btnActionDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                btnActionDialogWindowClosed(evt);
            }
        });

        javax.swing.GroupLayout btnActionDialogLayout = new javax.swing.GroupLayout(btnActionDialog.getContentPane());
        btnActionDialog.getContentPane().setLayout(btnActionDialogLayout);
        btnActionDialogLayout.setHorizontalGroup(
            btnActionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 544, Short.MAX_VALUE)
        );
        btnActionDialogLayout.setVerticalGroup(
            btnActionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
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
        userTable.setRowHeight(34);
        userTable.getTableHeader().setResizingAllowed(false);
        userTable.getTableHeader().setReorderingAllowed(false);
        userPane.setViewportView(userTable);
        if (userTable.getColumnModel().getColumnCount() > 0) {
            userTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            userTable.getColumnModel().getColumn(0).setCellRenderer(FontsAndFormats.cellCenterRenderer());
            userTable.getColumnModel().getColumn(1).setPreferredWidth(80);
            userTable.getColumnModel().getColumn(2).setPreferredWidth(80);
            userTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        }

        idRadio.setBackground(new java.awt.Color(255, 255, 255));
        radioBtnGroup.add(idRadio);
        idRadio.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        idRadio.setSelected(true);
        idRadio.setText("ID Number");
        idRadio.addActionListener(this::idRadioActionPerformed);

        empNameRadioBtn.setBackground(new java.awt.Color(255, 255, 255));
        radioBtnGroup.add(empNameRadioBtn);
        empNameRadioBtn.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        empNameRadioBtn.setText("Employee Name");
        empNameRadioBtn.addActionListener(this::empNameRadioBtnActionPerformed);

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
        userNameBtn.addActionListener(this::userNameBtnActionPerformed);

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

        decorLine.setBackground(new java.awt.Color(204, 204, 204));

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
                            .addComponent(searchBarTextField)
                            .addComponent(decorLine, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                        .addComponent(empNameRadioBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap(21, Short.MAX_VALUE))))
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
                    .addComponent(empNameRadioBtn)
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

    private void btnActionDialogWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_btnActionDialogWindowClosed
        // TODO add your handling code here:
        resetSearchAndTable();
        isIDSelected = true;
        idRadio.setSelected(true);
        
        loadUserAccounts();
    }//GEN-LAST:event_btnActionDialogWindowClosed

    private void idRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idRadioActionPerformed
        // TODO add your handling code here:
        if (isIDSelected) return;
        
        isIDSelected = true;
        resetSearchAndTable();
    }//GEN-LAST:event_idRadioActionPerformed

    private void empNameRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empNameRadioBtnActionPerformed
        // TODO add your handling code here:
        if (!isIDSelected) return;
        
        isIDSelected = false;
        resetSearchAndTable();
    }//GEN-LAST:event_empNameRadioBtnActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        // TODO add your handling code here:
        String title = "Reset Password";
        ResetPasswordPanel rpp = new ResetPasswordPanel(appContext, selectedUser, btnActionDialog);
        openActionDialog(title, rpp);
    }//GEN-LAST:event_resetBtnActionPerformed

    private void userNameBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userNameBtnActionPerformed
        // TODO add your handling code here:
        String title = "Update Username";
        ChangeUsernamePanel cup = new ChangeUsernamePanel(appContext, selectedUser, btnActionDialog);
        openActionDialog(title, cup);
    }//GEN-LAST:event_userNameBtnActionPerformed

    private javax.swing.JDialog parentDialog;
    private boolean isIDSelected;
    private List<UserAccount> userList;
    private List<UserAccount> displayedUser;
    private UserAccount selectedUser;
    private Employee currentUser;
    private AppContext appContext;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton activateBtn;
    private javax.swing.JDialog btnActionDialog;
    private javax.swing.JButton deactivateBtn;
    private javax.swing.JPanel decorLine;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JRadioButton empNameRadioBtn;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JRadioButton idRadio;
    private javax.swing.JLabel noteLabel;
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
