package quanlynguoidung.list;

import quanlynguoidung.User;

public class ListUser extends User {
    
    public ListUser() {
        super();
    }

    public ListUser(String id, String username, String password,
                    String fullName, String email, String phone, String address, String status) {
        super(id, username, password, fullName, email, phone, address, status);
    }

    @Override
    public void validate() {
        // ListUser không cần validate gì
    }
}