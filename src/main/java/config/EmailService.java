package config;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {
    
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    // ⭐ THAY EMAIL VÀ APP PASSWORD CỦA BẠN
    private static final String EMAIL_USERNAME = "savecache498@gmail.com"; 
    private static final String EMAIL_PASSWORD = "iivf gabi rxtk loes"; // App Password
    private static final String FROM_EMAIL = "savecache498@gmail.com";
    private static final String FROM_NAME = "Fashion Store";
    
    public static boolean sendOTPEmail(String toEmail, String username, String otpCode) {
        try {
            // 1. Setup SMTP với cấu hình đầy đủ
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props. put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail. smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            // ✅ FIX: Set localhost name để tránh lỗi HELO/EHLO
            props.put("mail.smtp.localhost", "localhost");
            
            // 2. Create session
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });
            
            // ✅ TẮT debug mode trong production
            session.setDebug(false);
            
            // 3. Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message. setRecipients(Message.RecipientType. TO, InternetAddress.parse(toEmail));
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
    
    private static String buildOTPEmailHTML(String username, String otpCode) {
        return "<!DOCTYPE html>" +
                "<html lang='vi'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                ".container { max-width: 600px; margin: 50px auto; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 40px 30px; text-align: center; }" +
                ".header h1 { margin: 0; font-size:  28px; }" +
                ". content { padding: 40px 30px; }" +
                ".otp-box { background: #f8f9fa; border: 2px dashed #667eea; border-radius: 10px; padding: 30px; text-align: center; margin: 30px 0; }" +
                ".otp-code { font-size: 36px; font-weight: bold; color: #667eea; letter-spacing: 8px; }" +
                ". info { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; border-radius: 5px; }" +
                ". footer { text-align: center; padding: 20px; color: #999; font-size: 14px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🛍️ Fashion Store</h1>" +
                "<p>Xác thực tài khoản của bạn</p>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Xin chào <strong>" + username + "</strong>,</p>" +
                "<p>Cảm ơn bạn đã đăng ký tài khoản tại Fashion Store!</p>" +
                "<p>Để hoàn tất đăng ký, vui lòng nhập mã OTP dưới đây:</p>" +
                "<div class='otp-box'>" +
                "<div class='otp-code'>" + otpCode + "</div>" +
                "</div>" +
                "<div class='info'>" +
                "<strong>⏰ Lưu ý:</strong>" +
                "<ul style='margin: 10px 0 0 20px;'>" +
                "<li>Mã OTP có hiệu lực trong <strong>10 phút</strong></li>" +
                "<li>Bạn có tối đa <strong>5 lần nhập</strong></li>" +
                "<li>Không chia sẻ mã này với bất kỳ ai</li>" +
                "</ul>" +
                "</div>" +
                "<p>Nếu bạn không thực hiện đăng ký này, vui lòng bỏ qua email này.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© 2025 Fashion Store. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}