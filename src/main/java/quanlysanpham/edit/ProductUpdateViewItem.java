package quanlysanpham.edit;

import java.math.BigDecimal;
import java.util.Date;

public class ProductUpdateViewItem {
    public String productId;
    public String productName;
    public String slug;
    public String description;
    public String brandId;
    public String categoryId;
    public String defaultImage;
    public BigDecimal price;
    public BigDecimal discountPrice;
    public Integer stockQuantity;
    public String status;
    public Date createdAt;
    public Date updatedAt;
}
