package xyz.bluspring.systems.hms.data.records;

import xyz.bluspring.systems.hms.data.room.HospitalRoom;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;

public enum MedicalTestType {
    IMAGING("Imaging", HospitalRoom.Type.IMAGING),
    LAB("Lab", HospitalRoom.Type.LAB),
    X_RAY("X-Ray", HospitalRoom.Type.X_RAY),
    ;

    public static final DataSerializer<MedicalTestType> SERIALIZER = DataSerializer.fromEnum(MedicalTestType.class);
    private final String properName;
    private final HospitalRoom.Type requiredRoom;

    MedicalTestType(String properName, HospitalRoom.Type requiredRoom) {
        this.properName = properName;
        this.requiredRoom = requiredRoom;
    }

    public HospitalRoom.Type getRequiredRoom() {
        return requiredRoom;
    }

    public String getProperName() {
        return properName;
    }

    @Override
    public String toString() {
        return properName;
    }
}
