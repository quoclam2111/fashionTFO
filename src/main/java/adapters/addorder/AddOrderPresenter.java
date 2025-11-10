package adapters.addorder;

import java.text.SimpleDateFormat;
import java.util.Date;
import quanlydonhang.QuanLyDonHangOutputBoundary;
import quanlydonhang.QuanLyDonHangResponseData;
import quanlydonhang.them.ResponseDataAddOrder;

public class AddOrderPresenter implements QuanLyDonHangOutputBoundary {
    private AddOrderViewModel model;

    public AddOrderPresenter(AddOrderViewModel model) {
        this.model = model;
    }

    @Override
    public void present(QuanLyDonHangResponseData res) {
        ResponseDataAddOrder resAdd = (ResponseDataAddOrder) res;

        if (resAdd.message != null) {
            model.message = resAdd.message;
        }

        model.success = resAdd.success;

        if (resAdd.addedOrderId != null) {
            model.orderId = resAdd.addedOrderId;
            model.orderStatus = resAdd.orderStatus;
        }

        model.timestamp = converter(res.timestamp);

        // Notify subscribers (GUI)
        model.notifySubscribers();
    }

    private String converter(Date timestamp) {
        SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return converter.format(timestamp);
    }
}