package xyz.bluspring.systems.hms.auth;

import xyz.bluspring.systems.hms.utils.data.DataSerializer;

public enum AccountType {
    ADMIN,
    MEDICAL_MANAGER,
    DOCTOR,
    PATIENT,
    ;

    public static final DataSerializer<AccountType> SERIALIZER = DataSerializer.fromEnum(AccountType.class);
}
