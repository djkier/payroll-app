/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.gui.managementpanels.tools;


import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.model.leave.LeaveRequest;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.FontsAndFormats;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author djjus
 */
public class ReportsPanel extends javax.swing.JPanel {

    /**
     * Creates new form ReviewLeavePanel
     */
    public ReportsPanel(
            AppContext appContext,
            javax.swing.JDialog parentDialog
    ) {
        this.appContext = appContext;
        this.currentEmployee = appContext.getSessionManager().getCurrentEmployee();
        this.policy = appContext.getSessionManager().getAccessPolicy();
        this.parentDialog = parentDialog;
        this.nonPendingList = new ArrayList<>();
        this.displayList = new ArrayList<>();
        this.selectedRequest = null;
        this.selectedName = null;
        this.isApproving = true;
        
        initComponents();
//        loadLeaveRequests();
//        hookRowDoubleClick();
//        hookSearch();
    }
    
    private void loadLeaveRequests() {
        try {
            this.nonPendingList = appContext.getLeaveService().getDecisionHistory(policy);
            this.displayList = new ArrayList<>(nonPendingList);
            
            //should use tableEmployeeList
            fillTable(displayList);
            
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error loading employee data");
        }
        
        customizeCellColumns();
    }
    
    private void customizeCellColumns() {
        leaveHistoryTable.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 12));
    }
    
    private void fillTable(List<LeaveRequest> list) {
        DefaultTableModel model = (DefaultTableModel) clearTable(leaveHistoryTable);
        
        if (list == null || list.isEmpty()) {
            return;
        }
        
        for (LeaveRequest request : list) {
            String employeeNo = request.getEmployeeNo();
            String name;
            try {
                name = appContext.getEmployeeService().findByEmployeeNo(employeeNo).getLastNameInitial();
            } catch (Exception ex) {
                name = "Name can't found";
            }
            
            model.addRow(new Object[]{
                Dates.shortFullDate(request.getFiledDate()),
                employeeNo,
                name,
                request.getSubject(),
                request.getStatus().toString()
            });
        } 
    }
    
    private DefaultTableModel clearTable(javax.swing.JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        return model;
    }
    
    private void hookRowDoubleClick() {
        leaveHistoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() != 2) return;
                
                int viewRow = leaveHistoryTable.getSelectedRow();
                if (viewRow < 0) return;
                
                int modelRow = leaveHistoryTable.convertRowIndexToModel(viewRow);
                if (modelRow < 0 || modelRow >= displayList.size()) return;

                showRequestInfo(displayList.get(modelRow));
            }
        });
    }
    
    private void showRequestInfo(LeaveRequest request) {
        String name;
        Employee employee;
        try {
                employee = appContext.getEmployeeService().findByEmployeeNo(request.getEmployeeNo());
                name = employee.getLastFirstName();
            } catch (Exception ex) {
                name = "Name can't found";
                employee = new Employee();
            }
        String title = "Leave request by : " + name;
        
        this.selectedRequest = request;
        this.selectedName = name;
        showRequestDialog(request, employee, title);
    }
    
    private void showRequestDialog(LeaveRequest request, Employee employee, String title) {

        fillEmployeeDetails(employee);
        fillRequestDetails(request);
        
        dialogOpener(viewLeaveDetailsDialog, title);
    }
    
    private void fillEmployeeDetails(Employee employee) {
        numField.setText(employee.getEmployeeNo());
        nameField.setText(employee.getFullName());
        positionField.setText(employee.getDepartmentInfo().getPosition());
        departmentField.setText(employee.getDepartmentInfo().getDepartment());
    }
    
    private void fillRequestDetails(LeaveRequest request) {
        subjectField.setText(request.getSubject());
        filedField.setText(Dates.fullDate(request.getFiledDate()));
        String dateRange = Dates.shortFullDate(request.getLeaveStart()) +
                " to " + Dates.shortFullDate(request.getLeaveEnd());
        leaveDateField.setText(dateRange);
        statusField.setText(request.getStatus().toString());
        messageTextArea.setText(request.getMessage());

    }
    
//    private void hookSearch() {
//        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
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
    
