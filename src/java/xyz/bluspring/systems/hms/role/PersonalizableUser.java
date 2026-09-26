package xyz.bluspring.systems.hms.role;

import xyz.bluspring.systems.hms.utils.data.DataSerializable;

public abstract class PersonalizableUser<T extends PersonalizableUser<T>> implements DataSerializable<T> {
    private final Profile profile;

    public PersonalizableUser(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }
}
