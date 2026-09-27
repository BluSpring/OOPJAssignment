package xyz.bluspring.systems.hms.data.room;

import java.util.ArrayList;
import java.util.List;

import xyz.bluspring.systems.hms.data.AdminDataStorage;
import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.role.patient.Patient;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class ImagingRoom extends HospitalRoom<ImagingRoom> {
    public static final DataSerializer<ImagingRoom> SERIALIZER = RecordDataSerializer.of(
        Doctor.REFERENCE_SERIALIZER.list(), ImagingRoom::getAssignedDoctors,
        Patient.REFERENCE_SERIALIZER, ImagingRoom::getAssignedPatient,
        ImagingRoom::new
    );

    private final List<Doctor> assignedDoctors = new ArrayList<>();
    private Patient assignedPatient;

    public ImagingRoom(List<Doctor> doctors, Patient patient) {
        super(Type.INPATIENT_WARD);
        this.assignedDoctors.addAll(doctors);
        this.assignedPatient = patient;
    }

    public List<Doctor> getAssignedDoctors() {
        return assignedDoctors;
    }

    public void addAssignedDoctor(Doctor doctor) {
        this.assignedDoctors.add(doctor);
        AdminDataStorage.INSTANCE.save();
    }

    public Patient getAssignedPatient() {
        return assignedPatient;
    }

    public void setAssignedPatient(Patient patient) {
        this.assignedPatient = patient;
        AdminDataStorage.INSTANCE.save();
    }
}
