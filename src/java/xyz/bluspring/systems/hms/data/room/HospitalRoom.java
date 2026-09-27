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

    @Override
    public final DataSerializer<T> getSerializer() {
        return (DataSerializer<T>) this.getType().getSerializer();
    }

    public enum Type {
        CONSULTATION(ConsultationRoom.SERIALIZER), INPATIENT_WARD(InPatientWard.SERIALIZER), LAB(LabRoom.SERIALIZER),
        X_RAY(XRayRoom.SERIALIZER), IMAGING(ImagingRoom.SERIALIZER),
        ;

        private final DataSerializer<? extends HospitalRoom<?>> serializer;

        Type(DataSerializer<? extends HospitalRoom<?>> serializer) {
            this.serializer = serializer;
        }

        public DataSerializer<HospitalRoom<?>> getSerializer() {
            return (DataSerializer<HospitalRoom<?>>) serializer;
        }

        public static final DataSerializer<Type> SERIALIZER = DataSerializer.fromEnum(Type.class);
    }
}
