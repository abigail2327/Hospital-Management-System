-- Group 9 - Abigail Da Costa (772001345), Praislin Peter (771003933), Kyaw Thu Hein (396006747) - April 20, 2025
USE Hospital;
-- Admin Stored Procedures
DROP PROCEDURE IF EXISTS AddPatient;
DELIMITER //
CREATE PROCEDURE AddPatient(
    IN p_first_name VARCHAR(50),
    IN p_last_name VARCHAR(50),
    IN p_dob DATE,
    IN p_gender ENUM('Male','Female'),
    IN p_phone VARCHAR(15),
    IN p_credits INT
)
BEGIN
    INSERT INTO Patients(first_name, last_name, date_of_birth, gender, phone_number, credits)
    VALUES(p_first_name, p_last_name, p_dob, p_gender, p_phone, p_credits);
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS UpdatePatient;
DELIMITER //
CREATE PROCEDURE UpdatePatient(
    IN p_patient_id INT,
    IN p_first_name VARCHAR(50),
    IN p_last_name VARCHAR(50),
    IN p_dob DATE,
    IN p_gender ENUM('Male','Female'),
    IN p_phone VARCHAR(15),
    IN p_credits INT
)
BEGIN
    UPDATE Patients
    SET first_name = p_first_name,
        last_name = p_last_name,
        date_of_birth = p_dob,
        gender = p_gender,
        phone_number = p_phone,
        credits = p_credits
    WHERE patient_id = p_patient_id;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS GetDoctorDetails;
-- GetDoctorDetails Procedure
DELIMITER //
CREATE PROCEDURE GetDoctorDetails(IN p_doctor_id INT)
BEGIN
    SELECT 
        d.doctor_id,
        d.first_name,
        d.last_name,
        d.gender,
        d.phone_number,
        d.department_id,
        dept.department_name,
        d.description
    FROM Doctors d
    JOIN Department dept ON d.department_id = dept.department_id
    WHERE d.doctor_id = p_doctor_id;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS AddDoctor;
-- AddDoctor Stored Procedure,handles doctor creation with multiple validations:
DELIMITER //
CREATE PROCEDURE AddDoctor(
    IN p_first_name VARCHAR(50),
    IN p_last_name VARCHAR(50),
    IN p_gender ENUM('Male','Female'),
    IN p_phone VARCHAR(15),
    IN p_department_id INT,
    IN p_description TEXT
)
BEGIN
    INSERT INTO Doctors(
        first_name, 
        last_name, 
        gender, 
        phone_number, 
        department_id, 
        description
    )
    VALUES(
        p_first_name,
        p_last_name,
        p_gender,
        p_phone,
        p_department_id,
        COALESCE(p_description, 'No description provided')
    );
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS DisplayTableByName;
DELIMITER //
CREATE PROCEDURE DisplayTableByName(IN table_name VARCHAR(100))
BEGIN
    SET @sql = CONCAT('SELECT * FROM ', table_name);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END //
DELIMITER ;

-- Doctor Stored Procedures
DROP PROCEDURE IF EXISTS changeApptStatus;

DELIMITER //

CREATE PROCEDURE changeApptStatus ( IN p_appointmentID INT, IN newStatus VARCHAR(50))
BEGIN
    UPDATE Appointments
    SET appointment_status = newStatus
    WHERE appointment_id = p_appointmentID;
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS AddPrescription;
DELIMITER //

CREATE PROCEDURE AddPrescription(IN p_appointment_id INT, IN p_medicine_id INT,IN p_dosage VARCHAR(100),IN p_consultation TEXT,
    IN p_diagnosis TEXT)
