package quanlysanpham.list;

import quanlysanpham.Product;

import java.math.BigDecimal;
import java.util.Date;

public class ListProduct extends Product {
    public ListProduct() {
    }

    public ListProduct(String productId, String productName, String slug, String description, String brandId, String categoryId, String defaultImage, BigDecimal price, BigDecimal discountPrice, int stockQuantity, Status status, Date createdAt, Date updatedAt) {
        super(productId, productName, slug, description, brandId, categoryId, defaultImage, price, discountPrice, stockQuantity, status, createdAt, updatedAt);
    }

    @Override
    public void validate() {

    }
}
