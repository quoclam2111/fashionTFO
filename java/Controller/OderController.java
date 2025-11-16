package Controller;

import java.util.List;

import control.CreateOrderDetail;
import model.OrderDetail;
import model.ProductVariantDTO;

public class OderController {

    public OrderDetail handleCreateOrder(String customerId, String paymentMethod, List<ProductVariantDTO> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            System.out.println("Giỏ hàng trống!");
            return null;
        }
        return CreateOrderDetail.createOrderDetail(customerId, paymentMethod, cartItems);
    }
}
