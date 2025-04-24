// Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.toedter.calendar.JDateChooser; 

public class Group9PatientPL {
    private Group9PatientBL patientBL;
    Color sidebarColor = new Color(43, 78, 140);


    private String patientId;
    public Group9PatientPL(String patientId) {
        this.patientBL = new Group9PatientBL();
        this.patientId = patientId;
        createAndShowGUI();
    }

    private JPanel mainPanel; // for switching content





    public void createAndShowGUI() {

        String patientName = patientBL.getPatientName(patientId);

        JFrame frame = new JFrame("Patient Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 650); // wider and taller

        frame.setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(29, 79, 145)); // Royal blue from image


        sidebar.setPreferredSize(new Dimension(210, 0));

        String[] labels = {"🏠 Home", "📅 Appointments", "📋 Transfer history", "🔁 Make a Transfer", "💊 View Prescriptions", "📤 Logout"};
        

        for (String text : labels) {
            JButton button = new JButton(text);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            button.setForeground(Color.WHITE);
            button.setBackground(sidebarColor);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setFont(new Font("SansSerif", Font.PLAIN, 18));
            button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 10));
            sidebar.add(Box.createVerticalStrut(10));
            sidebar.add(button);

            if (text.contains("Appointments")) {
                button.addActionListener(e -> showAppointmentsPanel());
            } else if (text.contains("Home")) {
                button.addActionListener(e -> showHomePanel(patientBL.getPatientName(patientId)));
            } else if (text.contains("Transfer history")) {
                button.addActionListener(e -> showTransferHistoryPanel());
            } else if (text.contains("Make a Transfer")) {
                button.addActionListener(e -> showTransferPanel());
            } else if (text.contains("View Prescriptions")) {
                button.addActionListener(e -> showPrescriptionsPanel());
            } else if (text.contains("Logout")) {
                button.addActionListener(e -> {
                    frame.dispose(); 
                    SwingUtilities.invokeLater(() -> {
                        new Login().setVisible(true); // Assuming Login is a JFrame
                    });
                });
            } 
            
            
            
            
        }

        // Main panel (content will switch)
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        showHomePanel(patientName);

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void showHomePanel(String patientName) {
        mainPanel.removeAll();
    
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBackground(Color.WHITE);
        homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // Welcome Section
        JPanel greetingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        greetingPanel.setBackground(Color.WHITE);
    
        JLabel avatar = new JLabel("👤");
        avatar.setOpaque(true);
        avatar.setBackground(Color.WHITE);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("SansSerif", Font.BOLD, 18));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(50, 50));
        avatar.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 2));
    
        JLabel greeting = new JLabel("Welcome, " + patientName + "!");
        greeting.setFont(new Font("SansSerif", Font.BOLD, 24));
        greeting.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
    
        greetingPanel.add(avatar);
        greetingPanel.add(greeting);
        homePanel.add(greetingPanel);
    
        homePanel.add(Box.createRigidArea(new Dimension(0, 30)));
    
        // ➤ Grid Panel for 4 Boxes
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        gridPanel.setPreferredSize(new Dimension(1000, 400)); // width × height
        
        gridPanel.setBackground(Color.WHITE);
    
        gridPanel.add(createPatientCard(
            "📅 Appointments", 
            "Register a new patient in the system", 
            "Schedule Appointment", 
            () -> showAppointmentsPanel()
        ));
        
        gridPanel.add(createPatientCard(
            "💊 Prescriptions", 
            "See medicines and diagnoses", 
            "View Prescriptions", 
            () -> showPrescriptionsPanel()
        ));
        
        gridPanel.add(createPatientCard(
            "🔁 Make a Transfer", 
            "Send credits to another patient", 
            "Make a Transfer", 
            () -> showTransferPanel()
        ));
        
        gridPanel.add(createPatientCard(
            "📋 Transfer History", 
            "View your past credit transactions", 
            "View Transfer History", 
            () -> showTransferHistoryPanel()
        ));
        
    
        homePanel.add(gridPanel);
    
        mainPanel.add(homePanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void showPrescriptionsPanel() {
        mainPanel.removeAll();
    
        JPanel prescriptionPanel = new JPanel();
        prescriptionPanel.setLayout(new BoxLayout(prescriptionPanel, BoxLayout.Y_AXIS));
        prescriptionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        prescriptionPanel.setBackground(Color.WHITE);
    
        JLabel title = new JLabel("💊 Your Prescriptions");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        prescriptionPanel.add(title);
    
        List<Object[]> prescriptions = patientBL.getPrescriptionsForPatient(patientId);
    
        if (prescriptions.isEmpty()) {
            prescriptionPanel.add(new JLabel("No prescriptions found."));
        } else {
            String[] columnNames = {"Doctor", "Appointment_Date", "Medicine", "Dosage", "Diagnosis", "Consultation"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // 🔒 Make all cells uneditable
                }
            };
            
            for (Object[] row : prescriptions) {
                model.addRow(row);
            }
    
            JTable table = new JTable(model);
            
            table.setFont(new Font("SansSerif", Font.PLAIN, 14));
            table.setRowHeight(25);
            table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
            enableMultilineCells(table);
            
            // Set preferred column widths
            // Set preferred column widths
            table.getColumnModel().getColumn(0).setPreferredWidth(100); // Doctor
            table.getColumnModel().getColumn(1).setPreferredWidth(150); // Date
            table.getColumnModel().getColumn(2).setPreferredWidth(80); // Medicine
            table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Dosage
            table.getColumnModel().getColumn(4).setPreferredWidth(150); // Diagnosis (less space)
            table.getColumnModel().getColumn(5).setPreferredWidth(300); // Consultation (more space)


    
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(800, 180)); // wider pane
            prescriptionPanel.add(scrollPane);
        }
    
        mainPanel.add(prescriptionPanel, BorderLayout.NORTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    

    private void showAppointmentsPanel() {
        mainPanel.removeAll();
    
        JPanel appointmentPanel = new JPanel();
        appointmentPanel.setLayout(new BoxLayout(appointmentPanel, BoxLayout.Y_AXIS));
        appointmentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        appointmentPanel.setBackground(Color.WHITE);
    
        // 1. Fetch all appointments and classify
        List<Object[]> appointments = patientBL.getAllAppointmentsForPatient(patientId);

        List<Object[]> upcoming = new ArrayList<>();
        List<Object[]> completed = new ArrayList<>();
        List<Object[]> cancelled = new ArrayList<>();

    
        for (Object[] row : appointments) {
            String status = (String) row[3];

    
            switch (status) {
                case "Pending", "Confirmed" -> upcoming.add(row);

                case "Completed" -> completed.add(row);
                case "Cancelled" -> cancelled.add(row);
            }
        }
    
        // 2. Add Upcoming Appointments FIRST
        appointmentPanel.add(createAppointmentTable("Upcoming Appointments", upcoming));
        appointmentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    
        // 3. Booking Section
        JLabel bookLabel = new JLabel("📆 Book New Appointment");
        bookLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        appointmentPanel.add(bookLabel);
        appointmentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        // Doctor selection
        JComboBox<String> doctorDropdown = new JComboBox<>();
        for (String doc : patientBL.getDoctorList()) doctorDropdown.addItem(doc);
        JLabel doctorLabel = new JLabel("Doctor:", SwingConstants.CENTER);
        doctorLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(doctorLabel);
        formPanel.add(doctorDropdown);
    
        // Date Picker
        JDateChooser dateChooser = new JDateChooser();
        ((JTextField) dateChooser.getDateEditor().getUiComponent()).setEditable(false);
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setMinSelectableDate(new java.util.Date());
        JTextField dateField = (JTextField) dateChooser.getDateEditor().getUiComponent();
        dateField.setPreferredSize(new Dimension(120, 28)); // adjust width & height
        dateField.setMaximumSize(new Dimension(120, 28));   // to enforce layout constraints
        dateField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel dateLabel = new JLabel("Select Date:", SwingConstants.CENTER);
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(dateLabel);
        formPanel.add(dateChooser);
    
        // Time Slot
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel timeLabel = new JLabel("Select Time Slot:", SwingConstants.CENTER);
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        formPanel.add(timeLabel);
        JPanel slotPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        ButtonGroup slotGroup = new ButtonGroup();
        List<JToggleButton> slotButtons = new ArrayList<>();
        formPanel.add(slotPanel);
    
        // Slot logic
        Runnable updateSlots = () -> {
            slotPanel.removeAll();
            slotButtons.clear();
            slotGroup.clearSelection();
    
            String selectedDoctor = (String) doctorDropdown.getSelectedItem();
            java.util.Date selectedDate = dateChooser.getDate();
    
            if (selectedDoctor != null && selectedDate != null) {
                String doctorId = selectedDoctor.split(" - ")[0];
                List<String> allSlots = List.of("09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
                        "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30");
                List<String> availableSlots = patientBL.getAvailableSlots(doctorId, selectedDate);
    
                Set<String> takenSlots = new HashSet<>();
                for (String s : allSlots) {
                    if (!availableSlots.contains(s)) takenSlots.add(s + ":00");
                }
    
                for (String slot : allSlots) {
                    JToggleButton button = new JToggleButton(slot);
                    button.setPreferredSize(new Dimension(80, 30));
                    button.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    button.setFocusPainted(false);
    
                    if (takenSlots.contains(slot + ":00")) {
                        button.setEnabled(false);
                        button.setBackground(new Color(220, 220, 220));
                        button.setToolTipText("Already booked");
                    } else {
                        button.setBackground(new Color(220, 240, 255));
                        slotGroup.add(button);
                        slotButtons.add(button);
                    }
                    slotPanel.add(button);
                }
            }
    
            slotPanel.revalidate();
            slotPanel.repaint();
        };
    
        doctorDropdown.addActionListener(e -> updateSlots.run());
        dateChooser.addPropertyChangeListener("date", e -> updateSlots.run());
    
        // Book Button
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton bookBtn = new JButton("Book");
        formPanel.add(bookBtn);
    
        bookBtn.addActionListener(e -> {
            String selectedDoctor = (String) doctorDropdown.getSelectedItem();
            java.util.Date selectedDate = dateChooser.getDate();
            String timeSlot = slotButtons.stream().filter(JToggleButton::isSelected).map(AbstractButton::getText).findFirst().orElse(null);
    
            if (selectedDoctor != null && selectedDate != null && timeSlot != null) {
                String doctorId = selectedDoctor.split(" - ")[0];
                String datetime = new java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate) + " " + timeSlot;
    
                boolean success = patientBL.bookAppointment(patientId, doctorId, datetime);
                if (success) {
                    JOptionPane.showMessageDialog(mainPanel, "Appointment booked successfully.");
                    showAppointmentsPanel(); // Refresh the appointments panel
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "You already have an appointment at that time.");
                }

            } else {
                JOptionPane.showMessageDialog(mainPanel, "Please select doctor, date, and time.");
            }
        });
    
        // Add booking section
        appointmentPanel.add(formPanel);
        appointmentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    
        // Divider
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        appointmentPanel.add(separator);
        appointmentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    
        // 4. Add completed and cancelled tables
        appointmentPanel.add(createAppointmentTable("Completed Appointments", completed));
        appointmentPanel.add(createAppointmentTable("Cancelled Appointments", cancelled));
    
        // Scroll wrapper
        JScrollPane scrollPane = new JScrollPane(appointmentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30); // faster scroll
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    

