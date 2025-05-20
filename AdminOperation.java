public class AdminOperation {
    private static AdminOperation instance = null;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123"; // Mật khẩu mặc định

    private AdminOperation() {}

    /**
     * Returns the single instance of AdminOperation.
     * @return AdminOperation instance
     */
    public static AdminOperation getInstance() {
        if (instance == null) {
            instance = new AdminOperation();
        }
        return instance;
    }

    /**
     * Creates an admin account. This function should be called when
     * the system starts. The same admin account should not be 
     * registered multiple times.
     */
    public void registerAdmin() {
        UserOperation userOperation = UserOperation.getInstance();

        // Kiểm tra xem tài khoản admin đã tồn tại chưa
        if (!userOperation.checkUsernameExist(ADMIN_USERNAME)) {
            // Tạo tài khoản admin mới
            String userId = userOperation.generateUniqueUserId(); // Tạo user ID duy nhất
            String encryptedPassword = userOperation.encryptPassword(ADMIN_PASSWORD); // Mã hóa mật khẩu

            // Tạo đối tượng admin (lớp User)
            User admin = new User(userId, ADMIN_USERNAME);

            // Đăng ký admin vào hệ thống
            boolean registrationSuccess = userOperation.boolean(ADMIN_USERNAME, encryptedPassword, admin);

            if (registrationSuccess) {
                System.out.println("✅ Admin account successfully created!");
            } else {
                System.out.println("❌ Failed to create admin account.");
            }
        } else {
            System.out.println("ℹ️ Admin account already exists.");
        }
    }
}
