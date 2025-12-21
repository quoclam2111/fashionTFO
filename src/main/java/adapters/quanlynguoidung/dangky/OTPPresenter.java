package adapters.quanlynguoidung.dangky;

import java.text.SimpleDateFormat;
import java.util.Date;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung. dangky.ResponseDataRegister;

public class OTPPresenter implements QuanLyNguoiDungOutputBoundary {
    private OTPViewModel model;
    
    public OTPPresenter(OTPViewModel model) {
        this.model = model;
    }
    
    @Override
    public void present(QuanLyNguoiDungResponseData res) {
        // Xử lý response cho đăng ký (gửi OTP)
        if (res instanceof ResponseDataRegister) {
            ResponseDataRegister registerRes = (ResponseDataRegister) res;
            
            model.success = registerRes.success;
            model.message = registerRes.message;
            
            // Set userId
            if (registerRes.registeredUserId != null) {
                model.userId = registerRes.registeredUserId;
            }
            
            // ✅ Sửa:  Truy cập trực tiếp vì là boolean primitive
            model.otpSent = registerRes.otpSent;
            
        } else {
            // Xử lý response cho verify OTP
            model.success = res. success;
            model.message = res.message;
            model. userId = res.userId;
            
            // ✅ Sửa:  Truy cập trực tiếp vì là boolean primitive
            model.otpVerified = res.otpVerified;
            model.otpSent = res.otpSent;
            model.remainingAttempts = res.remainingAttempts;
            model.expiryMinutes = 10;
        }
        
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