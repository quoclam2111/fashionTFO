package Pay;

import repository.DTO.OrderDetailDTO;

public interface CreateQRForm {
    void pay(OrderDetailDTO orderDetail);
}