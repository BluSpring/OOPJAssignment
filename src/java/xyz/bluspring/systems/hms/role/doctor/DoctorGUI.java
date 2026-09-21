package xyz.bluspring.systems.hms.role.doctor;

import javax.swing.*;
import java.awt.*;

public class DoctorGUI extends JFrame {

    private Doctor doctor;

    public DoctorGUI(Doctor doctor) {

        this.doctor = doctor;

        setTitle("Doctor Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));

        JButton profileButton = new JButton("Edit Profile");
        JButton vitalButton = new JButton("Log Vital Signs");
        JButton noteButton = new JButton("Add Consultation Note");
        JButton prescriptionButton = new JButton("Issue Prescription");
        JButton testButton = new JButton("Request Medical Test");
        JButton exitButton = new JButton("Exit");

        panel.add(profileButton);
        panel.add(vitalButton);
        panel.add(noteButton);
        panel.add(prescriptionButton);
        panel.add(testButton);
        panel.add(exitButton);

        add(panel);

        profileButton.addActionListener(e -> editProfile());
        vitalButton.addActionListener(e -> logVitalSigns());
        noteButton.addActionListener(e -> addConsultationNote());
        prescriptionButton.addActionListener(e -> issuePrescription());
        testButton.addActionListener(e -> requestMedicalTest());

        exitButton.addActionListener(e -> System.exit(0));
    }

    private void editProfile() {

        String name = JOptionPane.showInputDialog(
            this,
            "Enter your name:"
        );

        if (name == null) {
            return;
        }

        String address = JOptionPane.showInputDialog(
            this,
            "Enter your address:"
        );

        if (address == null) {
            return;
        }

        doctor.updateProfile(name, address);

        JOptionPane.showMessageDialog(
            this,
            "Profile updated successfully!"
        );
    }

    private void logVitalSigns() {

        String patientId = JOptionPane.showInputDialog(
            this,
            "Enter Patient ID:"
        );

        if (patientId == null) {
            return;
        }

        String temperatureText = JOptionPane.showInputDialog(
            this,
            "Enter Temperature:"
        );

        String heartRateText = JOptionPane.showInputDialog(
            this,
            "Enter Heart Rate:"
        );

        String bloodPressureText = JOptionPane.showInputDialog(
            this,
            "Enter Blood Pressure:"
        );

        String oxygenLevelText = JOptionPane.showInputDialog(
            this,
            "Enter Oxygen Level:"
        );

        try {

            double temperature =
                Double.parseDouble(temperatureText);

            int heartRate =
                Integer.parseInt(heartRateText);

            int bloodPressure =
                Integer.parseInt(bloodPressureText);

            int oxygenLevel =
                Integer.parseInt(oxygenLevelText);

            doctor.logVitalSign(
                patientId,
                temperature,
                heartRate,
                bloodPressure,
                oxygenLevel
            );

            JOptionPane.showMessageDialog(
                this,
                "Vital signs saved successfully!"
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter valid numbers.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addConsultationNote() {

        String patientId = JOptionPane.showInputDialog(
            this,
            "Enter Patient ID:"
        );

        if (patientId == null) {
            return;
        }

        String date = JOptionPane.showInputDialog(
            this,
            "Enter Date:"
        );

        if (date == null) {
            return;
        }

        String notes = JOptionPane.showInputDialog(
            this,
            "Enter Consultation Notes:"
        );

        if (notes == null) {
            return;
        }

        doctor.addConsultationNote(
            patientId,
            date,
            notes
        );

        JOptionPane.showMessageDialog(
            this,
            "Consultation note saved successfully!"
        );
    }

    private void issuePrescription() {

        String patientId = JOptionPane.showInputDialog(
            this,
            "Enter Patient ID:"
        );

        if (patientId == null) {
            return;
        }

        String medication = JOptionPane.showInputDialog(
            this,
            "Enter Medication:"
        );

        if (medication == null) {
            return;
        }

        String dosage = JOptionPane.showInputDialog(
            this,
            "Enter Dosage:"
        );

        if (dosage == null) {
            return;
        }

        String instructions = JOptionPane.showInputDialog(
            this,
            "Enter Instructions:"
        );

        if (instructions == null) {
            return;
        }

        doctor.issuePrescription(
            patientId,
            medication,
            dosage,
            instructions
        );

        JOptionPane.showMessageDialog(
            this,
            "Prescription saved successfully!"
        );
    }

    private void requestMedicalTest() {

        String patientId = JOptionPane.showInputDialog(
            this,
            "Enter Patient ID:"
        );

        if (patientId == null) {
            return;
        }

        String testType = JOptionPane.showInputDialog(
            this,
            "Enter Test Type:"
        );

        if (testType == null) {
            return;
        }

        String reason = JOptionPane.showInputDialog(
            this,
            "Enter Reason:"
        );

        if (reason == null) {
            return;
        }

        doctor.requestMedicalTest(
            patientId,
            testType,
            reason
        );

        JOptionPane.showMessageDialog(
            this,
            "Medical test request saved successfully!"
        );
    }
}
