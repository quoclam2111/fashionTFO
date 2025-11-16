package Pay.entity;
import repository.DTO.OrderDetailDTO;
import repository.DTO.ProductVariantDTO;

public abstract class Payment {
    protected OrderDetailDTO orderDetail;
    
    public Payment(OrderDetailDTO orderDetail) {
        this.orderDetail = orderDetail;
    }
    
    // Static method - Controller chỉ cần gọi cái này
    public static double tinhTongTien(OrderDetailDTO orderDetail) {
        String paymentMethod = orderDetail.getPaymentMethod();
        
        if ("BANKINGQR".equalsIgnoreCase(paymentMethod)) {
            BankingQR payment = new BankingQR(orderDetail);
            return payment.tinhTongTien(); // Thành tiền + 5%
        } else if ("COD".equalsIgnoreCase(paymentMethod)) {
            COD payment = new COD(orderDetail);
            return payment.tinhTongTien(); // Thành tiền + 30,000đ
        }
        
        throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " + paymentMethod);
    }
    
    protected double tinhThanhTienCoBan() {
        double tong = 0;
        if (orderDetail.getOrderItems() != null) {
            for (ProductVariantDTO item : orderDetail.getOrderItems()) {
                tong += item.getPrice() * item.getQuantity();
            }
        }
        return tong;
    }
    
    public double getThanhTien() {
        return tinhThanhTienCoBan();
    }
    
    public abstract double tinhTongTien();
}