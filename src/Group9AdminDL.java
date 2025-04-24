// Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Group9AdminDL {


    // Core method used by all load functions
   private void loadDataIntoTable(JTable table, String query, JFrame frame) {
    try (Connection conn = ConnectionManager.getConnection()) {

        ResultSet rs;

        if (query.trim().toUpperCase().startsWith("CALL")) {
            CallableStatement stmt = conn.prepareCall(query);
            rs = stmt.executeQuery();
        } else {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
        }

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        table.setModel(model);
        rs.close();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(frame, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}


    public void loadAllPatients(JTable table, JFrame frame) {
        loadDataIntoTable(table, "CALL DisplayTableByName('Patients')", frame);
    }

    public void loadAllDoctors(JTable table, JFrame frame) {
        String query = """
            SELECT d.doctor_id, d.first_name, d.last_name, d.gender, d.phone_number,
                   dept.department_name, d.description
            FROM Doctors d
            JOIN Department dept ON d.department_id = dept.department_id
        """;
        loadDataIntoTable(table, query, frame);
    }

    public void loadAllDepartments(JTable table, JFrame frame) {
        loadDataIntoTable(table, "CALL DisplayTableByName('Department')", frame);
    }

    public void loadAllMedicines(JTable table, JFrame frame) {
        loadDataIntoTable(table, "CALL DisplayTableByName('Medicines')", frame);
    }

    public void loadAllAppointments(JTable table, JFrame frame) {
        loadDataIntoTable(table, "CALL DisplayTableByName('Appointments')", frame);
    }

    public void loadAllPrescriptions(JTable table, JFrame frame) {
        loadDataIntoTable(table, "CALL DisplayTableByName('Prescriptions')", frame);
    }

    public void loadAllCreditTransfers(JTable table, JFrame frame) {
        loadDataIntoTable(table, "CALL DisplayTableByName('CreditTransfers')", frame);
    }

    public void loadAllUsers(JTable table, JFrame frame) {
        loadDataIntoTable(table, "CALL DisplayTableByName('Users')", frame);
    }
    public boolean addPatient(String firstName, String lastName, Date dob, String gender, String phone, double credits) { // Change credits to int
        String sql = "{call AddPatient(?, ?, ?, ?, ?, ?)}"; // Use stored procedure
        
        try (Connection conn = ConnectionManager.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) { // Use CallableStatement
                
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setDate(3, dob);
            stmt.setString(4, gender);
            stmt.setString(5, phone);
            stmt.setDouble(6, credits); // Match INT type in procedure
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            if ("50000".equals(ex.getSQLState())) { // Handle credit validation error
                System.out.println("Credit validation failed: " + ex.getMessage());
            }
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updatePatient(String patientId, String firstName, String lastName, Date dob, String gender, String phone, double credits) {
        String sql = "{call UpdatePatient(?, ?, ?, ?, ?, ?, ?)}"; // Use stored procedure
        
        try (Connection conn = ConnectionManager.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, patientId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setDate(4, dob);
            stmt.setString(5, gender);
            stmt.setString(6, phone);
            stmt.setDouble(7, credits); // INT type
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deletePatient(String patientId) {
        String query = "DELETE FROM Patients WHERE patient_id=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, patientId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ResultSet getPatientById(String patientId) throws SQLException {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Patients WHERE patient_id=?");
        stmt.setString(1, patientId);
        return stmt.executeQuery();
    }
    public boolean addDoctor(String firstName, String lastName, String gender, String phone, int deptId, String desc) {
        String sql = "{call AddDoctor(?, ?, ?, ?, ?, ?)}"; // Use stored procedure
        
        try (Connection conn = ConnectionManager.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, gender);
            stmt.setString(4, phone);
            stmt.setInt(5, deptId);
            stmt.setString(6, desc);
            
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            if ("45000".equals(ex.getSQLState())) { // Handle validation errors
                System.out.println("Validation error: " + ex.getMessage());
            }
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean updateDoctor(String doctorId, String firstName, String lastName, String gender, String phone, int deptId, String desc) {
        String query = "UPDATE Doctors SET first_name=?, last_name=?, gender=?, phone_number=?, department_id=?, description=? WHERE doctor_id=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, gender);
            stmt.setString(4, phone);
            stmt.setInt(5, deptId);
            stmt.setString(6, desc);
            stmt.setString(7, doctorId);
    
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteDoctor(String doctorId) {
        String query = "DELETE FROM Doctors WHERE doctor_id=?"; // Trigger deletes user
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, doctorId);
            return stmt.executeUpdate() > 0; // AfterDeleteDoctor trigger fires
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public ResultSet getDoctorById(String doctorId) throws SQLException {
        Connection conn = ConnectionManager.getConnection();
        CallableStatement stmt = conn.prepareCall("{call GetDoctorDetails(?)}"); // Use procedure
        stmt.setString(1, doctorId);
        return stmt.executeQuery(); // Returns joined doctor-department data
    }
    
    public boolean addDepartment(String name, String desc) {
        String query = "INSERT INTO Department (department_name, department_description) VALUES (?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, name);
            stmt.setString(2, desc);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean updateDepartment(String deptId, String name, String desc) {
        String query = "UPDATE Department SET department_name=?, department_description=? WHERE department_id=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.setString(3, deptId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteDepartment(String deptId) {
        String query = "DELETE FROM Department WHERE department_id=?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, deptId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            if ("45000".equals(ex.getSQLState())) { // Trigger's constraint error
                System.out.println("Cannot delete department: Doctors still assigned.");
            }
            ex.printStackTrace();
            return false;
        }
    }
    
    public ResultSet getDepartmentById(String deptId) throws SQLException {
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Department WHERE department_id=?");
        stmt.setString(1, deptId);
        return stmt.executeQuery();
    }
    public boolean addMedicine(String name) {
        String query = "INSERT INTO Medicines (medicine_name) VALUES (?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, name);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteMedicine(String medicineId) {
        String query = "DELETE FROM Medicines WHERE medicine_id=?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, medicineId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
}

