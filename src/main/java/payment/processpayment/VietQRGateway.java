package payment.processpayment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import repository.payment.PaymentGateway;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class VietQRGateway implements PaymentGateway {
    private static final String VIETQR_API = "https://api.vietqr.io/v2/generate";
    
    // ========== THÔNG TIN TÀI KHOẢN SHOP (HARDCODE) ==========
    private static final String SHOP_ACCOUNT_NO = "070133379612";
    private static final String SHOP_ACCOUNT_NAME = "TRAN QUOC AN";
    private static final String SHOP_BANK_CODE = "970403";  
    // =========================================================
    
    private final String accountNo;
    private final String accountName;
    private final String bankCode;
    private final HttpClient httpClient;
    private final Gson gson;

  
    public VietQRGateway() {
        this.accountNo = SHOP_ACCOUNT_NO;
        this.accountName = SHOP_ACCOUNT_NAME;
        this.bankCode = SHOP_BANK_CODE;
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }
    
 
    public VietQRGateway(String accountNo, String accountName, String bankCode) {
        this.accountNo = accountNo;
        this.accountName = accountName;
        this.bankCode = bankCode;
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    @Override
    public String requestPayment(String orderId, int amount) {
        try {
            // Tạo nội dung chuyển khoản
            String transferContent = "DH" + orderId;
            
            // Tạo request body theo VietQR API spec
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("accountNo", accountNo);
            requestBody.addProperty("accountName", accountName);
            requestBody.addProperty("acqId", bankCode);
            requestBody.addProperty("amount", amount);
            requestBody.addProperty("addInfo", transferContent);
            requestBody.addProperty("format", "text");
            requestBody.addProperty("template", "compact");

            // Gọi VietQR API
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIETQR_API))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());

            // Kiểm tra HTTP status
            if (response.statusCode() != 200) {
                return createErrorResponse("VietQR API returned status: " + response.statusCode());
            }

            // Parse JSON response
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            
            // Kiểm tra response code từ VietQR
            String code = jsonResponse.get("code").getAsString();
            if (!"00".equals(code)) {
                String desc = jsonResponse.has("desc") 
                    ? jsonResponse.get("desc").getAsString() 
                    : "Unknown error";
                return createErrorResponse("VietQR error [" + code + "]: " + desc);
            }

            JsonObject data = jsonResponse.getAsJsonObject("data");
            String qrDataURL = data.get("qrDataURL").getAsString();

            JsonObject result = new JsonObject();
            result.addProperty("success", true);
            result.addProperty("transactionId", orderId);
            result.addProperty("qrCode", qrDataURL);
            result.addProperty("accountNo", accountNo);
            result.addProperty("accountName", accountName);
            result.addProperty("bankCode", bankCode);
            result.addProperty("amount", amount);
            result.addProperty("transferContent", transferContent);
            result.addProperty("expiresAt", System.currentTimeMillis() + (15 * 60 * 1000));

            return result.toString();

        } catch (Exception e) {
            return createErrorResponse("Failed to generate VietQR code: " + e.getMessage());
        }
    }

    @Override
    public String checkPaymentStatus(String transactionId) {
        // VietQR API không cung cấp webhook hoặc check status
        // Cần tích hợp với ngân hàng hoặc dùng third-party service
        JsonObject result = new JsonObject();
        result.addProperty("success", false);
        result.addProperty("error", "Payment status check not supported by VietQR");
        result.addProperty("message", "Please implement webhook or bank integration");
        result.addProperty("status", "UNKNOWN");
        return result.toString();
    }

    @Override
    public String cancelPayment(String transactionId) {
        // VietQR chỉ tạo QR code, không thể cancel
        JsonObject result = new JsonObject();
        result.addProperty("success", false);
        result.addProperty("error", "Payment cancellation not supported by VietQR");
        result.addProperty("message", "VietQR only generates QR codes and cannot cancel payments");
        return result.toString();
    }

    @Override
    public String refundPayment(String transactionId, int amount) {
        // VietQR không hỗ trợ refund tự động
        JsonObject result = new JsonObject();
        result.addProperty("success", false);
        result.addProperty("error", "Automatic refund not supported by VietQR");
        result.addProperty("message", "Please process refund manually through your bank");
        return result.toString();
    }

    /**
     * Tạo JSON response cho lỗi
     */
    private String createErrorResponse(String errorMessage) {
        JsonObject error = new JsonObject();
        error.addProperty("success", false);
        error.addProperty("error", errorMessage);
        return error.toString();
    }
}