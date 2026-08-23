package xyz.bluspring.systems.hms.role;

public abstract class PersonalizableUser {
    private final Profile profile = new Profile();

    public Profile getProfile() {
        return profile;
    }
}
