package Pay;

import repository.DTO.OrderDetailDTO;

public interface PaymentOutputBoundary {
    void output(OrderDetailDTO orderDetail);
}