// Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025
import javax.swing.*;
import java.sql.*;

public class Group9AdminBL {
    private final Group9AdminDL hospitalAdminDL;

    public Group9AdminBL() {
        hospitalAdminDL = new Group9AdminDL();
    }
    // Patient operations
    public void loadAllPatients(JTable table, JFrame frame) {
        hospitalAdminDL.loadAllPatients(table, frame);
    }
    public boolean addPatient(String firstName, String lastName, Date dob, String gender, String phone, double credits) {
        return hospitalAdminDL.addPatient(firstName, lastName, dob, gender, phone, credits);
    }
    public boolean updatePatient(String patientId, String firstName, String lastName, Date dob, String gender, String phone, double credits) {
        return hospitalAdminDL.updatePatient(patientId, firstName, lastName, dob, gender, phone, credits);
    }
    public boolean deletePatient(String patientId) {
        return hospitalAdminDL.deletePatient(patientId);
    }
    public ResultSet getPatientById(String patientId) throws SQLException {
        return hospitalAdminDL.getPatientById(patientId);
    }
    // Doctor operations
    public void loadAllDoctors(JTable table, JFrame frame) {
        hospitalAdminDL.loadAllDoctors(table, frame);
    }
    public boolean addDoctor(String firstName, String lastName, String gender, String phone, int deptId, String desc) {
        return hospitalAdminDL.addDoctor(firstName, lastName, gender, phone, deptId, desc);
    }
    public boolean updateDoctor(String doctorId, String firstName, String lastName, String gender, String phone, int deptId, String desc) {
        return hospitalAdminDL.updateDoctor(doctorId, firstName, lastName, gender, phone, deptId, desc);
    }
    public boolean deleteDoctor(String doctorId) {
        return hospitalAdminDL.deleteDoctor(doctorId);
    }
    public ResultSet getDoctorById(String doctorId) throws SQLException {
        return hospitalAdminDL.getDoctorById(doctorId);
    }
    // Department operations
    public void loadAllDepartments(JTable table, JFrame frame) {
        hospitalAdminDL.loadAllDepartments(table, frame);
    }

    public boolean addDepartment(String name, String desc) {
        return hospitalAdminDL.addDepartment(name, desc);
    }

    public boolean updateDepartment(String deptId, String name, String desc) {
        return hospitalAdminDL.updateDepartment(deptId, name, desc);
    }

    public boolean deleteDepartment(String deptId) {
        return hospitalAdminDL.deleteDepartment(deptId);
    }

    public ResultSet getDepartmentById(String deptId) throws SQLException {
        return hospitalAdminDL.getDepartmentById(deptId);
    }

    // Medicine operations
    public void loadAllMedicines(JTable table, JFrame frame) {
        hospitalAdminDL.loadAllMedicines(table, frame);
    }

    public boolean addMedicine(String name) {
        return hospitalAdminDL.addMedicine(name);
    }

    public boolean deleteMedicine(String medicineId) {
        return hospitalAdminDL.deleteMedicine(medicineId);
    }

    // Other data loading
    public void loadAllAppointments(JTable table, JFrame frame) {
        hospitalAdminDL.loadAllAppointments(table, frame);
    }

    public void loadAllPrescriptions(JTable table, JFrame frame) {
        hospitalAdminDL.loadAllPrescriptions(table, frame);
    }

    public void loadAllCreditTransfers(JTable table, JFrame frame) {
        hospitalAdminDL.loadAllCreditTransfers(table, frame);
    }

    public void loadAllUsers(JTable table, JFrame frame) {
        hospitalAdminDL.loadAllUsers(table, frame);
    }
    
}