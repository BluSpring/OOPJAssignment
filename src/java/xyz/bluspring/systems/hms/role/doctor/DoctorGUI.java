package xyz.bluspring.systems.hms.role.doctor;

import xyz.bluspring.systems.hms.role.patient.Patient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DoctorGUI extends JFrame {

    private final Doctor doctor;
    private final Patient patient;

    private JTextField displayNameField;
    private JTextField addressField;

    private JTextField temperatureField;
    private JTextField heartRateField;
    private JTextField bloodPressureField;
    private JTextField oxygenLevelField;

    private JTextField consultationDateField;
    private JTextArea consultationNotesArea;

    private JTextField medicationField;
    private JTextField dosageField;
    private JTextArea instructionsArea;

    private JTextField testTypeField;
    private JTextArea testReasonArea;

    public DoctorGUI(Doctor doctor, Patient patient) {

        this.doctor = doctor;
        this.patient = patient;

        setTitle("Doctor Dashboard");
        setSize(800, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(
            new BoxLayout(mainPanel, BoxLayout.Y_AXIS)
        );

        mainPanel.setBorder(
            new EmptyBorder(15, 15, 15, 15)
        );

        JLabel titleLabel = new JLabel(
            "Doctor Dashboard - "
                + doctor.getProfile().getDisplayName()
        );

        titleLabel.setFont(
            new Font("Arial", Font.BOLD, 22)
        );

        titleLabel.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createPatientPanel());
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(createProfilePanel());
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(createVitalSignsPanel());
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(createConsultationPanel());
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(createPrescriptionPanel());
        mainPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(createMedicalTestPanel());

        JScrollPane scrollPane =
            new JScrollPane(mainPanel);

        scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        add(scrollPane);
    }

    private JPanel createPatientPanel() {

        JPanel panel =
            createSectionPanel("Patient Information");

        JTextArea patientInformation =
            new JTextArea(
                doctor.getPatientInformation(patient)
            );

        patientInformation.setEditable(false);
        patientInformation.setRows(4);
        patientInformation.setLineWrap(true);
        patientInformation.setWrapStyleWord(true);

        panel.add(
            new JLabel("Patient Details:")
        );

        panel.add(patientInformation);

        return panel;
    }

    private JPanel createProfilePanel() {

        JPanel panel =
            createSectionPanel("Doctor Profile");

        displayNameField =
            new JTextField(
                doctor.getProfile().getDisplayName()
            );

        addressField =
            new JTextField(
                doctor.getProfile().getAddress()
            );

        JButton updateButton =
            new JButton("Update Profile");

        panel.add(
            new JLabel("Display Name:")
        );

        panel.add(displayNameField);

        panel.add(
            new JLabel("Address:")
        );

        panel.add(addressField);

        panel.add(new JLabel(""));
        panel.add(updateButton);

        updateButton.addActionListener(
            e -> updateProfile()
        );

        return panel;
    }

    private void updateProfile() {

        String displayName =
            displayNameField.getText().trim();

        String address =
            addressField.getText().trim();

        if (displayName.isEmpty()
            || address.isEmpty()) {

            showError(
                "Please enter the display name and address."
            );

            return;
        }

        doctor.updateProfile(
            displayName,
            address
        );

        JOptionPane.showMessageDialog(
            this,
            "Profile updated successfully!"
        );
    }

    private JPanel createVitalSignsPanel() {

        JPanel panel =
            createSectionPanel("Vital Signs");

        temperatureField =
            new JTextField();

        heartRateField =
            new JTextField();

        bloodPressureField =
            new JTextField();

        oxygenLevelField =
            new JTextField();

        JButton saveButton =
            new JButton("Save Vital Signs");

        panel.add(
            new JLabel("Temperature:")
        );

        panel.add(temperatureField);

        panel.add(
            new JLabel("Heart Rate:")
        );

        panel.add(heartRateField);

        panel.add(
            new JLabel("Blood Pressure:")
        );

        panel.add(bloodPressureField);

        panel.add(
            new JLabel("Oxygen Level:")
        );

        panel.add(oxygenLevelField);

        panel.add(new JLabel(""));
        panel.add(saveButton);

        saveButton.addActionListener(
            e -> saveVitalSigns()
        );

        return panel;
    }

    private void saveVitalSigns() {

        try {

            double temperature =
                Double.parseDouble(
                    temperatureField
                        .getText()
                        .trim()
                );

            int heartRate =
                Integer.parseInt(
                    heartRateField
                        .getText()
                        .trim()
                );

            int bloodPressure =
                Integer.parseInt(
                    bloodPressureField
                        .getText()
                        .trim()
                );

            int oxygenLevel =
                Integer.parseInt(
                    oxygenLevelField
                        .getText()
                        .trim()
                );

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

            temperatureField.setText("");
            heartRateField.setText("");
            bloodPressureField.setText("");
            oxygenLevelField.setText("");

        } catch (NumberFormatException e) {

            showError(
                "Please enter valid numbers for all vital signs."
            );
        }
    }

    private JPanel createConsultationPanel() {

        JPanel panel =
            createSectionPanel("Consultation Note");

        consultationDateField =
            new JTextField();

        consultationDateField.setToolTipText(
            "DD/MM/YYYY"
        );

        consultationNotesArea =
            new JTextArea(4, 20);

        consultationNotesArea.setLineWrap(true);
        consultationNotesArea.setWrapStyleWord(true);

        JButton saveButton =
            new JButton("Save Consultation Note");

        panel.add(
            new JLabel("Date (DD/MM/YYYY):")
        );

        panel.add(consultationDateField);

        panel.add(
            new JLabel("Notes:")
        );

        panel.add(
            new JScrollPane(
                consultationNotesArea
            )
        );

        panel.add(new JLabel(""));
        panel.add(saveButton);

        saveButton.addActionListener(
            e -> saveConsultationNote()
        );

        return panel;
    }

    private void saveConsultationNote() {

        String dateText =
            consultationDateField
                .getText()
                .trim();

        String notes =
            consultationNotesArea
                .getText()
                .trim();

        if (dateText.isEmpty()
            || notes.isEmpty()) {

            showError(
                "Please enter the date and consultation notes."
            );

            return;
        }

        try {

            SimpleDateFormat format =
                new SimpleDateFormat(
                    "dd/MM/yyyy"
                );

            format.setLenient(false);

            Date date =
                format.parse(dateText);

            doctor.addConsultationNote(
                patient,
                date,
                notes
            );

            JOptionPane.showMessageDialog(
                this,
                "Consultation note saved successfully!"
            );

            consultationDateField.setText("");
            consultationNotesArea.setText("");

        } catch (Exception e) {

            showError(
                "Please enter the date as DD/MM/YYYY."
            );
        }
    }

    private JPanel createPrescriptionPanel() {

        JPanel panel =
            createSectionPanel("Prescription");

        medicationField =
            new JTextField();

        dosageField =
            new JTextField();

        instructionsArea =
            new JTextArea(3, 20);

        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);

        JButton issueButton =
            new JButton("Issue Prescription");

        panel.add(
            new JLabel("Medication:")
        );

        panel.add(medicationField);

        panel.add(
            new JLabel("Dosage:")
        );

        panel.add(dosageField);

        panel.add(
            new JLabel("Instructions:")
        );

        panel.add(
            new JScrollPane(
                instructionsArea
            )
        );

        panel.add(new JLabel(""));
        panel.add(issueButton);

        issueButton.addActionListener(
            e -> issuePrescription()
        );

        return panel;
    }

    private void issuePrescription() {

        String medication =
            medicationField
                .getText()
                .trim();

        String dosage =
            dosageField
                .getText()
                .trim();

        String instructions =
            instructionsArea
                .getText()
                .trim();

        if (medication.isEmpty()
            || dosage.isEmpty()
            || instructions.isEmpty()) {

            showError(
                "Please complete all prescription fields."
            );

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

        medicationField.setText("");
        dosageField.setText("");
        instructionsArea.setText("");
    }

    private JPanel createMedicalTestPanel() {

        JPanel panel =
            createSectionPanel(
                "Medical Test Request"
            );

        testTypeField =
            new JTextField();

        testReasonArea =
            new JTextArea(3, 20);

        testReasonArea.setLineWrap(true);
        testReasonArea.setWrapStyleWord(true);

        JButton requestButton =
            new JButton(
                "Request Medical Test"
            );

        panel.add(
            new JLabel("Test Type:")
        );

        panel.add(testTypeField);

        panel.add(
            new JLabel("Reason:")
        );

        panel.add(
            new JScrollPane(
                testReasonArea
            )
        );

        panel.add(new JLabel(""));
        panel.add(requestButton);

        requestButton.addActionListener(
            e -> requestMedicalTest()
        );

        return panel;
    }

    private void requestMedicalTest() {

        String testType =
            testTypeField
                .getText()
                .trim();

        String reason =
            testReasonArea
                .getText()
                .trim();

        if (testType.isEmpty()
            || reason.isEmpty()) {

            showError(
                "Please enter the test type and reason."
            );

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

        testTypeField.setText("");
        testReasonArea.setText("");
    }

    private JPanel createSectionPanel(
        String title
    ) {

        JPanel panel =
            new JPanel(
                new GridLayout(
                    0,
                    2,
                    10,
                    8
                )
            );

        panel.setBorder(
            BorderFactory
                .createTitledBorder(title)
        );

        panel.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                230
            )
        );

        panel.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        return panel;
    }

    private void showError(
        String message
    ) {

        JOptionPane.showMessageDialog(
            this,
            message,
            "Input Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
