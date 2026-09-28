package xyz.bluspring.systems.hms.data.room;

import xyz.bluspring.systems.hms.role.patient.Patient;

public interface PatientAssignableRoom {
    Patient getAssignedPatient();

    void setAssignedPatient(Patient patient);
}
