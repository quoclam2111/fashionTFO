package adapters.sanpham.edit;

import quanlysanpham.ProductOutputBoundary;
import quanlysanpham.ProductResponseData;
import quanlysanpham.edit.ResponseDataUpdateProduct;

import java.text.SimpleDateFormat;


public class UpdateProductPresenter implements ProductOutputBoundary {
    private final UpdateProductViewModel viewModel;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public UpdateProductPresenter(UpdateProductViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(ProductResponseData responseData) {
        if (responseData instanceof ResponseDataUpdateProduct) {
            ResponseDataUpdateProduct response = (ResponseDataUpdateProduct) responseData;

            viewModel.success = response.success;
            viewModel.message = response.message;
            viewModel.timestamp = formatter.format(new java.util.Date());

            if (response.success && response.updatedProduct != null) {
                viewModel.updatedProductId = response.updatedProduct.productId;
            }
        }
    }
}