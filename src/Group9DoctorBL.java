// Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025
import java.util.List;

public class Group9DoctorBL {
    private Group9DoctorDL doctorDL;

    public Group9DoctorBL() {
        doctorDL = new Group9DoctorDL();
    }

    public Doctor getDoctorById(int doctorId) {
        return doctorDL.getDoctorById(doctorId);
    }

    public List<Object[]> getAppointmentsForDoctor(int doctorId) {
        return doctorDL.getAppointmentsForDoctor(doctorId);
    }

    public Appointment getAppointmentById(int appointmentId) {
        return doctorDL.getAppointmentById(appointmentId);
    }

    public boolean updateAppointmentStatus(int appointmentId, String newStatus) {
        return doctorDL.updateAppointmentStatus(appointmentId, newStatus);
    }

    public boolean insertPrescription(int appointmentId, int medicineId, String dosage, String consultation, String diagnosis) {
        return doctorDL.insertPrescription(appointmentId, medicineId, dosage, consultation, diagnosis);
    }

    public String[] getPrescriptionByAppointmentId(int appointmentId) {
        return doctorDL.getPrescriptionByAppointmentId(appointmentId);
    }

    public List<String[]> fetchMedicines() {
        return doctorDL.fetchMedicines();
    }

    public List<Object[]> getPatientsForDoctor(int doctorId) {
        return doctorDL.getPatientsForDoctor(doctorId);
    }

    public List<String[]> getPrescriptionsByPatientId(int patientId) {
        return doctorDL.getPrescriptionsByPatientId(patientId);
    }
    public void disconnect() {
        doctorDL.disconnect();
    }
}
