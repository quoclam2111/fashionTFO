package config;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.  Properties;

public class EmailService {
    
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "your-email@gmail.com"; // ⭐ Thay email
    private static final String EMAIL_PASSWORD = "your-app-password"; // ⭐ Thay App Password
    private static final String FROM_EMAIL = "your-email@gmail.com";
    private static final String FROM_NAME = "Fashion Store";
    
    /**
     * Gửi mã OTP qua email
     */
    public static boolean sendOTPEmail(String toEmail, String username, String otpCode) {
        try {
            // 1. Setup SMTP
            Properties props = new Properties();
            props. put("mail.smtp.auth", "true");
            props. put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail. smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            // 2. Create session
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });
            
            session.setDebug(true);
            
            // 3. Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType. TO, InternetAddress.parse(toEmail));
            message. setSubject("Mã xác thực OTP - Fashion Store");
            
            // 4. Build HTML content
            String emailContent = buildOTPEmailHTML(username, otpCode);
            message.setContent(emailContent, "text/html; charset=UTF-8");
            
            // 5. Send
            Transport.send(message);
            
            System.out.println("✅ Email OTP đã được gửi đến: " + toEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tạo nội dung email HTML với OTP
     */
    private static String buildOTPEmailHTML(String username, String otpCode) {
        return "<!DOCTYPE html>" +
                "<html lang='vi'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                ". container { max-width: 600px; margin: 50px auto; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 40px 30px; text-align: center; }" +
                ".header h1 { margin: 0; font-size: 28px; }" +
                ". content { padding: 40px 30px; }" +
                ".otp-box { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px; margin: 30px 0; }" +
                ".otp-code { font-size: 48px; font-weight: bold; letter-spacing: 10px; margin: 20px 0; font-family: 'Courier New', monospace; }" +
                ".warning { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; border-radius: 4px; }" +
                ". warning p { color: #856404; margin: 0; }" +
                ". footer { background: #f8f9fa; padding: 20px 30px; text-align: center; color: #999; font-size: 12px; border-top: 1px solid #e0e0e0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🎉 Chào mừng đến với Fashion Store!</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Xin chào <strong>" + username + "</strong>,</h2>" +
                "<p>Cảm ơn bạn đã đăng ký tài khoản tại <strong>Fashion Store</strong>!</p>" +
                "<p>Để hoàn tất quá trình đăng ký, vui lòng sử dụng mã OTP bên dưới:</p>" +
                "<div class='otp-box'>" +
                "<p style='margin: 0; font-size: 18px;'>Mã xác thực của bạn: </p>" +
                "<div class='otp-code'>" + otpCode + "</div>" +
                "<p style='margin: 0; font-size: 14px; opacity: 0.9;'>Nhập mã này vào trang đăng ký</p>" +
                "</div>" +
                "<div class='warning'>" +
                "<p>⏰ <strong>Lưu ý:</strong></p>" +
                "<ul style='margin: 10px 0; padding-left: 20px; color: #856404;'>" +
                "<li>Mã OTP này chỉ có hiệu lực trong <strong>10 phút</strong></li>" +
                "<li>Không chia sẻ mã này với bất kỳ ai</li>" +
                "<li>Nếu bạn không thực hiện đăng ký, vui lòng bỏ qua email này</li>" +
                "</ul>" +
                "</div>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© 2025 Fashion Store. All rights reserved.</p>" +
                "<p>Email này được gửi tự động, vui lòng không trả lời. </p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}