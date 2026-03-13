/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.motorph.payrollsystem.gui.rightsidepanels;

import com.motorph.payrollsystem.config.AppContext;
import com.motorph.payrollsystem.config.SessionManager;
import com.motorph.payrollsystem.model.attendance.AttendanceRecord;
import com.motorph.payrollsystem.model.attendance.AttendanceState;
import com.motorph.payrollsystem.model.employee.Employee;
import com.motorph.payrollsystem.utility.Dates;
import com.motorph.payrollsystem.utility.ThemeColor;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author djjus
 */
public class HomePanel extends javax.swing.JPanel {

    /**
     * Creates new form HomePanel
     * @param appContext the current logged in user/employee
     */
    public HomePanel(
            AppContext appContext,
            javax.swing.JFrame parentFrame) {
        this.appContext = appContext;
        this.parentFrame = parentFrame;
        this.currentEmployee = appContext.getSessionManager().getCurrentEmployee();
        
        initComponents();
        defaultLabels();
        initAttendancePanel();
    }
    
    private void defaultLabels() {
        SessionManager currSession = appContext.getSessionManager();
        LocalDateTime loginTime = currSession.getLoginTime();
        
        nameLabel.setText(currSession.getCurrentEmployee().getFullName());
        loggedInTimeLabel.setText(Dates.formattedTime(loginTime));
    }
    
    private void initAttendancePanel(){
        startClock();
        refreshAttendanceState();
    }
    
    private void startClock() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");

        dateTodayLabel.setText(dateFormat.format(new Date()));
        timerLabel.setText(timeFormat.format(new Date()));

        Timer timer = new Timer(1000, e -> {
            timerLabel.setText(timeFormat.format(new Date()));
        });

