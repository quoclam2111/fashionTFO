package repository.payment;

import java.util.Optional;
import java.util.UUID;

import entity.Order;

public interface OrderPaymentRepository {
    void save(Order order);
    Optional<Order> findById(UUID id);
}