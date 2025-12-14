package adapters.sanpham.get;

import quanlysanpham.ProductOutputBoundary;
import quanlysanpham.ProductResponseData;
import quanlysanpham.get.GetProductItem;
import quanlysanpham.get.ResponseDataGetProduct;

import java.text.SimpleDateFormat;

public class GetProductPresenter implements ProductOutputBoundary {
    private final GetProductViewModel viewModel;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public GetProductPresenter(GetProductViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(ProductResponseData responseData) {
        if (responseData instanceof ResponseDataGetProduct) {
            ResponseDataGetProduct response = (ResponseDataGetProduct) responseData;

            viewModel.success = response.success;
            viewModel.message = response.message;
            viewModel.timestamp = formatter.format(new java.util.Date());

            if (response.success && response.product != null) {
                viewModel.product = mapToViewData(response.product);
            } else {
                viewModel.product = null;
            }
        }
    }

    private GetProductViewModel.ProductViewData mapToViewData(GetProductItem item) {
        GetProductViewModel.ProductViewData viewData = new GetProductViewModel.ProductViewData();

        viewData.productId = item.productId;
        viewData.productName = item.productName;
        viewData.slug = item.slug;
        viewData.description = item.description;
        viewData.brandId = item.brandId;
        viewData.categoryId = item.categoryId;
        viewData.defaultImage = item.defaultImage;
        viewData.price = item.price != null ? item.price.toString() : "";
        viewData.discountPrice = item.discountPrice != null ? item.discountPrice.toString() : "";
        viewData.stockQuantity = item.stockQuantity;
        viewData.status = item.status;
        viewData.createdAt = item.createdAt != null ? formatter.format(item.createdAt) : "";
        viewData.updatedAt = item.updatedAt != null ? formatter.format(item.updatedAt) : "";

        return viewData;
    }
}