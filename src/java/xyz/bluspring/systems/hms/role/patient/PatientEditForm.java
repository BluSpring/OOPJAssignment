package xyz.bluspring.systems.hms.role.patient;

import java.awt.Component;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.ParseException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PatientEditForm extends JPanel {
    private final Patient patient;

    private final JTextField nameField;
    private final JTextField addressField;
    private final JTextField dobField;
    private final JTextArea historyArea;

    public PatientEditForm(Patient patient) {
        this.patient = patient;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        nameField = new JTextField();
        addressField = new JTextField();
        dobField = new JTextField();
        historyArea = new JTextArea(4, 20);
        historyArea.setLineWrap(true);

        // Pre-fill with existing data, if any.
        if (patient.getProfile().getDisplayName() != null) {
            nameField.setText(patient.getProfile().getDisplayName());
        }
        if (patient.getProfile().getAddress() != null) {
            addressField.setText(patient.getProfile().getAddress());
        }
        if (patient.getDateOfBirth() != null) {
            dobField.setText(DateFormat.getDateInstance().format(patient.getDateOfBirth()));
        }
        if (patient.getMedicalHistory() != null) {
            historyArea.setText(patient.getMedicalHistory());
        }

        for (JTextField field : new JTextField[]{nameField, addressField, dobField}) {
            field.setMaximumSize(new Dimension(300, 30));
            field.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        historyArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveButton = new JButton("Save");
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.addActionListener(e -> save());

        this.add(new JLabel("Name:"));
        this.add(nameField);
        this.add(new JLabel("Address:"));
        this.add(addressField);
        this.add(new JLabel("Date of Birth:"));
        this.add(dobField);
        this.add(new JLabel("Medical History:"));
        this.add(new JScrollPane(historyArea));
        this.add(Box.createVerticalStrut(10));
        this.add(saveButton);
    }

    /**
     * Copies the values from the form fields back onto the Patient object.
     * Doesn't save to disk yet — persistence can be added later.
     */
    public void save() {
        patient.getProfile().setDisplayName(nameField.getText());
        patient.getProfile().setAddress(addressField.getText());
        try {
            patient.setDateOfBirth(DateFormat.getDateInstance().parse(dobField.getText()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        patient.setMedicalHistory(historyArea.getText());
    }
}
