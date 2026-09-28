package xyz.bluspring.systems.hms.role.doctor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import xyz.bluspring.systems.hms.Main;
import xyz.bluspring.systems.hms.data.records.MedicalTestType;
import xyz.bluspring.systems.hms.role.patient.Patient;
import xyz.bluspring.systems.hms.ui.ComponentHelper;
import xyz.bluspring.systems.hms.utils.Utils;

public class DoctorGUI extends JPanel {

    private final Doctor doctor;
    private final Patient patient;

    private JTextField displayNameField;
    private JTextField addressField;

    private JTextField temperatureField;
    private JTextField heartRateField;
    private JTextField bloodPressureField;
    private JTextField oxygenLevelField;

    private JTextArea consultationNotesArea;

    private JTextField medicationField;
    private JTextField dosageField;
    private JTextArea instructionsArea;

    private JComboBox<MedicalTestType> testTypeBox;
    private JTextArea testReasonArea;

    public DoctorGUI(Doctor doctor, Patient patient) {

        this.doctor = doctor;
        this.patient = patient;

        createGUI();
    }

    private void createGUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.setBorder(
            new EmptyBorder(15, 15, 15, 15)
        );

        var headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));

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

        headerPanel.add(titleLabel);
        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(e -> Main.logout(this));

        headerPanel.add(Box.createHorizontalStrut(15));
        headerPanel.add(logoutButton);

        this.add(headerPanel);
        this.add(Box.createVerticalStrut(15));

        var sidewayPanel = new JPanel();
        sidewayPanel.setLayout(new BoxLayout(sidewayPanel, BoxLayout.X_AXIS));

        var leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        var rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        leftPanel.add(createPatientPanel());
        leftPanel.add(Box.createVerticalStrut(10));

        rightPanel.add(createProfilePanel());
        rightPanel.add(Box.createVerticalStrut(10));

        rightPanel.add(createVitalSignsPanel());
        rightPanel.add(Box.createVerticalStrut(10));

        rightPanel.add(createConsultationPanel());
        rightPanel.add(Box.createVerticalStrut(10));

        rightPanel.add(createPrescriptionPanel());
        rightPanel.add(Box.createVerticalStrut(10));

        rightPanel.add(createMedicalTestPanel());

        var scrollableRight = new JScrollPane(rightPanel);
        scrollableRight.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(leftPanel);
        this.add(scrollableRight);
    }

    private JScrollPane createPatientPanel() {

        JPanel panel = createSectionPanel("Patient Information");

        panel.add(new JLabel("Name: " + patient.getProfile().getDisplayName()));
        panel.add(new JLabel("Age: " + patient.getAge()));
        panel.add(new JLabel("Gender: " + patient.getGender()));

        panel.add(Utils.make(new JTextArea("Medical History: " + patient.getMedicalHistory()), area -> {
            area.setEditable(false);
        }));

        var pane = new JScrollPane(panel);
        pane.setPreferredSize(new Dimension(795, 100));
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return pane;
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

        temperatureField = new JTextField();
        heartRateField = new JTextField();
        bloodPressureField = new JTextField();
        oxygenLevelField = new JTextField();

        ComponentHelper.configureFieldFilters(temperatureField, c -> Character.isDigit(c) || c == '.' || c == ',');
        ComponentHelper.configureFieldFilters(heartRateField, Character::isDigit);
        ComponentHelper.configureFieldFilters(bloodPressureField, Character::isDigit);
        ComponentHelper.configureFieldFilters(oxygenLevelField, Character::isDigit);

        JButton saveButton =
            new JButton("Save Vital Signs");

        panel.add(
            new JLabel("Temperature (°C):")
        );

        panel.add(temperatureField);

        panel.add(
            new JLabel("Heart Rate (bpm):")
        );

        panel.add(heartRateField);

        panel.add(
            new JLabel("Blood Pressure (mmHg):")
        );

        panel.add(bloodPressureField);

        panel.add(
            new JLabel("Oxygen Level (%):")
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

        consultationNotesArea =
            new JTextArea(4, 20);

        consultationNotesArea.setLineWrap(true);
        consultationNotesArea.setWrapStyleWord(true);

        JButton saveButton =
            new JButton("Save Consultation Note");

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
        String notes =
            consultationNotesArea
                .getText()
                .trim();

        if (notes.isEmpty()) {

            showError(
                "Please enter the consultation notes."
            );

            return;
        }

        try {

            SimpleDateFormat format =
                new SimpleDateFormat(
                    "dd/MM/yyyy"
                );

            format.setLenient(false);

            Date date = Date.from(Instant.now());

            doctor.addConsultationNote(
                patient,
                date,
                notes
            );

            JOptionPane.showMessageDialog(
                this,
                "Consultation note saved successfully!"
            );

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

        testTypeBox = new JComboBox<>(MedicalTestType.values());

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

        panel.add(testTypeBox);

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

        MedicalTestType testType = (MedicalTestType) testTypeBox.getSelectedItem();

        String reason =
            testReasonArea
                .getText()
                .trim();

        if (testType == null
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
