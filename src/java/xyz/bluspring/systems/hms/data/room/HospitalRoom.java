package xyz.bluspring.systems.hms.data.room;

import java.util.List;
import java.util.function.Supplier;

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
        CONSULTATION("Consultation Room", ConsultationRoom.SERIALIZER, () -> new ConsultationRoom(null, null)),
        INPATIENT_WARD("Inpatient Ward", InPatientWard.SERIALIZER, () -> new InPatientWard(List.of(), null)),
        LAB("Lab Room", LabRoom.SERIALIZER, () -> new LabRoom(List.of())),
        X_RAY("X-Ray Room", XRayRoom.SERIALIZER, () -> new XRayRoom(List.of(), null)),
        IMAGING("Imaging Room", ImagingRoom.SERIALIZER, () -> new ImagingRoom(List.of(), null)),
        ;

        private final String properName;
        private final DataSerializer<? extends HospitalRoom<?>> serializer;
        private final Supplier<HospitalRoom<?>> defaultSupplier;

        Type(String properName, DataSerializer<? extends HospitalRoom<?>> serializer, Supplier<HospitalRoom<?>> defaultSupplier) {
            this.properName = properName;
            this.serializer = serializer;
            this.defaultSupplier = defaultSupplier;
        }

        public String getProperName() {
            return properName;
        }

        public HospitalRoom<?> createDefault() {
            return this.defaultSupplier.get();
        }

        @Override
        public String toString() {
            return getProperName();
        }

        public DataSerializer<HospitalRoom<?>> getSerializer() {
            return (DataSerializer<HospitalRoom<?>>) serializer;
        }

        public static final DataSerializer<Type> SERIALIZER = DataSerializer.fromEnum(Type.class);
    }
}
