package repository.DTO;

import java.util.Date;
import java.util.List;

public class OrderDTO {
    public String id;
    public String orderNo;           // ⭐ THÊM
    public String userId;
    public String customerName;
    public String customerPhone;
    public String customerAddress;
    public double totalAmount;
    public String paymentMethod;     // ⭐ THÊM
    public String paymentStatus;     // ⭐ THÊM
    public String status;            // order_status
    public Date orderDate;
    public String note;
    
    // ⭐ THÊM - Danh sách sản phẩm
    public List<OrderItemDTO> items;
}