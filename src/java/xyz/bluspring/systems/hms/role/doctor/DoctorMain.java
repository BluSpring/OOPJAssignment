package xyz.bluspring.systems.hms.role.doctor;

import xyz.bluspring.systems.hms.role.Profile;
import xyz.bluspring.systems.hms.role.manager.MedicalManager;
import xyz.bluspring.systems.hms.role.patient.Patient;

import javax.swing.SwingUtilities;
import java.util.UUID;

public class DoctorMain {

    public static void main(String[] args) {

        Profile managerProfile = new Profile(
            UUID.randomUUID(),
            "Kuala Lumpur",
            "Medical Manager"
        );

        MedicalManager medicalManager = new MedicalManager(
            managerProfile
        );

        Profile doctorProfile = new Profile(
            UUID.randomUUID(),
            "Kuala Lumpur",
            "Dr. Ahmed"
        );

        Doctor doctor = new Doctor(
            doctorProfile,
            "General Medicine",
            medicalManager
        );

        Profile patientProfile = new Profile(
            UUID.randomUUID(),
            "Kuala Lumpur",
            "Patient One"
        );

        Patient patient = new Patient(
            patientProfile,
            25,
            "Male",
            "0123456789"
        );

        SwingUtilities.invokeLater(() -> {
            DoctorGUI gui = new DoctorGUI(doctor, patient);
            gui.setVisible(true);
        });
    }
}
