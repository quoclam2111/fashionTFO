package adapters.sanpham.add;

import quanlysanpham.ProductManageControl;
import quanlysanpham.ProductRequestData;
import quanlysanpham.add.AddProductUseCase;

import java.math.BigDecimal;

public class AddProductController {
    private final ProductManageControl control;

    public AddProductController(AddProductUseCase control) {
        this.control = control;
    }

    public void execute(AddProductInputDTO dto) {
        // Convert GUI DTO → Request Data
        ProductRequestData req = new ProductRequestData();
        req.productName = dto.productName;
        req.description = dto.description;
        req.brandId = dto.brandId;
        req.categoryId = dto.categoryId;
        req.defaultImage = dto.defaultImage;
        req.price = parseBigDecimal(dto.price);
        req.discountPrice = parseBigDecimal(dto.discountPrice);
        req.stockQuantity = Integer.parseInt(dto.stockQuantity);

        control.control(req);
    }
    private BigDecimal parseBigDecimal(String value) {
        try {
            if (value == null || value.isBlank()) return BigDecimal.ZERO;
            return new BigDecimal(value.replace(",", "").trim());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