//    private void applySearch() {
//        String keyword = searchField.getText().trim().toLowerCase();
//
//        if (keyword.isEmpty()) {
//            displayList = filterByStatus(new ArrayList(nonPendingList));
//            fillTable(displayList);
//            return;
//        }
//
//        List<LeaveRequest> filtered = new ArrayList<>();
//
//        for (LeaveRequest request : nonPendingList) {
//            String employeeNo = String.valueOf(request.getEmployeeNo().toLowerCase());
//            if (employeeNo.contains(keyword)) {
//                filtered.add(request);
//            }
//        }
//
//        displayList = filterByStatus(filtered);
//        fillTable(displayList);
//    }
    
    
    private void dialogOpener(javax.swing.JDialog dialog, String title) {
        dialog.setTitle(title);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentDialog);
        
        dialog.setVisible(true);
    }
    
//    private List<LeaveRequest> filterByStatus(List<LeaveRequest> requests) {
//        if (statusComboBox.getSelectedItem().toString().equalsIgnoreCase("All")) {
//            return requests;
//        }
//        
//        List<LeaveRequest> filteredList = new ArrayList<>();
//        
//        for (LeaveRequest request : requests) {
//            if (statusComboBox.getSelectedItem().toString().equalsIgnoreCase(request.getStatus().toString())) {
//                filteredList.add(request);
//            }
//        }
//        
//        return filteredList;
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewLeaveDetailsDialog = new javax.swing.JDialog(this.parentDialog, true);
        leaveDetailsPanel = new javax.swing.JPanel();
        subjectLabel = new javax.swing.JLabel();
        filedLabel = new javax.swing.JLabel();
        leaveDateLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        messageLabel = new javax.swing.JLabel();
        scrollPaneMessage = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        closeBtn = new javax.swing.JButton();
        numLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        positionLabel = new javax.swing.JLabel();
        departmentLabel = new javax.swing.JLabel();
        numField = new javax.swing.JLabel();
        nameField = new javax.swing.JLabel();
        positionField = new javax.swing.JLabel();
        departmentField = new javax.swing.JLabel();
        subjectField = new javax.swing.JLabel();
        filedField = new javax.swing.JLabel();
        leaveDateField = new javax.swing.JLabel();
        statusField = new javax.swing.JLabel();
        statusGroup = new javax.swing.ButtonGroup();
        leaveHistoryPane = new javax.swing.JScrollPane();
        leaveHistoryTable = new javax.swing.JTable();
        noteLabel = new javax.swing.JLabel();
        leaveHistoryPane1 = new javax.swing.JScrollPane();
        leaveHistoryTable1 = new javax.swing.JTable();

        viewLeaveDetailsDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        viewLeaveDetailsDialog.setAlwaysOnTop(true);
        viewLeaveDetailsDialog.setBackground(new java.awt.Color(255, 255, 255));
        viewLeaveDetailsDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        viewLeaveDetailsDialog.setIconImage(null);
        viewLeaveDetailsDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        viewLeaveDetailsDialog.setName("viewLeaveDetailsDialog"); // NOI18N
        viewLeaveDetailsDialog.setResizable(false);

        leaveDetailsPanel.setBackground(new java.awt.Color(255, 255, 255));

        subjectLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        subjectLabel.setText("Subject :");

        filedLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        filedLabel.setText("Filed Date :");

        leaveDateLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        leaveDateLabel.setText("Leave  Date :");

        statusLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        statusLabel.setText("Status :");

        messageLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        messageLabel.setText("Message :");

        scrollPaneMessage.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        messageTextArea.setEditable(false);
        messageTextArea.setColumns(20);
        messageTextArea.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        messageTextArea.setLineWrap(true);
        messageTextArea.setRows(5);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setFocusable(false);
        scrollPaneMessage.setViewportView(messageTextArea);

        closeBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        closeBtn.setText("CLOSE");
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(this::closeBtnActionPerformed);

        numLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        numLabel.setText("Employee No :");

        nameLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        nameLabel.setText("Employee Name :");

        positionLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        positionLabel.setText("Position :");

        departmentLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        departmentLabel.setText("Department :");

        numField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        numField.setText("--");

        nameField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        nameField.setText("--");

        positionField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        positionField.setText("--");

        departmentField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        departmentField.setText("--");

        subjectField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        subjectField.setText("--");

        filedField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        filedField.setText("--");

        leaveDateField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        leaveDateField.setText("--");

        statusField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        statusField.setText("--");

        javax.swing.GroupLayout leaveDetailsPanelLayout = new javax.swing.GroupLayout(leaveDetailsPanel);
        leaveDetailsPanel.setLayout(leaveDetailsPanelLayout);
        leaveDetailsPanelLayout.setHorizontalGroup(
            leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leaveDetailsPanelLayout.createSequentialGroup()
                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leaveDetailsPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, leaveDetailsPanelLayout.createSequentialGroup()
                                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(positionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(departmentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(numLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(subjectLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(numField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(positionField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(departmentField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(subjectField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(scrollPaneMessage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, leaveDetailsPanelLayout.createSequentialGroup()
                                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(messageLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statusLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(leaveDateLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                    .addComponent(filedLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(filedField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(leaveDateField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statusField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(leaveDetailsPanelLayout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 21, Short.MAX_VALUE))
        );
        leaveDetailsPanelLayout.setVerticalGroup(
            leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leaveDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numLabel)
                    .addComponent(numField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(positionLabel)
                    .addComponent(positionField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(departmentLabel)
                    .addComponent(departmentField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subjectLabel)
                    .addComponent(subjectField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filedLabel)
                    .addComponent(filedField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(leaveDateLabel)
                    .addComponent(leaveDateField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel)
                    .addComponent(statusField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageLabel)
                .addGap(12, 12, 12)
                .addComponent(scrollPaneMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout viewLeaveDetailsDialogLayout = new javax.swing.GroupLayout(viewLeaveDetailsDialog.getContentPane());
        viewLeaveDetailsDialog.getContentPane().setLayout(viewLeaveDetailsDialogLayout);
        viewLeaveDetailsDialogLayout.setHorizontalGroup(
            viewLeaveDetailsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leaveDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        viewLeaveDetailsDialogLayout.setVerticalGroup(
            viewLeaveDetailsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leaveDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(255, 255, 255));

        leaveHistoryTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        leaveHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date Created", "Period Start", "Period End", "Generated By"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        leaveHistoryTable.setRowHeight(24);
        leaveHistoryTable.getTableHeader().setReorderingAllowed(false);
        leaveHistoryPane.setViewportView(leaveHistoryTable);

        noteLabel.setFont(new java.awt.Font("Poppins", 2, 12)); // NOI18N
        noteLabel.setText("Double-click a row to view report.");

        leaveHistoryTable1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        leaveHistoryTable1.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date Created", "Period Start", "Period End", "Generated By"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        leaveHistoryTable1.setRowHeight(24);
        leaveHistoryTable1.getTableHeader().setReorderingAllowed(false);
        leaveHistoryPane1.setViewportView(leaveHistoryTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(noteLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(leaveHistoryPane, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                        .addGap(24, 24, 24))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(leaveHistoryPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(noteLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(leaveHistoryPane, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(leaveHistoryPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        // TODO add your handling code here:
        viewLeaveDetailsDialog.dispose();
    }//GEN-LAST:event_closeBtnActionPerformed

    private AppContext appContext;
    private Employee currentEmployee;
    private javax.swing.JDialog parentDialog;
    private List<LeaveRequest> displayList;
    private List<LeaveRequest> nonPendingList;
    private boolean isPendingMode;
    private LeaveRequest selectedRequest;
    private String selectedName;
    private boolean isApproving;
    private AccessPolicy policy;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeBtn;
    private javax.swing.JLabel departmentField;
    private javax.swing.JLabel departmentLabel;
    private javax.swing.JLabel filedField;
    private javax.swing.JLabel filedLabel;
    private javax.swing.JLabel leaveDateField;
    private javax.swing.JLabel leaveDateLabel;
    private javax.swing.JPanel leaveDetailsPanel;
    private javax.swing.JScrollPane leaveHistoryPane;
    private javax.swing.JScrollPane leaveHistoryPane1;
    private javax.swing.JTable leaveHistoryTable;
    private javax.swing.JTable leaveHistoryTable1;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JLabel nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JLabel numField;
    private javax.swing.JLabel numLabel;
    private javax.swing.JLabel positionField;
    private javax.swing.JLabel positionLabel;
    private javax.swing.JScrollPane scrollPaneMessage;
    private javax.swing.JLabel statusField;
    private javax.swing.ButtonGroup statusGroup;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel subjectField;
    private javax.swing.JLabel subjectLabel;
    private javax.swing.JDialog viewLeaveDetailsDialog;
    // End of variables declaration//GEN-END:variables
}
