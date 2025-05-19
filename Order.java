package ModelClass;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Order {
    private String orderId;
    private String userId;
    private String proId;
    private String orderTime;

    // Constructor with validation
    public Order(String orderId, String userId, String proId, String orderTime) {
        if (!orderId.matches("o_\\d{5}")) {
            throw new IllegalArgumentException("Order ID must be in format o_5digits (e.g., o_12345)");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (proId == null || proId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        if (orderTime == null || !orderTime.matches("\\d{2}-\\d{2}-\\d{4}_\\d{2}:\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Order time must be in format DD-MM-YYYY_HH:MM:SS");
        }
        
        this.orderId = orderId;
        this.userId = userId;
        this.proId = proId;
        this.orderTime = orderTime;
    }

    // Default constructor with default values
    public Order() {
        this.orderId = generateOrderId();
        this.userId = "guest";
        this.proId = "default_product";
        this.orderTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
    }

    // Getters and Setters with validation
    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public String getProId() { return proId; }
    public String getOrderTime() { return orderTime; }

    public void setOrderId(String orderId) {
        if (!orderId.matches("o_\\d{5}")) {
            throw new IllegalArgumentException("Order ID must be in format o_5digits");
        }
        this.orderId = orderId;
    }

    public void setUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        this.userId = userId;
    }

    public void setProId(String proId) {
        if (proId == null || proId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        this.proId = proId;
    }

    public void setOrderTime(String orderTime) {
        if (orderTime == null || !orderTime.matches("\\d{2}-\\d{2}-\\d{4}_\\d{2}:\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Invalid time format. Use DD-MM-YYYY_HH:MM:SS");
        }
        this.orderTime = orderTime;
    }

    private String generateOrderId() {
        Random rand = new Random();
        return String.format("o_%05d", rand.nextInt(100000));
    }

    //Save order to file
    public boolean saveToFile() {
        try {
            // Ensure directory exists
            new File("data").mkdirs();
            
            try (PrintWriter out = new PrintWriter(new FileWriter("data/orders.txt", true))) {
                out.println(this.toFileFormat());
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error saving order: " + e.getMessage());
            return false;
        }
    }

    private String toFileFormat() {
        return String.join(",", orderId, userId, proId, orderTime);
    }

    public static List<Order> loadAllOrders() {
        List<Order> orders = new ArrayList<>();
        File file = new File("data/orders.txt");
        
        if (!file.exists()) return orders;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    orders.add(new Order(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading orders: " + e.getMessage());
        }
        return orders;
    }

    @Override
    public String toString() {
        return String.format("{\"order_id\":\"%s\", \"user_id\":\"%s\", \"pro_id\":\"%s\", \"order_time\":\"%s\"}",
            orderId, userId, proId, orderTime);
    }

    public static void main(String[] args) {
        Order order1 = new Order("o_00123", "user1", "prod100", "01-01-2023_12:30:00");
        order1.saveToFile();

        Order order2 = new Order();
        order2.saveToFile();

        // Load and display all orders
        System.out.println("All Orders:");
        Order.loadAllOrders().forEach(System.out::println);
    }
}