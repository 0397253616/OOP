import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CustomerOperation {
    private static CustomerOperation instance = null;
    private final String FILE_PATH = "data/users.txt";
    private final int PAGE_SIZE = 10;

    private CustomerOperation() {}

    public static CustomerOperation getInstance() {
        if (instance == null) {
            instance = new CustomerOperation();
        }
        return instance;
    }

    public boolean validateEmail(String userEmail) {
        if (userEmail == null) return false;
        return userEmail.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public boolean validateMobile(String userMobile) {
        if (userMobile == null) return false;
        return userMobile.matches("^(04|03)\\d{8}$");
    }

    private boolean checkUserIdExist(String userId) {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = parseLine(line);
                if (parts.length >= 1 && parts[0].equals(userId)) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String generateUniqueUserId() {
        Random rand = new Random();
        String id;
        do {
            StringBuilder sb = new StringBuilder("u_");
            for (int i = 0; i < 10; i++) {
                sb.append(rand.nextInt(10));
            }
            id = sb.toString();
        } while (checkUserIdExist(id));
        return id;
    }

    public boolean registerCustomer(String userName, String userPassword,
                                    String userEmail, String userMobile) {

        UserOperation uo = UserOperation.getInstance();
        if (!uo.validateUsername(userName) || !uo.validatePassword(userPassword)
                || !validateEmail(userEmail) || !validateMobile(userMobile)) {
            return false;
        }

        if (uo.checkUsernameExist(userName)) return false;

        String userId = generateUniqueUserId();
        String encryptedPassword = uo.encryptPassword(userPassword);
        String regTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String data = String.join(",", escape(userId), escape(userName), escape(encryptedPassword),
                escape(userEmail), escape(userMobile), escape(regTime));

        try {
            Files.createDirectories(Paths.get("data"));
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            writer.write(data);
            writer.newLine();
            writer.close();

            Customer customer = new Customer(userId, userName, userEmail, userMobile);
            uo.registerUser(userName, userPassword, customer);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProfile(String attributeName, String value, Customer customerObject) {
        if (customerObject == null || attributeName == null || value == null) return false;
        switch (attributeName.toLowerCase()) {
            case "username":
                if (!UserOperation.getInstance().validateUsername(value)) return false;
                customerObject.setName(value);
                break;
            case "email":
                if (!validateEmail(value)) return false;
                customerObject.setEmail(value);
                break;
            case "mobile":
                if (!validateMobile(value)) return false;
                customerObject.setMobile(value);
                break;
            default:
                return false;
        }
        return updateCustomerInFile(customerObject);
    }

    private boolean updateCustomerInFile(Customer updatedCustomer) {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File("data/temp_users.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = parseLine(line);
                if (parts.length < 6) continue;

                if (parts[0].equals(updatedCustomer.getId())) {
                    String updatedLine = String.join(",", escape(updatedCustomer.getId()), escape(updatedCustomer.getName()),
                            parts[2], escape(updatedCustomer.getEmail()), escape(updatedCustomer.getMobile()), parts[5]);
                    writer.write(updatedLine);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

            writer.close();
            reader.close();

            Files.delete(inputFile.toPath());
            tempFile.renameTo(inputFile);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(String customerId) {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File("data/temp_users.txt");
        boolean deleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = parseLine(line);
                if (parts.length >= 1 && parts[0].equals(customerId)) {
                    deleted = true;
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }

            reader.close();
            writer.close();

            Files.delete(inputFile.toPath());
            tempFile.renameTo(inputFile);
            return deleted;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public CustomerListResult getCustomerList(int pageNumber) {
        List<Customer> customers = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) return new CustomerListResult(customers, pageNumber, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = parseLine(line);
                if (parts.length < 6) continue;
                customers.add(new Customer(unescape(parts[0]), unescape(parts[1]), unescape(parts[3]), unescape(parts[4])));
            }

            int totalPages = (int) Math.ceil(customers.size() / (double) PAGE_SIZE);
            int fromIndex = (pageNumber - 1) * PAGE_SIZE;
            int toIndex = Math.min(fromIndex + PAGE_SIZE, customers.size());

            if (fromIndex >= customers.size()) {
                return new CustomerListResult(Collections.emptyList(), pageNumber, totalPages);
            }

            return new CustomerListResult(customers.subList(fromIndex, toIndex), pageNumber, totalPages);

        } catch (IOException e) {
            e.printStackTrace();
            return new CustomerListResult(Collections.emptyList(), pageNumber, 0);
        }
    }

    public void deleteAllCustomers() {
        try {
            new FileWriter(FILE_PATH, false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Escape/unescape to handle commas in user input
    private String escape(String input) {
        return input.replace(",", "\\,");
    }

    private String unescape(String input) {
        return input.replace("\\,", ",");
    }

    private String[] parseLine(String line) {
        return line.split("(?<!\\\\),"); // split by unescaped commas
    }
}