        timer.start();
    }
    
    private void refreshAttendanceState() {
        try {
            AttendanceState state = appContext.getAttendanceService().getAttendanceState(currentEmployee);

            timeInBtn.setEnabled(state.canTimeIn());
            timeOutBtn.setEnabled(state.canTimeOut());

            if (state.isCurrentlyTimedIn()) {
                statusBarPanel.setBackground(ThemeColor.timeIn());
                statusLabel.setText("Currently Clocked In");
            } else {
                statusBarPanel.setBackground(ThemeColor.timeOut());
                statusLabel.setText("Currently Clocked Out");
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Attendance Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
    
    private void onTimeIn() {
        try {
            appContext.getAttendanceService().timeIn(currentEmployee);
            refreshAttendanceState();

        } catch (IllegalStateException e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Time In Error",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to save time in.",
                    "Attendance Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
    
    private void onTimeOut() {
        try {
            Employee currentEmployee = appContext.getSessionManager().getCurrentEmployee();
            appContext.getAttendanceService().timeOut(currentEmployee);
            refreshAttendanceState();

        } catch (IllegalStateException e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Time Out Error",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Failed to save time out.",
                    "Attendance Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }
    
    //Attendance history table
    private void loadAttendance() {
        try {
            List<AttendanceRecord> records = appContext.getAttendanceService().getAttendanceHistoryLatestFirst(currentEmployee);

            //should use tableEmployeeList
            fillTable(records);
            
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error loading attendance record");
        }
        
        customizeCellColumns();
    }
    
    private void customizeCellColumns() {
        historyTable.getTableHeader().setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 12));
    }
    
    private void fillTable(List<AttendanceRecord> records) {
        DefaultTableModel model = (DefaultTableModel) clearTable(historyTable);
        
        for (AttendanceRecord record : records) {
            model.addRow(new Object[]{
                record.getEmployeeNo(),
                record.getDate(),
                record.getTimeIn(),
                record.getTimeOut()
            });
        }
    }
    
    private DefaultTableModel clearTable(javax.swing.JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        return model;
    }
    
    private DefaultTableCellRenderer timeRenderer() {
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

                return cell;
            }
        };
    }
    
    private Image attendanceLogo() {
        ImageIcon newIcon = new ImageIcon(getClass().getResource("/images/tab-icon/attendance.png"));
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

        timeOutDialog = new javax.swing.JDialog(this.parentFrame, true);
        timeOutDialogPanel = new javax.swing.JPanel();
        timeOutDialogLabel = new javax.swing.JLabel();
        timeOutDialogCancelBtn = new javax.swing.JButton();
        timeOutDialogConfirmBtn = new javax.swing.JButton();
        historyDialog = new javax.swing.JDialog(this.parentFrame, true);
        historyPanel = new javax.swing.JPanel();
        historyHeader = new javax.swing.JLabel();
        tablePane = new javax.swing.JScrollPane();
        historyTable = new javax.swing.JTable();
        welcomeLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        preTimeLabel = new javax.swing.JLabel();
        loggedInTimeLabel = new javax.swing.JLabel();
        dashboardLabel = new javax.swing.JLabel();
        decorLine = new javax.swing.JPanel();
        attendanceTrackerPanel = new javax.swing.JPanel();
        attendanceTrackerLabel = new javax.swing.JLabel();
        statusBarPanel = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        timerLabel = new javax.swing.JLabel();
        dateTodayLabel = new javax.swing.JLabel();
        currentTimeLabel = new javax.swing.JLabel();
        timeInBtn = new javax.swing.JButton();
        timeOutBtn = new javax.swing.JButton();
        attendanceBtn = new javax.swing.JButton();

        timeOutDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        timeOutDialog.setAlwaysOnTop(true);
        timeOutDialog.setIconImage(attendanceLogo());
        timeOutDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                timeOutDialogWindowClosing(evt);
            }
        });

        timeOutDialogPanel.setBackground(new java.awt.Color(255, 255, 255));

        timeOutDialogLabel.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        timeOutDialogLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timeOutDialogLabel.setText("Are you sure you want to time out now?");

        timeOutDialogCancelBtn.setBackground(ThemeColor.lightRed());
        timeOutDialogCancelBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        timeOutDialogCancelBtn.setText("Cancel");
        timeOutDialogCancelBtn.setFocusPainted(false);
        timeOutDialogCancelBtn.addActionListener(this::timeOutDialogCancelBtnActionPerformed);

        timeOutDialogConfirmBtn.setBackground(ThemeColor.lightGreen());
        timeOutDialogConfirmBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        timeOutDialogConfirmBtn.setText("Confirm");
        timeOutDialogConfirmBtn.setFocusPainted(false);
        timeOutDialogConfirmBtn.addActionListener(this::timeOutDialogConfirmBtnActionPerformed);

        javax.swing.GroupLayout timeOutDialogPanelLayout = new javax.swing.GroupLayout(timeOutDialogPanel);
        timeOutDialogPanel.setLayout(timeOutDialogPanelLayout);
        timeOutDialogPanelLayout.setHorizontalGroup(
            timeOutDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timeOutDialogPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(timeOutDialogCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(timeOutDialogConfirmBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
            .addComponent(timeOutDialogLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        timeOutDialogPanelLayout.setVerticalGroup(
            timeOutDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, timeOutDialogPanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(timeOutDialogLabel)
                .addGap(18, 18, 18)
                .addGroup(timeOutDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeOutDialogCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeOutDialogConfirmBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout timeOutDialogLayout = new javax.swing.GroupLayout(timeOutDialog.getContentPane());
        timeOutDialog.getContentPane().setLayout(timeOutDialogLayout);
        timeOutDialogLayout.setHorizontalGroup(
            timeOutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(timeOutDialogPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        timeOutDialogLayout.setVerticalGroup(
            timeOutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(timeOutDialogPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        historyDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        historyDialog.setIconImage(attendanceLogo());

        historyPanel.setBackground(new java.awt.Color(255, 255, 255));

        historyHeader.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        historyHeader.setText("Attendance History");

        historyTable.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        historyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Employee No.", "Date", "Time In", "Time Out"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        historyTable.getTableHeader().setReorderingAllowed(false);
        tablePane.setViewportView(historyTable);
        if (historyTable.getColumnModel().getColumnCount() > 0) {
            historyTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            historyTable.getColumnModel().getColumn(0).setCellRenderer(this.timeRenderer());
            historyTable.getColumnModel().getColumn(2).setCellRenderer(this.timeRenderer());
            historyTable.getColumnModel().getColumn(3).setCellRenderer(this.timeRenderer());
        }

        javax.swing.GroupLayout historyPanelLayout = new javax.swing.GroupLayout(historyPanel);
        historyPanel.setLayout(historyPanelLayout);
        historyPanelLayout.setHorizontalGroup(
            historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(historyHeader)
                    .addComponent(tablePane, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        historyPanelLayout.setVerticalGroup(
            historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(historyHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tablePane, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout historyDialogLayout = new javax.swing.GroupLayout(historyDialog.getContentPane());
        historyDialog.getContentPane().setLayout(historyDialogLayout);
        historyDialogLayout.setHorizontalGroup(
            historyDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(historyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        historyDialogLayout.setVerticalGroup(
            historyDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(historyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(760, 640));

        welcomeLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        welcomeLabel.setText("Welcome back,");

        nameLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        nameLabel.setText("John Doe");

        preTimeLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        preTimeLabel.setText("You logged in at:");

        loggedInTimeLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        loggedInTimeLabel.setText("00:00 AM");

        dashboardLabel.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        dashboardLabel.setText("DASHBOARD");

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

        attendanceTrackerPanel.setBackground(new java.awt.Color(255, 255, 255));
        attendanceTrackerPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        attendanceTrackerLabel.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        attendanceTrackerLabel.setText("Attendance Tracker");

        statusBarPanel.setBackground(new java.awt.Color(0, 153, 51));

        statusLabel.setBackground(new java.awt.Color(255, 255, 255));
        statusLabel.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        statusLabel.setForeground(new java.awt.Color(255, 255, 255));
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusLabel.setText("You are currently Time Out");

        javax.swing.GroupLayout statusBarPanelLayout = new javax.swing.GroupLayout(statusBarPanel);
        statusBarPanel.setLayout(statusBarPanelLayout);
        statusBarPanelLayout.setHorizontalGroup(
            statusBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusBarPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        statusBarPanelLayout.setVerticalGroup(
            statusBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        timerLabel.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timerLabel.setText("--:--:-- AM");

        dateTodayLabel.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        dateTodayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dateTodayLabel.setText(" Monday, March 08, 2026");

        currentTimeLabel.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        currentTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentTimeLabel.setText("Current Time");

        timeInBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        timeInBtn.setForeground(new java.awt.Color(0, 0, 0));
        timeInBtn.setText("TIME IN");
        timeInBtn.setFocusPainted(false);
        timeInBtn.addActionListener(this::timeInBtnActionPerformed);

        timeOutBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        timeOutBtn.setForeground(new java.awt.Color(0, 0, 0));
        timeOutBtn.setText("TIME OUT");
        timeOutBtn.setFocusPainted(false);
        timeOutBtn.addActionListener(this::timeOutBtnActionPerformed);

        attendanceBtn.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        attendanceBtn.setText("View Attendance History");
        attendanceBtn.setFocusPainted(false);
        attendanceBtn.addActionListener(this::attendanceBtnActionPerformed);

        javax.swing.GroupLayout attendanceTrackerPanelLayout = new javax.swing.GroupLayout(attendanceTrackerPanel);
        attendanceTrackerPanel.setLayout(attendanceTrackerPanelLayout);
        attendanceTrackerPanelLayout.setHorizontalGroup(
            attendanceTrackerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(timerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(dateTodayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, attendanceTrackerPanelLayout.createSequentialGroup()
                .addGroup(attendanceTrackerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, attendanceTrackerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(attendanceTrackerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(attendanceTrackerPanelLayout.createSequentialGroup()
                                .addComponent(attendanceTrackerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                                .addComponent(statusBarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(currentTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, attendanceTrackerPanelLayout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(timeInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(timeOutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(attendanceBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        attendanceTrackerPanelLayout.setVerticalGroup(
            attendanceTrackerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(attendanceTrackerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(attendanceTrackerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(attendanceTrackerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(statusBarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(currentTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateTodayLabel)
                .addGap(24, 24, 24)
                .addGroup(attendanceTrackerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeOutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(attendanceBtn)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(dashboardLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(preTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loggedInTimeLabel)
                .addGap(47, 47, 47))
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(attendanceTrackerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(welcomeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dashboardLabel)
                    .addComponent(preTimeLabel)
                    .addComponent(loggedInTimeLabel))
                .addGap(6, 6, 6)
                .addComponent(decorLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(welcomeLabel)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(attendanceTrackerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(250, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void timeInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeInBtnActionPerformed
        // TODO add your handling code here:
        onTimeIn();
    }//GEN-LAST:event_timeInBtnActionPerformed

    private void timeOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeOutBtnActionPerformed
        // TODO add your handling code here:
        timeOutDialog.setResizable(false);
        timeOutDialog.setTitle("Confirm Time Out");
        timeOutDialog.pack();
        timeOutDialog.setLocationRelativeTo(null);
        
        timeOutDialog.setVisible(true); 

    }//GEN-LAST:event_timeOutBtnActionPerformed

    private void timeOutDialogCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeOutDialogCancelBtnActionPerformed
        // TODO add your handling code here:
        timeOutDialog.dispose();
    }//GEN-LAST:event_timeOutDialogCancelBtnActionPerformed

    private void timeOutDialogConfirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeOutDialogConfirmBtnActionPerformed
        // TODO add your handling code here:
        onTimeOut();
        timeOutDialog.dispose();
    }//GEN-LAST:event_timeOutDialogConfirmBtnActionPerformed

    private void timeOutDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_timeOutDialogWindowClosing
        // TODO add your handling code here:
        timeOutDialog.dispose();
    }//GEN-LAST:event_timeOutDialogWindowClosing

    private void attendanceBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attendanceBtnActionPerformed
        // TODO add your handling code here:
        historyDialog.setResizable(false);
        historyDialog.setTitle("Attendance Record");
        historyDialog.pack();
        historyDialog.setLocationRelativeTo(null);
        loadAttendance();
        
        historyDialog.setVisible(true); 
    }//GEN-LAST:event_attendanceBtnActionPerformed
    private Employee currentEmployee;
    private AppContext appContext;
    private javax.swing.JFrame parentFrame;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton attendanceBtn;
    private javax.swing.JLabel attendanceTrackerLabel;
    private javax.swing.JPanel attendanceTrackerPanel;
    private javax.swing.JLabel currentTimeLabel;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JLabel dateTodayLabel;
    private javax.swing.JPanel decorLine;
    private javax.swing.JDialog historyDialog;
    private javax.swing.JLabel historyHeader;
    private javax.swing.JPanel historyPanel;
    private javax.swing.JTable historyTable;
    private javax.swing.JLabel loggedInTimeLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel preTimeLabel;
    private javax.swing.JPanel statusBarPanel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JScrollPane tablePane;
    private javax.swing.JButton timeInBtn;
    private javax.swing.JButton timeOutBtn;
    private javax.swing.JDialog timeOutDialog;
    private javax.swing.JButton timeOutDialogCancelBtn;
    private javax.swing.JButton timeOutDialogConfirmBtn;
    private javax.swing.JLabel timeOutDialogLabel;
    private javax.swing.JPanel timeOutDialogPanel;
    private javax.swing.JLabel timerLabel;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
