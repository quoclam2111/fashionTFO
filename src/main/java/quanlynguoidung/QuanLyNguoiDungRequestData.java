package quanlynguoidung;

public class QuanLyNguoiDungRequestData {
    public String action; // "add", "get", "list", "update", "delete"
    
    // Data cho action "add"
    public String username;
    public String password;
    public String fullName;
    public String email;
    public String phone;
    public String address;
    
    // Data cho action "get"
    public String searchBy;    // "id", "username", "email", "phone"
    public String searchValue;
    
    // Data cho action "list"
    public String statusFilter; // "all", "active", "inactive"
    public String sortBy;       // "fullname", "email", "username"
    public boolean ascending;

}
