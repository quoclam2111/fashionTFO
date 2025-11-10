package quanlynguoidung.get;

import quanlynguoidung.User;

public class GetUser extends User {
    
    public GetUser() {
        super();
    }

    public GetUser(String id, String username, String password,
                   String fullName, String email, String phone, String address, String status) {
        super(id, username, password, fullName, email, phone, address, status);
    }

    @Override
    public void validate() {
        checkId(this.id);
    }

    // ⭐ Validation cho GetUser - chỉ cần ID
    private void checkId(String id) {
        if(id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập ID người dùng!");
    }
}