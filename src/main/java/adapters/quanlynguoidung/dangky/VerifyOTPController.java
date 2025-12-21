package adapters.quanlynguoidung.dangky;

import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.dangky.VerifyOTPUseCase;

/**
 * Controller riêng cho việc verify OTP
 */
public class VerifyOTPController {
    private final VerifyOTPUseCase useCase;
    
    public VerifyOTPController(VerifyOTPUseCase useCase) {
        this.useCase = useCase;
    }
    
    public void verify(String userId, String otpCode) {
        QuanLyNguoiDungRequestData request = new QuanLyNguoiDungRequestData();
        request.action = "verify-otp";
        request.id = userId;
        request.otpCode = otpCode;
        
        useCase.control(request);
    }
}