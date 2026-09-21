package xyz.bluspring.systems.hms.role.doctor;

public class DoctorMain {

    public static void main(String[] args) {

        Doctor doctor = new Doctor(
            "D001",
            "General Medicine"
        );

        doctor.getProfile().setDisplayName("Dr. Ahmed");

        DoctorGUI gui = new DoctorGUI(doctor);

        gui.setVisible(true);
    }
}
