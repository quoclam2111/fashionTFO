package view.interfaces;

import model.OrderDetail;

public interface InputInterface {
    void input(OrderDetail orderDetail, String paymentMethod);
}