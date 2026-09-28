package xyz.bluspring.systems.hms.data.room;

import java.util.ArrayList;
import java.util.List;

import xyz.bluspring.systems.hms.data.AdminDataStorage;
import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.role.doctor.MedicalTestRequest;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class LabRoom extends TestRequestableRoom<LabRoom> implements MultiDoctorAssignableRoom {
    public static final DataSerializer<LabRoom> SERIALIZER = RecordDataSerializer.of(
        Doctor.REFERENCE_SERIALIZER.list(), LabRoom::getAssignedDoctors,
        MedicalTestRequest.getReferenceSerializer(), LabRoom::getCurrentRequest,
        LabRoom::new
    );

    private final List<Doctor> assignedDoctors = new ArrayList<>();

    public LabRoom(List<Doctor> doctors) {
        this(doctors, null);
    }

    public LabRoom(List<Doctor> doctors, MedicalTestRequest request) {
        super(Type.LAB, request);
        this.assignedDoctors.addAll(doctors);
    }

    @Override
    public boolean isOccupied() {
        return !this.getAssignedDoctors().isEmpty();
    }

    public List<Doctor> getAssignedDoctors() {
        return assignedDoctors;
    }

    public void addAssignedDoctor(Doctor doctor) {
        this.assignedDoctors.add(doctor);
        AdminDataStorage.INSTANCE.save();
    }
}
