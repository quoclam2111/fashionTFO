package quanlynguoidung.dangnhap;

import config.PasswordUtil;  // ⭐ Import
import quanlynguoidung.User;

/**
 * Entity cho Login Use Case
 * Chứa business logic validation cho đăng nhập
 */
public class LoginUser extends User {
    private String role;
    private String accountType;
    
    public LoginUser() {
        super();
    }
    
    public LoginUser(String id, String username, String password,
                    String fullName, String email, String phone, 
                    String address, String status,
                    String role, String accountType) {
        super(id, username, password, fullName, email, phone, address, status);
        this.role = role;
        this.accountType = accountType;
    }
    
    /**
     * Validate business rules cho login
     */
    @Override
    public void validate() {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username không được để trống");
        }
        
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password không được để trống");
        }
        
        if (username. length() < 3) {
            throw new IllegalArgumentException("Username phải có ít nhất 3 ký tự");
        }
        
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password phải có ít nhất 6 ký tự");
        }
    }
    
    /**
     * Kiểm tra tài khoản có bị khóa không
     */
    public boolean isLocked() {
        return "locked".equalsIgnoreCase(this.status);
    }
    
    /**
     * ⭐ Kiểm tra password có khớp không - DÙNG BCRYPT
     */
    public boolean verifyPassword(String inputPassword) {
        if (inputPassword == null) {
            return false;
        }
        // ⭐ So sánh password nhập vào với hash trong DB
        return PasswordUtil.verifyPassword(inputPassword, this.password);
    }
    
    /**
     * Kiểm tra có phải nhân viên không
     */
    public boolean isEmployee() {
        return "NHANVIEN".equals(this.accountType);
    }
    
    /**
     * Kiểm tra có phải khách hàng không
     */
    public boolean isCustomer() {
        return "USER".equals(this.accountType);
    }
    
    /**
     * Validate role cho nhân viên
     */
    public void validateRole() {
        if (isEmployee()) {
            String roleUpper = role != null ? role.toUpperCase() : "";
            if (!roleUpper.equals("ADMIN") && 
                !roleUpper.equals("MANAGER") && 
                !roleUpper.equals("STAFF")) {
                throw new IllegalArgumentException("Role không hợp lệ:  " + role);
            }
        }
    }
    
    /**
     * Lấy role chuẩn hóa (uppercase)
     */
    public String getNormalizedRole() {
        if (isCustomer()) {
            return "CUSTOMER";
        }
        return role != null ? role.toUpperCase() : "STAFF";
    }
    
    // Getters & Setters
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}