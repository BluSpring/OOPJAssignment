package xyz.bluspring.systems.hms.role.patient;

import javax.swing.*;
import java.awt.*;

public class PatientDashboard extends JPanel {
    public PatientDashboard(Patient patient) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        var name = new JLabel("Name: " + patient.getProfile().getDisplayName());
        var address = new JLabel("Address: " + patient.getProfile().getAddress());
        var dob = new JLabel("Date of Birth: " + patient.getDateOfBirth());
        var history = new JLabel("Medical History: " + patient.getMedicalHistory());

        String doctorName = (patient.getAssignedDoctor() != null)
            ? patient.getAssignedDoctor().getProfile().getDisplayName()
            : "Not assigned";
        var doctor = new JLabel("Assigned Doctor: " + doctorName);

        for (JLabel label : new JLabel[]{name, address, dob, history, doctor}) {
            label.setForeground(Color.BLACK);
            this.add(label);
        }
    }
}
