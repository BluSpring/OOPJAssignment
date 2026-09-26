package xyz.bluspring.systems.hms.role.doctor;

import java.util.ArrayList;
import java.util.List;

import xyz.bluspring.systems.hms.role.PersonalizableUser;
import xyz.bluspring.systems.hms.role.Profile;
import xyz.bluspring.systems.hms.role.RoleManager;
import xyz.bluspring.systems.hms.role.patient.Patient;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class Doctor extends PersonalizableUser<Doctor> {
    public static final DataSerializer<Doctor> SERIALIZER = RecordDataSerializer.of(
        Profile.SERIALIZER, Doctor::getProfile,
        DataSerializer.STRING, Doctor::getDoctorId,
        DataSerializer.STRING, Doctor::getSpecialization,
        Doctor::new
    );

    public static final DataSerializer<Doctor> REFERENCE_SERIALIZER = DataSerializer.STRING.map(RoleManager.INSTANCE::findDoctorById, Doctor::getDoctorId);

    private String doctorId;
    private String specialization;

    private final List<VitalSign> vitalSigns;
    private final List<ConsultationNote> consultationNotes;
    private final List<Prescription> prescriptions;
    private final List<MedicalTestRequest> medicalTestRequests;

    private final DoctorDataStorage storage;

    public Doctor(Profile profile, String doctorId, String specialization) {
        super(profile);

        this.doctorId = doctorId;
        this.specialization = specialization;

        vitalSigns = new ArrayList<>();
        consultationNotes = new ArrayList<>();
        prescriptions = new ArrayList<>();
        medicalTestRequests = new ArrayList<>();

        storage = new DoctorDataStorage();
    }

    @Override
    public DataSerializer<Doctor> getSerializer() {
        return SERIALIZER;
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

    public void logVitalSign(
        Patient patient,
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

    public void addConsultationNote(
        Patient patient,
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

    public void issuePrescription(
        Patient patient,
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

    public void requestMedicalTest(
        Patient patient,
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
