package repository.DTO;

import java.util.List;


public class OrderDetailDTO {
    private String orderId;
    private String customerId;
    private double totalAmount; 
    private String paymentMethod;      
    private List<ProductVariantDTO> orderItems;

    private boolean isPaid;

    public OrderDetailDTO(String orderId, String customerId, double totalAmount,
                       String paymentMethod, List<ProductVariantDTO> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.orderItems = orderItems;
        this.isPaid = false;
    }

    // --- Getter / Setter ---
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public List<ProductVariantDTO> getOrderItems() { return orderItems; }
    public boolean isPaid() { return isPaid; }

    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setPaid(boolean paid) { isPaid = paid; }

    @Override
    public String toString() {
        return "Order ID: " + orderId +
                "\nCustomer ID: " + customerId +
                "\nTotal Amount: " + totalAmount +
                "\nPayment Method: " + paymentMethod +
                "\nPaid: " + (isPaid ? "Yes" : "No") +
                "\nItems: " + (orderItems != null ? orderItems.size() : 0);
    }
}
