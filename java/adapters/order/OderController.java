package adapters.order;

import java.util.List;

import Pay.CreateOrderDetailControl;
import repository.DTO.OrderDetailDTO;
import repository.DTO.ProductVariantDTO;

public class OderController {

    public OrderDetailDTO handleCreateOrder(String customerId, String paymentMethod, List<ProductVariantDTO> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            System.out.println("Giỏ hàng trống!");
            return null;
        }
        return CreateOrderDetailControl.createOrderDetail(customerId, paymentMethod, cartItems);
    }
}
