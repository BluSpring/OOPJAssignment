package xyz.bluspring.systems.hms.role.doctor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xyz.bluspring.systems.hms.data.DoctorDataStorage;
import xyz.bluspring.systems.hms.data.RoleManager;
import xyz.bluspring.systems.hms.data.records.MedicalRecord;
import xyz.bluspring.systems.hms.data.records.Prescription;
import xyz.bluspring.systems.hms.role.PersonalizableUser;
import xyz.bluspring.systems.hms.role.Profile;
import xyz.bluspring.systems.hms.role.manager.MedicalManager;
import xyz.bluspring.systems.hms.role.patient.Patient;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class Doctor extends PersonalizableUser<Doctor> {
    public static final DataSerializer<Doctor> SERIALIZER = RecordDataSerializer.of(
        Profile.SERIALIZER, Doctor::getProfile,
        DataSerializer.STRING, Doctor::getSpecialization,
        MedicalManager.REFERENCE_SERIALIZER, Doctor::getAssignedManager,
        Doctor::new
    );

    public static final DataSerializer<Doctor> REFERENCE_SERIALIZER = DataSerializer.STRING
        .map(RoleManager.INSTANCE::findDoctorById, doctor -> doctor != null ? doctor.getDoctorId() : null);

    private String specialization;
    private MedicalManager assignedManager;

    public Doctor(Profile profile, String specialization, MedicalManager assignedManager) {
        super(profile);

        this.specialization = specialization;
        this.assignedManager = assignedManager;
    }

    @Override
    public DataSerializer<Doctor> getSerializer() {
        return SERIALIZER;
    }

    public MedicalManager getAssignedManager() {
        return assignedManager;
    }

    public void setAssignedManager(MedicalManager manager) {
        this.assignedManager = manager;
    }

    public String getDoctorId() {
        return this.getProfile().getId().toString();
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void updateProfile(String displayName, String address) {
        getProfile().setDisplayName(displayName);
        getProfile().setAddress(address);
    }

    public String getPatientInformation(Patient patient) {
        return "Patient ID: " + patient.getPatientId()
            + "\nAge: " + patient.getAge()
            + "\nGender: " + patient.getGender()
            + "\nPhone Number: " + patient.getPhoneNumber();
    }

    public void logVitalSign(
        Patient patient,
        double temperature,
        int heartRate,
        int bloodPressure,
        int oxygenLevel) {

        VitalSign vitalSign = new VitalSign(patient, temperature, heartRate, bloodPressure, oxygenLevel);

        DoctorDataStorage.INSTANCE.saveVitalSign(vitalSign);
    }

    public void addConsultationNote(
        Patient patient,
        Date date,
        String notes) {

        ConsultationNote note = new ConsultationNote(
            patient,
            this,
            date,
            notes
        );

        DoctorDataStorage.INSTANCE.saveConsultationNote(note);
    }

    public void issuePrescription(
        Patient patient,
        String medication,
        String dosage,
        String instructions) {

        Prescription prescription = new Prescription(
            new Date(),
            this,
            patient,
            medication,
            dosage,
            instructions
        );

        patient.addMedicalRecord(prescription);
        RoleManager.INSTANCE.save();
    }

    public void requestMedicalTest(
        Patient patient,
        String testType,
        String reason) {

        MedicalTestRequest request = new MedicalTestRequest(
            patient,
            this,
            testType,
            reason,
            "Pending"
        );

        DoctorDataStorage.INSTANCE.saveMedicalTestRequest(request);
    }

    public List<Prescription> getPrescriptions() {
        List<Prescription> prescriptions = new ArrayList<>();

        for (Patient patient : RoleManager.INSTANCE.getPatients()) {
            for (MedicalRecord record : patient.getMedicalRecords()) {
                if (record instanceof Prescription prescription && prescription.getDoctor() == this) {
                    prescriptions.add(prescription);
                }
            }
        }

        return prescriptions;
    }

    @Override
    public String toString() {
        return "Dr. " + getProfile().getDisplayName()
            + " - " + specialization;
    }
}
