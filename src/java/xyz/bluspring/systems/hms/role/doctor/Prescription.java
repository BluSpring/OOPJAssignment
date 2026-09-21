package xyz.bluspring.systems.hms.role.doctor;

public class Prescription {

    private String patientId;
    private String doctorId;
    private String medication;
    private String dosage;
    private String instructions;

    public Prescription(String patientId,
                        String doctorId,
                        String medication,
                        String dosage,
                        String instructions) {

        this.patientId = patientId;
        this.doctorId = doctorId;
        this.medication = medication;
        this.dosage = dosage;
        this.instructions = instructions;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "Patient ID: " + patientId
            + ", Doctor ID: " + doctorId
            + ", Medication: " + medication
            + ", Dosage: " + dosage
            + ", Instructions: " + instructions;
    }
}
