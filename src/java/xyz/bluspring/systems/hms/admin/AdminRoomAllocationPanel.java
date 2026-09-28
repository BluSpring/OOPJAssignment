package xyz.bluspring.systems.hms.admin;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import xyz.bluspring.systems.hms.Main;
import xyz.bluspring.systems.hms.auth.Account;
import xyz.bluspring.systems.hms.data.AdminDataStorage;
import xyz.bluspring.systems.hms.data.DoctorDataStorage;
import xyz.bluspring.systems.hms.data.records.TestStatus;
import xyz.bluspring.systems.hms.data.room.ConsultationRoom;
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
                    if (rooms.stream().anyMatch(r -> r.getType() == request.getTestType().getRequiredRoom() && !r.isOccupied() && r instanceof TestRequestableRoom<?> trr && trr.getCurrentRequest() == null)) {
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

                                if (room instanceof TestRequestableRoom<?> testRequestableRoom) {
                                    testRequestableRoom.setCurrentRequest(request);
                                }

                                request.setStatus(TestStatus.WAITING);
                                AdminDataStorage.INSTANCE.save();
                                DoctorDataStorage.INSTANCE.save();

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
            var roomsMainPanel = new JPanel();
            roomsMainPanel.setLayout(new BoxLayout(roomsMainPanel, BoxLayout.Y_AXIS));
            roomsMainPanel.setBorder(new TitledBorder("Hospital Rooms"));

            var topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            var roomTypeBox = new JComboBox<>(HospitalRoom.Type.values());
            topPanel.add(roomTypeBox);

            var createRoomButton = new JButton("+");
            createRoomButton.addActionListener(_ -> {
                var type = (HospitalRoom.Type) roomTypeBox.getSelectedItem();
                AdminDataStorage.INSTANCE.getHospitalRooms().add(type.createDefault());
                AdminDataStorage.INSTANCE.save();

                refreshPage(account);
            });
            topPanel.add(createRoomButton);

            var roomsPanel = new JPanel();
            roomsPanel.setLayout(new BoxLayout(roomsPanel, BoxLayout.Y_AXIS));

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

                if (room.isOccupied()) {
                    var emptyRoomButton = new JButton("Empty Room");
                    emptyRoomButton.addActionListener(_ -> {
                        if (room instanceof MultiDoctorAssignableRoom doctorAssignableRoom) {
                            doctorAssignableRoom.getAssignedDoctors().clear();
                        }

                        if (room instanceof ConsultationRoom consultationRoom) {
                            consultationRoom.setAssignedDoctor(null);
                        }

                        if (room instanceof PatientAssignableRoom patientAssignableRoom) {
                            patientAssignableRoom.setAssignedPatient(null);
                        }

                        if (room instanceof TestRequestableRoom<?> requestableRoom) {
                            var request = requestableRoom.getCurrentRequest();
                            if (request != null) {
                                request.setStatus(TestStatus.COMPLETED);
                            }

                            requestableRoom.setCurrentRequest(null);
                        }

                        AdminDataStorage.INSTANCE.save();
                        DoctorDataStorage.INSTANCE.save();

                        refreshPage(account);
                    });

                    roomPanel.add(emptyRoomButton);
                }

                var deleteRoomButton = new JButton("Delete Room");
                deleteRoomButton.addActionListener(_ -> {
                    var option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this room?");

                    if (option == JOptionPane.YES_OPTION) {
                        if (room instanceof TestRequestableRoom<?> requestableRoom) {
                            var request = requestableRoom.getCurrentRequest();
                            if (request != null) {
                                request.setStatus(TestStatus.COMPLETED);
                            }

                            requestableRoom.setCurrentRequest(null);
                        }

                        AdminDataStorage.INSTANCE.getHospitalRooms().remove(room);

                        AdminDataStorage.INSTANCE.save();
                        DoctorDataStorage.INSTANCE.save();
                    }
                });

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