private void enableMultilineCells(JTable table) {
    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JTextArea textArea = new JTextArea(value == null ? "" : value.toString());
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setOpaque(true);
            textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
            textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            if (isSelected) {
                textArea.setBackground(table.getSelectionBackground());
                textArea.setForeground(table.getSelectionForeground());
            } else {
                textArea.setBackground(table.getBackground());
                textArea.setForeground(table.getForeground());
            }

            return textArea;
        }
    });

    // Adjust row height
    table.setRowHeight(50);
}

   
private JPanel createAppointmentTable(String title, List<Object[]> data) {
    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(700, 250));
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.WHITE);

    JLabel label = new JLabel("📅 " + title);
    label.setFont(new Font("SansSerif", Font.BOLD, 18));
    label.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
    panel.add(label);

    if (data.isEmpty()) {
        panel.add(new JLabel("No appointments found."));
    } else {
        boolean isCompleted = title.toLowerCase().contains("completed");
        String[] columns = isCompleted
            ? new String[]{"Doctor", "Date", "Time", "Status", "Action", "Appointment_ID"}
            : new String[]{"Doctor", "Date", "Time", "Status"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return isCompleted && column == 4;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.setIntercellSpacing(new Dimension(4, 6));

        for (Object[] row : data) {
            if (isCompleted) {
                model.addRow(new Object[]{row[0], row[1], row[2], row[3], "View Prescription", row[4]});
            } else {
                model.addRow(new Object[]{row[0], row[1], row[2], row[3]});
            }
        }

        if (isCompleted) {
            // Hide appointment_id column
            table.getColumnModel().getColumn(5).setMinWidth(0);
            table.getColumnModel().getColumn(5).setMaxWidth(0);

            table.getColumn("Action").setCellRenderer((tbl, val, isSelected, hasFocus, row, col) -> new JButton("View Prescription"));

            table.getColumn("Action").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
                private final JButton button = new JButton("View Prescription");
                private boolean clicked = false;

                {
                    button.addActionListener(e -> fireEditingStopped());
                }

                @Override
                public Component getTableCellEditorComponent(JTable table, Object value,
                                                             boolean isSelected, int row, int column) {
                    clicked = true;
                    return button;
                }

                @Override
                public Object getCellEditorValue() {
                    if (clicked) {
                        int row = table.getSelectedRow();
                        int appointmentId = (int) table.getValueAt(row, 5); // read hidden appointment_id
                        showPrescriptionPopup(appointmentId);
                    }
                    clicked = false;
                    return "View Prescription";
                }

                public boolean stopCellEditing() {
                    clicked = false;
                    return super.stopCellEditing();
                }
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(670, 200));
        panel.add(scrollPane);
    }

    panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));
    return panel;
}




