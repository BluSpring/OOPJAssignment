package xyz.bluspring.systems.hms.role.patient;

import xyz.bluspring.systems.hms.role.doctor.Doctor;

public class VisitNote extends MedicalRecord {
    private String reason;
    private String diagnosis;

    public VisitNote(String dateIssued, Doctor doctor, String reason, String diagnosis) {
        super(dateIssued, doctor);
        this.reason = reason;
        this.diagnosis = diagnosis;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    @Override
    public String getSummary() {
        return getDateIssued() + " - Visit for \"" + reason + "\" - Diagnosis: " + diagnosis + " (Dr. " + getDoctorName() + ")";
    }
}

