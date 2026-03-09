/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.gui.rightsidepanels;


import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.config.SessionManager;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.model.leave.LeaveRequest;
import com.motorph.payrollsystem.model.leave.LeaveStatus;
import com.motorph.payrollsystem.service.LeaveService;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.FontsAndFormats;
import com.motorph.payrollsystem.utility.ThemeColor;
import java.awt.Color;
import java.awt.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author djjus
 */
public class LeavePanel extends javax.swing.JPanel {

    /**
     * Creates new form HomePanel
     * @param appContext the current logged in user/employee
     */
    public LeavePanel(AppContext appContext) {
        this.appContext = appContext;
        this.currentHistory = List.of();
       
        initComponents();
        loadLeaveHistory();
        hookRowDoubleClick();
        
        customDatePicker(startDatePicker);
        customDatePicker(endDatePicker);
        setupStartEndLogic();

    }
    
    private void loadLeaveHistory() {
        LeaveService leaveService = appContext.getLeaveService();
        SessionManager currSession = appContext.getSessionManager();
        String employeeNo = currSession
                .getCurrentEmployee()
                .getEmployeeNo();
        
        if (employeeNo == null || employeeNo.isBlank()) {
            setSummaryText(0, 0, 0, 0);
            clearTable(requestTable);
            return;
        }
        
        try {
        
            List<LeaveRequest> history = leaveService.getEmployeeLeaveHistory(employeeNo);
            currentHistory = history;
           
            fillTable(history);
            
            Map<LeaveStatus, Integer> countCard = leaveService.getStatusCounts(employeeNo);
            
            int all = history.size();
            int pending = countCard.getOrDefault(LeaveStatus.PENDING, 0);
            int approved = countCard.getOrDefault(LeaveStatus.APPROVED, 0);
            int rejected = countCard.getOrDefault(LeaveStatus.REJECTED, 0);
            
            setSummaryText(all, pending, approved, rejected);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading leave data");
        }
        
        customizeCellColumns();
    }
    
