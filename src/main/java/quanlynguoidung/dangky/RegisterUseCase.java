package quanlynguoidung.dangky;

import config.EmailService;
import config.OTPUtil;
import quanlynguoidung.*;
import repository. DTO.UserDTO;
import repository. user.RegisterRepoGateway;

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
                request.username,
                request.password,
                request.fullName,
                request.email,
                request. phone,
                request.address
            );
            
            // 2. Validate (kiểm tra format)
            user.validate();
            
            // 3. ✅ Kiểm tra username đã tồn tại chưa
            if (repository. existsByUsername(request.username)) {
                response.success = false;
                response.message = "Tên đăng nhập này đã được sử dụng!";
                return;
            }
            
            // 4. ✅ Kiểm tra email đã tồn tại chưa
            if (repository.existsByEmail(request.email)) {
                response.success = false;
                response.message = "Email này đã được sử dụng!";
                return;
            }
            
            // 5. ✅ Kiểm tra phone đã tồn tại chưa
            if (repository.existsByPhone(request.phone)) {
                response.success = false;
                response.message = "Số điện thoại này đã được sử dụng!";
                return;
            }
            
            // 6. Convert sang DTO
            UserDTO dto = convertToDTO(user);
            
            // 7. ✅ Giờ mới save vào DB (đã chắc chắn không trùng)
            repository.save(dto);
            
            // 8. Generate OTP 6 chữ số
            String otpCode = OTPUtil.generateOTP();
            
            // 9.  Lưu OTP vào DB
            repository.saveOTP(user. getId(), otpCode);
            
            // 10. Gửi OTP qua email
            boolean emailSent = EmailService.sendOTPEmail(
                user.getEmail(), 
                user.getUsername(), 
                otpCode
            );
            
            // 11. Chuẩn bị response
            ResponseDataRegister registerResponse = (ResponseDataRegister) response;
            response.success = true;
            
            if (emailSent) {
                response.message = "Đăng ký thành công! Vui lòng kiểm tra email để lấy mã OTP.";
                registerResponse.otpSent = true;
            } else {
                response.message = "Đăng ký thành công! Tuy nhiên không thể gửi email OTP.  Vui lòng liên hệ admin.";
                registerResponse.otpSent = false;
            }
            
            registerResponse.registeredUserId = user.getId();
            registerResponse. username = user.getUsername();
            registerResponse.userId = user. getId();
            
        } catch (IllegalArgumentException ex) {
            // Lỗi validation (từ Register.validate())
            response.success = false;
            response.message = ex.getMessage();
            
        } catch (Exception ex) {
            // Lỗi không mong muốn
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
            ex.printStackTrace();
        }
    }
    
    private UserDTO convertToDTO(Register user) {
        UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.password = user. getPassword();
        dto.fullName = user.getFullName();
        dto.email = user. getEmail();
        dto.phone = user.getPhone();
        dto.address = user.getAddress();
        dto.status = user.getStatus(); // "pending"
        return dto;
    }
}