package xyz.bluspring.systems.hms.data.room;

import xyz.bluspring.systems.hms.utils.data.DataSerializable;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;

public abstract class HospitalRoom<T extends HospitalRoom<T>> implements DataSerializable<T> {
    public static final DataSerializer<HospitalRoom<?>> SERIALIZER = Type.SERIALIZER.dispatch(Type::getSerializer, HospitalRoom::getType);

    private final Type type;

    public HospitalRoom(Type type) {
        this.type = type;
    }

    public final Type getType() {
        return type;
    }

    public abstract boolean isOccupied();

    @Override
    public final DataSerializer<T> getSerializer() {
        return (DataSerializer<T>) this.getType().getSerializer();
    }

    public enum Type {
        CONSULTATION("Consultation Room", ConsultationRoom.SERIALIZER), INPATIENT_WARD("Inpatient Ward", InPatientWard.SERIALIZER),
        LAB("Lab Room", LabRoom.SERIALIZER),
        X_RAY("X-Ray Room", XRayRoom.SERIALIZER), IMAGING("Imaging Room", ImagingRoom.SERIALIZER),
        ;

        private final String properName;
        private final DataSerializer<? extends HospitalRoom<?>> serializer;

        Type(String properName, DataSerializer<? extends HospitalRoom<?>> serializer) {
            this.properName = properName;
            this.serializer = serializer;
        }

        public String getProperName() {
            return properName;
        }

        public DataSerializer<HospitalRoom<?>> getSerializer() {
            return (DataSerializer<HospitalRoom<?>>) serializer;
        }

        public static final DataSerializer<Type> SERIALIZER = DataSerializer.fromEnum(Type.class);
    }
}
