package xyz.bluspring.systems.hms.role.patient;

import xyz.bluspring.systems.hms.role.PersonalizableUser;
import xyz.bluspring.systems.hms.role.doctor.Doctor;

import java.util.ArrayList;
import java.util.List;

public class Patient extends PersonalizableUser {
    private String dateOfBirth;
    private String medicalHistory;
    private Doctor assignedDoctor;

    // Holds Prescription AND VisitNote objects interchangeably — this list
    // doesn't care which subclass each record actually is (polymorphism).
    private final List<MedicalRecord> medicalRecords = new ArrayList<>();

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public Doctor getAssignedDoctor() {
        return assignedDoctor;
    }

    public void setAssignedDoctor(Doctor assignedDoctor) {
        this.assignedDoctor = assignedDoctor;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
    }

    public void removeMedicalRecord(MedicalRecord record) {
        medicalRecords.remove(record);
    }
}

