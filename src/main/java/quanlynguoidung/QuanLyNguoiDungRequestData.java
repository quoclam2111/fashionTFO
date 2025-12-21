package quanlynguoidung;

public class QuanLyNguoiDungRequestData {
    public String action; // "add", "get", "list", "update", "delete", "verify-otp"
    
    // Data cho action "add"
    public String username;
    public String password;
    public String fullName;
    public String email;
    public String phone;
    public String address;
    
    // Data cho action "get"
    public String searchBy;    // "id", "username", "email", "phone"
    public String searchValue;
    
    // Data cho action "list"
    public String statusFilter; // "all", "active", "inactive"
    public String sortBy;       // "fullname", "email", "username"
    public boolean ascending;

    // Data cho action "update" & "delete"
    public String userId;      // ID của user cần update/delete
    public String status;      // Trạng thái mới cho update
    
    // ⭐ Data cho action "verify-otp"
    public String id;          // User ID để verify
    public String otpCode;     // Mã OTP 6 số
}