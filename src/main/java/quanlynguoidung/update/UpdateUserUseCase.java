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
            
            UserDTO dto = existingUserOpt.get();
            
            // 2. Validate dữ liệu mới
            if (request.fullName != null) {
            	dto.fullName = request.fullName;
            }
            
            if (request.email != null && !request.email.equals(dto.email)) {
                User.checkEmail(request.email);
                
                // Kiểm tra email mới đã được dùng chưa
                if (repository.existsByEmailExcludingUser(request.email, request.userId)) {
                    response.success = false;
                    response.message = "Email này đã được sử dụng bởi người khác!";
                    return;
                }
                dto.email = request.email;
            }
            
            if (request.phone != null && !request.phone.equals(dto.phone)) {
                User.checkPhone(request.phone);
                
                // Kiểm tra phone mới đã được dùng chưa
                if (repository.existsByPhoneExcludingUser(request.phone, request.userId)) {
                    response.success = false;
                    response.message = "Số điện thoại này đã được sử dụng bởi người khác!";
                    return;
                }
                dto.phone = request.phone;
            }
            
            if (request.address != null) {
            	dto.address = request.address;
            }
            
            if (request.status != null) {
            	dto.status = request.status;
            }
            
            // 3. Cập nhật password nếu có (optional)
            if (request.password != null && !request.password.trim().isEmpty()) {
                User.checkPassword(request.password);
                dto.password = request.password;
            }
            
            // 4. Lưu vào database
            repository.update(dto);
            
            // 5. ✅ Convert UserDTO → UserUpdateViewItem
            ResponseDataUpdateUser updateResponse = (ResponseDataUpdateUser) response;
            
            UserUpdateViewItem viewItem = new UserUpdateViewItem();
            viewItem.id = dto.id;
            viewItem.username = dto.username;
            viewItem.fullName = dto.fullName;
            viewItem.email = dto.email;
            viewItem.phone = dto.phone;
            viewItem.address = dto.address;
            viewItem.status = dto.status;
            
            updateResponse.updatedUser = viewItem;
            updateResponse.success = true;
            updateResponse.message = "Cập nhật người dùng thành công!";
            
        } catch (IllegalArgumentException ex) {
            response.success = false;
            response.message = ex.getMessage();
        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
    }
}