    private void customizeCellColumns() {
        requestTable.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 12));
    }

    private DefaultTableModel clearTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        return model;
    }
    
    private void fillTable(List<LeaveRequest> list) {
        DefaultTableModel model = (DefaultTableModel) clearTable(requestTable);
        
        
        for (LeaveRequest history : list) {
            model.addRow(new Object[]{
                history.getFiledDate(),
                history.getSubject(),
                history.getLeaveStart(),
                history.getLeaveEnd(),
                history.getStatus()
            });
        }
    }
    
    private void setSummaryText(int all, int pending, int approved, int rejected) {
        this.counts.setText("All: " + all +
                "  Pending: " + pending +
                "  Approved: " + approved + 
                "  Rejected: " + rejected
                );
    }
    
    private void hookRowDoubleClick() {
        requestTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() != 2) return;
                
                int row = requestTable.getSelectedRow();
                if (row < 0 || row >= currentHistory.size()) return;
                
                LeaveRequest req = currentHistory.get(row);
                showLeaveDetails(req);
            }
        }) ;
    }
    
    private void showLeaveDetails(LeaveRequest req) {
        String reviewersName;
        if (!(req.getApprovedById() == null)) {
            try {
                Employee reviewer = appContext.getEmployeeService().findByEmployeeNo(req.getApprovedById());
                reviewersName = reviewer.getLastNameInitial();
            } catch (Exception ex) {
                reviewersName = "-";
            }
        } else {
            reviewersName = "-";
        }
        
        
        subjectText.setText(req.getSubject());
        filedText.setText(Dates.shortFullDate(req.getFiledDate()));
        leaveText.setText(Dates.shortFullDate(req.getLeaveStart()) + " to " + Dates.shortFullDate(req.getLeaveEnd()));
        
        dynamicStatusText(statusText, req.getStatus());
        approvedText.setText(reviewersName);
        messageTextArea.setText(req.getMessage());
        
//        leaveDetailsDialog.setSize(450, 880);
        leaveDetailsDialog.pack();
        leaveDetailsDialog.setResizable(false);
        leaveDetailsDialog.setLocationRelativeTo(this);
        leaveDetailsDialog.setTitle("Leave Request - " + req.getFiledDate());
        
        leaveDetailsDialog.setVisible(true);
    }
    
    private void dynamicStatusText(javax.swing.JLabel statusLabel, LeaveStatus status) {
        java.awt.Color color;
        
        switch (status.name()) {
            case "APPROVED":
                color = ThemeColor.textGreen();
                break;
            case "PENDING":
                color = ThemeColor.textYellow();
                break;
            case "REJECTED":
                color = ThemeColor.textRed();
                break;
            default:
                color = Color.PINK;
        }
        statusLabel.setText(status.name());
        statusLabel.setForeground(color);
        statusLabel.setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 12));
        
    }
    
    private void addNewRequest() {
        newRequestDialog.pack();
        newRequestDialog.setResizable(false);
        newRequestDialog.setLocationRelativeTo(this);
        newRequestDialog.setTitle("Leave Request Form");
        
        newRequestDialog.setVisible(true);
    }
    
    private void customDatePicker(com.github.lgooddatepicker.components.DatePicker picker) {

        picker.getComponentDateTextField().setEditable(false);
        picker.getComponentDateTextField().setFocusable(false);
    }
    
    private void setupStartEndLogic() {
    // initial limits (no past dates)
        startDatePicker.getSettings().setDateRangeLimits(LocalDate.now(), null);
        endDatePicker.getSettings().setDateRangeLimits(LocalDate.now(), null);

        startDatePicker.addDateChangeListener(e -> {
            if (syncing) return;
            syncing = true;
            try {
                LocalDate today = LocalDate.now();
                LocalDate start = startDatePicker.getDate();
                LocalDate end = endDatePicker.getDate();

                // start is always at least today
                startDatePicker.getSettings().setDateRangeLimits(today, null);

                if (start != null) {
                    // end cannot be before start
                    endDatePicker.getSettings().setDateRangeLimits(start, null);

                    // if current end is invalid, clear it
                    if (end != null && end.isBefore(start)) {
                        endDatePicker.clear();
                    }
                } else {
                    // if start cleared, end goes back to today..null
                    endDatePicker.getSettings().setDateRangeLimits(today, null);
                }
            } finally {
                syncing = false;
            }
        });

        endDatePicker.addDateChangeListener(e -> {
            if (syncing) return;
            syncing = true;
            try {
                LocalDate today = LocalDate.now();
                LocalDate start = startDatePicker.getDate();
                LocalDate end = endDatePicker.getDate();

                // end is always at least today
                endDatePicker.getSettings().setDateRangeLimits(today, null);

                if (end != null) {
                    // start must be between today and end
                    startDatePicker.getSettings().setDateRangeLimits(today, end);

                    // if current start is invalid, clear it
                    if (start != null && start.isAfter(end)) {
                        startDatePicker.clear();
                    }
                } else {
                    // if end cleared, start goes back to today..null
                    startDatePicker.getSettings().setDateRangeLimits(today, null);
                }
            } finally {
                syncing = false;
            }
        });
    }
    
    private void cancelRequest() {
        if (!isDirty()) {
            this.newRequestDialog.dispose();
            return;
        } 
        
        cancelConfirmDialog.pack();
        cancelConfirmDialog.setResizable(false);
        cancelConfirmDialog.setLocationRelativeTo(this.newRequestDialog);
        cancelConfirmDialog.setTitle("Cancel Request");

        cancelConfirmDialog.setVisible(true);  

    }
    
    private boolean isDirty() {
        String subject = subjectTextField.getText();
        String message = newRequestTextArea.getText();
        
        boolean subjectDirty = subject != null && !subject.trim().isEmpty();
        boolean messageDirty = message != null && !message.trim().isEmpty();
        boolean startDirty = startDatePicker.getDate() != null;
        boolean endDirty = endDatePicker.getDate() != null;
        
        
        return subjectDirty || messageDirty || startDirty || endDirty;
        
    }
    
    private void resetRequestDialog() {
        subjectTextField.setText("");
        newRequestTextArea.setText("");
        startDatePicker.setDate(null);
        endDatePicker.setDate(null);
        
    }
    
    private void onSubmit() {
        try {
            String employeeNo = appContext.getSessionManager().getCurrentEmployee().getEmployeeNo();
            String subject = subjectTextField.getText();
            LocalDate start = startDatePicker.getDate();
            LocalDate end = endDatePicker.getDate();
            String message = newRequestTextArea.getText();
            
            appContext.getLeaveService().addNewRequest(employeeNo, subject, start, end, message);
            
            //refresh table + summary
            loadLeaveHistory();
            
            //close new request dialog
            resetRequestDialog();
            newRequestDialog.dispose();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this.newRequestDialog, ex.getMessage(), "Validation", JOptionPane.WARNING_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.newRequestDialog, "Failed to submit request.\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leaveDetailsDialog = new javax.swing.JDialog();
        subjectLabel = new javax.swing.JLabel();
        filedLabel = new javax.swing.JLabel();
        leaveLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        approveLabel = new javax.swing.JLabel();
        messageLabel = new javax.swing.JLabel();
        subjectText = new javax.swing.JLabel();
        filedText = new javax.swing.JLabel();
        leaveText = new javax.swing.JLabel();
        statusText = new javax.swing.JLabel();
        approvedText = new javax.swing.JLabel();
        scrollPaneMessage = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        leaveDetailsCloseBtn = new javax.swing.JButton();
        newRequestDialog = new javax.swing.JDialog();
        subjectLabel1 = new javax.swing.JLabel();
        filedLabel1 = new javax.swing.JLabel();
        leaveLabel1 = new javax.swing.JLabel();
        messageLabel1 = new javax.swing.JLabel();
        subjectTextField = new javax.swing.JTextField();
        scrollPaneNewRequest = new javax.swing.JScrollPane();
        newRequestTextArea = new javax.swing.JTextArea();
        cancelRequestBtn = new javax.swing.JButton();
        submitRequestBtn = new javax.swing.JButton();
        endDatePicker = new com.github.lgooddatepicker.components.DatePicker();
        startDatePicker = new com.github.lgooddatepicker.components.DatePicker();
        cancelConfirmDialog = new javax.swing.JDialog(newRequestDialog, true);
        cancelConfirmPanel = new javax.swing.JPanel();
        cancelConfrimLabel = new javax.swing.JLabel();
        cancelBtnConfirm = new javax.swing.JButton();
        confirmBtnConfirm = new javax.swing.JButton();
        dashboardLabel = new javax.swing.JLabel();
        decorLine = new javax.swing.JPanel();
        newRequestBtn = new javax.swing.JButton();
        requestLabel = new javax.swing.JLabel();
        scrollPaneTable = new javax.swing.JScrollPane();
        requestTable = new javax.swing.JTable();
        counts = new javax.swing.JLabel();

        leaveDetailsDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        leaveDetailsDialog.setAlwaysOnTop(true);
        leaveDetailsDialog.setBackground(new java.awt.Color(255, 255, 255));
        leaveDetailsDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        leaveDetailsDialog.setIconImage(null);
        leaveDetailsDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        leaveDetailsDialog.setName("leaveDetailsDialog"); // NOI18N
        leaveDetailsDialog.setResizable(false);

        subjectLabel.setText("Subject :");
        subjectLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        filedLabel.setText("Filed Date :");
        filedLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        leaveLabel.setText("Leave :");
        leaveLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        statusLabel.setText("Status :");
        statusLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        approveLabel.setText("Processed by :");
        approveLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        messageLabel.setText("Message :");
        messageLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        subjectText.setText("Subject :");
        subjectText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        filedText.setText("Filed Date :");
        filedText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        leaveText.setText("Leave");
        leaveText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        statusText.setText("Status");
        statusText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        approvedText.setText("Processed");
        approvedText.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        scrollPaneMessage.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        messageTextArea.setEditable(false);
        messageTextArea.setColumns(20);
        messageTextArea.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        messageTextArea.setLineWrap(true);
        messageTextArea.setRows(5);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setFocusable(false);
        scrollPaneMessage.setViewportView(messageTextArea);

        leaveDetailsCloseBtn.setText("CLOSE");
        leaveDetailsCloseBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        leaveDetailsCloseBtn.addActionListener(this::leaveDetailsCloseBtnActionPerformed);

        javax.swing.GroupLayout leaveDetailsDialogLayout = new javax.swing.GroupLayout(leaveDetailsDialog.getContentPane());
        leaveDetailsDialog.getContentPane().setLayout(leaveDetailsDialogLayout);
        leaveDetailsDialogLayout.setHorizontalGroup(
            leaveDetailsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leaveDetailsDialogLayout.createSequentialGroup()
                .addGroup(leaveDetailsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leaveDetailsDialogLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(leaveDetailsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPaneMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(leaveDetailsDialogLayout.createSequentialGroup()
                                .addGroup(leaveDetailsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(filedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(subjectLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(messageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(approveLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                                    .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(leaveLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, 0)
                                .addGroup(leaveDetailsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(filedText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(leaveText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(statusText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(approvedText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                    .addComponent(subjectText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(leaveDetailsDialogLayout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addComponent(leaveDetailsCloseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        leaveDetailsDialogLayout.setVerticalGroup(
            leaveDetailsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leaveDetailsDialogLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(leaveDetailsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(leaveDetailsDialogLayout.createSequentialGroup()
                        .addComponent(subjectText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filedText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(leaveText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statusText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(approvedText)
                        .addGap(31, 31, 31))
                    .addGroup(leaveDetailsDialogLayout.createSequentialGroup()
                        .addComponent(subjectLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filedLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(leaveLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statusLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(approveLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(messageLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(leaveDetailsCloseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        newRequestDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        newRequestDialog.setAlwaysOnTop(true);
        newRequestDialog.setBackground(new java.awt.Color(255, 255, 255));
        newRequestDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        newRequestDialog.setIconImage(null);
        newRequestDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        newRequestDialog.setName("leaveDetailsDialog"); // NOI18N
        newRequestDialog.setResizable(false);
        newRequestDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                newRequestDialogWindowClosing(evt);
            }
        });

        subjectLabel1.setText("Subject :");
        subjectLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        filedLabel1.setText("Start Date :");
        filedLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        leaveLabel1.setText("End Date :");
        leaveLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        messageLabel1.setText("Message :");
        messageLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N

        subjectTextField.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        subjectTextField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        newRequestTextArea.setColumns(20);
        newRequestTextArea.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        newRequestTextArea.setRows(5);
        scrollPaneNewRequest.setViewportView(newRequestTextArea);

        cancelRequestBtn.setText("Cancel");
        cancelRequestBtn.setBackground(ThemeColor.lightRed());
        cancelRequestBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        cancelRequestBtn.addActionListener(this::cancelRequestBtnActionPerformed);

        submitRequestBtn.setText("Submit");
        submitRequestBtn.setBackground(ThemeColor.lightBlue());
        submitRequestBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        submitRequestBtn.addActionListener(this::submitRequestBtnActionPerformed);

        endDatePicker.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        startDatePicker.setFocusable(false);
        startDatePicker.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        javax.swing.GroupLayout newRequestDialogLayout = new javax.swing.GroupLayout(newRequestDialog.getContentPane());
        newRequestDialog.getContentPane().setLayout(newRequestDialogLayout);
        newRequestDialogLayout.setHorizontalGroup(
            newRequestDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newRequestDialogLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(newRequestDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrollPaneNewRequest)
                    .addGroup(newRequestDialogLayout.createSequentialGroup()
                        .addGroup(newRequestDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(leaveLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(subjectLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(filedLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(messageLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(newRequestDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(startDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(endDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(subjectTextField))
                        .addGap(46, 46, 46)))
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newRequestDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelRequestBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(submitRequestBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );
        newRequestDialogLayout.setVerticalGroup(
            newRequestDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newRequestDialogLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(newRequestDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(subjectTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(subjectLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(newRequestDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filedLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(newRequestDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(leaveLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(messageLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneNewRequest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(newRequestDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelRequestBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(submitRequestBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        cancelConfirmPanel.setBackground(new java.awt.Color(255, 255, 255));

        cancelConfrimLabel.setText("You have unsaved changes. Discard them?");
        cancelConfrimLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        cancelBtnConfirm.setText("Cancel");
        cancelBtnConfirm.setBackground(ThemeColor.lightRed());
        cancelBtnConfirm.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        cancelBtnConfirm.addActionListener(this::cancelBtnConfirmActionPerformed);

        confirmBtnConfirm.setText("Confirm");
        confirmBtnConfirm.setBackground(ThemeColor.lightGreen());
        confirmBtnConfirm.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        confirmBtnConfirm.addActionListener(this::confirmBtnConfirmActionPerformed);

        javax.swing.GroupLayout cancelConfirmPanelLayout = new javax.swing.GroupLayout(cancelConfirmPanel);
        cancelConfirmPanel.setLayout(cancelConfirmPanelLayout);
        cancelConfirmPanelLayout.setHorizontalGroup(
            cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cancelConfirmPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(cancelConfirmPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cancelConfrimLabel)
                    .addGroup(cancelConfirmPanelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(cancelBtnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(confirmBtnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
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

        javax.swing.GroupLayout cancelConfirmDialogLayout = new javax.swing.GroupLayout(cancelConfirmDialog.getContentPane());
        cancelConfirmDialog.getContentPane().setLayout(cancelConfirmDialogLayout);
        cancelConfirmDialogLayout.setHorizontalGroup(
            cancelConfirmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cancelConfirmPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cancelConfirmDialogLayout.setVerticalGroup(
            cancelConfirmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cancelConfirmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(760, 640));

        dashboardLabel.setText("LEAVE REQUESTS");
        dashboardLabel.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N

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

        newRequestBtn.setText("+   New Request");
        newRequestBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        newRequestBtn.addActionListener(this::newRequestBtnActionPerformed);

        requestLabel.setText("Request History");
        requestLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N

        requestTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        requestTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Filed Date", "Subject", "From", "To", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        requestTable.setToolTipText("");
        requestTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        requestTable.setFocusable(false);
        requestTable.setRowHeight(28);
        requestTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        requestTable.getTableHeader().setReorderingAllowed(false);
        scrollPaneTable.setViewportView(requestTable);
        if (requestTable.getColumnModel().getColumnCount() > 0) {
            requestTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            requestTable.getColumnModel().getColumn(1).setPreferredWidth(160);
            requestTable.getColumnModel().getColumn(2).setPreferredWidth(40);
            requestTable.getColumnModel().getColumn(3).setPreferredWidth(40);
            requestTable.getColumnModel().getColumn(4).setCellRenderer(FontsAndFormats.statusCellRenderer());
        }

        counts.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        counts.setText("All: 15");
        counts.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(newRequestBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dashboardLabel)
                        .addComponent(scrollPaneTable, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(requestLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(counts, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(dashboardLabel)
                .addGap(6, 6, 6)
                .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(newRequestBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(requestLabel)
                    .addComponent(counts))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneTable, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void newRequestBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newRequestBtnActionPerformed
        // TODO add your handling code here:
        addNewRequest();
    }//GEN-LAST:event_newRequestBtnActionPerformed

    private void cancelRequestBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelRequestBtnActionPerformed
        // TODO add your handling code here:
        cancelRequest();
    }//GEN-LAST:event_cancelRequestBtnActionPerformed

    private void submitRequestBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitRequestBtnActionPerformed
        // TODO add your handling code here:
        onSubmit();
    }//GEN-LAST:event_submitRequestBtnActionPerformed

    private void leaveDetailsCloseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leaveDetailsCloseBtnActionPerformed
        // TODO add your handling code here:
        leaveDetailsDialog.dispose();
    }//GEN-LAST:event_leaveDetailsCloseBtnActionPerformed

    private void cancelBtnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnConfirmActionPerformed
        // TODO add your handling code here:
        cancelConfirmDialog.dispose();
    }//GEN-LAST:event_cancelBtnConfirmActionPerformed

    private void confirmBtnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmBtnConfirmActionPerformed
        // TODO add your handling code here:
        cancelConfirmDialog.dispose();
        resetRequestDialog();
        newRequestDialog.dispose();
        
    }//GEN-LAST:event_confirmBtnConfirmActionPerformed

    private void newRequestDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_newRequestDialogWindowClosing
        // TODO add your handling code here:
        cancelRequest();
    }//GEN-LAST:event_newRequestDialogWindowClosing
    
    private boolean syncing = false;
    private List<LeaveRequest> currentHistory;
    private AppContext appContext;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel approveLabel;
    private javax.swing.JLabel approvedText;
    private javax.swing.JButton cancelBtnConfirm;
    private javax.swing.JDialog cancelConfirmDialog;
    private javax.swing.JPanel cancelConfirmPanel;
    private javax.swing.JLabel cancelConfrimLabel;
    private javax.swing.JButton cancelRequestBtn;
    private javax.swing.JButton confirmBtnConfirm;
    private javax.swing.JLabel counts;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JPanel decorLine;
    private com.github.lgooddatepicker.components.DatePicker endDatePicker;
    private javax.swing.JLabel filedLabel;
    private javax.swing.JLabel filedLabel1;
    private javax.swing.JLabel filedText;
    private javax.swing.JButton leaveDetailsCloseBtn;
    private javax.swing.JDialog leaveDetailsDialog;
    private javax.swing.JLabel leaveLabel;
    private javax.swing.JLabel leaveLabel1;
    private javax.swing.JLabel leaveText;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JLabel messageLabel1;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JButton newRequestBtn;
    private javax.swing.JDialog newRequestDialog;
    private javax.swing.JTextArea newRequestTextArea;
    private javax.swing.JLabel requestLabel;
    private javax.swing.JTable requestTable;
    private javax.swing.JScrollPane scrollPaneMessage;
    private javax.swing.JScrollPane scrollPaneNewRequest;
    private javax.swing.JScrollPane scrollPaneTable;
    private com.github.lgooddatepicker.components.DatePicker startDatePicker;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel statusText;
    private javax.swing.JLabel subjectLabel;
    private javax.swing.JLabel subjectLabel1;
    private javax.swing.JLabel subjectText;
    private javax.swing.JTextField subjectTextField;
    private javax.swing.JButton submitRequestBtn;
    // End of variables declaration//GEN-END:variables
}
