package quanlynguoidung.cart;

public class ViewCartOutputData {
    private final String productName;
    private final double price;
    private final int quantity;
    private final double total;

    public ViewCartOutputData(String productName, double price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.total = price * quantity;
    }

    public String getProductName() { return productName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public double getTotal() { return total; }
}
