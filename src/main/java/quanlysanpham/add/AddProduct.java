package quanlysanpham.add;

import quanlysanpham.Product;

import java.math.BigDecimal;
import java.util.Date;

public class AddProduct extends Product {
    public AddProduct() {
    }

    public AddProduct(String productId, String productName, String slug, String description, String brandId, String categoryId, String defaultImage, BigDecimal price, BigDecimal discountPrice, int stockQuantity, Status status, Date createdAt, Date updatedAt) {
        super(productId, productName, slug, description, brandId, categoryId, defaultImage, price, discountPrice, stockQuantity, status, createdAt, updatedAt);
    }

    @Override
    public void validate() {
        checkPrice(this.price);
        checkProductName(this.productName);
        checkStockQuantity(this.stockQuantity);
    }


    private void checkProductName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
    }

    private void checkPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be null or negative");
        }
    }

    private void checkStockQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
    }
}
