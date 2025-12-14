package adapters.sanpham.delete;

import quanlysanpham.ProductRequestData;
import quanlysanpham.delete.DeleteProductUseCase;

public class DeleteProductController {
    private final DeleteProductUseCase useCase;

    public DeleteProductController(DeleteProductUseCase useCase) {
        this.useCase = useCase;
    }

    public void execute(DeleteProductInputDTO dto) {
        ProductRequestData req = new ProductRequestData();
        req.productId = dto.productId;

        useCase.control(req);
    }
}