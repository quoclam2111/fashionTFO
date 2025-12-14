package entity;

public class OrderItem {
    private final String productId;
    private final String name;
    private final int quantity;
    private final int price; // in smallest currency unit (e.g. cents)

    public OrderItem(String productId, String name, int quantity, int price) {
        if (productId == null || productId.isBlank()) throw new IllegalArgumentException("productId");
        if (quantity <= 0) throw new IllegalArgumentException("quantity");
        if (price < 0) throw new IllegalArgumentException("price");
        this.productId = productId;
        this.name = name == null ? "" : name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public int getPrice() { return price; }
    public int getTotal() { return price * quantity; }
}