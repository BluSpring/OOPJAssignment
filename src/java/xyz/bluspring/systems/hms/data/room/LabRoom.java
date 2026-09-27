package xyz.bluspring.systems.hms.data.room;

import java.util.ArrayList;
import java.util.List;

import xyz.bluspring.systems.hms.data.AdminDataStorage;
import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class LabRoom extends HospitalRoom<LabRoom> {
    public static final DataSerializer<LabRoom> SERIALIZER = RecordDataSerializer.of(
        Doctor.REFERENCE_SERIALIZER.list(), LabRoom::getAssignedDoctors,
        LabRoom::new
    );

    private final List<Doctor> assignedDoctors = new ArrayList<>();

    public LabRoom(List<Doctor> doctors) {
        super(Type.INPATIENT_WARD);
        this.assignedDoctors.addAll(doctors);
    }

    public List<Doctor> getAssignedDoctors() {
        return assignedDoctors;
    }

    public void addAssignedDoctor(Doctor doctor) {
        this.assignedDoctors.add(doctor);
        AdminDataStorage.INSTANCE.save();
    }
}
