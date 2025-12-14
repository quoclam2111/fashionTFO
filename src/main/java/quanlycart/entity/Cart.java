package quanlycart.entity;

import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart(List<CartItem> items) {
        this.items = items;
    }

    public List<CartItem> getItems() { 
        return items; 
    }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getTotal).sum();
    }
}