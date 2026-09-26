package xyz.bluspring.systems.hms.role.doctor;

import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import xyz.bluspring.systems.hms.role.patient.Patient;

public class DoctorGUI extends JFrame {

    private Doctor doctor;
    private Patient patient;

    public DoctorGUI(Doctor doctor, Patient patient) {
        this.doctor = doctor;
        this.patient = patient;

        setTitle("Doctor Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));

        JButton profileButton = new JButton("Edit Profile");
        JButton patientButton = new JButton("View Patient Information");
        JButton vitalButton = new JButton("Log Vital Signs");
        JButton noteButton = new JButton("Add Consultation Note");
        JButton prescriptionButton = new JButton("Issue Prescription");
        JButton testButton = new JButton("Request Medical Test");

        panel.add(profileButton);
        panel.add(patientButton);
        panel.add(vitalButton);
        panel.add(noteButton);
        panel.add(prescriptionButton);
        panel.add(testButton);

        add(panel);

        profileButton.addActionListener(e -> editProfile());
        patientButton.addActionListener(e -> viewPatientInformation());
        vitalButton.addActionListener(e -> logVitalSigns());
        noteButton.addActionListener(e -> addConsultationNote());
        prescriptionButton.addActionListener(e -> issuePrescription());
        testButton.addActionListener(e -> requestMedicalTest());
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

    private void viewPatientInformation() {
        String information = doctor.getPatientInformation(patient);

        JOptionPane.showMessageDialog(
            this,
            information,
            "Patient Information",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void logVitalSigns() {

        String temperatureText = JOptionPane.showInputDialog(
            this,
            "Enter Temperature:"
        );

        if (temperatureText == null) {
            return;
        }

        String heartRateText = JOptionPane.showInputDialog(
            this,
            "Enter Heart Rate:"
        );

        if (heartRateText == null) {
            return;
        }

        String bloodPressureText = JOptionPane.showInputDialog(
            this,
            "Enter Blood Pressure:"
        );

        if (bloodPressureText == null) {
            return;
        }

        String oxygenLevelText = JOptionPane.showInputDialog(
            this,
            "Enter Oxygen Level:"
        );

        if (oxygenLevelText == null) {
            return;
        }

        try {
            double temperature = Double.parseDouble(temperatureText);
            int heartRate = Integer.parseInt(heartRateText);
            int bloodPressure = Integer.parseInt(bloodPressureText);
            int oxygenLevel = Integer.parseInt(oxygenLevelText);

            doctor.logVitalSign(
                patient,
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

        Date date = null;

        while (date == null) {
            try {
                date = DateFormat.getDateInstance().parse(JOptionPane.showInputDialog(
                    this,
                    "Enter Date:"
                ));
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date inserted!");
            }
        }

        String notes = JOptionPane.showInputDialog(
            this,
            "Enter Consultation Notes:"
        );

        if (notes == null) {
            return;
        }

        doctor.addConsultationNote(
            patient,
            date,
            notes
        );

        JOptionPane.showMessageDialog(
            this,
            "Consultation note saved successfully!"
        );
    }

    private void issuePrescription() {

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
            patient,
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
            patient,
            testType,
            reason
        );

        JOptionPane.showMessageDialog(
            this,
            "Medical test request saved successfully!"
        );
    }
}
