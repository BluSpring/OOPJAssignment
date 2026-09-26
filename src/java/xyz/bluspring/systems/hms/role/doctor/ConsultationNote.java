package xyz.bluspring.systems.hms.role.doctor;

import java.util.Date;

import xyz.bluspring.systems.hms.role.patient.Patient;
import xyz.bluspring.systems.hms.utils.data.DataSerializable;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class ConsultationNote implements DataSerializable<ConsultationNote> {
    public static final DataSerializer<ConsultationNote> SERIALIZER = RecordDataSerializer.of(
        Patient.REFERENCE_SERIALIZER, ConsultationNote::getPatient,
        Doctor.REFERENCE_SERIALIZER, ConsultationNote::getDoctor,
        DataSerializer.DATE, ConsultationNote::getDate,
        DataSerializer.STRING, ConsultationNote::getNotes,
        ConsultationNote::new
    );

    private final Patient patient;
    private final Doctor doctor;
    private Date date;
    private String notes;

    public ConsultationNote(Patient patient,
                            Doctor doctor,
                            Date date,
                            String notes) {

        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.notes = notes;
    }

    @Override
    public DataSerializer<ConsultationNote> getSerializer() {
        return SERIALIZER;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
        return "Patient ID: " + patient.getPatientId()
            + ", Doctor ID: " + doctor.getDoctorId()
            + ", Date: " + date
            + ", Notes: " + notes;
    }
}
