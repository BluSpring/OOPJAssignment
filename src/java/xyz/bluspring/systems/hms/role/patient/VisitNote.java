package xyz.bluspring.systems.hms.role.patient;

import java.util.Date;

import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class VisitNote extends MedicalRecord {
    public static final DataSerializer<VisitNote> SERIALIZER = RecordDataSerializer.of(
        DataSerializer.DATE, VisitNote::getDateIssued,
        Doctor.REFERENCE_SERIALIZER, VisitNote::getDoctor,
        Patient.REFERENCE_SERIALIZER, VisitNote::getPatient,
        DataSerializer.STRING, VisitNote::getReason,
        DataSerializer.STRING, VisitNote::getDiagnosis,
        VisitNote::new
    );

    private String reason;
    private String diagnosis;

    public VisitNote(Date dateIssued, Doctor doctor, Patient patient, String reason, String diagnosis) {
        super(dateIssued, doctor, patient);
        this.reason = reason;
        this.diagnosis = diagnosis;
    }

    @Override
    public Type getType() {
        return Type.VISIT_NOTE;
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

