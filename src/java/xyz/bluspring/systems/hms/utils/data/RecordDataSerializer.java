package xyz.bluspring.systems.hms.utils.data;

import java.util.List;
import java.util.function.Function;

public class RecordDataSerializer {
    private RecordDataSerializer() {
    }

    public static <C1, T> DataSerializer<T> of(
        DataSerializer<C1> serializer1, Function<T, C1> getter1,
        Function<C1, T> builder
    ) {
        return DataSerializer.of(
            value -> DataSerializers.writeSegmentedLine(
                List.of(
                    serializer1.serialize(getter1.apply(value))
                )
            ),
            data -> {
                List<String> list = DataSerializers.readSegmentedLine(data);
                return builder.apply(
                    serializer1.deserialize(list.getFirst())
                );
            });
    }

    public static <C1, C2, T> DataSerializer<T> of(
        DataSerializer<C1> serializer1, Function<T, C1> getter1,
        DataSerializer<C2> serializer2, Function<T, C2> getter2,
        Functions.Function2<C1, C2, T> builder
    ) {
        return DataSerializer.of(
            value -> DataSerializers.writeSegmentedLine(
                List.of(
                    serializer1.serialize(getter1.apply(value)),
                    serializer2.serialize(getter2.apply(value))
                )
            ),
            data -> {
                List<String> list = DataSerializers.readSegmentedLine(data);
                return builder.create(
                    serializer1.deserialize(list.getFirst()),
                    serializer2.deserialize(list.get(1))
                );
            });
    }

    public static <C1, C2, C3, T> DataSerializer<T> of(
        DataSerializer<C1> serializer1, Function<T, C1> getter1,
        DataSerializer<C2> serializer2, Function<T, C2> getter2,
        DataSerializer<C3> serializer3, Function<T, C3> getter3,
        Functions.Function3<C1, C2, C3, T> builder
    ) {
        return DataSerializer.of(
            value -> DataSerializers.writeSegmentedLine(
                List.of(
                    serializer1.serialize(getter1.apply(value)),
                    serializer2.serialize(getter2.apply(value)),
                    serializer3.serialize(getter3.apply(value))
                )
            ),
            data -> {
                List<String> list = DataSerializers.readSegmentedLine(data);
                return builder.create(
                    serializer1.deserialize(list.getFirst()),
                    serializer2.deserialize(list.get(1)),
                    serializer3.deserialize(list.get(2))
                );
            });
    }

    public static <C1, C2, C3, C4, T> DataSerializer<T> of(
        DataSerializer<C1> serializer1, Function<T, C1> getter1,
        DataSerializer<C2> serializer2, Function<T, C2> getter2,
        DataSerializer<C3> serializer3, Function<T, C3> getter3,
        DataSerializer<C4> serializer4, Function<T, C4> getter4,
        Functions.Function4<C1, C2, C3, C4, T> builder
    ) {
        return DataSerializer.of(
            value -> DataSerializers.writeSegmentedLine(
                List.of(
                    serializer1.serialize(getter1.apply(value)),
                    serializer2.serialize(getter2.apply(value)),
                    serializer3.serialize(getter3.apply(value)),
                    serializer4.serialize(getter4.apply(value))
                )
            ),
            data -> {
                List<String> list = DataSerializers.readSegmentedLine(data);
                return builder.create(
                    serializer1.deserialize(list.getFirst()),
                    serializer2.deserialize(list.get(1)),
                    serializer3.deserialize(list.get(2)),
                    serializer4.deserialize(list.get(3))
                );
            });
    }

    public static <C1, C2, C3, C4, C5, T> DataSerializer<T> of(
        DataSerializer<C1> serializer1, Function<T, C1> getter1,
        DataSerializer<C2> serializer2, Function<T, C2> getter2,
        DataSerializer<C3> serializer3, Function<T, C3> getter3,
        DataSerializer<C4> serializer4, Function<T, C4> getter4,
        DataSerializer<C5> serializer5, Function<T, C5> getter5,
        Functions.Function5<C1, C2, C3, C4, C5, T> builder
    ) {
        return DataSerializer.of(
            value -> DataSerializers.writeSegmentedLine(
                List.of(
                    serializer1.serialize(getter1.apply(value)),
                    serializer2.serialize(getter2.apply(value)),
                    serializer3.serialize(getter3.apply(value)),
                    serializer4.serialize(getter4.apply(value)),
                    serializer5.serialize(getter5.apply(value))
                )
            ),
            data -> {
                List<String> list = DataSerializers.readSegmentedLine(data);
                return builder.create(
                    serializer1.deserialize(list.getFirst()),
                    serializer2.deserialize(list.get(1)),
                    serializer3.deserialize(list.get(2)),
                    serializer4.deserialize(list.get(3)),
                    serializer5.deserialize(list.get(4))
                );
            });
    }

