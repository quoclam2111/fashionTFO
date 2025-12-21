package quanlynguoidung;

import java.util.Date;
import java.util.List;

import quanlynguoidung.get.UserViewItem;
import repository.DTO.UserDTO;

/**
 * Response Data chung cho tất cả use case
 */
public class QuanLyNguoiDungResponseData {
    public boolean success;
    public String message;
    public Date timestamp;
    
    // Data cho "get" action
    public UserViewItem user;
    
    // Data cho "list" action
    public List<UserDTO> users;
    public int totalCount;
    public int filteredCount;
    
    // ⭐ Data cho "verify-otp" action
    public String userId;           // ID user vừa đăng ký
    public boolean otpSent;         // Email OTP đã gửi thành công chưa
    public boolean otpVerified;     // OTP đã được xác thực chưa
    public int remainingAttempts;   // Số lần thử còn lại (nếu nhập sai)
}