BEGIN
    DECLARE appt_exists INT;
    DECLARE med_exists INT;

    SELECT COUNT(*) INTO appt_exists
    FROM Appointments
    WHERE appointment_id = p_appointment_id;

    SELECT COUNT(*) INTO med_exists
    FROM Medicines
    WHERE medicine_id = p_medicine_id;

    IF appt_exists = 1 AND med_exists = 1 THEN
        INSERT INTO Prescriptions (appointment_id,medicine_id,dosage,consultation,diagnosis,prescription_date)
        VALUES (p_appointment_id,p_medicine_id,p_dosage,p_consultation,p_diagnosis,NOW());
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid appointment_id or medicine_id';
    END IF;
END //

DELIMITER ;

-- Patient Stored Procedures
DROP PROCEDURE IF EXISTS TransferCredits;
DELIMITER //

CREATE PROCEDURE TransferCredits (
    IN sender_id VARCHAR(36),
    IN receiver_id VARCHAR(36),
    IN amount DOUBLE,
    OUT success BOOLEAN
)
BEGIN
    DECLARE sender_balance DOUBLE;

    START TRANSACTION;

    -- Get sender's current credit balance
    SELECT credits INTO sender_balance
    FROM Patients
    WHERE patient_id = sender_id;

    IF sender_balance >= amount THEN
        -- Deduct from sender
        UPDATE Patients
        SET credits = credits - amount
        WHERE patient_id = sender_id;

        -- Add to receiver
        UPDATE Patients
        SET credits = credits + amount
        WHERE patient_id = receiver_id;

        -- Log the transfer
        INSERT INTO CreditTransfers (sender_id, receiver_id, amount)
        VALUES (sender_id, receiver_id, amount);

        SET success = TRUE;
        COMMIT;
    ELSE
        SET success = FALSE;
        ROLLBACK;
    END IF;
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS bookAppointment;
DELIMITER //

CREATE PROCEDURE bookAppointment (
    IN p_patient_id VARCHAR(36),
    IN p_doctor_id VARCHAR(36),
    IN p_date DATE,
    IN p_time TIME,
    OUT p_success BOOLEAN
)
BEGIN
    DECLARE conflict_count INT;

    -- Check for existing appointment
    SELECT COUNT(*) INTO conflict_count
    FROM Appointments
    WHERE patient_id = p_patient_id
      AND appointment_date = p_date
      AND time_slot = p_time;

    IF conflict_count > 0 THEN
        SET p_success = FALSE;
    ELSE
        INSERT INTO Appointments (
            patient_id, doctor_id, appointment_date, time_slot, appointment_status
        ) VALUES (
            p_patient_id, p_doctor_id, p_date, p_time, 'Pending'
        );
        SET p_success = TRUE;
    END IF;
END //

DELIMITER ;

-- Triggers
-- Create audit_log table if not exists
CREATE TABLE IF NOT EXISTS audit_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    action_type VARCHAR(10),
    table_name VARCHAR(50),
    record_id INT,
    old_value TEXT,
    action_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Audit trigger for Patients table
DELIMITER //
CREATE TRIGGER audit_delete_patients
AFTER DELETE ON Patients
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (action_type, table_name, record_id, old_value)
    VALUES ('DELETE', 'Patients', OLD.patient_id,
           CONCAT('first_name: ', OLD.first_name, 
                  ', last_name: ', OLD.last_name,
                  ', dob: ', OLD.date_of_birth,
                  ', gender: ', OLD.gender,
                  ', phone: ', OLD.phone_number,
                  ', credits: ', OLD.credits));
END //
DELIMITER ;

-- Audit trigger for Doctors table
DELIMITER //
CREATE TRIGGER audit_delete_doctors
AFTER DELETE ON Doctors
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (action_type, table_name, record_id, old_value)
    VALUES ('DELETE', 'Doctors', OLD.doctor_id,
           CONCAT('first_name: ', OLD.first_name, 
                  ', last_name: ', OLD.last_name,
                  ', gender: ', OLD.gender,
                  ', phone: ', OLD.phone_number,
                  ', department_id: ', OLD.department_id));
END //
DELIMITER ;





