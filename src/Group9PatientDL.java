// Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Group9PatientDL {

    private Connection connection;

    public Group9PatientDL() {
        connect();
    }

    private void connect() {
        try {
            connection = ConnectionManager.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
    }

    public List<String> fetchPrescriptionByAppointmentId(int appointmentId) {
        List<String> details = new ArrayList<>();
        String query = "SELECT m.medicine_name, p.dosage, p.consultation, p.diagnosis, " +
                       "CONCAT(d.first_name, ' ', d.last_name) AS signature " +
                       "FROM Prescriptions p " +
                       "JOIN Medicines m ON p.medicine_id = m.medicine_id " +
                       "JOIN Appointments a ON p.appointment_id = a.appointment_id " +
                       "JOIN Doctors d ON a.doctor_id = d.doctor_id " +
                       "WHERE p.appointment_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                details.add(rs.getString("medicine_name"));
                details.add(rs.getString("dosage"));
                details.add(rs.getString("consultation"));
                details.add(rs.getString("diagnosis"));
                details.add(rs.getString("signature"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return details;
    }

    public List<Object[]> getAllAppointmentsForPatient(String patientId) {
        List<Object[]> list = new ArrayList<>();
        String query = """
            SELECT a.appointment_id,
                   CONCAT(d.first_name, ' ', d.last_name) AS doctor_name,
                   a.appointment_date,
                   a.time_slot,
                   a.appointment_status
            FROM Appointments a
            JOIN Doctors d ON a.doctor_id = d.doctor_id
            WHERE a.patient_id = ?
            ORDER BY a.appointment_date, a.time_slot
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("appointment_id");
                String doctor = rs.getString("doctor_name");
                String date = rs.getString("appointment_date");
                String time = rs.getString("time_slot").substring(0, 5);
                String status = rs.getString("appointment_status");

                list.add(new Object[]{doctor, date, time, status, appointmentId});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Object[]> getPrescriptionsForPatient(String patientId) {
        List<Object[]> list = new ArrayList<>();
        String query = """
            SELECT 
                CONCAT(d.first_name, ' ', d.last_name) AS doctor_name,
                CONCAT(a.appointment_date, ' ', a.time_slot) AS appointment_datetime,
                m.medicine_name, 
                p.dosage, 
                p.diagnosis, 
                p.consultation
            FROM Prescriptions p
            JOIN Appointments a ON p.appointment_id = a.appointment_id
            JOIN Doctors d ON a.doctor_id = d.doctor_id
            JOIN Medicines m ON p.medicine_id = m.medicine_id
            WHERE a.patient_id = ?
            ORDER BY a.appointment_date DESC, a.time_slot DESC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String doctorName = rs.getString("doctor_name");
                String dateTime = rs.getString("appointment_datetime");
                String medicine = rs.getString("medicine_name");
                String dosage = rs.getString("dosage");
                String diagnosis = rs.getString("diagnosis");
                String consultation = rs.getString("consultation");

                list.add(new Object[]{doctorName, dateTime, medicine, dosage, diagnosis, consultation});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<String> getDoctorList() {
        List<String> doctors = new ArrayList<>();
        String query = "SELECT doctor_id, first_name, last_name FROM Doctors";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String doctor = rs.getInt("doctor_id") + " - Dr. " + rs.getString("first_name") + " " + rs.getString("last_name");
                doctors.add(doctor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }

    public boolean bookAppointment(String patientId, String doctorId, String datetime) {
        String callProcedure = "{CALL BookAppointment(?, ?, ?, ?, ?)}";
    
        try (Connection conn = ConnectionManager.getConnection();
             CallableStatement stmt = conn.prepareCall(callProcedure)) {
    
                String[] parts = datetime.split(" ");
                String datePart = parts[0];
                String timePart = parts[1];
                
                // Fix: Ensure time format is HH:mm:ss
                if (timePart.length() == 5) {
                    timePart += ":00"; // Convert "HH:mm" to "HH:mm:ss"
                }
                
                stmt.setString(1, patientId);
                stmt.setString(2, doctorId);
                stmt.setDate(3, java.sql.Date.valueOf(datePart));
                stmt.setTime(4, java.sql.Time.valueOf(timePart));
                stmt.registerOutParameter(5, java.sql.Types.BOOLEAN);
                
    
            stmt.execute();
    
            return stmt.getBoolean(5); // true if booked, false if conflict
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    public java.util.List<String> getOtherPatients(String patientId) {
        java.util.List<String> patients = new ArrayList<>();
        String query = "SELECT patient_id, first_name, last_name FROM Patients WHERE patient_id != ?";
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
    
            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                String entry = rs.getString("patient_id") + " - " + rs.getString("first_name") + " " + rs.getString("last_name");
                patients.add(entry);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return patients;
    }
    public boolean executeTransfer(String patientId, String receiverId, double amount) throws SQLException {
        String call = "{ CALL TransferCredits(?, ?, ?, ?) }";
    
        try (Connection conn = ConnectionManager.getConnection();
             CallableStatement stmt = conn.prepareCall(call)) {
    
            stmt.setString(1, patientId);
            stmt.setString(2, receiverId);
            stmt.setDouble(3, amount);
            stmt.registerOutParameter(4, Types.BOOLEAN);
    
            stmt.execute();
            return stmt.getBoolean(4);  // Returns true if success
        }
    }
    
    public java.util.List<String[]> getStyledTransferHistory(String patientId) {
        java.util.List<String[]> list = new ArrayList<>();
    
        String query = """
            SELECT 
                transfer_date, amount, sender_id, receiver_id,
                CASE 
                    WHEN sender_id = ? THEN 'Sent'
                    WHEN receiver_id = ? THEN 'Received'
                END AS direction,
                (SELECT CONCAT(first_name, ' ', last_name) FROM Patients WHERE patient_id = 
                    CASE 
                        WHEN sender_id = ? THEN receiver_id
                        ELSE sender_id
                    END
                ) AS other_party
            FROM CreditTransfers
            WHERE sender_id = ? OR receiver_id = ?
            ORDER BY transfer_date DESC
        """;
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
    
            for (int i = 1; i <= 5; i++) {
                stmt.setString(i, patientId);
            }
    
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String direction = rs.getString("direction");
                String other = rs.getString("other_party");
                double amount = rs.getDouble("amount");
                String date = rs.getString("transfer_date");
    
                String prefix = direction.equals("Sent") ? "-" : "+";
                String action = direction.equals("Sent") ? "to" : "from";
                String formatted = String.format("[%s] %s%.2f credits %s %s", date, prefix, amount, action, other);
    
                list.add(new String[]{formatted, direction});
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return list;
    }
    public java.util.List<String> getAvailableSlots(String doctorId, java.util.Date date) {
        java.util.List<String> slots = new ArrayList<>();
        String formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    
        String[] allSlots = {
            "09:00", "09:30", "10:00", "10:30",
            "11:00", "11:30", "13:00", "13:30",
            "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30"
        };
    
        String query = """
            SELECT time_slot
            FROM Appointments
            WHERE doctor_id = ? AND appointment_date = ?
        """;
        
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
    
            stmt.setString(1, doctorId);
            stmt.setString(2, formattedDate);
            ResultSet rs = stmt.executeQuery();
    
            java.util.Set<String> takenSlots = new java.util.HashSet<>();
            while (rs.next()) {
                takenSlots.add(rs.getString("time_slot"));
            }
    
            // Get current time if date is today
            boolean isToday = formattedDate.equals(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
            java.time.LocalTime now = java.time.LocalTime.now();
    
            for (String slot : allSlots) {
                String dbSlotFormat = slot + ":00";
                if (!takenSlots.contains(dbSlotFormat)) {
                    if (isToday) {
                        java.time.LocalTime slotTime = java.time.LocalTime.parse(slot);
                        if (slotTime.isAfter(now)) {
                            slots.add(slot);
                        }
                    } else {
                        slots.add(slot);
                    }
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return slots;
    }
    
            
    public String getPatientName(String patientId) {
        String fullName = "Patient";
        String query = "SELECT first_name, last_name FROM Patients WHERE patient_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                fullName = rs.getString("first_name") + " " + rs.getString("last_name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fullName;
    }

   

    
    
    public double getCurrentBalance(String patientId) {
        String query = "SELECT credits FROM Patients WHERE patient_id = ?";
        double balance = 0;
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
    
            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("credits");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return balance;
    }
}