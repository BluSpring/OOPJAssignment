package xyz.bluspring.systems.hms.data.room;

import xyz.bluspring.systems.hms.role.doctor.MedicalTestRequest;

public abstract class TestRequestableRoom<T extends TestRequestableRoom<T>> extends HospitalRoom<T> {
    public TestRequestableRoom(Type type) {
        this(type, null);
    }

    private MedicalTestRequest currentRequest;

    public TestRequestableRoom(Type type, MedicalTestRequest currentRequest) {
        super(type);
        this.currentRequest = currentRequest;
    }

    public MedicalTestRequest getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(MedicalTestRequest request) {
        this.currentRequest = request;
    }
}
