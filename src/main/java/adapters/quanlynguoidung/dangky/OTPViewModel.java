package adapters.quanlynguoidung.dangky;

public class OTPViewModel {
    public boolean success;
    public String message;
    public String timestamp;
    
    // OTP specific fields
    public String userId;              // ID của user cần verify
    public String email;               // Email đã gửi OTP
    public boolean otpSent;            // Email OTP đã gửi thành công chưa
    public boolean otpVerified;        // OTP đã được xác thực chưa
    public int remainingAttempts;      // Số lần nhập OTP còn lại (max 5)
    public int expiryMinutes;          // Số phút OTP còn hiệu lực (10 phút)
}