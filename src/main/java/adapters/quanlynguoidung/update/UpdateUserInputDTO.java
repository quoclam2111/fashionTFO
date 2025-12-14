package adapters.quanlynguoidung.update;

public class UpdateUserInputDTO {
    public String userId;      // Bắt buộc - ID của user cần update
    public String fullName;    // Optional
    public String email;       // Optional
    public String phone;       // Optional
    public String address;     // Optional
    public String status;      // Optional - "active" hoặc "inactive"
    public String password;    // Optional - chỉ update nếu muốn đổi mật khẩu
}
