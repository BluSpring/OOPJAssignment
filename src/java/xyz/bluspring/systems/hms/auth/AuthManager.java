package xyz.bluspring.systems.hms.auth;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import xyz.bluspring.systems.hms.utils.ByteArrayUtils;
import xyz.bluspring.systems.hms.utils.data.DataSerializers;

public class AuthManager implements Iterable<Account> {
    // Regular expression pattern for defining emails, which was officially provided by RFC 5322.
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    // Ensures that the provided email is valid.
    public static void checkValidEmail(String email) {
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email provided!");
        }
    }

    // Ensures that the user has a strong password
    public static void checkStrongPassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be more than 8 characters!");
        }
    }

    private final List<Account> accounts = new ArrayList<>();
    private final List<AuthLog> authLogs = new ArrayList<>();
    private final AccountType type;

    private final File accountFile;
    private final File authLogsFile;

    public AuthManager(AccountType type) {
        this.type = type;

        this.accountFile = DataSerializers.getPath(type.name().toLowerCase(Locale.ROOT) + "_accounts.txt");
        this.authLogsFile = DataSerializers.getPath(type.name().toLowerCase(Locale.ROOT) + "_auth_logs.txt");

        this.load();
    }

    public AccountType getType() {
        return type;
    }

    public Iterator<Account> iterator() {
        return this.accounts.iterator();
    }

    public Collection<Account> getAccounts() {
        return this.accounts;
    }

    public Iterator<AuthLog> authLogIterator() {
        return this.authLogs.iterator();
    }

    public void load() {
        accounts.clear();
        authLogs.clear();

        DataSerializers.deserializeLines(Account.SERIALIZER, accountFile, accounts);
        DataSerializers.deserializeLines(AuthLog.SERIALIZER, authLogsFile, authLogs);
    }

    public void save() {
        DataSerializers.serializeValues(Account.SERIALIZER, accountFile, accounts);
        DataSerializers.serializeValues(AuthLog.SERIALIZER, authLogsFile, authLogs);
    }

    public Account getAccountByEmail(String email) {
        // Search for an account with a given email
        for (Account account : accounts) {
            if (account.getEmail().equals(email)) {
                return account;
            }
        }

        return null;
    }

    public Account getAccountByUUID(UUID uuid) {
        // Search for an account with a given UUID
        for (Account account : accounts) {
            if (account.getUUID().equals(uuid)) {
                return account;
            }
        }

        return null;
    }

    public Account create(String email, String displayName, String password) {
        if (this.getAccountByEmail(email) != null) {
            throw new IllegalArgumentException("An account with that email already exists!");
        }

        checkValidEmail(email);
        checkStrongPassword(password);

        // Generate a random UUID. This UUID is used to uniquely identify an account.
        var uuid = UUID.randomUUID();

        // Ensure that the UUID is actually unique, as UUIDs are bound to collide even if it is an incredibly low chance of doing so.
        do {
            if (this.getAccountByUUID(uuid) != null) {
                uuid = UUID.randomUUID();
            } else {
                break;
            }
        } while (true);

        String[] newPasswordWithSalt = hashPassword(password, createSalt());
        var account = new Account(this.getType(), uuid, email, displayName, newPasswordWithSalt[0] + "." + newPasswordWithSalt[1]);
        this.accounts.add(account);
        this.addAuthLog(account, AuthLog.Type.REGISTER);
        this.save();

        return account;
    }

    public void deleteAccount(Account account) {
        this.accounts.remove(account);
        this.save();
    }

    public Account login(String email, String password) {
        var account = this.getAccountByEmail(email);

        if (account == null) {
            throw new IllegalArgumentException("Invalid email or password!");
        }

        String[] splitPassword = account.getPasswordHashWithSalt().split("\\.");
        String currentHash = splitPassword[0];
        byte[] salt = ByteArrayUtils.hexToBytes(splitPassword[1]);

        if (!currentHash.equals(this.hashPassword(password, salt)[0])) {
            throw new IllegalArgumentException("Invalid email or password!");
        }

        this.addAuthLog(account, AuthLog.Type.LOGIN);
        this.save();

        return account;
    }

    private byte[] createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] newSalt = new byte[16];
        random.nextBytes(newSalt);
        return newSalt;
    }

    public void changePassword(Account account, String oldPassword, String newPassword) {
        checkStrongPassword(newPassword);
        byte[] newSalt = createSalt();

        String[] splitPassword = account.getPasswordHashWithSalt().split("\\.");
        String originalHash = splitPassword[0];
        byte[] salt = ByteArrayUtils.hexToBytes(splitPassword[1]);

        String[] newHashWithSalt = this.hashPassword(oldPassword, newSalt);

        if (!originalHash.equals(newHashWithSalt[0])) {
            throw new IllegalArgumentException("Invalid email or password!");
        }

        if (originalHash.equals(this.hashPassword(newPassword, salt)[0])) {
            throw new IllegalArgumentException("New password is the same as the old password!");
        }

        account.setPasswordHashWithSalt(newHashWithSalt[0] + "." + newHashWithSalt[1]);
        this.addAuthLog(account, AuthLog.Type.CHANGE_PASSWORD);
        this.save();
    }

    public void addAuthLog(Account account, AuthLog.Type type) {
        this.addAuthLog(account, type, "");
    }

    public void addAuthLog(Account account, AuthLog.Type type, String extraData) {
        this.authLogs.add(new AuthLog(account.getUUID(), type, System.currentTimeMillis(), extraData));
    }

    private String[] hashPassword(String password, byte[] salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 4096, 256);

            // Convert the hashes into hexadecimal strings, for storage.
            return new String[] {
                ByteArrayUtils.bytesToHex(factory.generateSecret(spec).getEncoded()),
                ByteArrayUtils.bytesToHex(salt),
            };
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
