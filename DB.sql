-- Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025
DROP DATABASE IF EXISTS Hospital;
CREATE DATABASE Hospital;
USE Hospital;

-- Users table
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE,
    password_hash CHAR(64),
    salt CHAR(32), 
    role ENUM('admin', 'patient', 'doctor')
);

-- Department table
CREATE TABLE Department (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    department_name VARCHAR(100),
    department_description TEXT
);

-- Doctors table
CREATE TABLE Doctors (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    start_date TIMESTAMP,
    gender ENUM('Male', 'Female', 'Other'),
    phone_number VARCHAR(20),
    department_id INT,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) on delete cascade,
    FOREIGN KEY (department_id) REFERENCES Department(department_id) on delete cascade
);

-- Patients table
CREATE TABLE Patients (
    patient_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    date_of_birth DATE,
    gender ENUM('Male', 'Female', 'Other'),
    phone_number VARCHAR(20),
    credits DECIMAL(10, 2) DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) on delete cascade
);


-- Appointments
CREATE TABLE Appointments (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT,
    doctor_id INT,
    appointment_date DATE,
    time_slot TIME,  -- New column to store the time slot for the appointment
    appointment_status ENUM('Pending', 'Confirmed', 'Completed', 'Cancelled'),
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id) on delete cascade,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) on delete cascade
);


-- Medicines
CREATE TABLE Medicines (
    medicine_id INT PRIMARY KEY AUTO_INCREMENT,
    medicine_name VARCHAR(100)
);

-- Prescriptions
CREATE TABLE Prescriptions (
    prescription_id INT PRIMARY KEY AUTO_INCREMENT,
    appointment_id INT,
    medicine_id INT,
    dosage VARCHAR(100),
    prescription_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    consultation TEXT,
    diagnosis TEXT,
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id) on delete cascade,
    FOREIGN KEY (medicine_id) REFERENCES Medicines(medicine_id) on delete cascade 
);

-- Credit Transfers
CREATE TABLE CreditTransfers (
    transfer_id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT,
    receiver_id INT,
    amount DECIMAL(10, 2),
    transfer_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES Patients(patient_id) on delete cascade,
    FOREIGN KEY (receiver_id) REFERENCES Patients(patient_id) on delete cascade
);

-- Populating the Database

-- Users
INSERT INTO Users (username, password_hash, salt, role)
VALUES 
('admin1', SHA2(CONCAT('adminpass', 'randomsalt1'), 256), 'randomsalt1', 'admin'),
('drsmith', SHA2(CONCAT('docpass1', 'randomsalt2'), 256), 'randomsalt2', 'doctor'),
('drjohnson', SHA2(CONCAT('docpass2', 'randomsalt3'), 256), 'randomsalt3', 'doctor'),
('patientjane', SHA2(CONCAT('patient1', 'randomsalt4'), 256), 'randomsalt4', 'patient'),
('patientbob', SHA2(CONCAT('patient2', 'randomsalt5'), 256), 'randomsalt5', 'patient');

-- Departments
INSERT INTO Department (department_name, department_description)
VALUES 
('Cardiology', 'Heart and blood vessel care'),
('Neurology', 'Brain and nervous system treatments');

-- Doctors
INSERT INTO Doctors (user_id, first_name, last_name, start_date, gender, phone_number, department_id, description)
VALUES
(2, 'John', 'Smith', NOW(), 'Male', '555-1234', 1, 'Cardiology specialist'),
(3, 'Emily', 'Johnson', NOW(), 'Female', '555-5678', 2, 'Neurology expert');

-- Patients
INSERT INTO Patients (user_id, first_name, last_name, date_of_birth, gender, phone_number, credits)
VALUES
(4, 'Jane', 'Doe', '1990-06-15', 'Female', '555-1111', 100.00),
(5, 'Bob', 'Brown', '1985-02-20', 'Male', '555-2222', 50.00);

-- Appointments
INSERT INTO Appointments (patient_id, doctor_id, appointment_date, time_slot, appointment_status)
VALUES
(1, 2, '2025-04-10', '10:00:00', 'Confirmed'),  -- Jane (patient_id = 4) with Dr. Smith (doctor_id = 2)
(2, 1, '2025-04-12', '14:30:00', 'Pending');    -- Bob (patient_id = 5) with Dr. Johnson (doctor_id = 3)



-- Medicines
INSERT INTO Medicines (medicine_name)
VALUES 
('Aspirin'),
('Paracetamol'),
('Metformin');

-- Prescriptions
INSERT INTO Prescriptions (appointment_id, medicine_id, dosage, consultation, diagnosis)
VALUES
(1, 1, '100mg twice daily', 'Chest pain discussed', 'Angina'),
(2, 2, '500mg as needed', 'Headache complaint', 'Migraine');

-- Credit Transfers
INSERT INTO CreditTransfers (sender_id, receiver_id, amount)
VALUES 
(1, 2, 25.00);  -- Jane sent 25 to Bob