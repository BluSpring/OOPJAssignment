package xyz.bluspring.systems.hms.role.patient;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import xyz.bluspring.systems.hms.data.records.MedicalRecord;

public class PatientRecordsPanel extends JPanel {
    private final Patient patient;

    private final JLabel historyLabel;
    private final DefaultListModel<String> recordListModel;

    public PatientRecordsPanel(Patient patient) {
        this.patient = patient;

        this.setLayout(new BorderLayout(0, 10));

        historyLabel = new JLabel();
        historyLabel.setForeground(Color.BLACK);
        this.add(historyLabel, BorderLayout.NORTH);

        recordListModel = new DefaultListModel<>();
        var recordList = new JList<>(recordListModel);

        var listTitle = new JLabel("Medical Records:");
        listTitle.setForeground(Color.BLACK);

        var listPanel = new JPanel(new BorderLayout());
        listPanel.add(listTitle, BorderLayout.NORTH);
        listPanel.add(new JScrollPane(recordList), BorderLayout.CENTER);

        this.add(listPanel, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        String history = patient.getMedicalHistory();
        historyLabel.setText("Medical History: " + ((history == null || history.isBlank()) ? "None recorded" : history));

        recordListModel.clear();

        // Each 'record' here could be a Prescription OR a VisitNote — we don't
        // need to know or check which one. Calling getSummary() automatically
        // runs the correct version for whichever type it actually is.

        for (MedicalRecord record : patient.getMedicalRecords()) {
            recordListModel.addElement(record.getSummary());
        }
    }
}
