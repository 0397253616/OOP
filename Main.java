package ModelClass;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static IOInterface ioInterface = IOInterface.getInstance();
    private static UserOperation userOperation = UserOperation.getInstance();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            ioInterface.mainMenu(); 
            int choice = getChoice();

            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> {
                    ioInterface.printMessage("Exiting the system. Goodbye!");
                    running = false;
                }
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
        if ("admin".equals(user.getUserRole())) {
            adminMenu();
        } else if ("customer".equals(user.getUserRole())) {
            customerMenu(user);
        }
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

    private static void adminMenu() {
        boolean adminRunning = true;
        while (adminRunning) {
            ioInterface.adminMenu(); // Display admin menu
            int choice = getChoice();

            switch (choice) {
                case 1 -> ProductOperation.getInstance().extractProductsFromFiles(); // Placeholder
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
            ioInterface.customerMenu(); // Display customer menu
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
}
