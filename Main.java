package ModelClass;
import java.util.Scanner;
import java.util.List;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static IOInterface ioInterface = IOInterface.getInstance();
    private static UserOperation userOperation = UserOperation.getInstance();

    public static void main(String[] args) {
        // Tạo danh sách User
        List<User> users = List.of(
            new User("u_0000000001", "admin", "^^qwXzRtYuI7PaSd$$", "01-01-2024_09:00:00", "admin", null, null),
            new User("u_0000000002", "john_doe", "^^aB3cD4eF5gH6i$$", "15-01-2024_14:30:45", "customer", "john.doe@example.com", "0412345678")
        );

        // Tạo danh sách Product
        List<Product> products = List.of(
            new Product("p001", "iPhoneX", "Smartphone", "iPhone X 64GB", 699.99, 999.99, 30.0, 245),
            new Product("p002", "GalaxyS21", "Smartphone", "Samsung Galaxy S21 128GB", 749.99, 899.99, 16.67, 198)
        );

        // Tạo danh sách Order
        List<Order> orders = List.of(
            new Order("o_00001", "u_0000000002", "p001", "20-01-2024_09:15:32"),
            new Order("o_00002", "u_0000000003", "p005", "21-01-2024_14:23:47")
        );

        // Lưu dữ liệu vào file TXT
        DataSaver.saveToTxt(users, "users.txt");
        DataSaver.saveToTxt(products, "products.txt");
        DataSaver.saveToTxt(orders, "orders.txt");

        boolean running = true;
        while (running) {
            ioInterface.mainMenu();
            int choice = getChoice();

            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> exitSystem();
                default -> ioInterface.printErrorMessage("Main Menu", "Invalid choice. Please try again.");
            }
        }
    }

    private static void login() {
        ioInterface.printMessage("Enter your login credentials:");
        String[] credentials = ioInterface.getUserInput("Enter username and password: ", 2);

        User user = userOperation.login(credentials[0], credentials[1]);
        if (user == null) {
            ioInterface.printErrorMessage("Login", "Invalid username or password.");
            return;
        }

        ioInterface.printMessage("Login successful. Welcome, " + user.getUserName() + "!");
        navigateUserRole(user);
    }

    private static void register() {
        ioInterface.printMessage("Customer Registration:");
        String[] registrationData = ioInterface.getUserInput("Enter username, password, email, and mobile: ", 4);

        boolean success = CustomerOperation.getInstance().registerCustomer(
                registrationData[0], registrationData[1], registrationData[2], registrationData[3]);

        if (success) {
            ioInterface.printMessage("Registration successful. You can now log in.");
        } else {
            ioInterface.printErrorMessage("Registration", "Registration failed. Please try again.");
        }
    }

    private static void navigateUserRole(User user) {
        if ("admin".equals(user.getUserRole())) {
            adminMenu();
        } else if ("customer".equals(user.getUserRole())) {
            customerMenu(user);
        }
    }

    private static void adminMenu() {
        boolean adminRunning = true;
        while (adminRunning) {
            ioInterface.adminMenu();
            int choice = getChoice();

            switch (choice) {
                case 1 -> ProductOperation.getInstance().extractProductsFromFiles();
                case 2 -> ioInterface.printMessage("Add customers functionality not implemented.");
                case 3 -> ioInterface.printMessage("Show customers functionality not implemented.");
                case 4 -> ioInterface.printMessage("Show orders functionality not implemented.");
                case 5 -> ioInterface.printMessage("Generate test data functionality not implemented.");
                case 6 -> ioInterface.printMessage("Generate statistical figures functionality not implemented.");
                case 7 -> ioInterface.printMessage("Delete all data functionality not implemented.");
                case 8 -> {
                    ioInterface.printMessage("Logging out of admin mode.");
                    adminRunning = false;
                }
                default -> ioInterface.printErrorMessage("Admin Menu", "Invalid choice. Please try again.");
            }
        }
    }

    private static void customerMenu(User customer) {
        boolean customerRunning = true;
        while (customerRunning) {
            ioInterface.customerMenu();
            int choice = getChoice();

            switch (choice) {
                case 1 -> ioInterface.printMessage("Show profile functionality not implemented.");
                case 2 -> ioInterface.printMessage("Update profile functionality not implemented.");
                case 3 -> ioInterface.printMessage("Show products functionality not implemented.");
                case 4 -> ioInterface.printMessage("Show order history functionality not implemented.");
                case 5 -> ioInterface.printMessage("Generate consumption figures functionality not implemented.");
                case 6 -> {
                    ioInterface.printMessage("Logging out of customer mode.");
                    customerRunning = false;
                }
                default -> ioInterface.printErrorMessage("Customer Menu", "Invalid choice. Please try again.");
            }
        }
    }

    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void exitSystem() {
        ioInterface.printMessage("Exiting the system. Goodbye!");
        System.exit(0);
    }
}
