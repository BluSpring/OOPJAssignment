package xyz.bluspring.systems.hms.role.patient;

import xyz.bluspring.systems.hms.role.doctor.Doctor;

public class Prescription extends MedicalRecord {
    private String medicineName;
    private String dosage;
    private String notes;

    public Prescription(String dateIssued, Doctor doctor, String medicineName, String dosage, String notes) {
        super(dateIssued, doctor);
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.notes = notes;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String getSummary() {
        return getDateIssued() + " - Prescribed " + medicineName + " (" + dosage + ") by Dr. " + getDoctorName();
    }
}
