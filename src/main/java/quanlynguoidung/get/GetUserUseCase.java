package quanlynguoidung.get;

import java.util.Optional;

import quanlynguoidung.QuanLyNguoiDungControl;
import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungRequestData;

import repository.DTO.UserDTO;
import repository.user.GetUserRepositoryGateway;

public class GetUserUseCase extends QuanLyNguoiDungControl {
    private final GetUserRepositoryGateway repository;
    
    public GetUserUseCase(GetUserRepositoryGateway repository, 
                          QuanLyNguoiDungOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new ResponseDataGetUser();
    }
    
    @Override
    protected void execute(QuanLyNguoiDungRequestData request) {
        try {
            if (request.searchBy == null || request.searchValue == null || request.searchValue.isBlank()) {
                response.success = false;
                response.message = "Thiếu tiêu chí hoặc giá trị tìm kiếm!";
                return;
            }

            // 🔍 Gọi repository tìm user
            Optional<UserDTO> userOpt = switch (request.searchBy.toLowerCase()) {
                case "id" -> repository.findById(request.searchValue);
                case "username" -> repository.findByUsername(request.searchValue);
                case "email" -> repository.findByEmail(request.searchValue);
                case "phone" -> repository.findByPhone(request.searchValue);
                default -> throw new IllegalArgumentException("Tiêu chí tìm kiếm không hợp lệ!");
            };

            ResponseDataGetUser res = (ResponseDataGetUser) response;
            if (userOpt.isPresent()) {
                UserDTO dto = userOpt.get();

                // ✅ Convert sang View Model trung gian (Use Case model)
                UserViewItem item = new UserViewItem();
                item.id = dto.id;
                item.username = dto.username;
                item.fullName = dto.fullName;
                item.email = dto.email;
                item.phone = dto.phone;
                item.address = dto.address;
                item.status = dto.status;

                res.user = item;
                res.success = true;
                res.message = "Tìm thấy người dùng!";
            } else {
                res.user = null;
                res.success = false;
                res.message = "Không tìm thấy người dùng!";
            }

        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
    }
}