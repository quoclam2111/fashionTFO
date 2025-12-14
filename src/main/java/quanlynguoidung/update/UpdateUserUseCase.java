package quanlynguoidung.update;

import java.util.Optional;

import quanlynguoidung.QuanLyNguoiDungControl;
import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungRequestData;
import repository.DTO.UserDTO;
import repository.user.UpdateUserRepositoryGateway;

public class UpdateUserUseCase extends QuanLyNguoiDungControl {
    private final UpdateUserRepositoryGateway repository;
    
    public UpdateUserUseCase(UpdateUserRepositoryGateway repository, 
                            QuanLyNguoiDungOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new ResponseDataUpdateUser();
    }
    
    @Override
    protected void execute(QuanLyNguoiDungRequestData request) {
        try {
            // 1. Convert request → UpdateUser entity và validate
            UpdateUser user = convertToBusinessObject(request);
            user.validate();
            
            // 2. Kiểm tra user có tồn tại không
            Optional<UserDTO> existingUserOpt = repository.findById(request.userId);
            
            if (!existingUserOpt.isPresent()) {
                response.success = false;
                response.message = "Không tìm thấy người dùng với ID: " + request.userId;
                return;
            }
            
            UserDTO dto = existingUserOpt.get();
            
            // 3. Validate status - chỉ cho phép 'active' hoặc 'locked'
            if (request.status != null && 
                !request.status.equals("active") && 
                !request.status.equals("locked")) {
                response.success = false;
                response.message = "Trạng thái không hợp lệ! Chỉ chấp nhận: active hoặc locked";
                return;
            }
            
            // 4. Kiểm tra email mới đã được dùng chưa
            if (request.email != null && !request.email.equals(dto.email)) {
                if (repository.existsByEmailExcludingUser(request.email, request.userId)) {
                    response.success = false;
                    response.message = "Email này đã được sử dụng bởi người khác!";
                    return;
                }
            }
            
            // 5. Kiểm tra phone mới đã được dùng chưa
            if (request.phone != null && !request.phone.equals(dto.phone)) {
                if (repository.existsByPhoneExcludingUser(request.phone, request.userId)) {
                    response.success = false;
                    response.message = "Số điện thoại này đã được sử dụng bởi người khác!";
                    return;
                }
            }
            
            updateDTOFromRequest(dto, request);
            
            // 6. Lưu vào database
            repository.update(dto);
            
            // 7. Set response
            ResponseDataUpdateUser updateResponse = (ResponseDataUpdateUser) response;
            updateResponse.updatedUser = convertToViewItem(dto);
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
    
    /**
     * Convert Request → UpdateUser Entity
     */
    private UpdateUser convertToBusinessObject(QuanLyNguoiDungRequestData request) {
        return new UpdateUser(
            request.userId,
            request.fullName,
            request.email,
            request.phone,
            request.address,
            request.status,
            request.password
        );
    }
    
    /**
     * Cập nhật DTO từ request (chỉ update các field không null)
     */
    private void updateDTOFromRequest(UserDTO dto, QuanLyNguoiDungRequestData request) {
        if (request.fullName != null) {
            dto.fullName = request.fullName;
        }
        
        if (request.email != null) {
            dto.email = request.email;
        }
        
        if (request.phone != null) {
            dto.phone = request.phone;
        }
        
        if (request.address != null) {
            dto.address = request.address;
        }
        
        if (request.status != null) {
            dto.status = request.status;
        }
        
        // Cập nhật password nếu có
        if (request.password != null && !request.password.trim().isEmpty()) {
            dto.password = request.password;
        }
    }
    
    /**
     * Convert UserDTO → UserUpdateViewItem
     */
    private UserUpdateViewItem convertToViewItem(UserDTO dto) {
        UserUpdateViewItem viewItem = new UserUpdateViewItem();
        viewItem.id = dto.id;
        viewItem.username = dto.username;
        viewItem.fullName = dto.fullName;
        viewItem.email = dto.email;
        viewItem.phone = dto.phone;
        viewItem.address = dto.address;
        viewItem.status = dto.status;
        return viewItem;
    }
}