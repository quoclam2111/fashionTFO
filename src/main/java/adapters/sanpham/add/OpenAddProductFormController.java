package adapters.sanpham.add;

import quanlysanpham.ProductManageControl;
import quanlysanpham.ProductRequestData;
import quanlysanpham.add.OpenAddProductFormUseCase;

public class OpenAddProductFormController {
    private final ProductManageControl control;
    
    public OpenAddProductFormController(OpenAddProductFormUseCase control) {
        this.control = control;
    }
    
    public void openForm() {
        ProductRequestData req = new ProductRequestData();
        execute(req);
    }
    
    public void execute(ProductRequestData req) {
        control.control(req);
    }
}