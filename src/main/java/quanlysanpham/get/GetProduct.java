package quanlysanpham.get;

import quanlysanpham.Product;

import java.math.BigDecimal;
import java.util.Date;

public class GetProduct extends Product {
    public GetProduct() {
    }

    public GetProduct(String productId, String productName, String slug, String description, String brandId, String categoryId, String defaultImage, BigDecimal price, BigDecimal discountPrice, int stockQuantity, Status status, Date createdAt, Date updatedAt) {
        super(productId, productName, slug, description, brandId, categoryId, defaultImage, price, discountPrice, stockQuantity, status, createdAt, updatedAt);
    }

    @Override
    public void validate() {
        checkId(this.productId);
    }

    private void checkId(String id) {
        if(id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập ID sản phẩm!");
    }
}
