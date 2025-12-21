package frameworks.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta. servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet. http.HttpServletResponse;

import config.EmailService;
import config.OTPUtil;
import repository.jdbc.UserRepoImpl;
import repository. DTO.UserDTO;
import java.util.Optional;

@WebServlet("/resend-otp")
public class ResendOTPServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String userId = request.getParameter("userId");
            
            if (userId == null || userId.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"User ID không hợp lệ!\"}");
                return;
            }
            
            UserRepoImpl repository = new UserRepoImpl();
            
            // Lấy thông tin user
            Optional<UserDTO> userOpt = repository.findById(userId);
            if (!userOpt.isPresent()) {
                out.print("{\"success\":false,\"message\": \"Không tìm thấy người dùng!\"}");
                return;
            }
            
            UserDTO user = userOpt.get();
            
            // Generate OTP mới
            String newOtpCode = OTPUtil.generateOTP();
            
            // Lưu OTP mới vào DB (xóa OTP cũ)
            repository.saveOTP(userId, newOtpCode);
            
            // Gửi email
            boolean emailSent = EmailService.sendOTPEmail(
                user.email,
                user.username,
                newOtpCode
            );
            
            if (emailSent) {
                out.print("{\"success\":true,\"message\":\"Mã OTP mới đã được gửi đến email của bạn!\"}");
            } else {
                out. print("{\"success\":false,\"message\":\"Không thể gửi email OTP.  Vui lòng thử lại!\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Lỗi hệ thống:  " + e.getMessage() + "\"}");
        }
    }
}