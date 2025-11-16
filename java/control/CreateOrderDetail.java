package control;

import java.util.List;
import java.util.UUID;
import model.OrderDetail;
import model.ProductVariantDTO;

public class CreateOrderDetail {

    public static OrderDetail createOrderDetail(String customerId, String paymentMethod, List<ProductVariantDTO> items) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        double totalAmount = 0;

        return new OrderDetail(orderId, customerId, totalAmount, paymentMethod, items);
    }
}
