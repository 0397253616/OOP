package ModelClass;

public class Product {
    private String proId;          
    private String proModel;        
    private String proCategory;     
    private String proName;        
    private double proCurrentPrice; 
    private double proRawPrice;    
    private double proDiscount;    
    private int proLikesCount;      

    public Product(String proId, String proModel, String proCategory, String proName, 
                  double proCurrentPrice, double proRawPrice, double proDiscount, int proLikesCount) {
        this.proId = proId;
        this.proModel = proModel;
        this.proCategory = proCategory;
        this.proName = proName;
        this.proCurrentPrice = proCurrentPrice;
        this.proRawPrice = proRawPrice;
        this.proDiscount = proDiscount;
        this.proLikesCount = proLikesCount;
    }

    
    public Product() {
        this.proId = "Default Product";
        this.proModel = "Default Model";
        this.proCategory = "Default Category";
        this.proName = "Default Name";
        this.proCurrentPrice = 0.0;
        this.proRawPrice = 0.0;
        this.proDiscount = 0.0;
        this.proLikesCount = 0;
    }

  
    @Override
    public String toString() {
        return "{" +
               "\"pro_id\":\"" + proId + "\"" + ", " +
               "\"pro_model\":\"" + proModel + "\"" + ", " +
               "\"pro_category\":\"" + proCategory + "\"" + ", " +
               "\"pro_name\":\"" + proName + "\"" + ", " +
               "\"pro_current_price\":\"" + proCurrentPrice + "\"" + ", " +
               "\"pro_raw_price\":\"" + proRawPrice + "\"" + ", " +
               "\"pro_discount\":\"" + proDiscount + "\"" + ", " +
               "\"pro_likes_count\":\"" + proLikesCount + "\"" +
               "}";
    }

   
    public static Product fromString(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 8) {
                String proId = parts[0];
                String proModel = parts[1];
                String proCategory = parts[2];
                String proName = parts[3];
                double proCurrentPrice = Double.parseDouble(parts[4]);
                double proRawPrice = Double.parseDouble(parts[5]);
                double proDiscount = Double.parseDouble(parts[6]);
                int proLikesCount = Integer.parseInt(parts[7]);
                
                return new Product(proId, proModel, proCategory, proName, 
                                 proCurrentPrice, proRawPrice, proDiscount, proLikesCount);
            }
        } catch (Exception e) {
            System.err.println("Error parsing product data: " + e.getMessage());
        }
        return null;
    }

    public String toFileString() {
        return String.format("%s,%s,%s,%s,%.2f,%.2f,%.2f,%d", 
                proId, proModel, proCategory, proName, 
                proCurrentPrice, proRawPrice, proDiscount, proLikesCount);
    }
    
    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProModel() {
        return proModel;
    }

    public void setProModel(String proModel) {
        this.proModel = proModel;
    }

    public String getProCategory() {
        return proCategory;
    }

    public void setProCategory(String proCategory) {
        this.proCategory = proCategory;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public double getProCurrentPrice() {
        return proCurrentPrice;
    }

    public void setProCurrentPrice(double proCurrentPrice) {
        this.proCurrentPrice = proCurrentPrice;
    }

    public double getProRawPrice() {
        return proRawPrice;
    }

    public void setProRawPrice(double proRawPrice) {
        this.proRawPrice = proRawPrice;
    }

    public double getProDiscount() {
        return proDiscount;
    }

    public void setProDiscount(double proDiscount) {
        this.proDiscount = proDiscount;
    }

    public int getProLikesCount() {
        return proLikesCount;
    }

    public void setProLikesCount(int proLikesCount) {
        this.proLikesCount = proLikesCount;
    }
}