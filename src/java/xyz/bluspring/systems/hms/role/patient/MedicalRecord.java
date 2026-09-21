package xyz.bluspring.systems.hms.role.patient;

import xyz.bluspring.systems.hms.role.doctor.Doctor;

public abstract class MedicalRecord {
    private String dateIssued;
    private Doctor doctor;

    public MedicalRecord(String dateIssued, Doctor doctor) {
        this.dateIssued = dateIssued;
        this.doctor = doctor;
    }

    public String getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(String dateIssued) {
        this.dateIssued = dateIssued;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    protected String getDoctorName() {
        return (doctor != null && doctor.getProfile() != null)
            ? doctor.getProfile().getDisplayName()
            : "Unknown doctor";
    }


    public abstract String getSummary();
}
