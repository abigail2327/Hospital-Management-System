// Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025
import java.util.Date;
import java.util.List;

public class Group9PatientBL {
    private Group9PatientDL patientDL;

    public Group9PatientBL() {
        patientDL = new Group9PatientDL();
    }

    public String getPatientName(String patientId) {
        return patientDL.getPatientName(patientId);
    }

    public List<Object[]> getAllAppointmentsForPatient(String patientId) {
        return patientDL.getAllAppointmentsForPatient(patientId);
    }

    public List<Object[]> getPrescriptionsForPatient(String patientId) {
        return patientDL.getPrescriptionsForPatient(patientId);
    }

    public List<String> getDoctorList() {
        return patientDL.getDoctorList();
    }

    public List<String> getAvailableSlots(String doctorId, Date date) {
        return patientDL.getAvailableSlots(doctorId, date);
    }

    public boolean bookAppointment(String patientId, String doctorId, String datetime) {
        return patientDL.bookAppointment(patientId, doctorId, datetime);
    }

    public double getCurrentBalance(String patientId) {
        return patientDL.getCurrentBalance(patientId);
    }

    public boolean executeTransfer(String senderId, String receiverId, double amount) throws java.sql.SQLException {
        return patientDL.executeTransfer(senderId, receiverId, amount);
    }

    public List<String> getOtherPatients(String patientId) {
        return patientDL.getOtherPatients(patientId);
    }

    public List<String[]> getStyledTransferHistory(String patientId) {
        return patientDL.getStyledTransferHistory(patientId);
    }

    public List<String> fetchPrescriptionByAppointmentId(int appointmentId) {
        return patientDL.fetchPrescriptionByAppointmentId(appointmentId);
    }
}
