package xyz.bluspring.systems.hms.role.patient;

import xyz.bluspring.systems.hms.role.PersonalizableUser;
import xyz.bluspring.systems.hms.role.doctor.Doctor;

public class Patient extends PersonalizableUser {
    private String dateOfBirth;
    private String medicalHistory;
    private Doctor assignedDoctor;
}
