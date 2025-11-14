package quanlynguoidung.cart.entity;

import java.util.List;

public class Cart {
    private int userId;
    private List<CartItem> items;

    public Cart(int userId, List<CartItem> items) {
        this.userId = userId;
        this.items = items;
    }

    public int getUserId() { return userId; }
    public List<CartItem> getItems() { return items; }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getTotal).sum();
    }
}
