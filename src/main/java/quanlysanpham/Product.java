package quanlysanpham;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Date;
import java.util.UUID;

public abstract class Product {
    protected String productId;
    protected String productName;
    protected String slug;
    protected String description;
    protected String brandId;
    protected String categoryId;
    protected String defaultImage;
    protected BigDecimal price;
    protected BigDecimal discountPrice;
    protected int stockQuantity;
    protected Status status;
    protected Date createdAt;
    protected Date updatedAt;

    public Product() {
    }

    public Product(String productId, String productName, String slug, String description, String brandId, String categoryId, String defaultImage, BigDecimal price, BigDecimal discountPrice, int stockQuantity, Status status, Date createdAt, Date updatedAt) {
        this.productId = productId == null ? UUID.randomUUID().toString() : productId;
        this.productName = productName;
        this.slug = generateSlug(slug, productName);
        this.description = description;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.defaultImage = defaultImage;
        this.price = price;
        this.discountPrice = discountPrice;
        this.stockQuantity = stockQuantity;
        this.status = status == null ? Status.PUBLISHED : status;
        this.createdAt = createdAt == null ? new Date() : createdAt;
        this.updatedAt = updatedAt == null ? new Date() : updatedAt;
    }
    public abstract void validate();

    public static Date getCurrentTimestamp() {
        return new Date();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public enum Status {
        DRAFT,
        PUBLISHED,
        ARCHIVED;
        public static Status toEnum(String status) {
            for (Status item : values()) {
                if (item.toString().equalsIgnoreCase(status)) return item;
            }
            return null;
        }
    }

    private String generateSlug(String slug, String name) {
        if (slug != null && !slug.trim().isEmpty()) return slug;

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Cannot generate slug because product name is empty");
        }

        String normalized = Normalizer.normalize(name.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")         // bỏ dấu tiếng Việt
                .replaceAll("[^a-z0-9\\s-]", "")  // bỏ ký tự đặc biệt
                .replaceAll("\\s+", "-")          // thay khoảng trắng bằng "-"
                .replaceAll("^-+|-+$", "");       // xóa dấu '-' ở đầu/cuối

        return normalized;
    }

}
