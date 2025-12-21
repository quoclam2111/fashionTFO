<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="repository.DTO.OrderDTO" %>
<%@ page import="repository.DTO. OrderItemDTO" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util. Locale" %>
<%
    OrderDTO order = (OrderDTO) request.getAttribute("order");
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt hàng thành công - Fashion Store</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background:  #f5f5f5;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .success-container {
            background: white;
            max-width: 700px;
            width: 100%;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
            padding: 50px;
            text-align:  center;
        }

        . success-icon {
            width: 100px;
            height: 100px;
            margin: 0 auto 30px;
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 50px;
            animation: scaleIn 0.5s ease-out;
        }

        @keyframes scaleIn {
            from {
                transform: scale(0);
                opacity: 0;
            }
            to {
                transform: scale(1);
                opacity: 1;
            }
        }

        h1 {
            font-size: 32px;
            color: #333;
            margin-bottom: 15px;
        }

        .subtitle {
            color: #666;
            font-size: 16px;
            margin-bottom:  40px;
        }

        . order-info {
            background: #f8f9fa;
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 30px;
            text-align: left;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 12px 0;
            border-bottom: 1px solid #e0e0e0;
        }

        .info-row:last-child {
            border-bottom: none;
        }

        .info-label {
            color: #666;
            font-weight: 500;
        }

        .info-value {
            color: #333;
            font-weight: 600;
            text-align: right;
        }

        .order-total {
            font-size: 24px;
            color: #667eea;
        }

        .actions {
            display: flex;
            gap: 15px;
            justify-content:  center;
            flex-wrap: wrap;
        }

        .btn {
            padding: 14px 30px;
            border-radius: 10px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            transition:  all 0.3s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
        }

        .btn-secondary {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }

        .btn-secondary:hover {
            background: #f8f9ff;
        }

        .note {
            margin-top: 30px;
            color: #999;
            font-size: 14px;
        }

        .product-list {
            margin-top: 20px;
            text-align: left;
        }

        .product-item {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #e0e0e0;
        }

        .product-item:last-child {
            border-bottom: none;
        }

        .product-name {
            color: #333;
            font-weight: 500;
        }

        .product-details {
            color: #999;
            font-size: 13px;
        }

        .error-box {
            background: #fee;
            color: #c33;
            padding: 20px;
            border-radius:  10px;
            border-left: 4px solid #c33;
        }
    </style>
</head>
<body>
    <div class="success-container">
        <div class="success-icon">✓</div>
        
        <h1>Đặt hàng thành công! </h1>
        <p class="subtitle">Cảm ơn bạn đã mua hàng tại Fashion Store</p>

        <% if (order != null) { %>
            <div class="order-info">
                <div class="info-row">
                    <span class="info-label">Mã đơn hàng:</span>
                    <span class="info-value"><%= order.orderNo != null ? order.orderNo : order.id. substring(0, 8) %></span>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Thời gian đặt: </span>
                    <span class="info-value"><%= dateFormat.format(order.orderDate) %></span>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Người nhận:</span>
                    <span class="info-value"><%= order.customerName %></span>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Số điện thoại:</span>
                    <span class="info-value"><%= order.customerPhone %></span>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Địa chỉ:</span>
                    <span class="info-value"><%= order.customerAddress %></span>
                </div>
                
                <div class="info-row">
                    <span class="info-label">Phương thức thanh toán:</span>
                    <span class="info-value">
                        <% if ("COD". equals(order.paymentMethod)) { %>
                            💵 Thanh toán khi nhận hàng
                        <% } else if ("BANKING".equals(order.paymentMethod)) { %>
                            🏦 Chuyển khoản ngân hàng
                        <% } else if ("QR".equals(order.paymentMethod)) { %>
                            📱 Quét mã QR
                        <% } else if ("CARD".equals(order.paymentMethod)) { %>
                            💳 Thẻ tín dụng
                        <% } else { %>
                            <%= order.paymentMethod %>
                        <% } %>
                    </span>
                </div>
                
                <% if (order.items != null && ! order.items.isEmpty()) { %>
                    <div class="product-list">
                        <h3 style="margin:  20px 0 15px 0; font-size: 18px;">Sản phẩm đã đặt: </h3>
                        <% for (OrderItemDTO item : order.items) { %>
                            <div class="product-item">
                                <div>
                                    <div class="product-name"><%= item.productName %></div>
                                    <div class="product-details">
                                        <%= item.colorName %> - <%= item.sizeName %> 
                                        x <%= item.quantity %>
                                    </div>
                                </div>
                                <div class="info-value">
                                    <%= currencyFormat.format(item. subtotal) %>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } %>
                
                <div class="info-row" style="margin-top: 20px; padding-top: 20px; border-top: 2px solid #e0e0e0;">
                    <span class="info-label" style="font-size: 18px;">Tổng tiền:</span>
                    <span class="info-value order-total">
                        <%= currencyFormat. format(order.totalAmount) %>
                    </span>
                </div>
            </div>

            <div class="note">
                📧 Thông tin đơn hàng đã được lưu vào hệ thống. <br>
                Chúng tôi sẽ liên hệ với bạn trong thời gian sớm nhất.
            </div>
        <% } else { %>
            <div class="error-box">
                <%= request.getAttribute("error") != null ? request.getAttribute("error") : "Không tìm thấy thông tin đơn hàng" %>
            </div>
        <% } %>

        <div class="actions">
            <a href="<%= request.getContextPath() %>/customer/orders" class="btn btn-primary">
                Xem đơn hàng của tôi
            </a>
            <a href="<%= request.getContextPath() %>/home" class="btn btn-secondary">
                Tiếp tục mua sắm
            </a>
        </div>
    </div>
</body>
</html>