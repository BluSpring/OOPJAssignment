package xyz.bluspring.systems.hms.role.doctor;

public class VitalSign {

    private String patientId;
    private double temperature;
    private int heartRate;
    private int bloodPressure;
    private int oxygenLevel;

    public VitalSign(String patientId,
                     double temperature,
                     int heartRate,
                     int bloodPressure,
                     int oxygenLevel) {

        this.patientId = patientId;
        this.temperature = temperature;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        this.oxygenLevel = oxygenLevel;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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
        return "Patient ID: " + patientId
            + ", Temperature: " + temperature
            + ", Heart Rate: " + heartRate
            + ", Blood Pressure: " + bloodPressure
            + ", Oxygen Level: " + oxygenLevel;
    }
}
