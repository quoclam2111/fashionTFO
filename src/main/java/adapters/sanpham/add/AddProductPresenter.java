package adapters.sanpham.add;



import quanlysanpham.ProductOutputBoundary;
import quanlysanpham.ProductResponseData;
import quanlysanpham.add.ResponseDataAddProduct;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddProductPresenter implements ProductOutputBoundary {
    private AddProductViewModel model;

    public AddProductPresenter(AddProductViewModel model) {
        this.model = model;
    }

    @Override
    public void present(ProductResponseData res) {
        ResponseDataAddProduct resAdd = (ResponseDataAddProduct) res;

        if (resAdd.message != null) {
            model.message = resAdd.message;
        }

        model.success = resAdd.success;

        if (resAdd.productId != null) {
            model.productId = resAdd.productId;
        }

        model.timestamp = converter(res.timestamp);
    }

    private String converter(Date timestamp) {
        SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return converter.format(timestamp);
    }
}