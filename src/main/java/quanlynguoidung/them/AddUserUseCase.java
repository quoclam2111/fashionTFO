package quanlynguoidung.them;

import quanlynguoidung.*;
import repository.AddUserRepoGateway;
import repository.DTO.UserDTO;

/**
 * Control cụ thể cho Add User
 */
public class AddUserUseCase extends QuanLyNguoiDungControl {
    private final AddUserRepoGateway repository;
    
    public AddUserUseCase(AddUserRepoGateway repository, 
                          QuanLyNguoiDungOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new ResponseDataAddUser();
    }
    
    @Override
	public void execute(QuanLyNguoiDungRequestData request) {
        try {
            // 1. Validate input - sẽ ném ngoại lệ nếu sai
            User.checkUsername(request.username);
            User.checkPassword(request.password);
            User.checkEmail(request.email);
            User.checkPhone(request.phone);
            
            // 2. Kiểm tra email đã tồn tại
            if (repository.existsByEmail(request.email)) {
                response.success = false;
                response.message = "Email này đã được sử dụng!";
                return; // ⭐ RETURN để không chạy tiếp
            }
            
            // 3. Convert input → User entity (Business Object)
            User user = convertToBusinessObject(request);
            
            // 4. Convert User → UserDTO
            UserDTO dto = convertToDTO(user);
            
            // 5. Save vào repository
            repository.save(dto);
            
            // 6. Set response
            ResponseDataAddUser addResponse = (ResponseDataAddUser) response;
            
            response.success = true;
            response.message = "Thêm người dùng thành công!";
            addResponse.addedUserId = user.getId();
            
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
    private User convertToBusinessObject(QuanLyNguoiDungRequestData request) {
        return new User(
            null,                   // ID sẽ tự sinh UUID
            request.username,
            request.password,
            request.fullName,
            request.email,
            request.phone,
            request.address,
            "active"                // default status
        );
    }
    
    /**
     * Convert Domain User → UserDTO
     */
    private UserDTO convertToDTO(User user) {
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