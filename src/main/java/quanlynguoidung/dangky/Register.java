package quanlynguoidung.dangky;

import config.PasswordUtil;
import quanlynguoidung.User;

public class Register extends User{
    private String plainPassword;
    
    public Register() {
        super();
    }

    public Register(String username, String password, String fullName, 
            String email, String phone, String address) {
			 // ⭐ Status = "pending" cho đến khi verify email
			 super(null, username, null, fullName, email, phone, address, "pending");
			 this.plainPassword = password;
	}

    @Override
    public void validate() {
        checkUsername(this.username);
        checkPassword(this.plainPassword);
        checkEmail(this.email);
        checkPhone(this. phone);
        checkFullName(this.fullName);
        
        // Hash password sau khi validate
        this.password = PasswordUtil.hashPassword(this.plainPassword);
    }

    private void checkUsername(String username) {
        if(username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập tên đăng nhập!");
    }

    private void checkPassword(String password) {
        // 1. Kiểm tra null/empty
        if(password == null || password.isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập mật khẩu!");
        
        // 2. Kiểm tra độ dài tối thiểu
        if(password.length() < 6)
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự!");
        
        // 3. Kiểm tra có chữ HOA
        if(!hasUpperCase(password))
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 1 chữ cái viết hoa!");
        
        // 4. Kiểm tra có ký tự đặc biệt
        if(!hasSpecialChar(password))
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 1 ký tự đặc biệt (! @#$%^&*...)!");
    }
    
    /**
     * Kiểm tra password có ít nhất 1 chữ HOA
     */
    private boolean hasUpperCase(String password) {
        return password.matches(".*[A-Z].*");
    }
    
    /**
     * Kiểm tra password có ít nhất 1 ký tự đặc biệt
     */
    private boolean hasSpecialChar(String password) {
        // Các ký tự đặc biệt cho phép:  !@#$%^&*()_+-=[]{};':"\\|,.<>/? 
        return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
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
        if(fullName == null || fullName.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập họ tên!");
    }
}