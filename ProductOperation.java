package ModelClass;

import java.io.*;
import java.util.*;

public class ProductOperation {
    private static ProductOperation instance;

    private ProductOperation() {
    }

    public static ProductOperation getInstance() {
        if (instance == null) {
            instance = new ProductOperation();
        }
        return instance;
    }

    public void extractProductsFromFiles() {
        System.out.println("This method will read product data from files.");
    }
    public ProductListResult getProductList(int pageNumber) {
        final int ITEMS_PER_PAGE = 10;
        List<Product> allProducts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("data/products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Product product = new Product(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        Double.parseDouble(parts[4].trim()),
                        Double.parseDouble(parts[5].trim()),
                        Double.parseDouble(parts[6].trim()),
                        Integer.parseInt(parts[7].trim())
                );
                allProducts.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        int totalProducts = allProducts.size();
        int totalPages = (int) Math.ceil((double) totalProducts / ITEMS_PER_PAGE);

        if (pageNumber < 1 || pageNumber > totalPages) {
            System.out.println("Invalid page number.");
            return null;
        }

        int start = (pageNumber - 1) * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, totalProducts);

        List<Product> currentPageProducts = allProducts.subList(start, end);
        return new ProductListResult(currentPageProducts, pageNumber, totalPages);
    }
}
