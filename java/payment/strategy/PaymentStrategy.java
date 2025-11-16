package payment.strategy;

import model.OrderDetail;

public interface PaymentStrategy {
    void pay(OrderDetail orderDetail);
}