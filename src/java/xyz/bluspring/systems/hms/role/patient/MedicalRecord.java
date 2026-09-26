package xyz.bluspring.systems.hms.role.patient;

import java.util.Date;

import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.utils.data.DataSerializable;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;

public abstract class MedicalRecord implements DataSerializable<MedicalRecord> {
    public static final DataSerializer<MedicalRecord> SERIALIZER = Type.SERIALIZER.dispatch(Type::getSerializer, MedicalRecord::getType);

    private Date dateIssued;
    private Doctor doctor;

    public MedicalRecord(Date dateIssued, Doctor doctor) {
        this.dateIssued = dateIssued;
        this.doctor = doctor;
    }

    public Date getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued) {
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
