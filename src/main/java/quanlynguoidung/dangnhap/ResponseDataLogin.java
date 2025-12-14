package quanlynguoidung.dangnhap;

import quanlynguoidung.QuanLyNguoiDungResponseData;

public class ResponseDataLogin extends QuanLyNguoiDungResponseData {
    public String userId;
    public String username;
    public String fullName;
    public String role; // ADMIN, MANAGER, STAFF, CUSTOMER
    public String accountType; // "NHANVIEN" hoặc "USER"
}