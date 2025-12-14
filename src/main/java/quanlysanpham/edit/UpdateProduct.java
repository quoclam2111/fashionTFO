package quanlysanpham.edit;


import quanlysanpham.Product;

import java.math.BigDecimal;
import java.util.Date;

public class UpdateProduct extends Product {
    public UpdateProduct() {
    }

    public UpdateProduct(String productId, String productName, String slug, String description, String brandId, String categoryId, String defaultImage, BigDecimal price, BigDecimal discountPrice, int stockQuantity, Status status, Date createdAt, Date updatedAt) {
        super(productId, productName, slug, description, brandId, categoryId, defaultImage, price, discountPrice, stockQuantity, status, createdAt, updatedAt);
    }

    @Override
    public void validate() {
        checkProductId(this.productId);

        // Chỉ validate các field được cập nhật
        if(this.productName != null && !this.productName.trim().isEmpty()) {
            checkProductName(this.productName);
        }

        if(this.price != null) {
            checkPrice(this.price);
        }

        if(this.discountPrice != null) {
            checkDiscountPrice(this.discountPrice, this.price);
        }

        if(this.stockQuantity < 0) {
            throw new IllegalArgumentException("Số lượng tồn kho không được âm!");
        }

        if(this.brandId != null && !this.brandId.trim().isEmpty()) {
            checkBrandId(this.brandId);
        }

        if(this.categoryId != null && !this.categoryId.trim().isEmpty()) {
            checkCategoryId(this.categoryId);
        }
    }

    private void checkProductId(String productId) {
        if(productId == null || productId.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng chọn sản phẩm để cập nhật!");
    }

    private void checkProductName(String productName) {
        if(productName.trim().length() < 3)
            throw new IllegalArgumentException("Tên sản phẩm phải có ít nhất 3 ký tự!");
        if(productName.trim().length() > 200)
            throw new IllegalArgumentException("Tên sản phẩm không được vượt quá 200 ký tự!");
    }

    private void checkPrice(BigDecimal price) {
        if(price.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn 0!");
    }

    private void checkDiscountPrice(BigDecimal discountPrice, BigDecimal price) {
        if(discountPrice.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Giá khuyến mãi không được âm!");

        if(price != null && discountPrice.compareTo(price) > 0)
            throw new IllegalArgumentException("Giá khuyến mãi không được lớn hơn giá gốc!");
    }

    private void checkBrandId(String brandId) {
        if(brandId.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng chọn thương hiệu!");
    }

    private void checkCategoryId(String categoryId) {
        if(categoryId.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng chọn danh mục!");
    }
}
