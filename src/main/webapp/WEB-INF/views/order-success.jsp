<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String fullName = (String) session.getAttribute("fullName");
    if (fullName == null || !"CUSTOMER".equals(session.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    String orderId = request.getParameter("orderId");
    if (orderId == null) orderId = "ORD" + System.currentTimeMillis();
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .success-container {
            max-width: 600px;
            width: 100%;
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 50px;
            text-align: center;
            animation: slideUp 0.5s ease-out;
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .success-icon {
            width: 120px;
            height: 120px;
            margin: 0 auto 30px;
            border-radius: 50%;
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 60px;
            color: white;
            animation: scaleIn 0.5s ease-out 0.2s both;
        }

        @keyframes scaleIn {
            from {
                transform: scale(0);
            }
            to {
                transform: scale(1);
            }
        }

        h1 {
            color: #333;
            font-size: 32px;
            margin-bottom: 15px;
        }

        .success-message {
            color: #666;
            font-size: 16px;
            line-height: 1.6;
            margin-bottom: 30px;
        }

        .order-info {
            background: #f8f9fa;
            border-radius: 15px;
            padding: 25px;
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
            color: #999;
            font-size: 14px;
        }

        .info-value {
            color: #333;
            font-weight: 600;
            font-size: 14px;
        }

        .order-id {
            font-size: 20px;
            color: #667eea;
            font-weight: 700;
        }

        .next-steps {
            background: #fff8e1;
            border-left: 4px solid #ffc107;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 30px;
            text-align: left;
        }

        .next-steps h3 {
            color: #333;
            font-size: 16px;
            margin-bottom: 15px;
        }

        .next-steps ul {
            list-style: none;
            padding: 0;
        }

        .next-steps li {
            color: #666;
            font-size: 14px;
            margin-bottom: 10px;
            padding-left: 25px;
            position: relative;
        }

        .next-steps li:before {
            content: "✓";
            position: absolute;
            left: 0;
            color: #28a745;
            font-weight: bold;
        }

        .btn-group {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
        }

        .btn {
            padding: 14px 24px;
            border: none;
            border-radius: 10px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .btn-secondary {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }

        .btn-secondary:hover {
            background: #f8f9ff;
        }

        .support-note {
            margin-top: 30px;
            color: #999;
            font-size: 13px;
        }

        .support-note a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
        }

        @media (max-width: 600px) {
            .success-container {
                padding: 30px 20px;
            }

            .btn-group {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="success-container">
        <div class="success-icon">✓</div>
        
        <h1>Đặt hàng thành công!</h1>
        
        <p class="success-message">
            Cảm ơn bạn đã mua hàng tại Fashion Store. 
            Đơn hàng của bạn đã được tiếp nhận và đang được xử lý.
        </p>

        <div class="order-info">
            <div class="info-row">
                <div class="info-label">Mã đơn hàng</div>
                <div class="info-value order-id">#<%= orderId %></div>
            </div>
            <div class="info-row">
                <div class="info-label">Thời gian đặt</div>
                <div class="info-value" id="orderTime"></div>
            </div>
            <div class="info-row">
                <div class="info-label">Người nhận</div>
                <div class="info-value"><%= fullName %></div>
            </div>
            <div class="info-row">
                <div class="info-label">Trạng thái</div>
                <div class="info-value" style="color: #ffc107;">⏳ Đang xử lý</div>
            </div>
        </div>

        <div class="next-steps">
            <h3>📋 Các bước tiếp theo:</h3>
            <ul>
                <li>Chúng tôi sẽ xác nhận đơn hàng qua email/SMS trong vòng 24h</li>
                <li>Đơn hàng sẽ được giao trong 2-3 ngày làm việc</li>
                <li>Bạn có thể theo dõi đơn hàng trong mục "Đơn hàng của tôi"</li>
                <li>Liên hệ hotline 1900 1234 nếu cần hỗ trợ</li>
            </ul>
        </div>

        <div class="btn-group">
            <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary">
                Xem đơn hàng
            </a>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">
                Tiếp tục mua sắm
            </a>
        </div>

        <div class="support-note">
            Cần hỗ trợ? Liên hệ <a href="tel:19001234">Hotline: 1900 1234</a>
        </div>
    </div>

    <script>
        // Display order time
        const now = new Date();
        const timeStr = now.toLocaleString('vi-VN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
        document.getElementById('orderTime').textContent = timeStr;

        // Clear checkout cart
        localStorage.removeItem('checkoutCart');
    </script>
</body>
</html>