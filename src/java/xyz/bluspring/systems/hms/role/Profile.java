package xyz.bluspring.systems.hms.role;

public class Profile {

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
