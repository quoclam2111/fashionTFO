package adapters.quanlynguoidung.dangky;

import java.text.SimpleDateFormat;
import java.util.Date;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung. dangky.ResponseDataRegister;

public class RegisterPresenter implements QuanLyNguoiDungOutputBoundary {
    private RegisterViewModel model;
    
    public RegisterPresenter(RegisterViewModel model) {
        this.model = model;
    }
    
    @Override
    public void present(QuanLyNguoiDungResponseData res) {
        ResponseDataRegister registerRes = (ResponseDataRegister) res;
        
        model.success = registerRes.success;
        model.message = registerRes.message;
        
        if (registerRes.registeredUserId != null) {
            model. userId = registerRes.registeredUserId;
        }
        
        // ⭐ THÊM DÒNG NÀY
        if (registerRes.username != null) {
            model.username = registerRes.username;
        }
        
        model.timestamp = converter(res.timestamp);
    }
    
    private String converter(Date timestamp) {
        SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return converter. format(timestamp);
    }
}