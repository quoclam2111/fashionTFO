<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực thành công - Fashion Store</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family:  'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .success-container {
            background: white;
            max-width: 500px;
            width: 100%;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 50px;
            text-align:  center;
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
            width: 100px;
            height: 100px;
            margin: 0 auto 30px;
            border-radius: 50%;
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 50px;
            color: white;
            animation: scaleIn 0.5s ease-out 0.2s both;
        }

        @keyframes scaleIn {
            from {
                transform: scale(0) rotate(-180deg);
            }
            to {
                transform: scale(1) rotate(0deg);
            }
        }

        h1 {
            color: #333;
            font-size:  32px;
            margin-bottom:  20px;
        }

        .success-message {
            color: #666;
            font-size: 16px;
            line-height: 1.6;
            margin-bottom: 30px;
        }

        .info-card {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin-bottom:  30px;
            text-align: left;
        }

        .info-card . label {
            color: #999;
            font-size: 14px;
            margin-bottom: 5px;
        }

        .info-card . value {
            color: #333;
            font-size: 18px;
            font-weight: 600;
        }

        . btn-login {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition:  transform 0.2s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .countdown {
            margin-top: 20px;
            color: #999;
            font-size: 14px;
        }

        .countdown span {
            color: #667eea;
            font-weight: bold;
        }

        @media (max-width: 768px) {
            .success-container {
                padding: 40px 30px;
            }

            h1 {
                font-size: 28px;
            }
        }
    </style>
</head>
<body>
    <div class="success-container">
        <div class="success-icon">✓</div>
        
        <h1>Xác thực thành công! </h1>
        
        <p class="success-message">
            <%= request.getAttribute("success") != null 
                ? request.getAttribute("success") 
                : "Chúc mừng! Email của bạn đã được xác thực thành công.  Bạn có thể đăng nhập ngay bây giờ để bắt đầu mua sắm." 
            %>
        </p>

        <a href="${pageContext.request.contextPath}/login" class="btn-login">
            🔐 Đăng nhập ngay
        </a>

        <p class="countdown">
            Bạn sẽ được chuyển đến trang đăng nhập sau <span id="countdown">5</span> giây... 
        </p>
    </div>

    <script>
        let seconds = 5;
        const countdownElement = document.getElementById('countdown');
        
        const interval = setInterval(() => {
            seconds--;
            countdownElement.textContent = seconds;
            
            if (seconds <= 0) {
                clearInterval(interval);
                window.location.href = '${pageContext.request.contextPath}/login';
            }
        }, 1000);
    </script>
</body>
</html>