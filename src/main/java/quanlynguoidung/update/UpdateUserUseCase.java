package quanlynguoidung.update;

import java.util.Optional;

import quanlynguoidung.QuanLyNguoiDungControl;
import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.User;
import repository.UpdateUserRepositoryGateway;
import repository.DTO.UserDTO;

public class UpdateUserUseCase extends QuanLyNguoiDungControl {
    private final UpdateUserRepositoryGateway repository;
    
    public UpdateUserUseCase(UpdateUserRepositoryGateway repository, 
                            QuanLyNguoiDungOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new ResponseDataUpdateUser();
    }
    
    @Override
    public void execute(QuanLyNguoiDungRequestData request) {
        try {
            // 1. Kiểm tra user có tồn tại không
            Optional<UserDTO> existingUserOpt = repository.findById(request.userId);
            
            if (!existingUserOpt.isPresent()) {
                response.success = false;
                response.message = "Không tìm thấy người dùng với ID: " + request.userId;
                return;
            }
            
            UserDTO existingUser = existingUserOpt.get();
            
            // 2. Validate dữ liệu mới
            if (request.fullName != null) {
                existingUser.fullName = request.fullName;
            }
            
            if (request.email != null && !request.email.equals(existingUser.email)) {
                User.checkEmail(request.email);
                
                // Kiểm tra email mới đã được dùng chưa
                if (repository.existsByEmailExcludingUser(request.email, request.userId)) {
                    response.success = false;
                    response.message = "Email này đã được sử dụng bởi người khác!";
                    return;
                }
                existingUser.email = request.email;
            }
            
            if (request.phone != null && !request.phone.equals(existingUser.phone)) {
                User.checkPhone(request.phone);
                
                // Kiểm tra phone mới đã được dùng chưa
                if (repository.existsByPhoneExcludingUser(request.phone, request.userId)) {
                    response.success = false;
                    response.message = "Số điện thoại này đã được sử dụng bởi người khác!";
                    return;
                }
                existingUser.phone = request.phone;
            }
            
            if (request.address != null) {
                existingUser.address = request.address;
            }
            
            if (request.status != null) {
                existingUser.status = request.status;
            }
            
            // 3. Cập nhật password nếu có (optional)
            if (request.password != null && !request.password.trim().isEmpty()) {
                User.checkPassword(request.password);
                existingUser.password = request.password;
            }
            
            // 4. Lưu vào database
            repository.update(existingUser);
            
            // 5. Set response
            ResponseDataUpdateUser updateResponse = (ResponseDataUpdateUser) response;
            updateResponse.success = true;
            updateResponse.message = "Cập nhật người dùng thành công!";
            updateResponse.updatedUser = existingUser;
            
        } catch (IllegalArgumentException ex) {
            response.success = false;
            response.message = ex.getMessage();
        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
    }
}
