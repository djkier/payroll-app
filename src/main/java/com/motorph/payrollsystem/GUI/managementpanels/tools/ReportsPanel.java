/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.gui.managementpanels.tools;


import com.motorph.payrollsystem.access.AccessPolicy;
import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.gui.managementpanels.tools.reportviewermodal.MainReportViewer;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.model.leave.LeaveRequest;
import com.motorph.payrollsystem.model.report.PayrollReport;
import com.motorph.payrollsystem.model.report.PayrollReportInfo;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.FontsAndFormats;
import java.awt.Image;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import javax.swing.ImageIcon;
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
        this.parentDialog = parentDialog;
        
        this.displayList = new ArrayList<>();
        this.reportList = new ArrayList<>();
        
        initComponents();
        loadPayrollReports();
        loadComboBox();
        hookRowDoubleClick();
        
    }
    
    private void loadPayrollReports() {
        try {
            this.reportList = appContext.getPayrollReportService().getAllGeneratedReports();
            this.displayList = new ArrayList<>(reportList);
            
            fillTable(displayList);
            
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error loading employee data");
        }
        
        customizeCellColumns();
    }
    
    private void customizeCellColumns() {
        reportTable.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 12));
    }
    
    private void fillTable(List<PayrollReportInfo> list) {
        DefaultTableModel model = (DefaultTableModel) clearTable(reportTable);
        
        if (list == null || list.isEmpty()) {
            return;
        }
        
        for (PayrollReportInfo report : list) {
            model.addRow(new Object[]{
                report.getPayrollPeriod().toString(),
                Dates.fullDate(report.getGeneratedAt()),
                report.getGeneratedByName()
            });
        } 
    }
    
    private DefaultTableModel clearTable(javax.swing.JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        return model;
    }
    
    private void hookRowDoubleClick() {
        reportTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() != 2) return;
                
                int viewRow = reportTable.getSelectedRow();
                if (viewRow < 0) return;
                
                int modelRow = reportTable.convertRowIndexToModel(viewRow);
                if (modelRow < 0 || modelRow >= displayList.size()) return;

                showRequestInfo(displayList.get(modelRow));
            }
        });
    }
    
    private void showRequestInfo(PayrollReportInfo reportInfo) {
        try {
            PayrollReport report = appContext.getPayrollReportService().loadPayrollReport(reportInfo);
            
            reportViewerDialog.setContentPane(new MainReportViewer(report, reportViewerDialog));
            dialogOpener(reportViewerDialog, "Payroll Report Viewer");

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Failed to get ",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            ex.printStackTrace();
        }
    }
    
    private void loadComboBox() {
        yearComboBox.removeAllItems();
        try {
            List<Integer> years =
                    appContext.getPayrollReportService().getAllAvailablePayrollYears();

            if (years == null || years.isEmpty()) {
                yearComboBox.setEnabled(false);
                return;
            }
            
            yearComboBox.addItem("ALL");
            
            for (Integer year : years) {
                yearComboBox.addItem(String.valueOf(year));
            }

            yearComboBox.setEnabled(true);
            yearComboBox.setSelectedIndex(0);

        } catch (IllegalStateException | IllegalArgumentException ex) {
            yearComboBox.setEnabled(false);
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error on loading available years",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception ex) {
            yearComboBox.setEnabled(false);
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Error on getting payroll period year",
                    "Payroll Period Year Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
   
    private void dialogOpener(javax.swing.JDialog dialog, String title) {
        dialog.setTitle(title);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentDialog);
        
        dialog.setVisible(true);
    }
    
    private Image viewReportLogo() {
        ImageIcon newIcon = new ImageIcon(getClass().getResource("/images/tab-icon/module-icon/view-report.png"));
        return newIcon.getImage();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        reportViewerDialog = new javax.swing.JDialog(this.parentDialog, true);
        statusGroup = new javax.swing.ButtonGroup();
        reportPane = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        noteLabel = new javax.swing.JLabel();
        yearComboBox = new javax.swing.JComboBox<>();
        noteLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        reportPane1 = new javax.swing.JScrollPane();
        reportTable1 = new javax.swing.JTable();
        noteLabel2 = new javax.swing.JLabel();
        yearComboBox1 = new javax.swing.JComboBox<>();
        noteLabel3 = new javax.swing.JLabel();

        reportViewerDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        reportViewerDialog.setAlwaysOnTop(true);
        reportViewerDialog.setBackground(new java.awt.Color(255, 255, 255));
        reportViewerDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        reportViewerDialog.setIconImage(viewReportLogo());
        reportViewerDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        reportViewerDialog.setName("reportViewerDialog"); // NOI18N
        reportViewerDialog.setResizable(false);

        javax.swing.GroupLayout reportViewerDialogLayout = new javax.swing.GroupLayout(reportViewerDialog.getContentPane());
        reportViewerDialog.getContentPane().setLayout(reportViewerDialogLayout);
        reportViewerDialogLayout.setHorizontalGroup(
            reportViewerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 464, Short.MAX_VALUE)
        );
        reportViewerDialogLayout.setVerticalGroup(
            reportViewerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 423, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(255, 255, 255));

        reportTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        reportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Period", "Generated Date", "Generated By"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        reportTable.setRowHeight(24);
        reportTable.getTableHeader().setReorderingAllowed(false);
        reportPane.setViewportView(reportTable);
        if (reportTable.getColumnModel().getColumnCount() > 0) {
            reportTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        }

        noteLabel.setFont(new java.awt.Font("Poppins", 2, 12)); // NOI18N
        noteLabel.setText("Double-click a row to view report.");

        yearComboBox.setBackground(new java.awt.Color(255, 255, 255));
        yearComboBox.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        yearComboBox.addActionListener(this::yearComboBoxActionPerformed);

        noteLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        noteLabel1.setText("Filter by Year");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        reportTable1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        reportTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Period", "Generated Date", "Generated By"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        reportTable1.setRowHeight(24);
        reportTable1.getTableHeader().setReorderingAllowed(false);
        reportPane1.setViewportView(reportTable1);
        if (reportTable1.getColumnModel().getColumnCount() > 0) {
            reportTable1.getColumnModel().getColumn(0).setPreferredWidth(120);
        }

        noteLabel2.setFont(new java.awt.Font("Poppins", 2, 12)); // NOI18N
        noteLabel2.setText("Double-click a row to view report.");

        yearComboBox1.setBackground(new java.awt.Color(255, 255, 255));
        yearComboBox1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        yearComboBox1.addActionListener(this::yearComboBox1ActionPerformed);

        noteLabel3.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        noteLabel3.setText("Filter by Year");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(noteLabel3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(yearComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(noteLabel2))
                            .addComponent(reportPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
                        .addGap(25, 25, 25))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noteLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(yearComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(noteLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reportPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(noteLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(noteLabel))
                            .addComponent(reportPane, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
                        .addGap(25, 25, 25))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noteLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(noteLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reportPane, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void yearComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearComboBoxActionPerformed
        // TODO add your handling code here:
        if (yearComboBox.getSelectedIndex() == 0) {
            loadPayrollReports();
            return;
        } 
        
        int year = Integer.valueOf((String) yearComboBox.getSelectedItem());
        List<PayrollReportInfo> newList = new ArrayList<>();
        for (PayrollReportInfo info : reportList) {
            if (info.getPayrollPeriod().getStartDate().getYear() == year) {
                newList.add(info);
            }
        }
        
        this.displayList = newList;
        fillTable(displayList);
    }//GEN-LAST:event_yearComboBoxActionPerformed

    private void yearComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_yearComboBox1ActionPerformed

    private AppContext appContext;
    private javax.swing.JDialog parentDialog;
    private List<PayrollReportInfo> displayList;
    private List<PayrollReportInfo> reportList;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel noteLabel;
    private javax.swing.JLabel noteLabel1;
    private javax.swing.JLabel noteLabel2;
    private javax.swing.JLabel noteLabel3;
    private javax.swing.JScrollPane reportPane;
    private javax.swing.JScrollPane reportPane1;
    private javax.swing.JTable reportTable;
    private javax.swing.JTable reportTable1;
    private javax.swing.JDialog reportViewerDialog;
    private javax.swing.ButtonGroup statusGroup;
    private javax.swing.JComboBox<String> yearComboBox;
    private javax.swing.JComboBox<String> yearComboBox1;
    // End of variables declaration//GEN-END:variables
}
