package xyz.bluspring.systems.hms.role.doctor;

import xyz.bluspring.systems.hms.role.patient.Patient;

public class VitalSign {

    private final Patient patient;
    private double temperature;
    private int heartRate;
    private int bloodPressure;
    private int oxygenLevel;

    public VitalSign(Patient patient,
                     double temperature,
                     int heartRate,
                     int bloodPressure,
                     int oxygenLevel) {

        this.patient = patient;
        this.temperature = temperature;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        this.oxygenLevel = oxygenLevel;
    }

    public Patient getPatient() {
        return patient;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(int bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public int getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(int oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    @Override
    public String toString() {
        return "Patient ID: " + patient.getPatientId()
            + ", Temperature: " + temperature
            + ", Heart Rate: " + heartRate
            + ", Blood Pressure: " + bloodPressure
            + ", Oxygen Level: " + oxygenLevel;
    }
}
