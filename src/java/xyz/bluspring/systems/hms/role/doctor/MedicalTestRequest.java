package xyz.bluspring.systems.hms.role.doctor;

import xyz.bluspring.systems.hms.role.patient.Patient;
import xyz.bluspring.systems.hms.utils.data.DataSerializable;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class MedicalTestRequest implements DataSerializable<MedicalTestRequest> {
    public static final DataSerializer<MedicalTestRequest> SERIALIZER = RecordDataSerializer.of(
        Patient.REFERENCE_SERIALIZER, MedicalTestRequest::getPatient,
        Doctor.REFERENCE_SERIALIZER, MedicalTestRequest::getDoctor,
        DataSerializer.STRING, MedicalTestRequest::getTestType,
        DataSerializer.STRING, MedicalTestRequest::getReason,
        DataSerializer.STRING, MedicalTestRequest::getStatus,
        MedicalTestRequest::new
    );

    private final Patient patient;
    private final Doctor doctor;
    private String testType;
    private String reason;
    private String status;

    public MedicalTestRequest(Patient patient,
                              Doctor doctor,
                              String testType,
                              String reason,
                              String status) {

        this.patient = patient;
        this.doctor = doctor;
        this.testType = testType;
        this.reason = reason;
        this.status = status;
    }

    @Override
    public DataSerializer<MedicalTestRequest> getSerializer() {
        return SERIALIZER;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
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
        return "Patient ID: " + patient.getPatientId()
            + ", Doctor ID: " + doctor.getDoctorId()
            + ", Test Type: " + testType
            + ", Reason: " + reason
            + ", Status: " + status;
    }
}
