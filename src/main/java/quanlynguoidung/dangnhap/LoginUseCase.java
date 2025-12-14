package quanlynguoidung.dangnhap;

import quanlynguoidung.*;
import repository.user.LoginRepoGateway;
import repository.DTO.NhanVienDTO;
import repository.DTO.UserDTO;

public class LoginUseCase extends QuanLyNguoiDungControl {
    private final LoginRepoGateway repository;
    
    public LoginUseCase(LoginRepoGateway repository, 
                       QuanLyNguoiDungOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new ResponseDataLogin();
    }
    
    @Override
    protected void execute(QuanLyNguoiDungRequestData request) {
        try {
            // 1. Tạo LoginUser entity từ request để validate input
            LoginUser inputUser = new LoginUser();
            inputUser.setUsername(request.username);
            inputUser.setPassword(request.password);
            
            // Validate input theo business rules
            try {
                inputUser.validate();
            } catch (IllegalArgumentException ex) {
                response.success = false;
                response.message = ex.getMessage();
                return;
            }
            
            // 2. Tìm trong bảng nhanvien trước
            NhanVienDTO nhanvienDTO = repository.findNhanVienByUsername(request.username);
            
            if (nhanvienDTO != null) {
                // Chuyển DTO -> Entity
                LoginUser nhanvienEntity = convertNhanVienDTOToEntity(nhanvienDTO);
                
                // Business logic: Verify password
                if (!nhanvienEntity.verifyPassword(request.password)) {
                    response.success = false;
                    response.message = "Mật khẩu không chính xác!";
                    return;
                }
                
                // Business logic: Check locked status
                if (nhanvienEntity.isLocked()) {
                    response.success = false;
                    response.message = "Tài khoản nhân viên đã bị khóa!";
                    return;
                }
                
                // Business logic: Validate role
                try {
                    nhanvienEntity.validateRole();
                } catch (IllegalArgumentException ex) {
                    response.success = false;
                    response.message = ex.getMessage();
                    return;
                }
                
                // ✅ Đăng nhập thành công - NHÂN VIÊN
                populateSuccessResponse(nhanvienEntity);
                return;
            }
            
            // 3. Nếu không tìm thấy trong nhanvien, tìm trong users
            UserDTO userDTO = repository.findUserByUsername(request.username);
            
            if (userDTO == null) {
                response.success = false;
                response.message = "Tên đăng nhập không tồn tại!";
                return;
            }
            
            // Chuyển DTO -> Entity
            LoginUser userEntity = convertUserDTOToEntity(userDTO);
            
            // Business logic: Verify password
            if (!userEntity.verifyPassword(request.password)) {
                response.success = false;
                response.message = "Mật khẩu không chính xác!";
                return;
            }
            
            // Business logic: Check locked status
            if (userEntity.isLocked()) {
                response.success = false;
                response.message = "Tài khoản đã bị khóa!";
                return;
            }
            
            // ✅ Đăng nhập thành công - KHÁCH HÀNG
            populateSuccessResponse(userEntity);
            
        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
    }
    
    /**
     * Chuyển NhanVienDTO (từ database) sang LoginUser Entity (business logic)
     */
    private LoginUser convertNhanVienDTOToEntity(NhanVienDTO dto) {
        LoginUser entity = new LoginUser();
        entity.setId(dto.nhanvienID);
        entity.setUsername(dto.username);
        entity.setPassword(dto.password);
        entity.setFullName(dto.fullName);
        entity.setEmail(dto.email);
        entity.setPhone(dto.phone);
        // ❌ Nhân viên không có address trong DB
        entity.setStatus(dto.status);
        entity.setRole(dto.roleName);
        entity.setAccountType("NHANVIEN");
        return entity;
    }
    
    /**
     * Chuyển UserDTO (từ database) sang LoginUser Entity (business logic)
     */
    private LoginUser convertUserDTOToEntity(UserDTO dto) {
        LoginUser entity = new LoginUser();
        entity.setId(dto.id);
        entity.setUsername(dto.username);
        entity.setPassword(dto.password);
        entity.setFullName(dto.fullName);
        entity.setEmail(dto.email);
        entity.setPhone(dto.phone);
        entity.setAddress(dto.address);
        entity.setStatus(dto.status);
        entity.setRole("CUSTOMER");
        entity.setAccountType("USER");
        return entity;
    }
    
    /**
     * Điền response khi login thành công
     */
    private void populateSuccessResponse(LoginUser entity) {
        ResponseDataLogin loginResponse = (ResponseDataLogin) response;
        loginResponse.success = true;
        loginResponse.message = "Đăng nhập thành công!";
        loginResponse.userId = entity.getId();
        loginResponse.username = entity.getUsername();
        loginResponse.fullName = entity.getFullName();
        loginResponse.role = entity.getNormalizedRole(); // ADMIN, MANAGER, STAFF, CUSTOMER
        loginResponse.accountType = entity.getAccountType(); // NHANVIEN hoặc USER
    }
}