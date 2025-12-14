package quanlysanpham.list;

import java.math.BigDecimal;
import java.util.Date;

public class ProductViewItem {
    public String productId;
    public String productName;
    public String slug;
    public String description;
    public String brandId;
    public String categoryId;
    public String defaultImage;
    public BigDecimal price;
    public BigDecimal discountPrice;
    public int stockQuantity;
    public String status; // dạng String để dễ nhận từ JSON (sau convert sang enum)
    public Date createdAt;
    public Date updatedAt;
}