    public static <C1, C2, C3, C4, C5, C6, T> DataSerializer<T> of(
        DataSerializer<C1> serializer1, Function<T, C1> getter1,
        DataSerializer<C2> serializer2, Function<T, C2> getter2,
        DataSerializer<C3> serializer3, Function<T, C3> getter3,
        DataSerializer<C4> serializer4, Function<T, C4> getter4,
        DataSerializer<C5> serializer5, Function<T, C5> getter5,
        DataSerializer<C6> serializer6, Function<T, C6> getter6,
        Functions.Function6<C1, C2, C3, C4, C5, C6, T> builder
    ) {
        return DataSerializer.of(
            value -> DataSerializers.writeSegmentedLine(
                List.of(
                    serializer1.serialize(getter1.apply(value)),
                    serializer2.serialize(getter2.apply(value)),
                    serializer3.serialize(getter3.apply(value)),
                    serializer4.serialize(getter4.apply(value)),
                    serializer5.serialize(getter5.apply(value)),
                    serializer6.serialize(getter6.apply(value))
                )
            ),
            data -> {
                List<String> list = DataSerializers.readSegmentedLine(data);
                return builder.create(
                    serializer1.deserialize(list.getFirst()),
                    serializer2.deserialize(list.get(1)),
                    serializer3.deserialize(list.get(2)),
                    serializer4.deserialize(list.get(3)),
                    serializer5.deserialize(list.get(4)),
                    serializer6.deserialize(list.get(5))
                );
            });
    }

    public static <C1, C2, C3, C4, C5, C6, C7, T> DataSerializer<T> of(
        DataSerializer<C1> serializer1, Function<T, C1> getter1,
        DataSerializer<C2> serializer2, Function<T, C2> getter2,
        DataSerializer<C3> serializer3, Function<T, C3> getter3,
        DataSerializer<C4> serializer4, Function<T, C4> getter4,
        DataSerializer<C5> serializer5, Function<T, C5> getter5,
        DataSerializer<C6> serializer6, Function<T, C6> getter6,
        DataSerializer<C7> serializer7, Function<T, C7> getter7,
        Functions.Function7<C1, C2, C3, C4, C5, C6, C7, T> builder
    ) {
        return DataSerializer.of(
            value -> DataSerializers.writeSegmentedLine(
                List.of(
                    serializer1.serialize(getter1.apply(value)),
                    serializer2.serialize(getter2.apply(value)),
                    serializer3.serialize(getter3.apply(value)),
                    serializer4.serialize(getter4.apply(value)),
                    serializer5.serialize(getter5.apply(value)),
                    serializer6.serialize(getter6.apply(value)),
                    serializer7.serialize(getter7.apply(value))
                )
            ),
            data -> {
                List<String> list = DataSerializers.readSegmentedLine(data);
                return builder.create(
                    serializer1.deserialize(list.getFirst()),
                    serializer2.deserialize(list.get(1)),
                    serializer3.deserialize(list.get(2)),
                    serializer4.deserialize(list.get(3)),
                    serializer5.deserialize(list.get(4)),
                    serializer6.deserialize(list.get(5)),
                    serializer7.deserialize(list.get(6))
                );
            });
    }

    public static <C1, C2, C3, C4, C5, C6, C7, C8, T> DataSerializer<T> of(
        DataSerializer<C1> serializer1, Function<T, C1> getter1,
        DataSerializer<C2> serializer2, Function<T, C2> getter2,
        DataSerializer<C3> serializer3, Function<T, C3> getter3,
        DataSerializer<C4> serializer4, Function<T, C4> getter4,
        DataSerializer<C5> serializer5, Function<T, C5> getter5,
        DataSerializer<C6> serializer6, Function<T, C6> getter6,
        DataSerializer<C7> serializer7, Function<T, C7> getter7,
        DataSerializer<C8> serializer8, Function<T, C8> getter8,
        Functions.Function8<C1, C2, C3, C4, C5, C6, C7, C8, T> builder
    ) {
        return DataSerializer.of(
            value -> DataSerializers.writeSegmentedLine(
                List.of(
                    serializer1.serialize(getter1.apply(value)),
                    serializer2.serialize(getter2.apply(value)),
                    serializer3.serialize(getter3.apply(value)),
                    serializer4.serialize(getter4.apply(value)),
                    serializer5.serialize(getter5.apply(value)),
                    serializer6.serialize(getter6.apply(value)),
                    serializer7.serialize(getter7.apply(value)),
                    serializer8.serialize(getter8.apply(value))
                )
            ),
            data -> {
                List<String> list = DataSerializers.readSegmentedLine(data);
                return builder.create(
                    serializer1.deserialize(list.getFirst()),
                    serializer2.deserialize(list.get(1)),
                    serializer3.deserialize(list.get(2)),
                    serializer4.deserialize(list.get(3)),
                    serializer5.deserialize(list.get(4)),
                    serializer6.deserialize(list.get(5)),
                    serializer7.deserialize(list.get(6)),
                    serializer8.deserialize(list.get(7))
                );
            });
    }

