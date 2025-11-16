package Pay.entity;
import repository.DTO.OrderDetailDTO;


public class COD extends Payment {

    public COD(OrderDetailDTO orderDetail) {
        super(orderDetail);
    }

    @Override
    public double tinhTongTien() {
        return getThanhTien() + 30000;
    }
}