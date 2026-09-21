package xyz.bluspring.systems.hms.role.doctor;

public class ConsultationNote {

    private String patientId;
    private String doctorId;
    private String date;
    private String notes;

    public ConsultationNote(String patientId,
                            String doctorId,
                            String date,
                            String notes) {

        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.notes = notes;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Patient ID: " + patientId
            + ", Doctor ID: " + doctorId
            + ", Date: " + date
            + ", Notes: " + notes;
    }
}
