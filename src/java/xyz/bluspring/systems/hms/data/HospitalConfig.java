package xyz.bluspring.systems.hms.data;

import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public record HospitalConfig(
    float baseConfigurationRate
) {
    public static final DataSerializer<HospitalConfig> SERIALIZER = RecordDataSerializer.of(
        DataSerializer.FLOAT, HospitalConfig::baseConfigurationRate,
        HospitalConfig::new
    );
}
