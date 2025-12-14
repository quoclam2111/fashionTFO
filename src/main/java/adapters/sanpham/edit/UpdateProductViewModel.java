package adapters.sanpham.edit;

import quanlysanpham.edit.ProductUpdateViewItem;

public class UpdateProductViewModel extends UpdateProductPublisher {
    public String message;
    public String timestamp;
    public boolean success;
    public String updatedProductId;
    public ProductUpdateViewItem updatedProduct;
}