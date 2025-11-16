package entity;

import model.OrderDetail;
import model.ProductVariantDTO;

public abstract class Payment {
    protected OrderDetail orderDetail;

    public Payment(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
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
