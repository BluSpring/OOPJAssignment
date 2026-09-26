package xyz.bluspring.systems.hms.auth;

import java.util.UUID;

import xyz.bluspring.systems.hms.utils.data.DataSerializable;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public record AuthLog(
    UUID uuid,
    Type type,
    long timestamp,
    String extraData
) implements DataSerializable<AuthLog> {
    public static final DataSerializer<AuthLog> SERIALIZER = RecordDataSerializer.of(
        DataSerializer.UUID_SERIALIZER, AuthLog::uuid,
        Type.SERIALIZER, AuthLog::type,
        DataSerializer.LONG, AuthLog::timestamp,
        DataSerializer.STRING, AuthLog::extraData,
        AuthLog::new
    );

    public AuthLog(UUID uuid, Type type, long timestamp) {
        this(uuid, type, timestamp, "");
    }

    @Override
    public DataSerializer<AuthLog> getSerializer() {
        return SERIALIZER;
    }

    public enum Type {
        LOGIN,
        REGISTER,
        CHANGE_PASSWORD,
        CHANGE_EMAIL,
        CHANGE_DISPLAY_NAME,
        ;

        public static final DataSerializer<Type> SERIALIZER = DataSerializer.fromEnum(Type.class);
    }
}
