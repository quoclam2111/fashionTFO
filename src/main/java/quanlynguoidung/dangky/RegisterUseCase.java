package quanlynguoidung.dangky;

import quanlynguoidung.*;
import repository.DTO.UserDTO;
import repository.user.RegisterRepoGateway;


/**
 * Control cụ thể cho Add User
 */
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
            // 1. Convert input → AddUser entity
            Register user = convertToBusinessObject(request);
            
            // 2. Validate input - sẽ ném ngoại lệ nếu sai
            user.validate();
            
            // 3. Kiểm tra email đã tồn tại
            if (repository.existsByEmail(request.email)) {
                response.success = false;
                response.message = "Email này đã được sử dụng!";
                return;
            }
            
            // 4. Convert AddUser → UserDTO
            UserDTO dto = convertToDTO(user);
            
            // 5. Save vào repository
            repository.save(dto);
            
            // 6. Set response
            ResponseDataRegister registerResponse = (ResponseDataRegister) response;
            
            response.success = true;
            response.message = "Thêm người dùng thành công!";
            registerResponse.registeredUserId = user.getId();
            registerResponse.username = user.getUsername();
            
        } catch (IllegalArgumentException ex) {
            // Nếu validation fail → báo lỗi
            response.success = false;
            response.message = ex.getMessage();
        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
        
    }
    
    /**
     * Convert Request → Business Object (User Entity)
     */
    private Register convertToBusinessObject(QuanLyNguoiDungRequestData request) {
        return new Register(
            request.username,
            request.password,
            request.fullName,
            request.email,
            request.phone,
            request.address
        );
    }
    
    /**
     * Convert Domain User → UserDTO
     */
    private UserDTO convertToDTO(Register user) {
        UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.password = user.getPassword();  // đã hash nếu cần
        dto.fullName = user.getFullName();
        dto.email = user.getEmail();
        dto.phone = user.getPhone();
        dto.address = user.getAddress();
        dto.status = user.getStatus();
        return dto;
    }
}