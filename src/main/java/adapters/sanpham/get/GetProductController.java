package adapters.sanpham.get;

import quanlysanpham.ProductRequestData;
import quanlysanpham.add.AddProductUseCase;
import quanlysanpham.get.GetProductUseCase;

import java.math.BigDecimal;

public class GetProductController {
    private final GetProductUseCase control;

    public GetProductController(GetProductUseCase control) {
        this.control = control;
    }

    public void execute(GetProductInputDTO dto) {
        // Convert GUI DTO → Request Data
        ProductRequestData req = new ProductRequestData();
        req.productId = dto.productId;

        // ⭐ QUAN TRỌNG: Set searchBy và searchValue
        req.searchBy = "id";
        req.searchValue = dto.productId;

        // Gọi control (chỉ 1 lần)
        control.control(req);
    }
}
