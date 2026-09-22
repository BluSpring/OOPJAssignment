package xyz.bluspring.systems.hms.role.doctor;

import xyz.bluspring.systems.hms.role.PersonalizableUser;
import xyz.bluspring.systems.hms.role.patient.Patient;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends PersonalizableUser {

    private String doctorId;
    private String specialization;

    private List<VitalSign> vitalSigns;
    private List<ConsultationNote> consultationNotes;
    private List<Prescription> prescriptions;
    private List<MedicalTestRequest> medicalTestRequests;

    private DoctorDataStorage storage;

    public Doctor(String doctorId, String specialization) {
        this.doctorId = doctorId;
        this.specialization = specialization;

        vitalSigns = new ArrayList<>();
        consultationNotes = new ArrayList<>();
        prescriptions = new ArrayList<>();
        medicalTestRequests = new ArrayList<>();

        storage = new DoctorDataStorage();
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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

    public void logVitalSign(Patient patient,
                             double temperature,
                             int heartRate,
                             int bloodPressure,
                             int oxygenLevel) {

        VitalSign vitalSign = new VitalSign(
            patient.getPatientId(),
            temperature,
            heartRate,
            bloodPressure,
            oxygenLevel
        );

        vitalSigns.add(vitalSign);
        storage.saveVitalSign(vitalSign);
    }

    public void addConsultationNote(Patient patient,
                                    String date,
                                    String notes) {

        ConsultationNote note = new ConsultationNote(
            patient.getPatientId(),
            doctorId,
            date,
            notes
        );

        consultationNotes.add(note);
        storage.saveConsultationNote(note);
    }

    public void issuePrescription(Patient patient,
                                  String medication,
                                  String dosage,
                                  String instructions) {

        Prescription prescription = new Prescription(
            patient.getPatientId(),
            doctorId,
            medication,
            dosage,
            instructions
        );

        prescriptions.add(prescription);
        storage.savePrescription(prescription);
    }

    public void requestMedicalTest(Patient patient,
                                   String testType,
                                   String reason) {

        MedicalTestRequest request = new MedicalTestRequest(
            patient.getPatientId(),
            doctorId,
            testType,
            reason,
            "Pending"
        );

        medicalTestRequests.add(request);
        storage.saveMedicalTestRequest(request);
    }

    public List<VitalSign> getVitalSigns() {
        return vitalSigns;
    }

    public List<ConsultationNote> getConsultationNotes() {
        return consultationNotes;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public List<MedicalTestRequest> getMedicalTestRequests() {
        return medicalTestRequests;
    }

    @Override
    public String toString() {
        return "Dr. " + getProfile().getDisplayName()
            + " - " + specialization;
    }
}
