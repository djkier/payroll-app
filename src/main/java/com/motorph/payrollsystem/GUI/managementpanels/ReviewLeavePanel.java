/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.gui.managementpanels;

import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.gui.managementpanels.tools.LeaveHistoryPanel;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.model.leave.LeaveRequest;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.FontsAndFormats;
import com.motorph.payrollsystem.utility.ThemeColor;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author djjus
 */
public class ReviewLeavePanel extends javax.swing.JPanel {

    /**
     * Creates new form ReviewLeavePanel
     */
    public ReviewLeavePanel(
            AppContext appContext,
            javax.swing.JDialog parentDialog
    ) {
        this.appContext = appContext;
        this.currentEmployee = appContext.getSessionManager().getCurrentEmployee();
        this.policy = appContext.getSessionManager().getAccessPolicy();
        this.parentDialog = parentDialog;
        this.pendingList = new ArrayList<>();
        this.displayList = new ArrayList<>();
        this.selectedRequest = null;
        this.selectedName = null;
        this.isApproving = true;
        
        initComponents();
        loadLeaveRequests();
        hookRowDoubleClick();
        hookSearch();
    }
    
    private void loadLeaveRequests() {
        try {
            this.pendingList= appContext.getLeaveService().getPendingRequestsForReview(policy);
            this.displayList = new ArrayList<>(pendingList);
            
            //should use tableEmployeeList
            fillTable(displayList);
            
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error loading employee data");
        }
        
        customizeCellColumns();
    }
    
