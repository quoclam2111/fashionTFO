package quanlydonhang;

import java.util.Date;
import java.util.UUID;

public class Order {
    private String id;
    private String userId;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private double totalAmount;
    private String status; // "pending", "confirmed", "shipping", "completed", "cancelled"
    private Date orderDate;
    private String note;

    public Order() {}

    public Order(String id, String userId, String customerName, String customerPhone,
                 String customerAddress, double totalAmount, String status,
                 Date orderDate, String note) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.userId = userId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.totalAmount = totalAmount;
        this.status = status == null ? "pending" : status;
        this.orderDate = orderDate == null ? new Date() : orderDate;
        this.note = note;
    }


    // ⭐ Validation methods với message tiếng Việt
    public static void checkCustomerName(String name) {
        if(name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập tên khách hàng!");
    }

    public static void checkCustomerPhone(String phone) {
        if(phone == null || phone.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập số điện thoại!");

        // ✅ FIXED: Phải bắt đầu bằng số 0 và có 10-11 chữ số
        if(!phone.matches("^0\\d{9,10}$"))
            throw new IllegalArgumentException("Số điện thoại phải bắt đầu bằng số 0 và có 10-11 chữ số!");
    }

    public static void checkCustomerAddress(String address) {
        if(address == null || address.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập địa chỉ giao hàng!");
    }

    public static void checkTotalAmount(double amount) {
        if(amount <= 0)
            throw new IllegalArgumentException("Tổng tiền phải lớn hơn 0!");
    }

    public static void checkUserId(String userId) {
        if(userId == null || userId.trim().isEmpty())
            throw new IllegalArgumentException("Thiếu thông tin người dùng!");
    }

    public static Date getCurrentTimestamp() {
        return new Date();
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}