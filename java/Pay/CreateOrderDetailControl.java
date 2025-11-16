package Pay;

import java.util.List;
import java.util.UUID;

import repository.DTO.OrderDetailDTO;
import repository.DTO.ProductVariantDTO;

public class CreateOrderDetailControl {

    public static OrderDetailDTO createOrderDetail(String customerId, String paymentMethod, List<ProductVariantDTO> items) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        double totalAmount = 0;

        return new OrderDetailDTO(orderId, customerId, totalAmount, paymentMethod, items);
    }
}
