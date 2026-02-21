/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.GUI.rightsidepanels;

import com.motorph.payrollsystem.app.AppContext;
import com.motorph.payrollsystem.app.SessionManager;
import com.motorph.payrollsystem.domain.leave.LeaveRequest;
import com.motorph.payrollsystem.domain.leave.LeaveStatus;
import com.motorph.payrollsystem.service.LeaveService;
import com.motorph.payrollsystem.utility.ThemeColor;
import java.awt.Color;
import java.awt.Component;
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
        this.statusCell = statusCellRenderer();
        this.currentHistory = List.of();
       
        initComponents();
        loadLeaveHistory();
        

    }
    
    private void loadLeaveHistory() {
        LeaveService leaveService = appContext.getLeaveService();
        SessionManager currSession = appContext.getSessionManager();
        String employeeNo = currSession
                .getCurrentEmployee()
                .getEmployeeNo();
        
        if (employeeNo == null || employeeNo.isBlank()) {
            setSummaryText(0, 0, 0, 0);
            clearTable();
            return;
        }
        
        try {
        
            List<LeaveRequest> history = leaveService.getLeaveHistory(employeeNo);
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

    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) requestTable.getModel();
        model.setRowCount(0);
    }
    
    private void fillTable(List<LeaveRequest> list) {
        DefaultTableModel model = (DefaultTableModel) requestTable.getModel();
        model.setRowCount(0);
        
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
    
    private DefaultTableCellRenderer statusCellRenderer() {
        return new DefaultTableCellRenderer() {
            
            
            @Override
            public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
                setHorizontalAlignment(javax.swing.JLabel.CENTER);
                Component cell = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Reset default
                cell.setBackground(Color.WHITE);

                if (value != null) {
                    String status = value.toString();

                    switch (status) {
                        case "APPROVED":
                            cell.setBackground(ThemeColor.lightGreen());
                            break;
                        case "PENDING":
                            cell.setBackground(ThemeColor.lightYellow());
                            break;
                        case "REJECTED":
                            cell.setBackground(ThemeColor.lightRed());
                            break;
                        default:
                            cell.setBackground(Color.WHITE);
                    }
                }
                
                

                return cell;
            }
        };
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dashboardLabel = new javax.swing.JLabel();
        decorLine = new javax.swing.JPanel();
        newRequestBtn = new javax.swing.JButton();
        requestLabel = new javax.swing.JLabel();
        scrollPaneTable = new javax.swing.JScrollPane();
        requestTable = new javax.swing.JTable();
        counts = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(760, 640));

        dashboardLabel.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        dashboardLabel.setText("LEAVE REQUESTS");

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

        newRequestBtn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        newRequestBtn.setText("+   New Request");
        newRequestBtn.addActionListener(this::newRequestBtnActionPerformed);

        requestLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        requestLabel.setText("Request History");

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
        requestTable.setShowHorizontalLines(true);
        requestTable.getTableHeader().setReorderingAllowed(false);
        scrollPaneTable.setViewportView(requestTable);
        if (requestTable.getColumnModel().getColumnCount() > 0) {
            requestTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            requestTable.getColumnModel().getColumn(1).setPreferredWidth(160);
            requestTable.getColumnModel().getColumn(2).setPreferredWidth(40);
            requestTable.getColumnModel().getColumn(3).setPreferredWidth(40);
            requestTable.getColumnModel().getColumn(4).setCellRenderer(this.statusCell);
        }

        counts.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        counts.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        counts.setText("All: 15");

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
    }//GEN-LAST:event_newRequestBtnActionPerformed
    
    private DefaultTableCellRenderer statusCell;
    private List<LeaveRequest> currentHistory;
    private AppContext appContext;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel counts;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JPanel decorLine;
    private javax.swing.JButton newRequestBtn;
    private javax.swing.JLabel requestLabel;
    private javax.swing.JTable requestTable;
    private javax.swing.JScrollPane scrollPaneTable;
    // End of variables declaration//GEN-END:variables
}
