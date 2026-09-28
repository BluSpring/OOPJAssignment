package xyz.bluspring.systems.hms.data.room;

import xyz.bluspring.systems.hms.data.AdminDataStorage;
import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.role.doctor.MedicalTestRequest;
import xyz.bluspring.systems.hms.role.patient.Patient;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class ConsultationRoom extends HospitalRoom<ConsultationRoom> {
    public static final DataSerializer<ConsultationRoom> SERIALIZER = RecordDataSerializer.of(
        Doctor.REFERENCE_SERIALIZER, ConsultationRoom::getAssignedDoctor,
        Patient.REFERENCE_SERIALIZER, ConsultationRoom::getAssignedPatient,
        MedicalTestRequest.SERIALIZER,
        ConsultationRoom::new
    );

    private Doctor assignedDoctor;
    private Patient assignedPatient;

    public ConsultationRoom(Doctor doctor, Patient patient) {
        super(Type.CONSULTATION);
        this.assignedDoctor = doctor;
        this.assignedPatient = patient;
    }

    @Override
    public boolean isOccupied() {
        return this.getAssignedDoctor() != null && this.getAssignedPatient() != null;
    }

    public Doctor getAssignedDoctor() {
        return assignedDoctor;
    }

    public void setAssignedDoctor(Doctor assignedDoctor) {
        this.assignedDoctor = assignedDoctor;
        AdminDataStorage.INSTANCE.save();
    }

    public Patient getAssignedPatient() {
        return assignedPatient;
    }

    public void setAssignedPatient(Patient assignedPatient) {
        this.assignedPatient = assignedPatient;
        AdminDataStorage.INSTANCE.save();
    }
}
