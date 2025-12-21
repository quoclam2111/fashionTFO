package quanlynguoidung.dangky;

import config.EmailService;
import config.OTPUtil;
import config. PasswordUtil;
import quanlynguoidung.*;
import repository. DTO.UserDTO;
import repository.user.RegisterRepoGateway;

public class RegisterUseCase extends QuanLyNguoiDungControl {
    private final RegisterRepoGateway repository;
    
    public RegisterUseCase(RegisterRepoGateway repository, 
                          QuanLyNguoiDungOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new ResponseDataRegister();
    }
    
    @Override
    protected void execute(QuanLyNguoiDungRequestData request) {
        try {
            // 1. Tạo entity
            Register user = new Register(
                request. username,
                request.password,
                request.fullName,
                request.email,
                request.phone,
                request.address
            );
            
            // 2. Validate
            user.validate();
            
            // 3. Check email tồn tại
            if (repository.existsByEmail(request.email)) {
                response. success = false;
                response. message = "Email này đã được sử dụng!";
                return;
            }
            
            // 4. Convert DTO
            UserDTO dto = convertToDTO(user);
            
            // 5. Save user (status = "pending")
            repository.save(dto);
            
            // 6. Generate OTP 6 số
            String otpCode = OTPUtil.generateOTP();
            
            // 7. Lưu OTP vào DB
            repository.saveOTP(user.getId(), otpCode);
            
            // 8. Gửi OTP qua email
            boolean emailSent = EmailService.sendOTPEmail(
                user.getEmail(), 
                user.getUsername(), 
                otpCode
            );
            
            // 9. Response
            ResponseDataRegister registerResponse = (ResponseDataRegister) response;
            response.success = true;
            
            if (emailSent) {
                response.message = "Đăng ký thành công!  Vui lòng kiểm tra email để lấy mã OTP.";
                registerResponse.otpSent = true;  // ⭐ Set flag
            } else {
                response.message = "Đăng ký thành công! Tuy nhiên không thể gửi email OTP.  Vui lòng liên hệ admin.";
                registerResponse.otpSent = false; // ⭐ Set flag
            }
            
            registerResponse.registeredUserId = user.getId();
            registerResponse.username = user.getUsername();
            registerResponse.userId = user.getId(); // ⭐ Set userId cho cha
            
        } catch (IllegalArgumentException ex) {
            response.success = false;
            response.message = ex.getMessage();
        } catch (Exception ex) {
            response. success = false;
            response. message = "Lỗi hệ thống: " + ex.getMessage();
            ex.printStackTrace();
        }
    }
    
    private UserDTO convertToDTO(Register user) {
        UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.password = user.getPassword();
        dto.fullName = user.getFullName();
        dto.email = user.getEmail();
        dto.phone = user.getPhone();
        dto.address = user.getAddress();
        dto.status = user.getStatus(); // "pending"
        return dto;
    }
}