package adapters.sanpham.list;


import quanlysanpham.ProductOutputBoundary;
import quanlysanpham.ProductResponseData;
import quanlysanpham.list.ResponseDataProductList;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListProductPresenter implements ProductOutputBoundary {
    private ListProductViewModel model;

    public ListProductPresenter(ListProductViewModel model) {
        this.model = model;
    }

    @Override
    public void present(ProductResponseData res) {
        ResponseDataProductList listRes = (ResponseDataProductList) res;

        model.message = listRes.message;
        model.success = listRes.success;
        model.timestamp = converter(res.timestamp);
        model.products = listRes.products;
        model.totalCount = listRes.totalCount;
        model.filteredCount = listRes.filteredCount;
    }

    private String converter(Date timestamp) {
        SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return converter.format(timestamp);
    }
}