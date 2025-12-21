package repository.DTO;

import java.math.BigDecimal;

public class OrderItemDTO {
    public String orderItemId;
    public String orderId;
    public String variantId;
    public int quantity;
    public BigDecimal subtotal;
    
    // Thông tin hiển thị
    public String productId;
    public String productName;
    public String productImage;
    public String colorName;
    public String sizeName;
    public BigDecimal unitPrice;
}