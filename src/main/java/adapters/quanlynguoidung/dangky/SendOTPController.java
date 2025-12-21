package adapters.quanlynguoidung.dangky;

import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.dangky.RegisterUseCase;

/**
 * Controller riêng cho việc gửi OTP khi đăng ký
 */
public class SendOTPController {
    private final RegisterUseCase useCase;
    
    public SendOTPController(RegisterUseCase useCase) {
        this.useCase = useCase;
    }
    
    public void executeWithDTO(RegisterInputDTO dto) {
        QuanLyNguoiDungRequestData req = new QuanLyNguoiDungRequestData();
        req.action = "register";
        req.username = dto.username;
        req.password = dto.password;
        req.fullName = dto.fullName;
        req.email = dto.email;
        req.phone = dto.phone;
        req.address = dto.address;
        
        useCase.control(req);
    }
}