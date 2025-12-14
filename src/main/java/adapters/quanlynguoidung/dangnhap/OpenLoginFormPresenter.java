package adapters.quanlynguoidung.dangnhap;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung.dangnhap.ResponseDataOpenLoginForm;

public class OpenLoginFormPresenter implements QuanLyNguoiDungOutputBoundary {
    private OpenLoginFormViewModel model;
    
    public OpenLoginFormPresenter(OpenLoginFormViewModel model) {
        this.model = model;
    }
    
    @Override
    public void present(QuanLyNguoiDungResponseData res) {
        ResponseDataOpenLoginForm openFormRes = (ResponseDataOpenLoginForm) res;
        
        model.success = openFormRes.success;
        model.message = openFormRes.message;
        model.formTitle = openFormRes.formTitle;
        model.welcomeMessage = openFormRes.welcomeMessage;
    }
}