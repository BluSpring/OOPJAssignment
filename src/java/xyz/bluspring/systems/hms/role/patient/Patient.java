package xyz.bluspring.systems.hms.role.patient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xyz.bluspring.systems.hms.role.PersonalizableUser;
import xyz.bluspring.systems.hms.role.Profile;
import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class Patient extends PersonalizableUser<Patient> {
    public static final DataSerializer<Patient> SERIALIZER = RecordDataSerializer.of(
        Profile.SERIALIZER, Patient::getProfile,
        DataSerializer.STRING, Patient::getPatientId,
        DataSerializer.INT, Patient::getAge,
        DataSerializer.STRING, Patient::getGender,
        DataSerializer.STRING, Patient::getPhoneNumber,

        DataSerializer.DATE, Patient::getDateOfBirth,
        DataSerializer.STRING, Patient::getMedicalHistory,
        Doctor.REFERENCE_SERIALIZER, Patient::getAssignedDoctor,
        MedicalRecord.SERIALIZER.list(), Patient::getMedicalRecords,
        Rating.SERIALIZER.list(), Patient::getRatings,
        Patient::new
    );

    // Fields used by the Doctor role
    private String patientId;
    private int age;
    private String gender;
    private String phoneNumber;

    // Fields used by the Patient role
    private Date dateOfBirth;
    private String medicalHistory;
    private Doctor assignedDoctor;
    private final List<MedicalRecord> medicalRecords = new ArrayList<>();
    private final List<Rating> ratings = new ArrayList<>();

    private Patient(Profile profile, String patientId, int age, String gender, String phoneNumber, Date dateOfBirth, String medicalHistory, Doctor assignedDoctor, List<MedicalRecord> records, List<Rating> ratings) {
        this(profile, patientId, age, gender, phoneNumber);
        this.dateOfBirth = dateOfBirth;
        this.medicalHistory = medicalHistory;
        this.assignedDoctor = assignedDoctor;
        this.medicalRecords.addAll(records);
        this.ratings.addAll(ratings);
    }

    public Patient(Profile profile, String patientId, int age, String gender, String phoneNumber) {
        super(profile);
        this.patientId = patientId;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public DataSerializer<Patient> getSerializer() {
        return SERIALIZER;
    }

    // --- Doctor-role fields ---

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // --- Patient-role fields ---

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public Doctor getAssignedDoctor() {
        return assignedDoctor;
    }

    public void setAssignedDoctor(Doctor assignedDoctor) {
        this.assignedDoctor = assignedDoctor;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
    }

    public void removeMedicalRecord(MedicalRecord record) {
        medicalRecords.remove(record);
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
    }

    // --- Shared behaviour ---

    public void updateProfile(String displayName, String address,
                              int age, String gender, String phoneNumber) {
        getProfile().setDisplayName(displayName);
        getProfile().setAddress(address);

        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public void displayPatientInfo() {
        System.out.println("Patient ID: " + patientId);
        System.out.println("Age: " + age);
        System.out.println("Gender: " + gender);
        System.out.println("Phone Number: " + phoneNumber);
    }

    public void displayProfile() {
        System.out.println("===== PATIENT PROFILE =====");
        System.out.println("Patient ID: " + patientId);
        System.out.println("Name: " + getProfile().getDisplayName());
        System.out.println("Address: " + getProfile().getAddress());
        System.out.println("Age: " + age);
        System.out.println("Gender: " + gender);
        System.out.println("Phone Number: " + phoneNumber);
    }
}


