// Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Group9DoctorDL {

    private Connection connection;

    public Group9DoctorDL() {
        connect();
    }

    private void connect() {
        try {
            connection = ConnectionManager.getConnection();

        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
    }

    
    public Appointment getAppointmentById(int appointmentId) {
        Appointment appt = null;
        String query = "SELECT * FROM Appointments WHERE appointment_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                appt = new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getString("appointment_date"),
                    rs.getString("time_slot"),
                    rs.getString("appointment_status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return appt;
    }
    
    public List<Object[]> getAppointmentsForDoctor(int doctorId) {
        List<Object[]> rows = new ArrayList<>();

        String query = "SELECT a.appointment_id, CONCAT(p.first_name, ' ', p.last_name) AS patient_name, " +
                "a.appointment_date, a.time_slot, a.appointment_status " +
                "FROM Appointments a " +
                "JOIN Patients p ON a.patient_id = p.patient_id " +
                "WHERE a.doctor_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("appointment_id");
                String name = rs.getString("patient_name");
                String dateTime = rs.getDate("appointment_date") + " " + rs.getTime("time_slot");
                String status = rs.getString("appointment_status");

                rows.add(new Object[]{name, dateTime, status, "Change", "", id});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    public boolean updateAppointmentStatus(int appointmentId, String newStatus) {
        String callQuery = "{call changeApptStatus(?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(callQuery)) {
            stmt.setInt(1, appointmentId);
            stmt.setString(2, newStatus);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<String[]> fetchMedicines() {
        List<String[]> medicines = new ArrayList<>();
        String query = "SELECT medicine_id, medicine_name FROM Medicines";
    
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String[] row = new String[2];
                row[0] = String.valueOf(rs.getInt("medicine_id")); // ID
                row[1] = rs.getString("medicine_name"); // Name
                medicines.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicines;
    }
    public String[] getPrescriptionByAppointmentId(int appointmentId) {
        String query = "SELECT m.medicine_name, p.dosage, p.consultation, p.diagnosis " +
                       "FROM Prescriptions p " +
                       "JOIN Medicines m ON p.medicine_id = m.medicine_id " +
                       "WHERE p.appointment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new String[] {
                    rs.getString("medicine_name"),          // medicine name
                    rs.getString("dosage"),
                    rs.getString("consultation"),
                    rs.getString("diagnosis")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // not found
    }
    public List<Object[]> getPatientsForDoctor(int doctorId) {
        List<Object[]> list = new ArrayList<>();
    
        String query = "SELECT DISTINCT p.patient_id, CONCAT(p.first_name, ' ', p.last_name) AS name, " +
                       "p.gender, TIMESTAMPDIFF(YEAR, p.date_of_birth, CURDATE()) AS age, " +
                       "p.phone_number " +
                       "FROM Appointments a " +
                       "JOIN Patients p ON a.patient_id = p.patient_id " +
                       "WHERE a.doctor_id = ?";
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("patient_id"),
                    rs.getString("name"),
                    rs.getString("gender"),
                    rs.getInt("age"),
                    rs.getString("phone_number"),
                    "View"
                });
                
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return list;
    }
    public List<String[]> getPrescriptionsByPatientId(int patientId) {
        List<String[]> list = new ArrayList<>();
    
        String query = "SELECT m.medicine_name, p.dosage, p.consultation, p.diagnosis, " +
                       "CONCAT(d.first_name, ' ', d.last_name) AS doctor_name, " +
                       "a.appointment_date, a.time_slot " +
                       "FROM Prescriptions p " +
                       "JOIN Medicines m ON p.medicine_id = m.medicine_id " +
                       "JOIN Appointments a ON p.appointment_id = a.appointment_id " +
                       "JOIN Doctors d ON a.doctor_id = d.doctor_id " +
                       "WHERE a.patient_id = ? " +
                       "ORDER BY a.appointment_date DESC, a.time_slot DESC";
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new String[] {
                    rs.getString("medicine_name"),
                    rs.getString("dosage"),
                    rs.getString("consultation"),
                    rs.getString("diagnosis"),
                    rs.getString("doctor_name"),
                    rs.getString("appointment_date") + " " + rs.getString("time_slot")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return list;
    }
    
    public Doctor getDoctorById(int doctorId) {
        Doctor doctor = null;
        String query = "SELECT * FROM Doctors WHERE doctor_id = ?";
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                doctor = new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getInt("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getTimestamp("start_date"),
                    rs.getString("gender"),
                    rs.getString("phone_number"),
                    rs.getInt("department_id"),
                    rs.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return doctor;
    }
    
    
    
    
    
    // Future: Insert a prescription
    public boolean insertPrescription(int appointmentId, int medicineId, String dosage, String consultation, String diagnosis) {
        String query = "INSERT INTO Prescriptions (appointment_id, medicine_id, dosage, consultation, diagnosis) " +
                       "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, appointmentId);
            stmt.setInt(2, medicineId);
            stmt.setString(3, dosage);
            stmt.setString(4, consultation);
            stmt.setString(5, diagnosis);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void disconnect() {
        ConnectionManager.disconnect(connection);
    }
}
