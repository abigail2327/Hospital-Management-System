// Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group9DoctorPL extends JFrame {

    public JFrame frame = new JFrame("Doctor Dashboard");
    private JPanel contentPanel;
    private Group9DoctorBL doctorBL;
    private int currentDoctorId;

    public Group9DoctorPL(int doctorId) {
        this.doctorBL = new Group9DoctorBL();
        this.currentDoctorId = doctorId;
    
        Doctor doctor = doctorBL.getDoctorById(doctorId);
        String doctorName = (doctor != null) ? doctor.getFullName() : "Unknown";

        frame = new JFrame("Doctor Dashboard");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Sidebar
        Color sidebarColor = new Color(43, 78, 140);
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(sidebarColor);
        sidebar.setPreferredSize(new Dimension(200, 0));

        String[] menuItems = {"🏠 Home", "📅 Appointments", "📋 Patient Records", "📤 Logout"};
        for (String item : menuItems) {
            JButton button = new JButton(item);
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

            if (item.contains("Appointments")) {
                button.addActionListener(e -> showAppointmentsPanel());
            }
            else if (item.contains("Patient Records")) {
                button.addActionListener(e -> showPatientRecordsPanel());
            }
            else if (item.contains("Logout")) {
                button.addActionListener(e -> {
                    // Disconnect database connection if managed in DL or BL
                    try {
                        if (doctorBL != null) {
                            doctorBL.disconnect(); // Ensure this method exists in your BL
                        }
                    } catch (Exception ex) {
                        System.err.println("Error during disconnect: " + ex.getMessage());
                    }
            
                    frame.dispose(); // Closes the dashboard
            
                    // Redirect to login page
                    SwingUtilities.invokeLater(() -> {
                        new Login().setVisible(true);
                    });
                });
            }
            
            else if (item.contains("Home")) {
                button.addActionListener(e -> showHomePanel());
            }
            
        }

        // Main Panel
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createCard("📅 Appointments", "Check your appointments, change status of appointments and add prescriptions", "Go to Appointments"));
        mainPanel.add(createCard("📋 View Patient Records", "Access patients’ medical history", "View Records"));
        

        JLabel greeting = new JLabel("Welcome, Dr. " + doctorName + "!", JLabel.CENTER);
        greeting.setFont(new Font("Arial", Font.BOLD, 26));
        greeting.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(greeting, BorderLayout.NORTH);
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        frame.add(sidebar, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createCard(String title, String subtitle, String buttonText) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel subtitleLabel = new JLabel(subtitle, JLabel.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton button = new JButton(buttonText);
        button.setPreferredSize(new Dimension(160, 30));

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(button);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(subtitleLabel, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);
        if (buttonText.equals("Go to Appointments")) {
            button.addActionListener(e -> showAppointmentsPanel());
        } else if (buttonText.equals("View Records")) {
            button.addActionListener(e -> showPatientRecordsPanel());
        }

        

        return card;
    }

    private void showAppointmentsPanel() {
        JPanel appointmentsPanel = new JPanel(new BorderLayout());
        appointmentsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        appointmentsPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Your Upcoming Appointments", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        appointmentsPanel.add(title, BorderLayout.NORTH);

        String[] columnNames = {"Patient Name", "Appointment Date", "Appointment Status", "Change Status", "Prescription", "appointment_id"};

        List<Object[]> appointments = doctorBL.getAppointmentsForDoctor(currentDoctorId);
        Object[][] data = appointments.toArray(new Object[0][]);

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3 || column == 4;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(30);

        // Status Dropdown
        String[] statuses = {"Pending", "Confirmed", "Completed", "Cancelled"};
        JComboBox<String> statusComboBox = new JComboBox<>(statuses);
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(statusComboBox));

        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), table));

        table.getColumnModel().getColumn(4).setCellRenderer(new PrescriptionButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new PrescriptionButtonEditor(new JCheckBox(), table));

        // Hide appointment_id column
        table.getColumnModel().getColumn(5).setMinWidth(0);
        table.getColumnModel().getColumn(5).setMaxWidth(0);
        table.getColumnModel().getColumn(5).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);
        appointmentsPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.removeAll();
        contentPanel.add(appointmentsPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    private void showHomePanel() {
        Doctor doctor = doctorBL.getDoctorById(currentDoctorId);
        String doctorName = (doctor != null) ? doctor.getFullName() : "Unknown";
    
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);
    
        mainPanel.add(createCard("📅 View Appointments", "Check your upcoming schedule", "View Appointments"));
        mainPanel.add(createCard("📋 View Patient Records", "Access patients’ medical history", "View Records"));
    
        JLabel greeting = new JLabel("Welcome, Dr. " + doctorName + "!", JLabel.CENTER);
        greeting.setFont(new Font("Arial", Font.BOLD, 26));
        greeting.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
    
        contentPanel.removeAll();
        contentPanel.add(greeting, BorderLayout.NORTH);
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    
    private void showPatientRecordsPanel() {
        JPanel recordsPanel = new JPanel(new BorderLayout());
        recordsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        recordsPanel.setBackground(Color.WHITE);
    
        JLabel title = new JLabel("Patient Records", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        recordsPanel.add(title, BorderLayout.NORTH);
    
        String[] columnNames = {"Patient ID", "Patient Name", "Gender", "Age", "Phone", "Prescriptions"};
    
        List<Object[]> patients = doctorBL.getPatientsForDoctor(currentDoctorId);
        Object[][] data = patients.toArray(new Object[0][]);
    
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
    
        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(28);
    
        // Set custom renderer and editor for "Prescriptions" button column
        table.getColumnModel().getColumn(5).setCellRenderer(new ViewPrescriptionButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ViewPrescriptionButtonEditor(new JCheckBox(), table));
    
        JScrollPane scrollPane = new JScrollPane(table);
        recordsPanel.add(scrollPane, BorderLayout.CENTER);
    
        contentPanel.removeAll();
        contentPanel.add(recordsPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Change Status");
            return this;
        }
    }
    class ViewPrescriptionButtonRenderer extends JButton implements TableCellRenderer {
        public ViewPrescriptionButtonRenderer() {
            setText("🔍 View");
            setForeground(new Color(33, 102, 176));
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setBorderPainted(false);
            setContentAreaFilled(false);
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }
    class ViewPrescriptionButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private JTable table;
    
        public ViewPrescriptionButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table;
            button = new JButton("🔍 View");
            button.setForeground(new Color(33, 102, 176));
            button.setFont(new Font("SansSerif", Font.BOLD, 12));
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setHorizontalAlignment(SwingConstants.CENTER);
    
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
                int patientId = (int) table.getValueAt(table.getSelectedRow(), 0);
                List<String[]> prescriptions = doctorBL.getPrescriptionsByPatientId(patientId);
    
                if (prescriptions.isEmpty()) {
                    JOptionPane.showMessageDialog(button, "No prescriptions found.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    int count = 1;
                    for (String[] p : prescriptions) {
                        sb.append("Prescription #").append(count++).append("\n");
                        sb.append("Medicine      : ").append(p[0]).append("\n");
                        sb.append("Dosage        : ").append(p[1]).append("\n");
                        sb.append("Consultation  : ").append(p[2]).append("\n");
                        sb.append("Diagnosis     : ").append(p[3]).append("\n");
                        sb.append("Prescribed By : ").append(p[4]).append("\n");
                        sb.append("Date          : ").append(p[5]).append("\n");
                        sb.append("\n----------------------------------------------------------------\n\n");
                    }
                
                    JTextArea textArea = new JTextArea(sb.toString());
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(520, 400));
                
                    JOptionPane.showMessageDialog(button, scrollPane, "Prescription History", JOptionPane.INFORMATION_MESSAGE);
                }
                
            }
            clicked = false;
            return "View";
        }
    
        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
        

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private JTable table;
    
        public ButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table;
            button = new JButton("Change Status");
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
    
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            clicked = true;
            return button;
        }
    
        public Object getCellEditorValue() {
            if (clicked) {
                int row = table.getSelectedRow();
                String patient = table.getValueAt(row, 0).toString();
                String newStatus = table.getValueAt(row, 2).toString();
                int appointmentId = (int) table.getModel().getValueAt(row, 5);
    
                // ✅ Get actual status from DB
                Appointment appt = doctorBL.getAppointmentById(appointmentId);
                if (appt == null) {
                    JOptionPane.showMessageDialog(button, "Failed to fetch appointment from database.");
                    clicked = false;
                    return "Change Status";
                }
    
                String originalStatus = appt.getStatus();
    
                if (originalStatus.equals(newStatus)) {
                    JOptionPane.showMessageDialog(button, "No status change detected.");
                    clicked = false;
                    return "Change Status";
                }
    
                boolean allowed = false;
                if (originalStatus.equals("Pending") &&
                        (newStatus.equals("Confirmed") || newStatus.equals("Cancelled"))) {
                    allowed = true;
                } else if ((originalStatus.equals("Confirmed") || originalStatus.equals("Cancelled")) &&
                        (newStatus.equals("Confirmed") || newStatus.equals("Cancelled")) &&
                        !newStatus.equals("Pending")) {
                    allowed = true;
                } else if (originalStatus.equals("Completed")) {
    JOptionPane.showMessageDialog(button,
        "Cannot change status of a completed appointment.\nCompleted appointments are final and cannot be modified.");
    ((DefaultTableModel) table.getModel()).setValueAt(originalStatus, row, 2); // Reset UI
} else {
    String message = String.format(
        "Cannot change status from %s to %s.\nAllowed transitions:\n" +
        "Pending → Confirmed / Cancelled\n" +
        "Confirmed ↔ Cancelled\n" +
        "Completed → ❌ Not allowed",
        originalStatus, newStatus
    );
    JOptionPane.showMessageDialog(button, message, "Invalid status change", JOptionPane.ERROR_MESSAGE);
    ((DefaultTableModel) table.getModel()).setValueAt(originalStatus, row, 2); // Reset UI
}

    
                if (allowed) {
                    int confirm = JOptionPane.showConfirmDialog(button,
                            "Are you sure you want to change the status for " + patient +
                                    " from " + originalStatus + " to " + newStatus + "?",
                            "Confirm Status Change", JOptionPane.YES_NO_OPTION);
    
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = doctorBL.updateAppointmentStatus(appointmentId, newStatus);
                        if (success) {
                            ((DefaultTableModel) table.getModel()).setValueAt(newStatus, row, 2);
                            JOptionPane.showMessageDialog(button, "Appointment status updated to: " + newStatus);
                        } else {
                            JOptionPane.showMessageDialog(button, "Failed to update status.");
                        }
                    }
                }
            }
            clicked = false;
            return "Change Status";
        }
    
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
    
    class PrescriptionButtonRenderer extends JButton implements TableCellRenderer {
        public PrescriptionButtonRenderer() {
            setOpaque(true);
        }
    
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            try {
                int appointmentId = (int) table.getValueAt(row, 5);
                Appointment appt = doctorBL.getAppointmentById(appointmentId);
                if (appt == null) {
                    setText("Error");
                    setEnabled(false);
                    JOptionPane.showMessageDialog(this, "Failed to fetch appointment from database.");
                } else {
                    String status = appt.getStatus();
                    if (status.equals("Confirmed")) {
                        setText("Add Prescription");
                        setEnabled(true);
                    } else if (status.equals("Completed")) {
                        setText("View Prescription");
                        setEnabled(true);
                    } else {
                        setText("N/A");
                        setEnabled(false);
                    }
                }
            } catch (Exception e) {
                setText("Error");
                setEnabled(false);
                JOptionPane.showMessageDialog(this, "Error rendering button: " + e.getMessage());
            }
    
            return this;
        }
    }
    

    class PrescriptionButtonEditor extends DefaultCellEditor {
        private JButton button;
        private JTable table;
        private boolean clicked;

        public PrescriptionButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            String status = table.getValueAt(row, 2).toString();

            if (status.equals("Confirmed")) {
                button.setText("Add Prescription");
                button.setEnabled(true);
            } else if (status.equals("Completed")) {
                button.setText("View Prescription");
                button.setEnabled(true);
            } else {
                button.setText("N/A");
                button.setEnabled(false);
            }

            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                int row = table.getSelectedRow();
                String patientName = table.getValueAt(row, 0).toString();
                String status = table.getValueAt(row, 2).toString();

                if (status.equals("Confirmed")) {
                    int appointmentId = (int) table.getValueAt(row, 5);
                    showAddPrescriptionDialog(patientName, appointmentId, row);
                } else if (status.equals("Completed")) {
                    int appointmentId = (int) table.getValueAt(row, 5);
                    String[] prescription = doctorBL.getPrescriptionByAppointmentId(appointmentId);

                    if (prescription != null) {
                        String message = String.format(
                            "Medicine: %s\nDosage: %s\nConsultation: %s\nDiagnosis: %s",
                            prescription[0], prescription[1], prescription[2], prescription[3]
                        );
                        JOptionPane.showMessageDialog(button, message, "Prescription Details", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(button, "No prescription found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
            clicked = false;
            return "Prescription";
        }
        
        

        private void showAddPrescriptionDialog(String patientName, int appointmentId, int row) {
            JDialog dialog = new JDialog((Frame) null, "Add Prescription", true);
            dialog.setSize(500, 400);
            dialog.setLocationRelativeTo(button);
            dialog.setLayout(new BorderLayout());

            JLabel titleLabel = new JLabel("Add Prescription for " + patientName, JLabel.CENTER);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            dialog.add(titleLabel, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            List<String[]> medicines = doctorBL.fetchMedicines();

            Map<String, Integer> medicineMap = new HashMap<>();
            
            for (String[] r : medicines) {
                medicineMap.put(r[1], Integer.parseInt(r[0])); // name -> id
            } 
            JComboBox<String> medicineDropdown = new JComboBox<>(medicineMap.keySet().toArray(new String[0]));
            
            JTextField dosageField = new JTextField();
            JTextArea consultationArea = new JTextArea(3, 20);
            JTextArea diagnosisArea = new JTextArea(3, 20);

            formPanel.add(new JLabel("Medicine:"));
            formPanel.add(medicineDropdown);

            formPanel.add(new JLabel("Dosage:"));
            formPanel.add(dosageField);

            formPanel.add(new JLabel("Consultation Notes:"));
            formPanel.add(new JScrollPane(consultationArea));

            formPanel.add(new JLabel("Diagnosis:"));
            formPanel.add(new JScrollPane(diagnosisArea));

            dialog.add(formPanel, BorderLayout.CENTER);

            JButton submitButton = new JButton("Submit");
            submitButton.addActionListener(e -> {
                try {
                    String selectedMedicine = (String) medicineDropdown.getSelectedItem();
                    int medicineId = medicineMap.get(selectedMedicine);
                    String dosage = dosageField.getText().trim();
                    String consultation = consultationArea.getText().trim();
                    String diagnosis = diagnosisArea.getText().trim();

                    if (dosage.isEmpty() || consultation.isEmpty() || diagnosis.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "All fields are required.");
                        return;
                    }

                    boolean success = doctorBL.insertPrescription(appointmentId, medicineId, dosage, consultation, diagnosis);
                    if (success) {
                        doctorBL.updateAppointmentStatus(appointmentId, "Completed");
                        ((DefaultTableModel) table.getModel()).setValueAt("Completed", row, 2);
                        JOptionPane.showMessageDialog(dialog, "Prescription saved and appointment marked as Completed.");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to save prescription.");
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Medicine ID must be a number.");
                }
            });

            JPanel bottomPanel = new JPanel();
            bottomPanel.add(submitButton);
            dialog.add(bottomPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
        
    }

} 

