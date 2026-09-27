package xyz.bluspring.systems.hms.utils.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class DataSerializer<T> {
    public static final DataSerializer<String> STRING = DataSerializer.of(Function.identity(), Function.identity());
    public static final DataSerializer<Byte> BYTE = DataSerializer.of(Object::toString, Byte::parseByte);
    public static final DataSerializer<Short> SHORT = DataSerializer.of(Object::toString, Short::parseShort);
    public static final DataSerializer<Integer> INT = DataSerializer.of(Object::toString, Integer::parseInt);
    public static final DataSerializer<Long> LONG = DataSerializer.of(Objects::toString, Long::parseLong);
    public static final DataSerializer<Float> FLOAT = DataSerializer.of(Object::toString, Float::parseFloat);
    public static final DataSerializer<Double> DOUBLE = DataSerializer.of(Object::toString, Double::parseDouble);
    public static final DataSerializer<Boolean> BOOL = DataSerializer.of(Object::toString, Boolean::parseBoolean);

    public static final DataSerializer<UUID> UUID_SERIALIZER = STRING.map(UUID::fromString, UUID::toString);
    public static final DataSerializer<Date> DATE = LONG.map(Date::new, Date::getTime);

    public static <E extends Enum<E>> DataSerializer<E> fromEnum(Class<E> enumClass) {
        return DataSerializer.of(E::name, name -> Enum.valueOf(enumClass, name));
    }

    DataSerializer() {
    }

    public abstract String serialize(T value);
    public abstract T deserialize(String data);

    public <U> DataSerializer<U> map(Function<T, U> to, Function<U, T> from) {
        return DataSerializer.of(
            value -> this.serialize(from.apply(value)),
            data -> to.apply(this.deserialize(data))
        );
    }

    public DataSerializer<T> orElse(Supplier<T> defaultValue) {
        return DataSerializer.of(value -> {
            if (value != null) {
                return this.serialize(value);
            } else {
                return this.serialize(defaultValue.get());
            }
        }, data -> {
            try {
                T value = this.deserialize(data);
                if (value == null) {
                    return defaultValue.get();
                }

                return value;
            } catch (Throwable e) {
                System.err.printf("Failed to decode value \"%s\", using default value instead.\n", data);
                e.printStackTrace(System.err);
                return defaultValue.get();
            }
        });
    }

    public <U> DataSerializer<U> dispatch(Function<T, DataSerializer<? extends U>> serializerGetter, Function<U, T> typeGetter) {
        return DataSerializer.of(value -> {
            T type = typeGetter.apply(value);
            DataSerializer<U> serializer = (DataSerializer<U>) serializerGetter.apply(type);
            return DataSerializers.writeSegmentedLine(List.of(this.serialize(type), serializer.serialize(value)));
        }, data -> {
            List<String> segments = DataSerializers.readSegmentedLine(data);
            if (segments.size() != 2) {
                throw new IllegalArgumentException("Failed to decode dispatched value: Total segments is invalid! (expected 2, got " + segments.size() + ")");
            }

            T type = this.deserialize(segments.getFirst());
            DataSerializer<U> serializer = (DataSerializer<U>) serializerGetter.apply(type);
            return serializer.deserialize(segments.get(1));
        });
    }

    public DataSerializer<List<T>> list() {
        return DataSerializer.<List<T>>of(list -> DataSerializers.writeSegmentedLine(list.stream().map(this::serialize).toList()),
                data -> new ArrayList<>(DataSerializers.readSegmentedLine(data).stream().map(this::deserialize).toList()))
            .orElse(ArrayList::new);
    }

    public DataSerializer<List<T>> list(int min, int max) {
        return this.list().map(list -> {
            if (list.size() < min || list.size() > max) {
                throw new IllegalArgumentException("Size out of bounds, expected value between [" + min + ", " + max + "]" + ", got " + list.size());
            }

            return list;
        }, list -> {
            if (list.size() < min || list.size() > max) {
                throw new IllegalArgumentException("Size out of bounds, expected value between [" + min + ", " + max + "]" + ", got " + list.size());
            }

            return list;
        }).orElse(ArrayList::new);
    }

    public static <T> DataSerializer<T> of(Function<T, String> encoder, Function<String, T> decoder) {
        return new DataSerializer<>() {
            @Override
            public String serialize(T value) {
                try {
                    if (value == null) {
                        return "";
                    }

                    return encoder.apply(value);
                } catch (Exception e) {
                    System.err.println("Failed to serialize \"" + value + "\"!");
                    throw new RuntimeException(e);
                }
            }

            @Override
            public T deserialize(String data) {
                try {
                    return decoder.apply(data);
                } catch (Exception e) {
                    System.err.println("Failed to deserialize \"" + data + "\", returning default value instead.");
                    e.printStackTrace(System.err);
                    return null;
                }
            }
        };
    }
}
