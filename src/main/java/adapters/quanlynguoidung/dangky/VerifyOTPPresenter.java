package adapters.quanlynguoidung.dangky;

import java.text.SimpleDateFormat;
import java.util.Date;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungResponseData;

public class VerifyOTPPresenter implements QuanLyNguoiDungOutputBoundary {
    private OTPViewModel model;  // ⭐ Đổi từ RegisterViewModel → OTPViewModel
    
    public VerifyOTPPresenter(OTPViewModel model) {
        this.model = model;
    }
    
    @Override
    public void present(QuanLyNguoiDungResponseData res) {
        model.success = res.success;
        model.message = res.message;
        model.userId = res. userId;
        
        // ⭐ Set các field OTP - KHÔNG CẦN check null vì là primitive
        model.otpVerified = res.otpVerified;
        model.remainingAttempts = res.remainingAttempts;
        
        model.timestamp = converter(res.timestamp);
    }
    
    private String converter(Date timestamp) {
        if (timestamp == null) {
            timestamp = new Date();
        }
        SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return converter.format(timestamp);
    }
}