public void showPrescriptionPopup(int appointmentId) {
    List<String> details = patientBL.fetchPrescriptionByAppointmentId(appointmentId);

    if (!details.isEmpty()) {
        String htmlContent = "<html><body style='font-family: SansSerif; font-size: 11px;'>" +
                "<b>Medicine:</b> " + details.get(0) + "<br><br>" +
                "<b>Dosage:</b> " + details.get(1) + "<br><br>" +
                "<b>Consultation:</b><br>" + details.get(2) + "<br><br>" +
                "<b>Diagnosis:</b><br>" + details.get(3) + "<br><br>" +
                "<b>Doctor's Signature:</b> " + details.get(4) +
                "</body></html>";

        JEditorPane editorPane = new JEditorPane("text/html", htmlContent);
        editorPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(400, 220));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(420, 250));
        panel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(null, panel, "Prescription Details", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null, "No prescription found for this appointment.");
    }
}
 
    private JPanel createPatientCard(String title, String subtitle, String buttonText, Runnable action) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)  // ⬅️ Increased padding inside the card
        ));
        card.setBackground(Color.WHITE);
    
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        JLabel subtitleLabel = new JLabel("<html><body style='width:180px;'>" + subtitle + "</body></html>");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(Color.DARK_GRAY);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));  // ⬅️ More space between lines
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        JButton button = new JButton(buttonText);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setPreferredSize(new Dimension(200, 30));
        button.setMaximumSize(new Dimension(200, 30));
        button.addActionListener(e -> action.run());
    
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 12)));  // ⬅️ More space below title
        card.add(subtitleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 12)));  // ⬅️ More space before button
        card.add(button);
    
        return card;
    }
    
    
    
    private void showTransferPanel() {
        mainPanel.removeAll();
    
        JPanel transferPanel = new JPanel();
        transferPanel.setLayout(new BoxLayout(transferPanel, BoxLayout.Y_AXIS));
        transferPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        transferPanel.setBackground(Color.WHITE);
    
        JLabel title = new JLabel("🔁 Transfer Insurance Credits");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        transferPanel.add(title);

        JLabel balanceLabel = new JLabel("💰 Your Current Balance: " + String.format("%.2f", patientBL.getCurrentBalance(patientId)) + " credits");
        balanceLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        transferPanel.add(balanceLabel);
    
        JComboBox<String> patientDropdown = new JComboBox<>();
        JTextField amountField = new JTextField(10);
        JButton transferBtn = new JButton("Send Credits");
    
        for (String patient : patientBL.getOtherPatients(patientId)) {
            patientDropdown.addItem(patient);
        }
    
        transferPanel.add(new JLabel("Select Recipient:"));
        transferPanel.add(patientDropdown);
        transferPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        transferPanel.add(new JLabel("Amount to Transfer:"));
        transferPanel.add(amountField);
        transferPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        transferPanel.add(transferBtn);
    
        transferBtn.addActionListener(e -> {
            String selected = (String) patientDropdown.getSelectedItem();
            String amountStr = amountField.getText();
        
            if (selected != null && !amountStr.isBlank()) {
                String receiverId = selected.split(" - ")[0];
                try {
                    double amount = Double.parseDouble(amountStr);
                    if (amount <= 0) throw new NumberFormatException();
        
                    try {
                        boolean success = patientBL.executeTransfer(patientId, receiverId, amount);
                        if (success) {
                            JOptionPane.showMessageDialog(mainPanel, "Transfer successful!");
                            showTransferPanel();
                        } else {
                            JOptionPane.showMessageDialog(mainPanel, "Transfer failed. Please ensure you have enough credits.");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(mainPanel, "An error occurred while processing the transfer: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Please enter a valid positive number.");
                }
            }
        });
        
    
        mainPanel.add(transferPanel, BorderLayout.NORTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }


    private void showTransferHistoryPanel() {
    mainPanel.removeAll();

    JPanel historyPanel = new JPanel();
    historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
    historyPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    historyPanel.setBackground(Color.WHITE);

    JLabel title = new JLabel("📋 Credit Transfer History");
    title.setFont(new Font("SansSerif", Font.BOLD, 20));
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    historyPanel.add(title);

    historyPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing

    JLabel balanceLabel = new JLabel("💰 Current Balance: " + String.format("%.2f", patientBL.getCurrentBalance(patientId)) + " credits");
    balanceLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
    balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    historyPanel.add(balanceLabel);

    historyPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing

    // Table Data
    String[] columnNames = {"Timestamp", "Amount", "Recipient/Sender", "Direction"};
    java.util.List<String[]> rawData = patientBL.getStyledTransferHistory(patientId);
    String[][] data = new String[rawData.size()][4];

    for (int i = 0; i < rawData.size(); i++) {
        String formatted = rawData.get(i)[0];
        String direction = rawData.get(i)[1];

        String timestamp = formatted.substring(1, 20);
        String remaining = formatted.substring(22);
        String[] parts = remaining.split(" credits ");
        String amountStr = parts[0].replace("-", "").replace("+", "");
        String[] directionSplit = parts[1].split(" ", 2);
        String party = directionSplit[1];

        String amount = (direction.equals("Sent") ? "-" : "+") + amountStr + " credits";

        data[i][0] = timestamp;
        data[i][1] = amount;
        data[i][2] = party;
        data[i][3] = direction;
    }

    JTable table = new JTable(data, columnNames) {
        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);
            String direction = getValueAt(row, 3).toString();
            if (column == 1) { // Amount column
                if ("Sent".equals(direction)) {
                    c.setForeground(Color.RED);
                } else if ("Received".equals(direction)) {
                    c.setForeground(new Color(0, 128, 0)); // Green
                }
            } else {
                c.setForeground(Color.BLACK);
            }
            return c;
        }
    };

    table.setFont(new Font("SansSerif", Font.PLAIN, 14));
    table.setRowHeight(25);
    table.setGridColor(Color.LIGHT_GRAY);
    table.setShowGrid(true);
    table.setFillsViewportHeight(true);

    JTableHeader header = table.getTableHeader();
    header.setFont(new Font("SansSerif", Font.BOLD, 14));
    header.setBackground(new Color(230, 230, 230));
    header.setOpaque(true);
    header.setBorder(BorderFactory.createLineBorder(Color.GRAY));

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    scrollPane.setPreferredSize(new Dimension(700, 150));
    scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

    historyPanel.add(scrollPane);
    mainPanel.add(historyPanel, BorderLayout.NORTH);
    mainPanel.revalidate();
    mainPanel.repaint();
}

    

    
}