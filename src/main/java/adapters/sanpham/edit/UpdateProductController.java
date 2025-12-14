package adapters.sanpham.edit;

import quanlysanpham.ProductRequestData;
import quanlysanpham.edit.UpdateProductUseCase;
import quanlysanpham.list.ProductListUseCase;

import java.math.BigDecimal;

public class UpdateProductController {
    private final UpdateProductUseCase useCase;

    public UpdateProductController(UpdateProductUseCase useCase) {
        this.useCase = useCase;
    }

    public void execute(UpdateProductInputDTO dto) {
        ProductRequestData req = new ProductRequestData();
        req.productId = dto.productId;
        req.productName = dto.productName;
        req.description = dto.description;
        req.brandId = dto.brandId;
        req.categoryId = dto.categoryId;
        req.defaultImage = dto.defaultImage;
        req.price = parseBigDecimal(dto.price);
        req.discountPrice = parseBigDecimal(dto.discountPrice);
        req.stockQuantity = parseInteger(dto.stockQuantity);
        req.status = dto.status;

        useCase.control(req);
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            if (value == null || value.isBlank()) return BigDecimal.ZERO;
            return new BigDecimal(value.replace(",", "").trim());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private Integer parseInteger(String value) {
        try {
            if (value == null || value.isBlank()) return 0;
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

}