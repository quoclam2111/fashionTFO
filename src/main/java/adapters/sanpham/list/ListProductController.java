package adapters.sanpham.list;


import quanlysanpham.ProductRequestData;
import quanlysanpham.list.ProductListUseCase;

public class ListProductController {
    private final ProductListUseCase control;

    public ListProductController(ProductListUseCase control) {
        this.control = control;
    }

    public void execute(ListProductInputDTO dto) {
        ProductRequestData req = new ProductRequestData();
        req.statusFilter = dto.statusFilter != null ? dto.statusFilter : "all";
        req.sortBy = dto.sortBy != null ? dto.sortBy : "fullName";
        req.ascending = dto.ascending;


        control.control(req);
    }
}