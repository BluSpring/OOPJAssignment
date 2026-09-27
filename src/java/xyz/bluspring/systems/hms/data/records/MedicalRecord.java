package xyz.bluspring.systems.hms.data.records;

import java.util.Date;

import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.role.patient.Patient;
import xyz.bluspring.systems.hms.utils.data.DataSerializable;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;

public abstract class MedicalRecord implements DataSerializable<MedicalRecord> {
    public static final DataSerializer<MedicalRecord> SERIALIZER = Type.SERIALIZER.dispatch(Type::getSerializer, MedicalRecord::getType);

    private final Date dateIssued;
    private final Doctor doctor;
    private final Patient patient;

    public MedicalRecord(Date dateIssued, Doctor doctor, Patient patient) {
        this.dateIssued = dateIssued;
        this.doctor = doctor;
        this.patient = patient;
    }

    public Date getDateIssued() {
        return dateIssued;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    protected String getDoctorName() {
        return (doctor != null && doctor.getProfile() != null)
            ? doctor.getProfile().getDisplayName()
            : "Unknown doctor";
    }

    public abstract Type getType();

    @Override
    public DataSerializer<MedicalRecord> getSerializer() {
        return (DataSerializer<MedicalRecord>) this.getType().getSerializer();
    }

    public abstract String getSummary();

    public enum Type {
        PRESCRIPTION(Prescription.SERIALIZER), VISIT_NOTE(VisitNote.SERIALIZER);

        private final DataSerializer<? extends MedicalRecord> serializer;

        Type(DataSerializer<? extends MedicalRecord> serializer) {
            this.serializer = serializer;
        }

        public DataSerializer<? extends MedicalRecord> getSerializer() {
            return serializer;
        }

        public static final DataSerializer<Type> SERIALIZER = DataSerializer.fromEnum(Type.class);
    }
}
