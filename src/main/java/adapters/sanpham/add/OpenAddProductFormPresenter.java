package adapters.sanpham.add;

import quanlysanpham.ProductOutputBoundary;
import quanlysanpham.ProductResponseData;
import quanlysanpham.add.ResponseDataOpenAddProductForm;

public class OpenAddProductFormPresenter implements ProductOutputBoundary {
    private OpenAddProductFormViewModel model;
    
    public OpenAddProductFormPresenter(OpenAddProductFormViewModel model) {
        this.model = model;
    }
    
    @Override
    public void present(ProductResponseData res) {
        ResponseDataOpenAddProductForm openFormRes = (ResponseDataOpenAddProductForm) res;
        
        model.success = openFormRes.success;
        model.message = openFormRes.message;
        model.formTitle = openFormRes.formTitle;
    }
}