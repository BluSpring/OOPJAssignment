package xyz.bluspring.systems.hms.data;

import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public record InsuranceNetwork(String name, boolean isInNetwork) {
    public static final DataSerializer<InsuranceNetwork> SERIALIZER = RecordDataSerializer.of(
        DataSerializer.STRING, InsuranceNetwork::name,
        DataSerializer.BOOL, InsuranceNetwork::isInNetwork,
        InsuranceNetwork::new
    );
}
