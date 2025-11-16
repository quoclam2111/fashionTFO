package entity;
import model.OrderDetail;

public class BankingQR extends Payment {

    public BankingQR(OrderDetail orderDetail) {
        super(orderDetail);
    }

    @Override
    public double tinhTongTien() {
        return getThanhTien() + (getThanhTien() * 0.05);
    }
}