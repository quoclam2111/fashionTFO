package repository.payment;

import application.port.CartRepository;
import pay.entity.OrderItem;

import java.util.*;

public class InMemoryCartRepository implements CartRepository {
    private final Map<String, List<OrderItem>> carts = new HashMap<>();
    @Override
    public List<OrderItem> getCartForUser(String userId) { return carts.getOrDefault(userId, Collections.emptyList()); }
    @Override
    public void clearCart(String userId) { carts.remove(userId); }

    // helper to seed demo cart
    public void addItem(String userId, OrderItem item) {
        carts.computeIfAbsent(userId, k -> new ArrayList<>()).add(item);
    }
}