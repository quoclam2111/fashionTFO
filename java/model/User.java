package model;

public class User {
    private String userId;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;
    private UserRole role;
    
    public enum UserRole {
        GUEST, CUSTOMER, ADMIN
    }
    
    // Constructor cho Guest (không cần đăng nhập)
    public User() {
        this.role = UserRole.GUEST;
    }
    
    // Constructor cho Customer/Admin
    public User(String userId, String email, String fullName, UserRole role) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public boolean isGuest() { return role == UserRole.GUEST; }
    public boolean isCustomer() { return role == UserRole.CUSTOMER; }
    public boolean isAdmin() { return role == UserRole.ADMIN; }
}
