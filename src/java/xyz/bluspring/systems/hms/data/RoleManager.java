package xyz.bluspring.systems.hms.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xyz.bluspring.systems.hms.LoginScreen;
import xyz.bluspring.systems.hms.auth.AccountType;
import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.role.manager.MedicalManager;
import xyz.bluspring.systems.hms.role.patient.Patient;
import xyz.bluspring.systems.hms.utils.data.DataSerializers;

public class RoleManager {
    private static final File DOCTORS_FILE = DataSerializers.getPath("doctors.txt");
    private static final File MANAGERS_FILE = DataSerializers.getPath("managers.txt");
    private static final File PATIENTS_FILE = DataSerializers.getPath("patients.txt");
    private static final File ADMINS_FILE = DataSerializers.getPath("admins.txt");

    public static final RoleManager INSTANCE = new RoleManager();

    private RoleManager() {
    }

    private final List<Doctor> doctors = new ArrayList<>();
    private final List<MedicalManager> managers = new ArrayList<>();
    private final List<Patient> patients = new ArrayList<>();

    public void load() {
        doctors.clear();
        managers.clear();
        patients.clear();

        DataSerializers.deserializeLines(Doctor.SERIALIZER, DOCTORS_FILE, doctors);
        DataSerializers.deserializeLines(MedicalManager.SERIALIZER, MANAGERS_FILE, managers);
        DataSerializers.deserializeLines(Patient.SERIALIZER, PATIENTS_FILE, patients);
    }

    public void save() {
        DataSerializers.serializeValues(DOCTORS_FILE, doctors);
        DataSerializers.serializeValues(MANAGERS_FILE, managers);
        DataSerializers.serializeValues(PATIENTS_FILE, patients);
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public List<MedicalManager> getManagers() {
        return managers;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public MedicalManager findManagerById(String id) {
        for (MedicalManager manager : managers) {
            if (LoginScreen.getAuthManager(AccountType.MEDICAL_MANAGER).getAccountByUUID(manager.getProfile().getId()) == null) {
                continue;
            }

            if (manager.getManagerId().equals(id)) {
                return manager;
            }
        }

        return null;
    }

    public Doctor findDoctorById(String id) {
        for (Doctor doctor : doctors) {
            if (LoginScreen.getAuthManager(AccountType.DOCTOR).getAccountByUUID(doctor.getProfile().getId()) == null) {
                continue;
            }

            if (doctor.getDoctorId().equals(id)) {
                return doctor;
            }
        }

        return null;
    }

    public Patient findPatientById(String id) {
        for (Patient patient : patients) {
            if (LoginScreen.getAuthManager(AccountType.PATIENT).getAccountByUUID(patient.getProfile().getId()) == null) {
                continue;
            }

            if (patient.getPatientId().equals(id)) {
                return patient;
            }
        }

        return null;
    }
}
