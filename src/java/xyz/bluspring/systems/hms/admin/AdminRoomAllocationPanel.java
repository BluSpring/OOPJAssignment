package xyz.bluspring.systems.hms.admin;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import xyz.bluspring.systems.hms.Main;
import xyz.bluspring.systems.hms.auth.Account;
import xyz.bluspring.systems.hms.data.AdminDataStorage;
import xyz.bluspring.systems.hms.data.DoctorDataStorage;
import xyz.bluspring.systems.hms.data.records.TestStatus;
import xyz.bluspring.systems.hms.data.room.HospitalRoom;
import xyz.bluspring.systems.hms.data.room.MultiDoctorAssignableRoom;
import xyz.bluspring.systems.hms.data.room.PatientAssignableRoom;
import xyz.bluspring.systems.hms.data.room.TestRequestableRoom;
import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.role.doctor.MedicalTestRequest;
import xyz.bluspring.systems.hms.utils.Utils;

public class AdminRoomAllocationPanel extends JPanel {
    public AdminRoomAllocationPanel(Account account) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        var rooms = new ArrayList<>(AdminDataStorage.INSTANCE.getHospitalRooms());

        {
            var requestsPanel = new JPanel();
            requestsPanel.setLayout(new BoxLayout(requestsPanel, BoxLayout.Y_AXIS));
            requestsPanel.setBorder(new TitledBorder("Doctor's Requests"));

            var requests = new ArrayList<>(DoctorDataStorage.INSTANCE.getMedicalTestRequests());
            requests.sort(Comparator.comparingInt(req -> req.getStatus().ordinal()));

            for (MedicalTestRequest request : requests) {
                var requestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                requestPanel.add(new JLabel("Doctor: " + request.getDoctor().getProfile().getDisplayName()));
                requestPanel.add(new JLabel("Patient: " + request.getPatient().getProfile().getDisplayName()));
                requestPanel.add(new JLabel("Test Type: " + request.getTestType().getProperName()));
                requestPanel.add(Utils.make(new JTextArea("Reason:  " + request.getReason()), area -> {
                    area.setEditable(false);
                }));
                requestPanel.add(new JLabel("Status: " + request.getStatus().getProperName()));

                if (request.getStatus() == TestStatus.REQUESTED) {
                    if (rooms.stream().anyMatch(r -> r.getType() == request.getTestType().getRequiredRoom() && !r.isOccupied())) {
                        requestPanel.add(Utils.make(new JButton("Auto-allocate Room"), button -> {
                            button.addActionListener(e -> {
                                var room = rooms.stream().filter(r -> r.getType() == request.getTestType().getRequiredRoom() && !r.isOccupied())
                                    .findFirst().orElseThrow();

                                if (room instanceof MultiDoctorAssignableRoom doctorAssignableRoom) {
                                    doctorAssignableRoom.addAssignedDoctor(request.getDoctor());
                                }

                                if (room instanceof PatientAssignableRoom patientAssignableRoom) {
                                    patientAssignableRoom.setAssignedPatient(request.getPatient());
                                }

                                request.setStatus(TestStatus.WAITING);

                                refreshPage(account);
                            });
                        }));
                    } else {
                        requestPanel.add(Utils.make(new JLabel("No rooms are currently available."), label -> label.setForeground(Color.RED)));
                    }
                }

                requestsPanel.add(requestPanel);
            }

            var scrollPane = new JScrollPane(requestsPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            this.add(scrollPane);
        }

        {
            var roomsPanel = new JPanel();
            roomsPanel.setLayout(new BoxLayout(roomsPanel, BoxLayout.Y_AXIS));
            roomsPanel.setBorder(new TitledBorder("Hospital Rooms"));

            rooms.sort(Comparator.comparing(HospitalRoom::isOccupied));
            for (HospitalRoom<?> room : rooms) {
                var roomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                roomPanel.add(new JLabel("Type: " + room.getType().getProperName()));

                if (room instanceof MultiDoctorAssignableRoom doctorAssignableRoom) {
                    roomPanel.add(new JLabel("Doctors: " + String.join(", ", doctorAssignableRoom.getAssignedDoctors().stream().map(Doctor::toString).toList())));
                }

                if (room instanceof PatientAssignableRoom patientAssignableRoom) {
                    roomPanel.add(new JLabel("Patient: " + patientAssignableRoom.getAssignedPatient().toString()));
                }

                if (room instanceof TestRequestableRoom<?> requestableRoom) {
                    if (requestableRoom.getCurrentRequest() != null) {
                        roomPanel.add(new JLabel("Currently running tests..."));
                    }
                }

                roomsPanel.add(roomPanel);
            }

            var scrollPane = new JScrollPane(roomsPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            this.add(scrollPane);
        }
    }

    private static void refreshPage(Account account) {
        Main.reset();
        var page = AdminUI.openAdminUI(account);
        Main.getFrame().getContentPane().add(page);
        page.setSelectedIndex(1);

        Main.refresh();
    }
}
