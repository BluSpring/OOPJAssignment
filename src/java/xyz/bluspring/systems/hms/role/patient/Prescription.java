package xyz.bluspring.systems.hms.role.patient;

import java.util.Date;

import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class Prescription extends MedicalRecord {
    public static final DataSerializer<Prescription> SERIALIZER = RecordDataSerializer.of(
        DataSerializer.DATE, Prescription::getDateIssued,
        Doctor.REFERENCE_SERIALIZER, Prescription::getDoctor,
        Patient.REFERENCE_SERIALIZER, Prescription::getPatient,
        DataSerializer.STRING, Prescription::getMedicineName,
        DataSerializer.STRING, Prescription::getDosage,
        DataSerializer.STRING, Prescription::getNotes,
        Prescription::new
    );

    private String medicineName;
    private String dosage;
    private String notes;

    public Prescription(Date dateIssued, Doctor doctor, Patient patient, String medicineName, String dosage, String notes) {
        super(dateIssued, doctor, patient);
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.notes = notes;
    }

    @Override
    public Type getType() {
        return Type.PRESCRIPTION;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String getSummary() {
        return getDateIssued() + " - Prescribed " + medicineName + " (" + dosage + ") by Dr. " + getDoctorName();
    }
}
