package quanlynguoidung.update;

import quanlynguoidung.User;

public class UpdateUser extends User {
    
    public UpdateUser() {
        super();
    }

    public UpdateUser(String id, String fullName, String email, 
                      String phone, String address, String status, String password) {
        super(id, null, password, fullName, email, phone, address, status);
    }

    @Override
    public void validate() {
        checkId(this.id);
        
        // Chỉ validate các field được cập nhật
        if(this.email != null && !this.email.trim().isEmpty()) {
            checkEmail(this.email);
        }
        
        if(this.phone != null && !this.phone.trim().isEmpty()) {
            checkPhone(this.phone);
        }
        
        if(this.password != null && !this.password.isEmpty()) {
            checkPassword(this.password);
        }
    }

    // ⭐ Validation methods cho UpdateUser
    private void checkId(String id) {
        if(id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng chọn người dùng để cập nhật!");
    }

    private void checkEmail(String email) {
        if(!email.contains("@") || !email.contains("."))
            throw new IllegalArgumentException("Email không đúng định dạng!");
    }

    private void checkPhone(String phone) {
        if(!phone.matches("\\d{9,12}"))
            throw new IllegalArgumentException("Số điện thoại phải từ 9-12 chữ số!");
    }

    private void checkPassword(String password) {
        if(password.length() < 6)
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự!");
    }
}