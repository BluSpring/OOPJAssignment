package xyz.bluspring.systems.hms.admin;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import xyz.bluspring.systems.hms.data.AdminDataStorage;
import xyz.bluspring.systems.hms.data.DoctorDataStorage;
import xyz.bluspring.systems.hms.data.room.HospitalRoom;
import xyz.bluspring.systems.hms.role.doctor.MedicalTestRequest;

public class AdminRoomAllocationPanel extends JPanel {
    public AdminRoomAllocationPanel() {
        setLayout(new BorderLayout());

        {
            var requestsPanel = new JPanel();
            requestsPanel.setName("Doctor's Requests");

            for (MedicalTestRequest request : DoctorDataStorage.INSTANCE.getMedicalTestRequests()) {

            }

            var scrollPane = new JScrollPane(requestsPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            this.add(scrollPane);
        }

        {
            var roomsPanel = new JPanel();
            roomsPanel.setName("Hospital Rooms");

            for (HospitalRoom<?> room : AdminDataStorage.INSTANCE.getHospitalRooms()) {

            }

            var scrollPane = new JScrollPane(roomsPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            this.add(scrollPane);
        }
    }
}
