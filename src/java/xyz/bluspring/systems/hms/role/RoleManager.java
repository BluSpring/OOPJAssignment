package xyz.bluspring.systems.hms.role;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xyz.bluspring.systems.hms.role.admin.Admin;
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

    public final List<Doctor> doctors = new ArrayList<>();
    public final List<MedicalManager> managers = new ArrayList<>();
    public final List<Patient> patients = new ArrayList<>();
    public final List<Admin> admins = new ArrayList<>();

    public void load() {
        doctors.clear();
        managers.clear();
        patients.clear();
        admins.clear();

        DataSerializers.deserializeLines(Doctor.SERIALIZER, DOCTORS_FILE, doctors);
        DataSerializers.deserializeLines(MedicalManager.SERIALIZER, MANAGERS_FILE, managers);
        DataSerializers.deserializeLines(Patient.SERIALIZER, PATIENTS_FILE, patients);
//        DataSerializers.deserializeLines(Admin.SERIALIZER, ADMINS_FILE, admins);
    }

    public void save() {
        DataSerializers.serializeValues(DOCTORS_FILE, doctors);
        DataSerializers.serializeValues(MANAGERS_FILE, managers);
        DataSerializers.serializeValues(PATIENTS_FILE, patients);
//        DataSerializers.serializeValues(ADMINS_FILE, admins);
    }

    public Doctor findDoctorById(String id) {
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorId().equals(id)) {
                return doctor;
            }
        }

        return null;
    }

    public Patient findPatientById(String id) {
        for (Patient patient : patients) {
            if (patient.getPatientId().equals(id)) {
                return patient;
            }
        }

        return null;
    }
}
