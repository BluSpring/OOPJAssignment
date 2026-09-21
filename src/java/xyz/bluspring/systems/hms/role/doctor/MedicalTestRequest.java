package xyz.bluspring.systems.hms.role.doctor;

public class MedicalTestRequest {

    private String patientId;
    private String doctorId;
    private String testType;
    private String reason;
    private String status;

    public MedicalTestRequest(String patientId,
                              String doctorId,
                              String testType,
                              String reason,
                              String status) {

        this.patientId = patientId;
        this.doctorId = doctorId;
        this.testType = testType;
        this.reason = reason;
        this.status = status;
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

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Patient ID: " + patientId
            + ", Doctor ID: " + doctorId
            + ", Test Type: " + testType
            + ", Reason: " + reason
            + ", Status: " + status;
    }
}
