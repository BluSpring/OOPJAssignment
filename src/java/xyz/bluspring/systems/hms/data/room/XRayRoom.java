package xyz.bluspring.systems.hms.data.room;

import java.util.ArrayList;
import java.util.List;

import xyz.bluspring.systems.hms.data.AdminDataStorage;
import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.role.doctor.MedicalTestRequest;
import xyz.bluspring.systems.hms.role.patient.Patient;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class XRayRoom extends TestRequestableRoom<XRayRoom> implements MultiDoctorAssignableRoom, PatientAssignableRoom {
    public static final DataSerializer<XRayRoom> SERIALIZER = RecordDataSerializer.of(
        Doctor.REFERENCE_SERIALIZER.list(), XRayRoom::getAssignedDoctors,
        Patient.REFERENCE_SERIALIZER, XRayRoom::getAssignedPatient,
        MedicalTestRequest.getReferenceSerializer(), XRayRoom::getCurrentRequest,
        XRayRoom::new
    );

    private final List<Doctor> assignedDoctors = new ArrayList<>();
    private Patient assignedPatient;

    public XRayRoom(List<Doctor> doctors, Patient patient) {
        this(doctors, patient, null);
    }

    public XRayRoom(List<Doctor> doctors, Patient patient, MedicalTestRequest request) {
        super(Type.X_RAY, request);
        this.assignedDoctors.addAll(doctors);
        this.assignedPatient = patient;
    }

    @Override
    public boolean isOccupied() {
        return !this.getAssignedDoctors().isEmpty() && this.getAssignedPatient() != null;
    }

    @Override
    public List<Doctor> getAssignedDoctors() {
        return assignedDoctors;
    }

    @Override
    public void addAssignedDoctor(Doctor doctor) {
        this.assignedDoctors.add(doctor);
        AdminDataStorage.INSTANCE.save();
    }

    @Override
    public Patient getAssignedPatient() {
        return assignedPatient;
    }

    @Override
    public void setAssignedPatient(Patient patient) {
        this.assignedPatient = patient;
        AdminDataStorage.INSTANCE.save();
    }
}
