package adapters.quanlynguoidung.dangky;

import java. text.SimpleDateFormat;
import java.util.Date;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungResponseData;

public class VerifyOTPPresenter implements QuanLyNguoiDungOutputBoundary {
    private RegisterViewModel model;
    
    public VerifyOTPPresenter(RegisterViewModel model) {
        this.model = model;
    }
    
    @Override
    public void present(QuanLyNguoiDungResponseData res) {
        model.success = res.success;
        model.message = res.message;
        model.userId = res.userId;
        
        if (res.otpVerified != null) {
            // Có thể thêm field vào ViewModel nếu cần
        }
        
        model.timestamp = converter(res.timestamp);
    }
    
    private String converter(Date timestamp) {
        SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return converter. format(timestamp);
    }
}