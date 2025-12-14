package adapters.quanlynguoidung.dangnhap;

import java.text.SimpleDateFormat;
import java.util.Date;
import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung.dangnhap.ResponseDataLogin;

public class LoginPresenter implements QuanLyNguoiDungOutputBoundary {
    private LoginViewModel model;
    
    public LoginPresenter(LoginViewModel model) {
        this.model = model;
    }
    
    @Override
    public void present(QuanLyNguoiDungResponseData res) {
        ResponseDataLogin loginRes = (ResponseDataLogin) res;
        
        model.success = loginRes.success;
        model.message = loginRes.message;
        
        if (loginRes.success) {
            model.userId = loginRes.userId;
            model.username = loginRes.username;
            model.fullName = loginRes.fullName;
            model.role = loginRes.role;
            model.accountType = loginRes.accountType;
        }
        
        model.timestamp = converter(res.timestamp);
    }
    
    private String converter(Date timestamp) {
        SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return converter.format(timestamp);
    }
}