package frameworks.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/payment/qr")
public class QRPaymentServlet extends HttpServlet {
    
    // ⭐ THÔNG TIN NGÂN HÀNG CỦA BẠN
    private static final String BANK_ID = "970422"; // Mã ngân hàng (VD: MB Bank)
    private static final String ACCOUNT_NO = "0123456789"; // Số tài khoản
    private static final String ACCOUNT_NAME = "NGUYEN VAN A"; // Tên chủ TK
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy thông tin từ query string
        String orderId = request.getParameter("orderId");
        String amountStr = request.getParameter("amount");
        
        if (orderId == null || amountStr == null) {
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountStr);
            
            // Tạo nội dung chuyển khoản
            String content = "DH " + orderId.substring(0, 8);
            
            // Tạo QR Code URL (sử dụng chuẩn VietQR)
            // Format: https://img.vietqr.io/image/{BANK_ID}-{ACCOUNT_NO}-{TEMPLATE}.jpg?amount={AMOUNT}&addInfo={CONTENT}
            String qrUrl = generateVietQRUrl(BANK_ID, ACCOUNT_NO, amount, content);
            
            // Set attributes để JSP sử dụng
            request.setAttribute("qrUrl", qrUrl);
            request.setAttribute("orderId", orderId);
            request.setAttribute("amount", amount);
            request.setAttribute("bankName", getBankName(BANK_ID));
            request.setAttribute("accountNo", ACCOUNT_NO);
            request.setAttribute("accountName", ACCOUNT_NAME);
            request.setAttribute("content", content);
            
            // Forward đến JSP
            request.getRequestDispatcher("/WEB-INF/views/qr-payment.jsp")
                   .forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }
    
    /**
     * Tạo URL QR Code theo chuẩn VietQR
     */
    private String generateVietQRUrl(String bankId, String accountNo, double amount, String content) {
        try {
            String encodedContent = URLEncoder.encode(content, "UTF-8");
            
            return String.format(
                "https://img.vietqr.io/image/%s-%s-compact2.jpg?amount=%.0f&addInfo=%s&accountName=%s",
                bankId,
                accountNo,
                amount,
                encodedContent,
                URLEncoder.encode(ACCOUNT_NAME, "UTF-8")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * Lấy tên ngân hàng từ mã
     */
    private String getBankName(String bankId) {
        switch (bankId) {
            case "970422": return "MB Bank";
            case "970415": return "Vietinbank";
            case "970436": return "Vietcombank";
            case "970418": return "BIDV";
            case "970405": return "Agribank";
            case "970407": return "Techcombank";
            case "970432": return "VPBank";
            case "970423": return "TPBank";
            case "970403": return "Sacombank";
            case "970416": return "ACB";
            default: return "Ngân hàng";
        }
    }
}