package quanlynguoidung.dangnhap;

import config. PasswordUtil;
import quanlynguoidung.User;

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
    
    @Override
    public void validate() {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username không được để trống");
        }
        
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password không được để trống");
        }
        
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username phải có ít nhất 3 ký tự");
        }
        
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password phải có ít nhất 6 ký tự");
        }
    }
    
    public boolean isLocked() {
        return "locked".equalsIgnoreCase(this.status);
    }
    
    /**
     * ⭐ Verify password
     * - Admin/Staff: Plain text
     * - Customer: BCrypt hash
     */
    public boolean verifyPassword(String inputPassword) {
        if (inputPassword == null) {
            return false;
        }
        
        // ✅ Nếu là NHÂN VIÊN (admin/manager/staff) → Plain text
        if (isEmployee()) {
            System.out.println("🔑 Admin/Staff login - Using plain text comparison");
            return inputPassword.equals(this.password);
        }
        
        // ✅ Nếu là CUSTOMER → BCrypt hash
        try {
            System.out.println("🔑 Customer login - Using BCrypt verification");
            return PasswordUtil.verifyPassword(inputPassword, this.password);
        } catch (Exception e) {
            System.err.println("❌ Lỗi BCrypt verify: " + e.getMessage());
            return false;
        }
    }
    
    public boolean isEmployee() {
        return "NHANVIEN".equals(this. accountType);
    }
    
    public boolean isCustomer() {
        return "USER".equals(this.accountType);
    }
    
    public void validateRole() {
        if (isEmployee()) {
            String roleUpper = role != null ? role.toUpperCase() : "";
            if (!roleUpper. equals("ADMIN") && 
                !roleUpper.equals("MANAGER") && 
                !roleUpper.equals("STAFF")) {
                throw new IllegalArgumentException("Role không hợp lệ:  " + role);
            }
        }
    }
    
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