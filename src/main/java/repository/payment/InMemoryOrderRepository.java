package repository.payment;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import pay.entity.Order;

public class InMemoryOrderRepository implements OrderPaymentRepository {
    private final Map<UUID, Order> store = new ConcurrentHashMap<>();
    @Override
    public void save(Order order) { store.put(order.getId(), order); }
    @Override
    public Optional<Order> findById(UUID id) { return Optional.ofNullable(store.get(id)); }
}