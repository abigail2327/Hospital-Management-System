// Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class Group9AdminPL {
    private final Group9AdminBL hospitalAdminBL  = new Group9AdminBL();
    private JFrame frame;
    private JPanel mainPanel;
    private JTextField txtFirstName, txtLastName, txtDOB, txtPatientId, txtPhone, txtDoctorId, txtDeptId, txtMedicineId, txtCreditScore;
    private JTextField txtDoctorFirst, txtDoctorLast, txtDoctorPhone, txtDoctorDesc, txtDeptName, txtDeptDesc, txtMedicine;
    private JComboBox<String> genderDoctor, genderPatient;

    public Group9AdminPL() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Hospital Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = createSidebar();
        frame.add(sidebar, BorderLayout.WEST);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        showDashboardPanel();

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    private void showDashboardPanel() {
        mainPanel.removeAll();
    
        JPanel greetingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        greetingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        greetingPanel.setBackground(Color.WHITE);

        JLabel avatar = new JLabel("👨‍💼");
        avatar.setOpaque(true);
        avatar.setBackground(new Color(0, 120, 215));
        avatar.setForeground(Color.BLACK);
        avatar.setFont(new Font("SansSerif", Font.BOLD, 18));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(50, 50));
        avatar.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

        JLabel greeting = new JLabel("Welcome, Administrator!");
        greeting.setFont(new Font("SansSerif", Font.BOLD, 24));
        greeting.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        greetingPanel.add(avatar);
        greetingPanel.add(greeting);

        // Quick action cards
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        cardsPanel.setBackground(Color.WHITE);

        // Add Patient Card
        JPanel patientCard = createCard(
            "👤 Add New Patient", 
            "Register a new patient in the system", 
            "Add Patient",
            this::showPatientManagementPanel
        );

        // Add Doctor Card
        JPanel doctorCard = createCard(
            "👨⚕️ Add New Doctor", 
            "Register a new doctor in the system", 
            "Add Doctor",
            this::showDoctorManagementPanel
        );

        // Add Department Card
        JPanel deptCard = createCard(
        "🏥 Add Department", 
        "Create a new hospital department", 
        "Add Department",
        this::showDepartmentManagementPanel
    );

        // Add Medicine Card
        JPanel medicineCard = createCard(
            "💊 Add Medicine", 
            "Add new medicine to inventory", 
            "Add Medicine",
            this::showMedicineManagementPanel
        );

        cardsPanel.add(patientCard);
        cardsPanel.add(doctorCard);
        cardsPanel.add(deptCard);
        cardsPanel.add(medicineCard);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(greetingPanel);
        contentPanel.add(cardsPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    private JPanel createCard(String title, String description, String buttonText, Runnable action) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(250, 150));
    
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
    
        JButton actionBtn = new JButton(buttonText);
        actionBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionBtn.setBackground(new Color(70, 130, 180));
        actionBtn.setForeground(Color.BLACK);
        actionBtn.setFocusPainted(false);
        actionBtn.setMaximumSize(new Dimension(150, 30));
        actionBtn.addActionListener(e -> action.run());
    
        card.add(titleLabel);
        card.add(descLabel);
        card.add(actionBtn);
    
        return card;
    }
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(29, 79, 145));
        sidebar.setPreferredSize(new Dimension(210, 0));

        String[] labels = {
            "🏠 Dashboard", "👨‍⚕️ Manage Doctors", "👥 Manage Patients", 
            "🏥 Departments", "💊 Medicines", "📊 View All Data", "📤 Logout"
        };

        Font sidebarFont = new Font("SansSerif", Font.PLAIN, 16);
        for (String text : labels) {
            JLabel label = new JLabel(text);
            label.setForeground(Color.WHITE);
            label.setFont(sidebarFont);
            label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            switch(text) {
                case "🏠 Dashboard":
                    label.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) { showDashboardPanel(); }
                    });
                    break;
                case "👨‍⚕️ Manage Doctors":
                    label.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) { showDoctorManagementPanel(); }
                    });
                    break;
                case "👥 Manage Patients":
                    label.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) { showPatientManagementPanel(); }
                    });
                    break;
                case "🏥 Departments":
                    label.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) { showDepartmentManagementPanel(); }
                    });
                    break;
                case "💊 Medicines":
                    label.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) { showMedicineManagementPanel(); }
                    });
                    break;
                case "📊 View All Data":
                    label.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) { showAllDataPanel(); }
                    });
                    break;
                    case "📤 Logout":
                    label.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            frame.dispose(); // close current admin window
                            new Login().setVisible(true); // launch login window
                        }
                    });
                    break;
                
            }
            sidebar.add(label);
        }
        return sidebar;
    }
    private void showPatientManagementPanel() {
        mainPanel.removeAll();
    
        JPanel patientPanel = new JPanel();
        patientPanel.setLayout(new BoxLayout(patientPanel, BoxLayout.Y_AXIS));
        patientPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        patientPanel.setBackground(Color.WHITE);
    
        JLabel title = new JLabel("👤 Patient Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        patientPanel.add(title);
    
        // ID Panel
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idPanel.add(new JLabel("Patient ID:"));
        txtPatientId = new JTextField(15);
        idPanel.add(txtPatientId);
        JButton loadPatientBtn = new JButton("Load");
        loadPatientBtn.addActionListener(e -> loadPatientData());
        idPanel.add(loadPatientBtn);
        patientPanel.add(idPanel);
    
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formPanel.setBackground(Color.WHITE);
    
        formPanel.add(new JLabel("First Name:"));
        txtFirstName = new JTextField();
        formPanel.add(txtFirstName);
    
        formPanel.add(new JLabel("Last Name:"));
        txtLastName = new JTextField();
        formPanel.add(txtLastName);
    
        formPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        txtDOB = new JTextField();
        formPanel.add(txtDOB);
    
        formPanel.add(new JLabel("Gender:"));
        genderPatient = new JComboBox<>(new String[]{"Male", "Female"});
        formPanel.add(genderPatient);
    
        formPanel.add(new JLabel("Phone Number:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Credit:"));
        txtCreditScore = new JTextField();
        formPanel.add(txtCreditScore);

    
        patientPanel.add(formPanel);
    
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addPatientBtn = new JButton("Add Patient");
        addPatientBtn.setBackground(new Color(70, 130, 180));
        addPatientBtn.setForeground(Color.BLACK);
        addPatientBtn.addActionListener(e -> addPatientToDB());
        buttonPanel.add(addPatientBtn);
    
        JButton updatePatientBtn = new JButton("Update");
        updatePatientBtn.setBackground(new Color(70, 130, 180));
        updatePatientBtn.setForeground(Color.BLACK);
        updatePatientBtn.addActionListener(e -> updatePatient());
        buttonPanel.add(updatePatientBtn);
    
        JButton deletePatientBtn = new JButton("Delete");
        deletePatientBtn.setBackground(new Color(70, 130, 180));
        deletePatientBtn.setForeground(Color.BLACK);
        deletePatientBtn.addActionListener(e -> deletePatient());
        buttonPanel.add(deletePatientBtn);
    
        patientPanel.add(buttonPanel);
    
        // Existing patients table
        JTable patientsTable = new JTable();
        hospitalAdminBL.loadAllPatients(patientsTable, frame);
        patientsTable.removeColumn(patientsTable.getColumnModel().getColumn(1));
        
        JScrollPane scrollPane = new JScrollPane(patientsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Patients"));
        patientPanel.add(scrollPane);
    
        mainPanel.add(patientPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void loadPatientData() {
        String patientId = txtPatientId.getText().trim();
        if (patientId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter Patient ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            ResultSet rs = hospitalAdminBL.getPatientById(patientId);
            if (rs.next()) {
                txtFirstName.setText(rs.getString("first_name"));
                txtLastName.setText(rs.getString("last_name"));
                txtDOB.setText(rs.getDate("date_of_birth").toString());
                genderPatient.setSelectedItem(rs.getString("gender"));
                txtPhone.setText(rs.getString("phone_number"));
                txtCreditScore.setText(rs.getString("credits"));
            } else {
                JOptionPane.showMessageDialog(frame, "Patient not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
            rs.getStatement().getConnection().close(); // Close connection since DL opened it
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading patient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    private void updatePatient() {
        String patientId = txtPatientId.getText().trim();
        if (patientId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter Patient ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String dob = txtDOB.getText().trim();
        String gender = genderPatient.getSelectedItem().toString();
        String phone = txtPhone.getText().trim();
        String creditScoreText = txtCreditScore.getText().trim();
    
        // Validate input fields
        StringBuilder errors = new StringBuilder();
        if (firstName.isEmpty()) errors.append("First Name cannot be empty\n");
        if (lastName.isEmpty()) errors.append("Last Name cannot be empty\n");
        if (dob.isEmpty()) errors.append("Date of Birth cannot be empty\n");
        if (gender.isEmpty()) errors.append("Gender cannot be empty\n");
        if (phone.isEmpty()) errors.append("Phone Number cannot be empty\n");
        if (creditScoreText.isEmpty()) errors.append("Credit Score cannot be empty\n");
    
        double creditScore = 0;
        try {
            creditScore = Double.parseDouble(creditScoreText);
        } catch (NumberFormatException e) {
            errors.append("Credit Score must be a valid number\n");
        }
    
        Date parsedDob = null;
        try {
            parsedDob = Date.valueOf(dob);
        } catch (IllegalArgumentException e) {
            errors.append("Invalid date format (use YYYY-MM-DD)\n");
        }
    
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(frame, "Validation errors:\n" + errors, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            ResultSet rs = hospitalAdminBL.getPatientById(patientId);
            if (rs.next()) {
                String currentFirstName = rs.getString("first_name");
                String currentLastName = rs.getString("last_name");
                String currentDob = rs.getDate("date_of_birth").toString();
                String currentGender = rs.getString("gender");
                String currentPhone = rs.getString("phone_number");
                String currentCredits = rs.getString("credits");
    
                if (currentFirstName.equalsIgnoreCase(firstName) &&
                    currentLastName.equalsIgnoreCase(lastName) &&
                    currentDob.equals(dob) &&
                    currentGender.equalsIgnoreCase(gender) &&
                    currentPhone.equals(phone) &&
                    currentCredits.equals(creditScoreText)) {
                    JOptionPane.showMessageDialog(frame, "No changes detected - same as existing data", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
    
            boolean success = hospitalAdminBL.updatePatient(patientId, firstName, lastName, parsedDob, gender, phone, creditScore);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Patient updated successfully!");
                showPatientManagementPanel();
            } else {
                JOptionPane.showMessageDialog(frame, "Update failed - patient not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
    
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deletePatient() {
        String patientId = txtPatientId.getText().trim();
        if (patientId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter Patient ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        int confirm = JOptionPane.showConfirmDialog(frame, "Delete patient " + patientId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        hospitalAdminBL.deletePatient(patientId);
        JOptionPane.showMessageDialog(frame, "Patient deleted successfully!");
        showPatientManagementPanel();
    }

    
    private void showDoctorManagementPanel() {
        mainPanel.removeAll();

        JPanel doctorPanel = new JPanel();
        doctorPanel.setLayout(new BoxLayout(doctorPanel, BoxLayout.Y_AXIS));
        doctorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        doctorPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("👨‍⚕️ Doctor Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        doctorPanel.add(title);

        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idPanel.add(new JLabel("Doctor ID:"));
        txtDoctorId = new JTextField(15);
        idPanel.add(txtDoctorId);
        JButton loadDoctorBtn = new JButton("Load");
        loadDoctorBtn.addActionListener(e -> loadDoctorData());
        idPanel.add(loadDoctorBtn);
        doctorPanel.add(idPanel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("First Name:"));
        txtDoctorFirst = new JTextField();
        formPanel.add(txtDoctorFirst);

        formPanel.add(new JLabel("Last Name:"));
        txtDoctorLast = new JTextField();
        formPanel.add(txtDoctorLast);

        formPanel.add(new JLabel("Gender:"));
        genderDoctor = new JComboBox<>(new String[]{"Male", "Female"});
        formPanel.add(genderDoctor);

        formPanel.add(new JLabel("Phone Number:"));
        txtDoctorPhone = new JTextField();
        formPanel.add(txtDoctorPhone);

        formPanel.add(new JLabel("Department ID:"));
        txtDeptId = new JTextField();
        formPanel.add(txtDeptId);

        formPanel.add(new JLabel("Description:"));
        txtDoctorDesc = new JTextField();  
        formPanel.add(txtDoctorDesc);

        doctorPanel.add(formPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addDoctorBtn = new JButton("Add Doctor");
        addDoctorBtn.setBackground(new Color(70, 130, 180));
        addDoctorBtn.setForeground(Color.BLACK);
        addDoctorBtn.addActionListener(e -> addDoctorToDB());
        buttonPanel.add(addDoctorBtn);

        JButton updateDoctorBtn = new JButton("Update");
        updateDoctorBtn.setBackground(new Color(70, 130, 180));
        updateDoctorBtn.setForeground(Color.BLACK);
        updateDoctorBtn.addActionListener(e -> updateDoctor());
        buttonPanel.add(updateDoctorBtn);
    
        JButton deleteDoctorBtn = new JButton("Delete");
        deleteDoctorBtn.setBackground(new Color(70, 130, 180));
        deleteDoctorBtn.setForeground(Color.BLACK);
        deleteDoctorBtn.addActionListener(e -> deleteDoctor());
        buttonPanel.add(deleteDoctorBtn);

        doctorPanel.add(buttonPanel);

        // Add table showing existing doctors
        JTable doctorsTable = new JTable();
        hospitalAdminBL.loadAllDoctors(doctorsTable, frame);
        
        JScrollPane scrollPane = new JScrollPane(doctorsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Doctors"));
        doctorPanel.add(scrollPane);
        mainPanel.add(doctorPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showDepartmentManagementPanel() {
        mainPanel.removeAll();

        JPanel deptPanel = new JPanel();
        deptPanel.setLayout(new BoxLayout(deptPanel, BoxLayout.Y_AXIS));
        deptPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        deptPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("🏥 Department Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        deptPanel.add(title);

        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idPanel.add(new JLabel("Department ID:"));
        txtDeptId = new JTextField(15);
        idPanel.add(txtDeptId);
        JButton loadDepartmentBtn = new JButton("Load");
        loadDepartmentBtn.addActionListener(e -> loadDepartmentData());
        idPanel.add(loadDepartmentBtn);
        deptPanel.add(idPanel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Department Name:"));
        txtDeptName = new JTextField();
        formPanel.add(txtDeptName);

        formPanel.add(new JLabel("Description:"));
        txtDeptDesc = new JTextField();
        formPanel.add(txtDeptDesc);

        deptPanel.add(formPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addDepartmentBtn = new JButton("Add Department");
        addDepartmentBtn.setBackground(new Color(70, 130, 180));
        addDepartmentBtn.setForeground(Color.BLACK);
        addDepartmentBtn.addActionListener(e -> addDepartmentToDB());
        buttonPanel.add(addDepartmentBtn);

        JButton updateDepartmentBtn = new JButton("Update");
        updateDepartmentBtn.setBackground(new Color(70, 130, 180));
        updateDepartmentBtn.setForeground(Color.BLACK);
        updateDepartmentBtn.addActionListener(e -> updateDepartment());
        buttonPanel.add(updateDepartmentBtn);
    
        JButton deleteDepartmentBtn = new JButton("Delete");
        deleteDepartmentBtn.setBackground(new Color(70, 130, 180));
        deleteDepartmentBtn.setForeground(Color.BLACK);
        deleteDepartmentBtn.addActionListener(e -> deleteDepartment());
        buttonPanel.add(deleteDepartmentBtn);

        deptPanel.add(buttonPanel);

        // Add table showing existing departments
        JTable deptTable = new JTable();
        hospitalAdminBL.loadAllDepartments(deptTable,frame);
        
        JScrollPane scrollPane = new JScrollPane(deptTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Departments"));
        deptPanel.add(scrollPane);
        mainPanel.add(deptPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    
    private void loadDepartmentData() {
        String departmentId = txtDeptId.getText().trim();
        if (departmentId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please Enter Department ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            ResultSet rs = hospitalAdminBL.getDepartmentById(departmentId);
            if (rs.next()) {
                txtDeptName.setText(rs.getString("department_name"));
                txtDeptDesc.setText(rs.getString("department_description"));
            } else {
                JOptionPane.showMessageDialog(frame, "Department not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
            rs.getStatement().getConnection().close(); // Close the connection
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading department: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void updateDepartment() {
        String departmentId = txtDeptId.getText().trim();
        String deptName = txtDeptName.getText().trim();
        String deptDesc = txtDeptDesc.getText().trim();
    
        ArrayList<String> errors = new ArrayList<>();
        if (departmentId.isEmpty()) errors.add("Department ID cannot be empty");
        if (deptName.isEmpty()) errors.add("Department name cannot be empty");
        if (deptDesc.isEmpty()) errors.add("Description cannot be empty");
    
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(frame, String.join("\n• ", errors), "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            ResultSet rs = hospitalAdminBL.getDepartmentById(departmentId);
            if (rs.next()) {
                String currentName = rs.getString("department_name");
                String currentDesc = rs.getString("department_description");
    
                if (currentName.equals(deptName) && currentDesc.equals(deptDesc)) {
                    JOptionPane.showMessageDialog(frame, "No changes detected - same as existing data", "Information", JOptionPane.INFORMATION_MESSAGE);
                    rs.getStatement().getConnection().close();
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Department not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            rs.getStatement().getConnection().close();
    
            boolean updated = hospitalAdminBL.updateDepartment(departmentId, deptName, deptDesc);
            if (updated) {
                JOptionPane.showMessageDialog(frame, "Department updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                showDepartmentManagementPanel();
            } else {
                JOptionPane.showMessageDialog(frame, "Update failed - department not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
    
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void deleteDepartment() {
        String departmentId = txtDeptId.getText().trim();
        if (departmentId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter Department ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        int confirm = JOptionPane.showConfirmDialog(frame, "Delete this department: " + departmentId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
    
        boolean deleted = hospitalAdminBL.deleteDepartment(departmentId);
        if (deleted) {
            JOptionPane.showMessageDialog(frame, "Department deleted successfully!");
            showDepartmentManagementPanel();
        } else {
            JOptionPane.showMessageDialog(frame, "Deletion failed - department may not exist", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadDoctorData() {
        String doctorId = txtDoctorId.getText().trim();
        if (doctorId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please Enter Doctor ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try (ResultSet rs = hospitalAdminBL.getDoctorById(doctorId)) {
            if (rs.next()) {
                txtDoctorFirst.setText(rs.getString("first_name"));
                txtDoctorLast.setText(rs.getString("last_name"));
                genderDoctor.setSelectedItem(rs.getString("gender"));
                txtDoctorPhone.setText(rs.getString("phone_number"));
                txtDeptId.setText(rs.getString("department_id"));
                
                String description = rs.getString("description");
                txtDoctorDesc.setText(description != null ? description : "No description available");
            } else {
                JOptionPane.showMessageDialog(frame, "Doctor not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading Doctor data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateDoctor() {
        String doctorId = txtDoctorId.getText().trim();
        if (doctorId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter Doctor ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Get input values
        String firstName = txtDoctorFirst.getText().trim();
        String lastName = txtDoctorLast.getText().trim();
        String gender = genderDoctor.getSelectedItem().toString();
        String phone = txtDoctorPhone.getText().trim();
        String deptIdStr = txtDeptId.getText().trim();
        String description = txtDoctorDesc.getText().trim();
    
        // Validate fields
        StringBuilder errors = new StringBuilder();
        if (firstName.isEmpty()) errors.append("First Name cannot be empty\n");
        if (lastName.isEmpty()) errors.append("Last Name cannot be empty\n");
        if (gender.isEmpty()) errors.append("Gender cannot be empty\n");
        if (phone.isEmpty()) errors.append("Phone Number cannot be empty\n");
        if (deptIdStr.isEmpty()) errors.append("Department ID cannot be empty\n");
        if (description.isEmpty()) errors.append("Description cannot be empty\n");
    
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(frame, "Validation errors:\n" + errors, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Validate department ID format
        int deptId;
        try {
            deptId = Integer.parseInt(deptIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Department ID must be a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            ResultSet rs = hospitalAdminBL.getDoctorById(doctorId);
            if (rs.next()) {
                String currentFirstName = rs.getString("first_name");
                String currentLastName = rs.getString("last_name");
                String currentGender = rs.getString("gender");
                String currentPhone = rs.getString("phone_number");
                int currentDeptId = rs.getInt("department_id");
                String currentDesc = rs.getString("description");
    
                if (currentFirstName.equalsIgnoreCase(firstName) &&
                    currentLastName.equalsIgnoreCase(lastName) &&
                    currentGender.equalsIgnoreCase(gender) &&
                    currentPhone.equals(phone) &&
                    currentDeptId == deptId &&
                    currentDesc.equals(description)) {
    
                    JOptionPane.showMessageDialog(frame, "No changes detected - same as existing data", "Info", JOptionPane.INFORMATION_MESSAGE);
                    rs.getStatement().getConnection().close(); // Close connection from DL
                    return;
                }
            }
    
            rs.getStatement().getConnection().close(); // Always close connection
    
            // Perform update
            boolean updated = hospitalAdminBL.updateDoctor(doctorId, firstName, lastName, gender, phone, deptId, description);
            if (updated) {
                JOptionPane.showMessageDialog(frame, "Doctor updated successfully!");
                showDoctorManagementPanel();
            } else {
                JOptionPane.showMessageDialog(frame, "Update failed - doctor not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
    
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    
    private void deleteDoctor() {
        String doctorId = txtDoctorId.getText().trim();
        if (doctorId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter Doctor ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        int confirm = JOptionPane.showConfirmDialog(frame, "Delete Doctor " + doctorId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
    
        boolean deleted = hospitalAdminBL.deleteDoctor(doctorId);
        if (deleted) {
            JOptionPane.showMessageDialog(frame, "Doctor deleted successfully!");
            showDoctorManagementPanel();
        } else {
            JOptionPane.showMessageDialog(frame, "Error deleting doctor", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showMedicineManagementPanel() {
        mainPanel.removeAll();

        JPanel medicinePanel = new JPanel();
        medicinePanel.setLayout(new BoxLayout(medicinePanel, BoxLayout.Y_AXIS));
        medicinePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        medicinePanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("💊 Medicine Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        medicinePanel.add(title);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 10));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formPanel.setBackground(Color.WHITE);
        formPanel.add(new JLabel("Medicine Name:"));
        txtMedicine = new JTextField();
        formPanel.add(txtMedicine);
        medicinePanel.add(formPanel);

        JButton addMedicineBtn = new JButton("Add Medicine");
        addMedicineBtn.setBackground(new Color(70, 130, 180));
        addMedicineBtn.setForeground(Color.BLACK);
        addMedicineBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addMedicineBtn.addActionListener(e -> addMedicineToDB());
        medicinePanel.add(addMedicineBtn);
        
        
        JPanel IdPanel = new JPanel();
        IdPanel.setLayout(new GridLayout(0, 2, 10, 10));
        IdPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        IdPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        IdPanel.setBackground(Color.WHITE);
        IdPanel.add(new JLabel("Medicine ID:"));
        txtMedicineId = new JTextField();
        IdPanel.add(txtMedicineId);
        medicinePanel.add(IdPanel);

        JButton deleteMedicineBtn = new JButton("Delete");
        deleteMedicineBtn.setBackground(new Color(70, 130, 180));
        deleteMedicineBtn.setForeground(Color.BLACK);
        deleteMedicineBtn.addActionListener(e -> deleteMedicine());
        medicinePanel.add(deleteMedicineBtn);
    

        // Add table showing existing medicines
        JTable medicineTable = new JTable();
        hospitalAdminBL.loadAllMedicines(medicineTable, frame);
        
        JScrollPane scrollPane = new JScrollPane(medicineTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Medicines"));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        medicinePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        medicinePanel.add(scrollPane);

        mainPanel.add(medicinePanel, BorderLayout.NORTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    private void deleteMedicine() {
        String medicineId = txtMedicineId.getText().trim();
        if (medicineId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter Medicine ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        int confirm = JOptionPane.showConfirmDialog(frame, "Delete this Medicine " + medicineId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
    
        boolean success = hospitalAdminBL.deleteMedicine(medicineId);
        if (success) {
            JOptionPane.showMessageDialog(frame, "Medicine deleted successfully!");
            showMedicineManagementPanel();
        } else {
            JOptionPane.showMessageDialog(frame, "Error deleting medicine", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void showAllDataPanel() {
        mainPanel.removeAll();

        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BorderLayout());
        dataPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dataPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("📊 All Hospital Data");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        dataPanel.add(title, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Patients table
        JTable patientsTable = new JTable();
        hospitalAdminBL.loadAllPatients(patientsTable,frame);
        tabbedPane.addTab("Patients", new JScrollPane(patientsTable));
        
        // Doctors table
        JTable doctorsTable = new JTable();
        hospitalAdminBL.loadAllDoctors(doctorsTable, frame);
        tabbedPane.addTab("Doctors", new JScrollPane(doctorsTable));
        
        // Departments table
        JTable deptTable = new JTable();
        hospitalAdminBL.loadAllDepartments(deptTable,frame);
        tabbedPane.addTab("Departments", new JScrollPane(deptTable));
        
        // Medicine table
        JTable medicineTable = new JTable();
        hospitalAdminBL.loadAllMedicines(medicineTable, frame);
        tabbedPane.addTab("Medicines", new JScrollPane(medicineTable));

          // Appoinment table
        JTable AppoinmentTable = new JTable();
        hospitalAdminBL.loadAllAppointments(AppoinmentTable, frame);
        tabbedPane.addTab("Appoinment", new JScrollPane(AppoinmentTable));
          
        JTable PrescriptionTable = new JTable();
        hospitalAdminBL.loadAllPrescriptions(PrescriptionTable, frame);
        tabbedPane.addTab("Prescription", new JScrollPane(PrescriptionTable));
        
        JTable CreditTransferTable = new JTable();
        hospitalAdminBL.loadAllCreditTransfers(CreditTransferTable, frame);
        tabbedPane.addTab("CreditTransfer", new JScrollPane(CreditTransferTable));

          
        // Users table
        JTable usersTable = new JTable();
        hospitalAdminBL.loadAllUsers(usersTable, frame);
        tabbedPane.addTab("Users", new JScrollPane(usersTable));

        dataPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

   private void addPatientToDB() {
    // Retrieve and trim input values
    String firstName = txtFirstName.getText().trim();
    String lastName = txtLastName.getText().trim();
    String dobText = txtDOB.getText().trim();
    String gender = genderPatient.getSelectedItem().toString();
    String phone = txtPhone.getText().trim();
    String creditScoreText = txtCreditScore.getText().trim();

    ArrayList <String> errors = new ArrayList<>();

    // Validate required fields
    if (firstName.isEmpty()) {
        errors.add("First name cannot be empty.");
    }
    if (lastName.isEmpty()) {
        errors.add("Last name cannot be empty.");
    }
    if (dobText.isEmpty()) {
        errors.add("Date of birth cannot be empty.");
    }
    if (gender.isEmpty()) {
        errors.add("Gender cannot be empty.");
    }
    if (phone.isEmpty()) {
        errors.add("Phone number cannot be empty.");
    }
    if (creditScoreText.isEmpty()) {
        errors.add("Credit score cannot be empty.");
    }

    // Validate credit score is a valid integer
    double creditScore = 0.0;
    if (!creditScoreText.isEmpty()) {
        try {
            creditScore = Double.parseDouble(creditScoreText);
        } catch (NumberFormatException e) {
            errors.add("Credit score must be a valid integer.");
        }
    }

    // Check if there are any validation errors
    if (!errors.isEmpty()) {
        String errorMessage = String.join("\n", errors);
        JOptionPane.showMessageDialog(frame, errorMessage, "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }



    try {
        Date dob = Date.valueOf(dobText); // May throw IllegalArgumentException if format is wrong
        hospitalAdminBL.addPatient(firstName, lastName, dob, gender, phone, creditScore);
        JOptionPane.showMessageDialog(frame, "Patient added successfully!");
        showPatientManagementPanel();
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(frame, "Invalid date format. Please use YYYY-MM-DD (e.g., 2001-12-31).", "Date Format Error", JOptionPane.ERROR_MESSAGE);
    }
    
}
    
private void addDoctorToDB() {
    // Retrieve and trim input values
    String firstName = txtDoctorFirst.getText().trim();
    String lastName = txtDoctorLast.getText().trim();
    String gender = genderDoctor.getSelectedItem().toString();
    String phone = txtDoctorPhone.getText().trim();
    String deptIdStr = txtDeptId.getText().trim();
    String description = txtDoctorDesc.getText().trim();

    // Validate inputs
    StringBuilder errorMessage = new StringBuilder();

    if (firstName.isEmpty()) {
        errorMessage.append("First name cannot be empty.\n");
    }
    if (lastName.isEmpty()) {
        errorMessage.append("Last name cannot be empty.\n");
    }
    if (gender.isEmpty()) {
        errorMessage.append("Gender cannot be empty.\n");
    }
    if (phone.isEmpty()) {
        errorMessage.append("Phone number cannot be empty.\n");
    }
    if (deptIdStr.isEmpty()) {
        errorMessage.append("Department ID cannot be empty.\n");
    } else {
        try {
            Integer.parseInt(deptIdStr);
        } catch (NumberFormatException e) {
            errorMessage.append("Department ID must be a valid number.\n");
        }
    }
    // Add validation for description if required
     if (description.isEmpty()) {
         errorMessage.append("Description cannot be empty.\n");
     }
    // Show errors if any
    if (errorMessage.length() > 0) {
        JOptionPane.showMessageDialog(frame, errorMessage.toString(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    hospitalAdminBL.addDoctor(firstName, lastName,genderDoctor.getSelectedItem().toString(), phone, Integer.parseInt(deptIdStr), description);
    JOptionPane.showMessageDialog(frame, "Doctor added successfully!");
    showDoctorManagementPanel();

}
private void addDepartmentToDB() {
    String deptName = txtDeptName.getText().trim();
    String deptDesc = txtDeptDesc.getText().trim();

    ArrayList<String> errors = new ArrayList<>();
    if (deptName.isEmpty()) errors.add("Department name cannot be empty");
    if (deptDesc.isEmpty()) errors.add("Department description cannot be empty");

    if (!errors.isEmpty()) {
        JOptionPane.showMessageDialog(frame, String.join("\n• ", errors), "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    boolean added = hospitalAdminBL.addDepartment(deptName, deptDesc);
    if (added) {
        JOptionPane.showMessageDialog(frame, "Department added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        showDepartmentManagementPanel();
    } else {
        JOptionPane.showMessageDialog(frame, "Failed to add department.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void addMedicineToDB() {
    String medicineName = txtMedicine.getText().trim();
    if (medicineName.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Please enter Medicine Name", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    boolean success = hospitalAdminBL.addMedicine(medicineName);
    if (success) {
        JOptionPane.showMessageDialog(frame, "Medicine added successfully!");
        showMedicineManagementPanel();
    } else {
        JOptionPane.showMessageDialog(frame, "Failed to add medicine.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
}