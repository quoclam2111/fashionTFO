package quanlynguoidung;

import java.util.Date;
import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String status;

    public User() {}

    public User(String id, String username, String password,
                String fullName, String email, String phone, String address, String status) {

        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status == null ? "active" : status;
    }

    // ⭐ Validation methods với message tiếng Việt
    public static void checkUsername(String username) {
        if(username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập tên đăng nhập!");
    }

    public static void checkPassword(String password) {
        if(password == null || password.isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập mật khẩu!");
        if(password.length() < 6)
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự!");
    }

    public static void checkEmail(String email) {
        if(email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập email!");
        
        if(!email.contains("@") || !email.contains("."))
            throw new IllegalArgumentException("Email không đúng định dạng!");
    }

    public static void checkPhone(String phone) {
        if(phone == null || phone.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập số điện thoại!");
        
        if(!phone.matches("\\d{9,12}"))
            throw new IllegalArgumentException("Số điện thoại phải từ 9-12 chữ số!");
    }
    
    public static Date getCurrentTimestamp() {
        return new Date();
    }

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}