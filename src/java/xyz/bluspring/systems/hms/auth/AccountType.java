package xyz.bluspring.systems.hms.auth;

import xyz.bluspring.systems.hms.utils.data.DataSerializer;

public enum AccountType {
    ADMIN("Administrator"),
    MEDICAL_MANAGER("Medical Manager"),
    DOCTOR("Doctor"),
    PATIENT("Patient"),
    ;

    private final String properName;

    AccountType(String properName) {
        this.properName = properName;
    }

    public String getProperName() {
        return properName;
    }

    @Override
    public String toString() {
        return this.getProperName();
    }

    public static final DataSerializer<AccountType> SERIALIZER = DataSerializer.fromEnum(AccountType.class);
}
