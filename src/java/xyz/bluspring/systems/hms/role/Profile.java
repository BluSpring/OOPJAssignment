package xyz.bluspring.systems.hms.role;

import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class Profile {
    public static final DataSerializer<Profile> SERIALIZER = RecordDataSerializer.of(
        DataSerializer.STRING, Profile::getAddress,
        DataSerializer.STRING, Profile::getDisplayName,
        Profile::new
    );

    public Profile(String address, String displayName) {
        this.address = address;
        this.displayName = displayName;
    }

    private String address;
    private String displayName;

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


