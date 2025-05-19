package ModelClass;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderOperation {
    private static OrderOperation instance;

    private OrderOperation() {
    }

    public static OrderOperation getInstance() {
        if (instance == null) {
            instance = new OrderOperation();
        }
        return instance;
    }

    public String generateUniqueOrderId() {
        Random random = new Random();
        int id = random.nextInt(100000);
        return String.format("o_%05d", id);
    }

    public boolean createAnOrder(String customerId, String productId, String createTime) {
        String orderId = generateUniqueOrderId();
        String orderTime = createTime != null ? createTime
                : new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());

        Order order = new Order(orderId, customerId, productId, orderTime);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/orders.txt", true))) {
            writer.write(order.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteOrder(String orderId) {
        File inputFile = new File("data/orders.txt");
        File tempFile = new File("data/orders_temp.txt");

        boolean orderDeleted = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.contains("\"order_id\":\"" + orderId + "\"")) {
                    writer.write(currentLine);
                    writer.newLine();
                } else {
                    orderDeleted = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (orderDeleted) {
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                return false;
            }
        }
        return orderDeleted;
    }

    public OrderListResult getOrderList(String customerId, int pageNumber) {
        final int ITEMS_PER_PAGE = 10;
        List<Order> allOrders = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("data/orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_id\":\"" + customerId + "\"")) {
                    String[] parts = line.replaceAll("[{}\"]", "").split(",");
                    Order order = new Order(
                            parts[0].split(":")[1].trim(),
                            parts[1].split(":")[1].trim(),
                            parts[2].split(":")[1].trim(),
                            parts[3].split(":")[1].trim()
                    );
                    allOrders.add(order);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        int totalOrders = allOrders.size();
        int totalPages = (int) Math.ceil((double) totalOrders / ITEMS_PER_PAGE);

        if (pageNumber < 1 || pageNumber > totalPages) {
            System.out.println("Invalid page number.");
            return null;
        }

        int start = (pageNumber - 1) * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, totalOrders);

        List<Order> currentPageOrders = allOrders.subList(start, end);
        return new OrderListResult(currentPageOrders, pageNumber, totalPages);
    }

    public void deleteAllOrders() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/orders.txt"))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
