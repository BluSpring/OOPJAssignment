package xyz.bluspring.systems.hms.role.doctor;

import java.util.UUID;

import xyz.bluspring.systems.hms.data.DoctorDataStorage;
import xyz.bluspring.systems.hms.data.records.MedicalTestType;
import xyz.bluspring.systems.hms.data.records.TestStatus;
import xyz.bluspring.systems.hms.role.patient.Patient;
import xyz.bluspring.systems.hms.utils.data.DataSerializable;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class MedicalTestRequest implements DataSerializable<MedicalTestRequest> {
    public static final DataSerializer<MedicalTestRequest> SERIALIZER = RecordDataSerializer.of(
        DataSerializer.UUID_SERIALIZER, MedicalTestRequest::getId,
        Patient.REFERENCE_SERIALIZER, MedicalTestRequest::getPatient,
        Doctor.REFERENCE_SERIALIZER, MedicalTestRequest::getDoctor,
        MedicalTestType.SERIALIZER, MedicalTestRequest::getTestType,
        DataSerializer.STRING, MedicalTestRequest::getReason,
        TestStatus.SERIALIZER, MedicalTestRequest::getStatus,
        MedicalTestRequest::new
    );

    public static final DataSerializer<MedicalTestRequest> REFERENCE_SERIALIZER = DataSerializer.UUID_SERIALIZER
        .map(uuid -> {
            for (MedicalTestRequest request : DoctorDataStorage.INSTANCE.getMedicalTestRequests()) {
                if (request.getId().equals(uuid)) {
                    return request;
                }
            }

            return null;
        }, request -> request != null ? request.getId() : null);

    private final UUID id;
    private final Patient patient;
    private final Doctor doctor;
    private final MedicalTestType testType;
    private final String reason;
    private TestStatus status = TestStatus.REQUESTED;

    private MedicalTestRequest(UUID id, Patient patient,
                               Doctor doctor,
                               MedicalTestType testType,
                               String reason,
                               TestStatus status) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.testType = testType;
        this.reason = reason;
        this.status = status;
    }

    public MedicalTestRequest(Patient patient, Doctor doctor, MedicalTestType testType, String reason) {
        this(UUID.randomUUID(), patient, doctor, testType, reason, TestStatus.REQUESTED);
    }

    @Override
    public DataSerializer<MedicalTestRequest> getSerializer() {
        return SERIALIZER;
    }

    public UUID getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public MedicalTestType getTestType() {
        return testType;
    }

    public String getReason() {
        return reason;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Patient ID: " + patient.getPatientId()
            + ", Doctor ID: " + doctor.getDoctorId()
            + ", Test Type: " + testType.getProperName()
            + ", Reason: " + reason
            + ", Status: " + status.getProperName();
    }
}
