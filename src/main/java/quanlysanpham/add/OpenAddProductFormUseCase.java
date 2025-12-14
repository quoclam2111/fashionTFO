package quanlysanpham.add;

import quanlysanpham.*;

public class OpenAddProductFormUseCase extends ProductManageControl {
    
    public OpenAddProductFormUseCase(ProductOutputBoundary presenter) {
        super(presenter);
        this.response = new ResponseDataOpenAddProductForm();
    }
    
    @Override
    protected void execute(ProductRequestData request) {
        try {
            ResponseDataOpenAddProductForm openFormResponse = (ResponseDataOpenAddProductForm) response;
            
            openFormResponse.success = true;
            openFormResponse.message = "Form thêm sản phẩm đã sẵn sàng";
            openFormResponse.formTitle = "Thêm sản phẩm mới";
            
        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi khi mở form: " + ex.getMessage();
        }
    }
}