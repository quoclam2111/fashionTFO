package adapters.quanlydonhang.listorders;

import java.text.SimpleDateFormat;
import java.util.Date;
import quanlydonhang.QuanLyDonHangOutputBoundary;
import quanlydonhang.QuanLyDonHangResponseData;
import quanlydonhang.list.ResponseDataListOrders;

public class ListOrdersPresenter implements QuanLyDonHangOutputBoundary {
    private ListOrdersViewModel model;

    public ListOrdersPresenter(ListOrdersViewModel model) {
        this.model = model;
    }

    @Override
    public void present(QuanLyDonHangResponseData res) {
        ResponseDataListOrders listRes = (ResponseDataListOrders) res;

        model.message = listRes.message;
        model.success = listRes.success;
        model.timestamp = converter(res.timestamp);
        model.orders = listRes.orders;
        model.totalCount = listRes.totalCount;
        model.filteredCount = listRes.filteredCount;

        // Notify subscribers (GUI)
        model.notifySubscribers();
    }

    private String converter(Date timestamp) {
        SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return converter.format(timestamp);
    }
}