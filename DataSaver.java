package ModelClass;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataSaver {
    public static void saveToTxt(List<?> data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Object obj : data) {
                writer.write(obj.toString()); // Ghi dữ liệu từng dòng (dựa vào `toString()`)
                writer.newLine(); 
            }
            System.out.println("Data saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving data to TXT file: " + e.getMessage());
        }
    }
}
