package quanlynguoidung.delete;

import quanlynguoidung.User;

public class DeleteUser extends User {
    
    public DeleteUser() {
        super();
    }

    public DeleteUser(String id) {
        super(id, null, null, null, null, null, null, null);
    }

    @Override
    public void validate() {
        checkId(this.id);
    }

    // ⭐ Validation cho DeleteUser - chỉ cần ID
    private void checkId(String id) {
        if(id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng chọn người dùng để xóa!");
    }
}