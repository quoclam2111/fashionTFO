package Pay.entity;
import repository.DTO.OrderDetailDTO;

public class BankingQR extends Payment {

    public BankingQR(OrderDetailDTO orderDetail) {
        super(orderDetail);
    }

    @Override
    public double tinhTongTien() {
        return getThanhTien() + (getThanhTien() * 0.05);
    }
}