    private void customizeCellColumns() {
        leaveMngtTable.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 12));
    }
    
    private void fillTable(List<LeaveRequest> list) {
        DefaultTableModel model = (DefaultTableModel) clearTable(leaveMngtTable);
        
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
                Dates.shortFullDate(request.getLeaveStart()),
                Dates.shortFullDate(request.getLeaveEnd())
            });
        } 
    }
    
    private DefaultTableModel clearTable(javax.swing.JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        return model;
    }
    
    private void hookRowDoubleClick() {
        leaveMngtTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() != 2) return;
                
                int viewRow = leaveMngtTable.getSelectedRow();
                if (viewRow < 0) return;
                
                int modelRow = leaveMngtTable.convertRowIndexToModel(viewRow);
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
        boolean isRequestFromTheCurrentUser = employee.getEmployeeNo()
                .equalsIgnoreCase(currentEmployee.getEmployeeNo());
        decisionEligibility(isRequestFromTheCurrentUser);
        
        dialogOpener(leaveDetailsDialog, title);
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
    
    private void decisionEligibility(boolean isSamePerson) {
        denyBtn.setEnabled(!isSamePerson);
        approveBtn.setEnabled(!isSamePerson);
    }
    
    private void showConfirmationDialog(String title) {
        String decision = isApproving ? "Approve" : "Reject";
        rejectOrApproveLabel.setText(decision + " leave request?");
        
        employeeNameField.setText(this.selectedName);
        String dateRange = Dates.shortFullDate(selectedRequest.getLeaveStart()) +
                " to " + Dates.shortFullDate(selectedRequest.getLeaveEnd());
        leavePeriodField.setText(dateRange);
        
        dialogOpener(rejectOrApprove, title);
    }
    
    private void hookSearch() {
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
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
        String keyword = searchField.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            displayList = new ArrayList<>(pendingList);
            fillTable(displayList);
            return;
        }

        List<LeaveRequest> filtered = new ArrayList<>();

        for (LeaveRequest pending : pendingList) {
            String employeeNo = String.valueOf(pending.getEmployeeNo().toLowerCase());
            if (employeeNo.contains(keyword)) {
                filtered.add(pending);
            }
        }

        displayList = filtered;
        fillTable(displayList);
    }
    
    private void resetSearchAndTable() {
        applySearch();
        searchField.setText("");
        searchField.requestFocus();
        
        loadLeaveRequests();
        
    }
    
    private void dialogOpener(javax.swing.JDialog dialog, String title) {
        dialog.setTitle(title);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentDialog);
        
        dialog.setVisible(true);
    }
    
    private void handleApproval() {
        try {
            appContext.getLeaveService().approveRequest(
                    this.selectedRequest.getRequestId(), 
                    currentEmployee, 
                    policy);
            
            isApproving = true;
            leaveDetailsDialog.dispose();
            rejectOrApprove.dispose();
            resetSearchAndTable();
            showSuccessDialog("approved");
            this.selectedName = "";
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to approve request.",
                    "Approval Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
    
    private void handleRejection() {
        try {
            appContext.getLeaveService().rejectRequest(
                    this.selectedRequest.getRequestId(), 
                    currentEmployee, 
                    policy);
            
            isApproving = true;
            leaveDetailsDialog.dispose();
            rejectOrApprove.dispose();
            resetSearchAndTable();
            showSuccessDialog("rejection");
            this.selectedName = "";
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to reject request.",
                    "Rejection Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
    
    private void showSuccessDialog(String action) {
        String successMessage = "Leave request " + action + " successfully.";
        successDialogMessage.setText(successMessage);
        dialogOpener(successDialog, "Successful");
    }
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leaveDetailsDialog = new javax.swing.JDialog(parentDialog, true);
        leaveDetailsPanel = new javax.swing.JPanel();
        subjectLabel = new javax.swing.JLabel();
        filedLabel = new javax.swing.JLabel();
        leaveDateLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        messageLabel = new javax.swing.JLabel();
        scrollPaneMessage = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        denyBtn = new javax.swing.JButton();
        approveBtn = new javax.swing.JButton();
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
        rejectOrApprove = new javax.swing.JDialog(this.parentDialog, true);
        rejectOrApprovePanel = new javax.swing.JPanel();
        rejectOrApproveLabel = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        confirmBtn = new javax.swing.JButton();
        employeeLabel = new javax.swing.JLabel();
        leavePeriodLabel = new javax.swing.JLabel();
        employeeNameField = new javax.swing.JLabel();
        leavePeriodField = new javax.swing.JLabel();
        successDialog = new javax.swing.JDialog(this.parentDialog, true);
        successPanel = new javax.swing.JPanel();
        successDialogMessage = new javax.swing.JLabel();
        successDialogOkBtn = new javax.swing.JButton();
        historyDialog = new javax.swing.JDialog(this.parentDialog, true);
        leaveMngtHeader = new javax.swing.JLabel();
        tablePane = new javax.swing.JScrollPane();
        leaveMngtTable = new javax.swing.JTable();
        searchField = new javax.swing.JTextField();
        filterLabel = new javax.swing.JLabel();
        historyBtn = new javax.swing.JButton();
        noteLabel = new javax.swing.JLabel();

        leaveDetailsDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        leaveDetailsDialog.setAlwaysOnTop(true);
        leaveDetailsDialog.setBackground(new java.awt.Color(255, 255, 255));
        leaveDetailsDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        leaveDetailsDialog.setIconImage(null);
        leaveDetailsDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        leaveDetailsDialog.setName("leaveDetailsDialog"); // NOI18N
        leaveDetailsDialog.setResizable(false);

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

        denyBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        denyBtn.setText("DENY");
        denyBtn.setFocusPainted(false);
        denyBtn.addActionListener(this::denyBtnActionPerformed);

        approveBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        approveBtn.setText("APPROVE");
        approveBtn.setFocusPainted(false);
        approveBtn.addActionListener(this::approveBtnActionPerformed);

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
                .addGap(24, 24, 24)
                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leaveDetailsPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(denyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(approveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leaveDetailsPanelLayout.createSequentialGroup()
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
                                    .addComponent(statusField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(24, 24, 24))
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
                .addGap(6, 6, 6)
                .addComponent(scrollPaneMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(leaveDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(denyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(approveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout leaveDetailsDialogLayout = new javax.swing.GroupLayout(leaveDetailsDialog.getContentPane());
        leaveDetailsDialog.getContentPane().setLayout(leaveDetailsDialogLayout);
        leaveDetailsDialogLayout.setHorizontalGroup(
            leaveDetailsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leaveDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        leaveDetailsDialogLayout.setVerticalGroup(
            leaveDetailsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leaveDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rejectOrApprove.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        rejectOrApprove.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                rejectOrApproveWindowClosing(evt);
            }
        });

        rejectOrApprovePanel.setBackground(new java.awt.Color(255, 255, 255));

        rejectOrApproveLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        rejectOrApproveLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rejectOrApproveLabel.setText("Reject leave request?");

        cancelBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        cancelBtn.setText("CANCEL");
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(this::cancelBtnActionPerformed);

        confirmBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        confirmBtn.setText("CONFIRM");
        confirmBtn.setFocusPainted(false);
        confirmBtn.addActionListener(this::confirmBtnActionPerformed);

        employeeLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        employeeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        employeeLabel.setText("Employee :  ");

        leavePeriodLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        leavePeriodLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        leavePeriodLabel.setText("Leave Period :");

        employeeNameField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        employeeNameField.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        employeeNameField.setText("Don Justine Fontanilla");

        leavePeriodField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        leavePeriodField.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        leavePeriodField.setText("Don Justine Fontanilla");

        javax.swing.GroupLayout rejectOrApprovePanelLayout = new javax.swing.GroupLayout(rejectOrApprovePanel);
        rejectOrApprovePanel.setLayout(rejectOrApprovePanelLayout);
        rejectOrApprovePanelLayout.setHorizontalGroup(
            rejectOrApprovePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rejectOrApproveLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(rejectOrApprovePanelLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(rejectOrApprovePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rejectOrApprovePanelLayout.createSequentialGroup()
                        .addComponent(leavePeriodLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(leavePeriodField, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(rejectOrApprovePanelLayout.createSequentialGroup()
                        .addComponent(employeeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(employeeNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rejectOrApprovePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(confirmBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );
        rejectOrApprovePanelLayout.setVerticalGroup(
            rejectOrApprovePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rejectOrApprovePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rejectOrApproveLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(rejectOrApprovePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employeeLabel)
                    .addComponent(employeeNameField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rejectOrApprovePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(leavePeriodLabel)
                    .addComponent(leavePeriodField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(rejectOrApprovePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout rejectOrApproveLayout = new javax.swing.GroupLayout(rejectOrApprove.getContentPane());
        rejectOrApprove.getContentPane().setLayout(rejectOrApproveLayout);
        rejectOrApproveLayout.setHorizontalGroup(
            rejectOrApproveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rejectOrApprovePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        rejectOrApproveLayout.setVerticalGroup(
            rejectOrApproveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rejectOrApprovePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        successDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        successPanel.setBackground(new java.awt.Color(255, 255, 255));

        successDialogMessage.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        successDialogMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        successDialogMessage.setText("Leave request approved successfully.");

        successDialogOkBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        successDialogOkBtn.setText("OK");
        successDialogOkBtn.addActionListener(this::successDialogOkBtnActionPerformed);

        javax.swing.GroupLayout successPanelLayout = new javax.swing.GroupLayout(successPanel);
        successPanel.setLayout(successPanelLayout);
        successPanelLayout.setHorizontalGroup(
            successPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(successDialogMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
            .addGroup(successPanelLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(successDialogOkBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        successPanelLayout.setVerticalGroup(
            successPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, successPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(successDialogMessage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(successDialogOkBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout successDialogLayout = new javax.swing.GroupLayout(successDialog.getContentPane());
        successDialog.getContentPane().setLayout(successDialogLayout);
        successDialogLayout.setHorizontalGroup(
            successDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(successPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        successDialogLayout.setVerticalGroup(
            successDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(successPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        historyDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout historyDialogLayout = new javax.swing.GroupLayout(historyDialog.getContentPane());
        historyDialog.getContentPane().setLayout(historyDialogLayout);
        historyDialogLayout.setHorizontalGroup(
            historyDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        historyDialogLayout.setVerticalGroup(
            historyDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(255, 255, 255));

        leaveMngtHeader.setFont(new java.awt.Font("Poppins", 1, 20)); // NOI18N
        leaveMngtHeader.setText("LEAVE REQUEST MANAGEMENT");

        leaveMngtTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        leaveMngtTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Date Filed", "Employee No.", "Employee Name", "Subject", "Leave Start", "Leave End"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        leaveMngtTable.setRowHeight(24);
        leaveMngtTable.getTableHeader().setReorderingAllowed(false);
        tablePane.setViewportView(leaveMngtTable);
        if (leaveMngtTable.getColumnModel().getColumnCount() > 0) {
            leaveMngtTable.getColumnModel().getColumn(1).setCellRenderer(FontsAndFormats.cellCenterRenderer());
        }

        searchField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchFieldKeyTyped(evt);
            }
        });

        filterLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        filterLabel.setText("Filter by ID Number :");

        historyBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        historyBtn.setText("View Leave History");
        historyBtn.setFocusPainted(false);
        historyBtn.addActionListener(this::historyBtnActionPerformed);

        noteLabel.setFont(new java.awt.Font("Poppins", 2, 12)); // NOI18N
        noteLabel.setText("Double-click a row to review the leave request.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(historyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tablePane, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE))
                        .addGap(24, 24, 24))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(noteLabel)
                            .addComponent(leaveMngtHeader)
                            .addComponent(filterLabel))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(leaveMngtHeader)
                .addGap(6, 6, 6)
                .addComponent(filterLabel)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(historyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(noteLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tablePane, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void denyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_denyBtnActionPerformed
        // TODO add your handling code here:
        isApproving = false;
        showConfirmationDialog("Rejecting");
    }//GEN-LAST:event_denyBtnActionPerformed

    private void approveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_approveBtnActionPerformed
        // TODO add your handling code here:
        isApproving = true;
        showConfirmationDialog("Approving");
    }//GEN-LAST:event_approveBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        // TODO add your handling code here:
        rejectOrApprove.dispose();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void confirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmBtnActionPerformed
        // TODO add your handling code here:
        if (isApproving) {
            //make approving here
            handleApproval();
        } else {
            //do rejection message here
            handleRejection();
        }
        
        
    }//GEN-LAST:event_confirmBtnActionPerformed

    private void rejectOrApproveWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_rejectOrApproveWindowClosing
        // TODO add your handling code here:
        rejectOrApprove.dispose();
    }//GEN-LAST:event_rejectOrApproveWindowClosing

    private void successDialogOkBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_successDialogOkBtnActionPerformed
        // TODO add your handling code here:
        successDialog.dispose();
    }//GEN-LAST:event_successDialogOkBtnActionPerformed

    private void searchFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();

        // allow digits only
        if (!Character.isDigit(c)) {
            evt.consume();
            return;
        }

        // limit to 12 digits
        if (searchField.getText().length() >= 12) {
            evt.consume();
        }
    }//GEN-LAST:event_searchFieldKeyTyped

    private void historyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historyBtnActionPerformed
        // TODO add your handling code here:
        historyDialog.setContentPane(new LeaveHistoryPanel(appContext, historyDialog));
        dialogOpener(historyDialog, "Leave History");
    }//GEN-LAST:event_historyBtnActionPerformed

    private AppContext appContext;
    private Employee currentEmployee;
    private javax.swing.JDialog parentDialog;
    private List<LeaveRequest> displayList;
    private List<LeaveRequest> pendingList;
    private boolean isPendingMode;
    private LeaveRequest selectedRequest;
    private String selectedName;
    private boolean isApproving;
    private AccessPolicy policy;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton approveBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton confirmBtn;
    private javax.swing.JButton denyBtn;
    private javax.swing.JLabel departmentField;
    private javax.swing.JLabel departmentLabel;
    private javax.swing.JLabel employeeLabel;
    private javax.swing.JLabel employeeNameField;
    private javax.swing.JLabel filedField;
    private javax.swing.JLabel filedLabel;
    private javax.swing.JLabel filterLabel;
    private javax.swing.JButton historyBtn;
    private javax.swing.JDialog historyDialog;
    private javax.swing.JLabel leaveDateField;
    private javax.swing.JLabel leaveDateLabel;
    private javax.swing.JDialog leaveDetailsDialog;
    private javax.swing.JPanel leaveDetailsPanel;
    private javax.swing.JLabel leaveMngtHeader;
    private javax.swing.JTable leaveMngtTable;
    private javax.swing.JLabel leavePeriodField;
    private javax.swing.JLabel leavePeriodLabel;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JLabel nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JLabel numField;
    private javax.swing.JLabel numLabel;
    private javax.swing.JLabel positionField;
    private javax.swing.JLabel positionLabel;
    private javax.swing.JDialog rejectOrApprove;
    private javax.swing.JLabel rejectOrApproveLabel;
    private javax.swing.JPanel rejectOrApprovePanel;
    private javax.swing.JScrollPane scrollPaneMessage;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel statusField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel subjectField;
    private javax.swing.JLabel subjectLabel;
    private javax.swing.JDialog successDialog;
    private javax.swing.JLabel successDialogMessage;
    private javax.swing.JButton successDialogOkBtn;
    private javax.swing.JPanel successPanel;
    private javax.swing.JScrollPane tablePane;
    // End of variables declaration//GEN-END:variables
}
