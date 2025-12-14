package adapters.quanlynguoidung.dangky;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung.dangky.ResponseDataOpenRegisterForm;

public class OpenRegisterFormPresenter implements QuanLyNguoiDungOutputBoundary {
    private OpenRegisterFormViewModel model;
    
    public OpenRegisterFormPresenter(OpenRegisterFormViewModel model) {
        this.model = model;
    }
    
    @Override
    public void present(QuanLyNguoiDungResponseData res) {
        ResponseDataOpenRegisterForm openFormRes = (ResponseDataOpenRegisterForm) res;
        
        model.success = openFormRes.success;
        model.message = openFormRes.message;
        model.formTitle = openFormRes.formTitle;
    }
}