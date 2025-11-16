package entity;
import model.OrderDetail;


public class COD extends Payment {

    public COD(OrderDetail orderDetail) {
        super(orderDetail);
    }

    @Override
    public double tinhTongTien() {
        return getThanhTien() + 30000;
    }
}