// Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025

import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;
import java.sql.*;

public class Login extends JFrame {

    private JTextField txtUsername = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);

    private final Color themeColor = new Color(43, 78, 140); // Your custom color

    public Login() {
        setTitle("Hospital Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        JLabel lblTitle = new JLabel("Hospital System Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(themeColor); // Set label color

        JButton btnLogin = new JButton("Login");
        btnLogin.setForeground(Color.WHITE);           // Button text color
        btnLogin.setBackground(themeColor);            // Button background
        btnLogin.setFocusPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        btnLogin.addActionListener(e -> performLogin());

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);
        add(panel);
    }

    // ... [All other methods remain unchanged below]

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if(username.isEmpty() || password.isEmpty()) {
            showError("Please fill your credentials in all fields");
            return;
        }

        try (Connection conn = ConnectionManager.getConnection()) {
            String salt = getSaltForUser(username, conn);
            if(salt == null || salt.isEmpty()) {
                showError("Invalid username or password");
                return;
            }
            String hashedPassword = hashPassword(password + salt);
            String query = "SELECT user_id, role FROM Users WHERE username = ? AND password_hash = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                int userId = rs.getInt("user_id");
                String role = rs.getString("role");
                redirectToDashboard(userId, role, conn);
            } else {
                showError("Invalid username or password");
            }
        } catch (Exception e) {
            showError("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getSaltForUser(String username, Connection conn) throws SQLException {
        String query = "SELECT salt FROM Users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            return rs.getString("salt");
        }
        return null;
    }

    private String hashPassword(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private void redirectToDashboard(int userId, String role, Connection conn) {
        try {
            switch(role.toLowerCase()) {
                case "admin":
                    new Group9AdminPL();
                    break;
                case "doctor":
                    int doctorId = getDoctorId(userId, conn);
                    new Group9DoctorPL(doctorId).setVisible(true);
                    break;
                case "patient":
                    String patientId = String.valueOf(getPatientId(userId, conn));
                    new Group9PatientPL(patientId);
                    break;
                default:
                    showError("Unknown user role");
                    return;
            }
            dispose();
        } catch (Exception e) {
            showError("Error opening dashboard: " + e.getMessage());
        }
    }

    private int getDoctorId(int userId, Connection conn) throws SQLException {
        String query = "SELECT doctor_id FROM Doctors WHERE user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) return rs.getInt("doctor_id");
        throw new SQLException("Doctor not found!");
    }

    private int getPatientId(int userId, Connection conn) throws SQLException {
        String query = "SELECT patient_id FROM Patients WHERE user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) return rs.getInt("patient_id");
        throw new SQLException("Patient not found!");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            SwingUtilities.invokeLater(() -> new Login().setVisible(true));
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
}
