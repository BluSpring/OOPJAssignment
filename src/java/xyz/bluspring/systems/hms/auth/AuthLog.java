package xyz.bluspring.systems.hms.auth;

import java.util.UUID;

import xyz.bluspring.systems.hms.utils.Utils;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.DataSerializers;

public record AuthLog(
    UUID uuid,
    Type type,
    long timestamp,
    String extraData
) {
    public AuthLog(UUID uuid, Type type, long timestamp) {
        this(uuid, type, timestamp, "");
    }

    public enum Type {
        LOGIN,
        REGISTER,
        CHANGE_PASSWORD,
        CHANGE_EMAIL,
        CHANGE_DISPLAY_NAME
    }

    public static class Serializer extends DataSerializer<AuthLog> {
        public Serializer() {
            super(AuthLog.class);
        }

        @Override
        public String serialize(AuthLog value) {
            return DataSerializers.writeSegmentedLine(Utils.allToStrings(value.uuid(), value.type().name(), value.timestamp(), value.extraData()));
        }

        @Override
        public AuthLog deserialize(String data) {
            var split = DataSerializers.readSegmentedLine(data);

            return new AuthLog(UUID.fromString(split.get(0)), Type.valueOf(split.get(1)), Long.parseLong(split.get(2)), split.get(3));
        }
    }

    static {
        DataSerializers.register("auth_log", new Serializer());
    }

    public static void init() {
    }
}
