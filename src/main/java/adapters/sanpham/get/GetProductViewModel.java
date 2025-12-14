package adapters.sanpham.get;

public class GetProductViewModel {
    public String message;
    public String timestamp;
    public boolean success;
    public ProductViewData product;

    // Inner class để chứa dữ liệu sản phẩm hiển thị
    public static class ProductViewData {
        public String productId;
        public String productName;
        public String slug;
        public String description;
        public String brandId;
        public String categoryId;
        public String defaultImage;
        public String price;
        public String discountPrice;
        public Integer stockQuantity;
        public String status;
        public String createdAt;
        public String updatedAt;
    }
}
