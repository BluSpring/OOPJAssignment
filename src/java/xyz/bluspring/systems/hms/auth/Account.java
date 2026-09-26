package xyz.bluspring.systems.hms.auth;

import java.util.UUID;

import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class Account {
    public static final DataSerializer<Account> SERIALIZER = RecordDataSerializer.of(
        AccountType.SERIALIZER, Account::getAccountType,
        DataSerializer.UUID_SERIALIZER, Account::getUUID,
        DataSerializer.STRING, Account::getEmail,
        DataSerializer.STRING, Account::getDisplayName,
        DataSerializer.STRING, Account::getPasswordHash,
        Account::new
    );

    private final AccountType accountType;
    private final UUID uuid;
    private String email;
    private String displayName;
    private String passwordHash;

    public Account(AccountType accountType, UUID uuid, String email, String displayName, String passwordHash) {
        this.accountType = accountType;
        this.uuid = uuid;
        this.email = email;
        this.displayName = displayName;
        this.passwordHash = passwordHash;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