    public static <C1, C2, C3, C4, C5, C6, C7, C8, C9, T> DataSerializer<T> of(
        DataSerializer<C1> serializer1, Function<T, C1> getter1,
        DataSerializer<C2> serializer2, Function<T, C2> getter2,
        DataSerializer<C3> serializer3, Function<T, C3> getter3,
        DataSerializer<C4> serializer4, Function<T, C4> getter4,
        DataSerializer<C5> serializer5, Function<T, C5> getter5,
        DataSerializer<C6> serializer6, Function<T, C6> getter6,
        DataSerializer<C7> serializer7, Function<T, C7> getter7,
        DataSerializer<C8> serializer8, Function<T, C8> getter8,
        DataSerializer<C9> serializer9, Function<T, C9> getter9,
        Functions.Function9<C1, C2, C3, C4, C5, C6, C7, C8, C9, T> builder
    ) {
        return DataSerializer.of(
            value -> DataSerializers.writeSegmentedLine(
                List.of(
                    serializer1.serialize(getter1.apply(value)),
                    serializer2.serialize(getter2.apply(value)),
                    serializer3.serialize(getter3.apply(value)),
                    serializer4.serialize(getter4.apply(value)),
                    serializer5.serialize(getter5.apply(value)),
                    serializer6.serialize(getter6.apply(value)),
                    serializer7.serialize(getter7.apply(value)),
                    serializer8.serialize(getter8.apply(value)),
                    serializer9.serialize(getter9.apply(value))
                )
            ),
            data -> {
                List<String> list = DataSerializers.readSegmentedLine(data);
                return builder.create(
                    serializer1.deserialize(list.getFirst()),
                    serializer2.deserialize(list.get(1)),
                    serializer3.deserialize(list.get(2)),
                    serializer4.deserialize(list.get(3)),
                    serializer5.deserialize(list.get(4)),
                    serializer6.deserialize(list.get(5)),
                    serializer7.deserialize(list.get(6)),
                    serializer8.deserialize(list.get(7)),
                    serializer9.deserialize(list.get(8))
                );
            });
    }

    public static <C1, C2, C3, C4, C5, C6, C7, C8, C9, C10, T> DataSerializer<T> of(
        DataSerializer<C1> serializer1, Function<T, C1> getter1,
        DataSerializer<C2> serializer2, Function<T, C2> getter2,
        DataSerializer<C3> serializer3, Function<T, C3> getter3,
        DataSerializer<C4> serializer4, Function<T, C4> getter4,
        DataSerializer<C5> serializer5, Function<T, C5> getter5,
        DataSerializer<C6> serializer6, Function<T, C6> getter6,
        DataSerializer<C7> serializer7, Function<T, C7> getter7,
        DataSerializer<C8> serializer8, Function<T, C8> getter8,
        DataSerializer<C9> serializer9, Function<T, C9> getter9,
        DataSerializer<C10> serializer10, Function<T, C10> getter10,
        Functions.Function10<C1, C2, C3, C4, C5, C6, C7, C8, C9, C10, T> builder
    ) {
        return DataSerializer.of(
            value -> DataSerializers.writeSegmentedLine(
                List.of(
                    serializer1.serialize(getter1.apply(value)),
                    serializer2.serialize(getter2.apply(value)),
                    serializer3.serialize(getter3.apply(value)),
                    serializer4.serialize(getter4.apply(value)),
                    serializer5.serialize(getter5.apply(value)),
                    serializer6.serialize(getter6.apply(value)),
                    serializer7.serialize(getter7.apply(value)),
                    serializer8.serialize(getter8.apply(value)),
                    serializer9.serialize(getter9.apply(value)),
                    serializer10.serialize(getter10.apply(value))
                )
            ),
            data -> {
                List<String> list = DataSerializers.readSegmentedLine(data);
                return builder.create(
                    serializer1.deserialize(list.getFirst()),
                    serializer2.deserialize(list.get(1)),
                    serializer3.deserialize(list.get(2)),
                    serializer4.deserialize(list.get(3)),
                    serializer5.deserialize(list.get(4)),
                    serializer6.deserialize(list.get(5)),
                    serializer7.deserialize(list.get(6)),
                    serializer8.deserialize(list.get(7)),
                    serializer9.deserialize(list.get(8)),
                    serializer10.deserialize(list.get(9))
                );
            });
    }
}
