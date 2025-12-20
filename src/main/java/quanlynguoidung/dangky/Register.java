package quanlynguoidung.dangky;

import config.PasswordUtil;
import quanlynguoidung. User;

public class Register extends User{
    private String plainPassword;  // ⭐ Lưu password gốc tạm thời
    
    public Register() {
        super();
    }

    public Register(String username, String password, String fullName, 
                   String email, String phone, String address) {
        super(null, username, null, fullName, email, phone, address, "active");
        this.plainPassword = password;  // ⭐ Lưu password gốc
    }

    @Override
    public void validate() {
        checkUsername(this.username);
        checkPassword(this.plainPassword);  // ⭐ Check password GỐC
        checkEmail(this.email);
        checkPhone(this.phone);
        checkFullName(this.fullName);
        
        // ⭐ SAU KHI VALIDATE XONG → HASH PASSWORD
        this.password = PasswordUtil.hashPassword(this.plainPassword);
    }

    private void checkUsername(String username) {
        if(username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập tên đăng nhập!");
    }

    private void checkPassword(String password) {
        if(password == null || password.isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập mật khẩu!");
        
        if(password.length() < 6)
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự!");
    }

    private void checkEmail(String email) {
        if(email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập email!");

        if(!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            throw new IllegalArgumentException("Email không đúng định dạng!");
    }

    private void checkPhone(String phone) {
        if(phone == null || phone.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập số điện thoại!");

        if(!phone.matches("\\d{9,12}"))
            throw new IllegalArgumentException("Số điện thoại phải từ 9-12 chữ số!");
    }
    
    private void checkFullName(String fullName) {
        if(fullName == null || fullName. trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập họ tên!");
    }
}