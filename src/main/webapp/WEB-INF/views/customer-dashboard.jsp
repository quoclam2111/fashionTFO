<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String fullName = (String) session.getAttribute("fullName");
    String username = (String) session.getAttribute("username");
    
    if (fullName == null || !"CUSTOMER".equals(session.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ - Fashion Store</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
        }

        .navbar {
            background: white;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 15px 0;
            position: sticky;
            top: 0;
            z-index: 100;
        }

        .navbar-content {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            font-size: 28px;
            font-weight: 700;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .nav-menu {
            display: flex;
            gap: 30px;
            list-style: none;
        }

        .nav-menu a {
            text-decoration: none;
            color: #333;
            font-weight: 500;
            transition: color 0.3s;
        }

        .nav-menu a:hover {
            color: #667eea;
        }

        .user-menu {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: 600;
        }

        .btn-logout {
            padding: 8px 20px;
            background: #f44336;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            transition: background 0.3s;
        }

        .btn-logout:hover {
            background: #d32f2f;
        }

        .hero {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 80px 20px;
            text-align: center;
        }

        .hero h1 {
            font-size: 48px;
            margin-bottom: 20px;
        }

        .hero p {
            font-size: 20px;
            opacity: 0.9;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 40px 20px;
        }

        .dashboard-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 30px;
            margin-bottom: 40px;
        }

        .card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            transition: transform 0.3s, box-shadow 0.3s;
            cursor: pointer;
            text-align: center;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.15);
        }

        .card-icon {
            width: 70px;
            height: 70px;
            margin: 0 auto 20px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 32px;
        }

        .card-icon.products {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .card-icon.cart {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
        }

        .card-icon.orders {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
        }

        .card h3 {
            color: #333;
            margin-bottom: 10px;
            font-size: 22px;
        }

        .card p {
            color: #666;
            line-height: 1.6;
        }

        .section-title {
            font-size: 32px;
            color: #333;
            margin-bottom: 30px;
            text-align: center;
        }

        .coming-soon {
            background: white;
            border-radius: 15px;
            padding: 60px;
            text-align: center;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
        }

        .coming-soon h2 {
            color: #667eea;
            font-size: 36px;
            margin-bottom: 20px;
        }

        .coming-soon p {
            color: #666;
            font-size: 18px;
        }

        @media (max-width: 768px) {
            .nav-menu {
                display: none;
            }

            .hero h1 {
                font-size: 32px;
            }

            .dashboard-cards {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-content">
            <div class="logo">Fashion Store</div>
            
            <ul class="nav-menu">
                <li><a href="${pageContext.request.contextPath}/home">Sản phẩm</a></li>
                <li><a href="#cart">Giỏ hàng</a></li>
                <li><a href="#orders">Đơn hàng</a></li>
            </ul>
            
            <div class="user-menu">
                <div class="user-info">
                    <div class="avatar"><%= fullName.substring(0, 1).toUpperCase() %></div>
                    <span><strong><%= fullName %></strong></span>
                </div>
                <form method="post" action="${pageContext.request.contextPath}/logout" style="display: inline;">
                    <button type="submit" class="btn-logout">Đăng xuất</button>
                </form>
            </div>
        </div>
    </nav>

    <div class="hero">
        <h1>Chào mừng, <%= fullName %>!</h1>
        <p>Khám phá bộ sưu tập thời trang mới nhất của chúng tôi</p>
    </div>

    <div class="container">
        <div class="dashboard-cards">
            <div class="card" onclick="location.href='${pageContext.request.contextPath}/home'">
                <div class="card-icon products">🛍️</div>
                <h3>Danh sách sản phẩm</h3>
                <p>Xem và mua các sản phẩm thời trang mới nhất</p>
            </div>

            <div class="card" onclick="alert('Chức năng đang được phát triển')">
                <div class="card-icon cart">🛒</div>
                <h3>Giỏ hàng</h3>
                <p>Quản lý các sản phẩm bạn muốn mua</p>
            </div>

            <div class="card" onclick="alert('Chức năng đang được phát triển')">
                <div class="card-icon orders">📦</div>
                <h3>Đơn hàng của tôi</h3>
                <p>Theo dõi trạng thái đơn hàng</p>
            </div>
        </div>

        <h2 class="section-title">Các chức năng sắp ra mắt</h2>
        <div class="coming-soon">
            <h2>🚀 Đang phát triển</h2>
            <p>Chúng tôi đang làm việc chăm chỉ để mang đến cho bạn trải nghiệm mua sắm tuyệt vời nhất.<br>
               Các tính năng giỏ hàng và thanh toán sẽ sớm được tích hợp!</p>
        </div>
    </div>
</body>
</html>