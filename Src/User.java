import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public abstract class User {
    // Class variables
    protected String userId;
    protected String userName;
    protected String userPassword; // encrypted
    protected String userRegisterTime;
    protected String userRole;

    /**
     * Constructs a user object.
     * @param userId Must be unique, format: u_10 digits, such as u_1234567890
     * @param userName The user's name
     * @param userPassword The user's password (will be encrypted)
     * @param userRegisterTime Format: "DD-MM-YYYY_HH:MM:SS"
     * @param userRole Default value: "customer"
     */
    public User(String userId, String userName, String userPassword,
                String userRegisterTime, String userRole) {

        // Validate userId format
        if (!userId.matches("u_\\d{10}")) {
            throw new IllegalArgumentException("Invalid userId format. Must be: u_10 digits");
        }

        this.userId = userId;
        this.userName = userName;
        this.userPassword = encryptPassword(userPassword);
        this.userRegisterTime = userRegisterTime;
        this.userRole = userRole;
    }

    /**
     * Default constructor
     */
    public User() {
        this.userId = "u_0000000000";
        this.userName = "default_user";
        this.userPassword = encryptPassword("default");
        this.userRegisterTime = "01-01-2000_00:00:00";
        this.userRole = "customer";
    }

    /**
     * Encrypts the password using SHA-256
     * @param password Plain text password
     * @return Encrypted password as hex string
     */
    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the user Information as a formatted string.
     * @return JSON-like format string
     */
    @Override
    public String toString() {
        return String.format(
            "{\"user_id\":\"%s\", \"user_name\":\"%s\", \"user_password\":\"%s\", " +
            "\"user_register_time\":\"%s\", \"user_role\":\"%s\"}",
            userId, userName, userPassword, userRegisterTime, userRole
        );
    }

    // Getters (optional - for access in child classes)
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getUserPassword() { return userPassword; }
    public String getUserRegisterTime() { return userRegisterTime; }
    public String getUserRole() { return userRole; }
}
