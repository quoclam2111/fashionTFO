package quanlynguoidung.delete;

import quanlynguoidung.*;
import repository.DeleteUserRepositoryGateway;
import repository.DTO.UserDTO;

import java.util.Optional;

/**
 * Use Case cho Delete User
 */
public class DeleteUserUseCase extends QuanLyNguoiDungControl {
    private final DeleteUserRepositoryGateway repository;
    
    public DeleteUserUseCase(DeleteUserRepositoryGateway repository, 
                            QuanLyNguoiDungOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new ResponseDataDeleteUser();
    }
    
    @Override
    protected void execute(QuanLyNguoiDungRequestData request) {
        try {
            // 1. Validate input
            if (request.userId == null || request.userId.trim().isEmpty()) {
                response.success = false;
                response.message = "Vui lòng cung cấp ID người dùng cần xóa!";
                return;
            }
            
            // 2. Kiểm tra user có tồn tại không
            Optional<UserDTO> existingUserOpt = repository.findById(request.userId);
            
            if (!existingUserOpt.isPresent()) {
                response.success = false;
                response.message = "Không tìm thấy người dùng với ID: " + request.userId;
                return;
            }
            
            UserDTO userToDelete = existingUserOpt.get();
            
            // 3. Xóa user
            repository.deleteById(request.userId);
            
            // 4. Set response
            ResponseDataDeleteUser deleteResponse = (ResponseDataDeleteUser) response;
            deleteResponse.success = true;
            deleteResponse.message = "Xóa người dùng thành công!";
            deleteResponse.deletedUserId = request.userId;
            deleteResponse.deletedUsername = userToDelete.username;
            
        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
    }
}
