package xyz.bluspring.systems.hms.role;

import java.util.UUID;

import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class Profile {
    public static final DataSerializer<Profile> SERIALIZER = RecordDataSerializer.of(
        DataSerializer.UUID_SERIALIZER, Profile::getId,
        DataSerializer.STRING, Profile::getAddress,
        DataSerializer.STRING, Profile::getDisplayName,
        Profile::new
    );

    public Profile(UUID id, String address, String displayName) {
        this.id = id;
        this.address = address;
        this.displayName = displayName;
    }

    private final UUID id;
    private String address;
    private String displayName;

    public UUID getId() {
        return id;
    }

    // Getter for display name
    public String getDisplayName() {
        return displayName;
    }

    // Getter for address
    public String getAddress() {
        return address;
    }

    // Setter for display name
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    // Setter for address
    public void setAddress(String address) {
        this.address = address;
    }
}


