package quanlynguoidung.dangky;

import quanlynguoidung.*;
import repository. DTO.UserDTO;
import repository. user.RegisterRepoGateway;
import java.util.Optional;

public class VerifyOTPUseCase extends QuanLyNguoiDungControl {
    private final RegisterRepoGateway repository;
    
    public VerifyOTPUseCase(RegisterRepoGateway repository,
                           QuanLyNguoiDungOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new QuanLyNguoiDungResponseData();
    }
    
    @Override
    protected void execute(QuanLyNguoiDungRequestData request) {
        try {
            String userId = request.id;
            String otpCode = request.otpCode;
            
            // 1. Validate input
            if (userId == null || userId.trim().isEmpty()) {
                response.success = false;
                response.message = "User ID không hợp lệ! ";
                return;
            }
            
            if (otpCode == null || otpCode. trim().isEmpty()) {
                response.success = false;
                response.message = "Vui lòng nhập mã OTP!";
                return;
            }
            
            if (otpCode.length() != 6 || ! otpCode.matches("\\d{6}")) {
                response.success = false;
                response.message = "Mã OTP phải gồm 6 chữ số!";
                return;
            }
            
            // 2.  Kiểm tra số lần nhập sai
            int attempts = repository.getOTPAttempts(userId);
            if (attempts >= 5) {
                response.success = false;
                response.message = "Bạn đã nhập sai OTP quá 5 lần!  Vui lòng đăng ký lại.";
                response.remainingAttempts = 0;
                return;
            }
            
            // 3. Verify OTP
            boolean isValid = repository.verifyOTP(userId, otpCode);
            
            if (isValid) {
                // ✅ Xác thực thành công
                repository.markEmailAsVerified(userId);
                
                // ✅ FIX:  Lấy thông tin user để hiển thị username
                Optional<UserDTO> userOpt = repository.findById(userId);
                String username = userOpt.map(u -> u.username).orElse("Người dùng");
                
                response.success = true;
                response.message = "Xác thực email thành công! Chào mừng " + username + "!  Bạn có thể đăng nhập ngay bây giờ.";
                response.userId = userId;
                response.otpVerified = true;
                
            } else {
                // ❌ OTP sai → Tăng attempts
                repository.incrementOTPAttempts(userId);
                int remainingAttempts = 5 - (attempts + 1);
                
                response.success = false;
                response.message = "Mã OTP không chính xác! Còn " + remainingAttempts + " lần thử.";
                response.remainingAttempts = remainingAttempts;
                response.otpVerified = false;
            }
            
        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống:  " + ex.getMessage();
            ex.printStackTrace();
        }
    }
}