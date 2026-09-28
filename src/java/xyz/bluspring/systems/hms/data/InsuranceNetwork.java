package xyz.bluspring.systems.hms.data;

import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public record InsuranceNetwork(String name) {
    public static final DataSerializer<InsuranceNetwork> SERIALIZER = RecordDataSerializer.of(
        DataSerializer.STRING, InsuranceNetwork::name,
        InsuranceNetwork::new
    );
}
