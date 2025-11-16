package Pay;


import repository.DTO.OrderDetailDTO;

public interface PaymentInputBoundary {
    void input(OrderDetailDTO orderDetail, String paymentMethod);
}