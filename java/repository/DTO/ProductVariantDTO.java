package repository.DTO;

public class ProductVariantDTO {
    private final String variantId;
    private final String productName;
    private final String colorName;
    private final String sizeName;
    private final double price;
    private final int quantity;

    public ProductVariantDTO(String variantId, String productName, String colorName, String sizeName, double price, int quantity) {
        this.variantId = variantId;
        this.productName = productName;
        this.colorName = colorName;
        this.sizeName = sizeName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getVariantId() { return variantId; }
    public String getProductName() { return productName; }
    public String getColorName() { return colorName; }
    public String getSizeName() { return sizeName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}
