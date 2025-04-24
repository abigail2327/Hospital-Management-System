## Getting Started

To run the project, you only need to:
Open src/ConnectionManager.java
Modify the database URL, username, and password in the getConnection() method to match your DBMS setup.

## Folder Structure
src: Contains all Java source files
ConnectionManager.java: Handles MySQL connection setup.
Login.java: Entry point for login functionality.
PatientUI.java, DoctorDashboard.java, HospitalAdmin.java: Presentation Layer (PL) — user interfaces for Patient, Doctor, and Admin.
PatientDL.java, DoctorDL.java, HospitalAdminDL.java: Data Layer (DL) — contains database-related methods.
PatientBL.java, DoctorBL.java, HospitalAdminBL.java: Business Layer (BL) — handles business logic between UI and data access.
Appointment.java, Doctor.java: ORM classes representing entities in the database.

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management
jcalendar-1.4.jar: Required for calendar date selection components.

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
