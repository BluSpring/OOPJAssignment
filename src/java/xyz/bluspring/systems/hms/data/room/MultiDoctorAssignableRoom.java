package xyz.bluspring.systems.hms.data.room;

import java.util.List;

import xyz.bluspring.systems.hms.role.doctor.Doctor;

public interface MultiDoctorAssignableRoom {
    List<Doctor> getAssignedDoctors();

    void addAssignedDoctor(Doctor doctor);
}
