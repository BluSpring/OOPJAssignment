package xyz.bluspring.systems.hms.role.doctor;

import xyz.bluspring.systems.hms.role.patient.Patient;

import javax.swing.SwingUtilities;

public class DoctorMain {

    public static void main(String[] args) {

        Doctor doctor = new Doctor(
                "D001",
                "General Medicine"
        );

        doctor.getProfile().setDisplayName("Dr. Ahmed");

        Patient patient = new Patient(
                "P001",
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
