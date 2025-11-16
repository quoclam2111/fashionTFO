package repository.DTO;

public class CartDTO {
    private String productId;
    private String productName;
    private double price;
    private int quantity;
    private String variantId; 
    
    public CartDTO(String productId, String productName, double price, int quantity, String variantId) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.variantId = variantId;
    }
    
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getVariantId() { return variantId; }
    
    public void setProductId(String productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
}