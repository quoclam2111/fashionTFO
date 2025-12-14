package adapters.sanpham.delete;

import quanlysanpham.ProductOutputBoundary;
import quanlysanpham.ProductResponseData;
import quanlysanpham.delete.ResponseDataDeleteProduct;

import java.time.LocalDateTime;

public class DeleteProductPresenter implements ProductOutputBoundary {
    private final DeleteProductViewModel viewModel;

    public DeleteProductPresenter(DeleteProductViewModel viewModel) {
        this.viewModel = viewModel;
    }
    @Override
    public void present(ProductResponseData response) {
        ResponseDataDeleteProduct res = (ResponseDataDeleteProduct) response;
        viewModel.success = res.success;
        viewModel.message = res.message;
        viewModel.timestamp = LocalDateTime.now().toString();
        viewModel.deletedProductId = res.deletedProductId;
        viewModel.deleteProductName = res.deletedProductName;

    }
}
