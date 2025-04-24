// Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025
import java.sql.Timestamp;

public class Doctor {
    private int doctorId;
    private int userId;
    private String firstName;
    private String lastName;
    private Timestamp startDate;
    private String gender;
    private String phoneNumber;
    private int departmentId;
    private String description;

    // Constructor
    public Doctor(int doctorId, int userId, String firstName, String lastName, Timestamp startDate,
                  String gender, String phoneNumber, int departmentId, String description) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.startDate = startDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.departmentId = departmentId;
        this.description = description;
    }

    // Getters
    public int getDoctorId() { return doctorId; }
    public int getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Timestamp getStartDate() { return startDate; }
    public String getGender() { return gender; }
    public String getPhoneNumber() { return phoneNumber; }
    public int getDepartmentId() { return departmentId; }
    public String getDescription() { return description; }
    public String getFullName() { return firstName + " " + lastName; }

    // Setters
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setStartDate(Timestamp startDate) { this.startDate = startDate; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", fullName='" + getFullName() + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phoneNumber + '\'' +
                ", departmentId=" + departmentId +
                ", startDate=" + startDate +
                ", description='" + description + '\'' +
                '}';
    }
}
