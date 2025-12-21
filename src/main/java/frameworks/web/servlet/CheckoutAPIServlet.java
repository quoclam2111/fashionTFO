package frameworks.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import repository. DTO.OrderDTO;
import repository. DTO.OrderItemDTO;
import repository.jdbc.OrderRepoImpl;

@WebServlet("/api/checkout")
public class CheckoutAPIServlet extends HttpServlet {
    
    private OrderRepoImpl orderRepo;
    
    @Override
    public void init() throws ServletException {
        orderRepo = new OrderRepoImpl();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // ✅ SET ENCODING NGAY ĐẦU TIÊN
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        
        try {
            // 1. Kiểm tra đăng nhập
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                out.print("{\"success\": false,\"message\":\"Vui lòng đăng nhập!\"}");
                return;
            }
            
            String userId = (String) session.getAttribute("userId");
            
            // 2. Lấy thông tin từ form
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String note = request.getParameter("note");
            String paymentMethod = request.getParameter("paymentMethod");
            String totalAmountStr = request.getParameter("totalAmount");
            
            // ✅ DEBUG LOG
            System.out.println("=== RECEIVED PARAMETERS ===");
            System.out.println("fullName: [" + fullName + "]");
            System.out.println("phone: [" + phone + "]");
            System.out. println("address: [" + address + "]");
            System.out.println("paymentMethod: [" + paymentMethod + "]");
            System.out.println("totalAmount: [" + totalAmountStr + "]");
            
            // 3. Validate
            if (fullName == null || fullName.trim().isEmpty()) {
                System.err.println("❌ fullName is null or empty!");
                out.print("{\"success\":false,\"message\":\"Vui lòng nhập họ tên!\"}");
                return;
            }
            if (phone == null || phone.trim().isEmpty()) {
                out. print("{\"success\":false,\"message\":\"Vui lòng nhập số điện thoại!\"}");
                return;
            }
            
            if (address == null || address.trim().isEmpty()) {
                out. print("{\"success\":false,\"message\":\"Vui lòng nhập địa chỉ!\"}");
                return;
            }
            
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"Vui lòng chọn phương thức thanh toán!\"}");
                return;
            }
            
            if (totalAmountStr == null || totalAmountStr.trim().isEmpty()) {
                out. print("{\"success\":false,\"message\":\"Không tìm thấy tổng tiền!\"}");
                return;
            }
            
            double totalAmount = Double.parseDouble(totalAmountStr);
            
            // 4. Lấy cart items từ localStorage
            String cartItemsJson = request.getParameter("cartItems");
            if (cartItemsJson == null || cartItemsJson.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\": \"Giỏ hàng trống!\"}");
                return;
            }
            
            // 5. Parse cart items
            List<OrderItemDTO> items = parseCartItems(cartItemsJson);
            
            if (items.isEmpty()) {
                out.print("{\"success\":false,\"message\":\"Không có sản phẩm nào trong giỏ hàng!\"}");
                return;
            }
            
            // 6. Tạo OrderDTO
            OrderDTO order = new OrderDTO();
            order.userId = userId;
            order.customerName = fullName. trim();
            order.customerPhone = phone.trim();
            order.customerAddress = address.trim();
            order.note = note != null ? note.trim() : "";
            order.paymentMethod = paymentMethod.toUpperCase();
            order.totalAmount = totalAmount;
            order.items = items;
            
            // 7. Lưu vào database
            String orderId = orderRepo.createOrder(order);
            
            // 8. Trả về kết quả
            String jsonResponse = "{\"success\":true,\"message\":\"Đặt hàng thành công!\",\"orderId\":\"" + orderId + "\"}";
            out.print(jsonResponse);
            
            System.out.println("✅ Đơn hàng mới:  " + orderId + " - Khách:  " + fullName);
            
        } catch (NumberFormatException e) {
            out.print("{\"success\":false,\"message\":\"Số tiền không hợp lệ!\"}");
            e.printStackTrace();
        } catch (Exception e) {
            String errorMsg = e.getMessage() != null ? e.getMessage().replace("\"", "'") : "Lỗi không xác định";
            out.print("{\"success\":false,\"message\":\"Lỗi hệ thống: " + errorMsg + "\"}");
            e.printStackTrace();
        }
    }
    
    private List<OrderItemDTO> parseCartItems(String json) {
        List<OrderItemDTO> items = new ArrayList<>();
        
        try {
            json = json.trim();
            if (json.startsWith("[")) json = json.substring(1);
            if (json.endsWith("]")) json = json.substring(0, json.length() - 1);
            
            String[] objects = splitJsonArray(json);
            
            for (String obj : objects) {
                if (obj.trim().isEmpty()) continue;
                
                OrderItemDTO item = new OrderItemDTO();
                
                String variantId = extractJsonValue(obj, "variantId");
                if (variantId == null || variantId.isEmpty()) {
                    variantId = extractJsonValue(obj, "id");
                }
                
                if (variantId == null || variantId.isEmpty()) {
                    System.err.println("⚠️ Item không có variantId: " + obj);
                    continue;
                }
                
                item.variantId = variantId;
                
                String quantityStr = extractJsonValue(obj, "quantity");
                item.quantity = quantityStr != null ? Integer.parseInt(quantityStr) : 1;
                
                String priceStr = extractJsonValue(obj, "price");
                double price = priceStr != null ?  Double.parseDouble(priceStr) : 0;
                
                item.subtotal = java.math.BigDecimal. valueOf(price * item.quantity);
                
                items.add(item);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi parse cart items: " + e.getMessage());
            e.printStackTrace();
        }
        
        return items;
    }
    
    private String[] splitJsonArray(String json) {
        List<String> result = new ArrayList<>();
        int braceCount = 0;
        int startIndex = 0;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '{') {
                if (braceCount == 0) startIndex = i;
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0) {
                    result.add(json.substring(startIndex, i + 1));
                }
            }
        }
        
        return result.toArray(new String[0]);
    }
    
    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\"";
            int keyIndex = json.indexOf(searchKey);
            
            if (keyIndex == -1) return null;
            
            int colonIndex = json.indexOf(":", keyIndex);
            if (colonIndex == -1) return null;
            
            int valueStart = colonIndex + 1;
            while (valueStart < json.length() && Character.isWhitespace(json. charAt(valueStart))) {
                valueStart++;
            }
            
            boolean isString = json.charAt(valueStart) == '"';
            
            if (isString) {
                valueStart++;
                int valueEnd = json.indexOf("\"", valueStart);
                if (valueEnd == -1) return null;
                return json.substring(valueStart, valueEnd);
            } else {
                int valueEnd = valueStart;
                while (valueEnd < json.length()) {
                    char c = json.charAt(valueEnd);
                    if (c == ',' || c == '}') break;
                    valueEnd++;
                }
                return json. substring(valueStart, valueEnd).trim();
            }
            
        } catch (Exception e) {
            return null;
        }
    }
}