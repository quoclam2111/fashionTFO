<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String fullName = (String) session.getAttribute("fullName");
    String role = (String) session.getAttribute("role");
    
    if (fullName == null || "CUSTOMER".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    String roleDisplay = "";
    if ("ADMIN".equals(role)) roleDisplay = "Quản trị viên";
    else if ("MANAGER".equals(role)) roleDisplay = "Quản lý";
    else if ("STAFF".equals(role)) roleDisplay = "Nhân viên";
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản trị - Fashion Store</title>
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

        .sidebar {
            position: fixed;
            left: 0;
            top: 0;
            width: 260px;
            height: 100vh;
            background: linear-gradient(180deg, #2c3e50 0%, #34495e 100%);
            padding: 30px 0;
            overflow-y: auto;
        }

        .sidebar-logo {
            text-align: center;
            margin-bottom: 40px;
            padding: 0 20px;
        }

        .sidebar-logo h2 {
            color: white;
            font-size: 24px;
            margin-bottom: 5px;
        }

        .sidebar-logo p {
            color: #bdc3c7;
            font-size: 14px;
        }

        .sidebar-menu {
            list-style: none;
        }

        .sidebar-menu li {
            margin-bottom: 5px;
        }

        .sidebar-menu a {
            display: flex;
            align-items: center;
            padding: 15px 25px;
            color: #ecf0f1;
            text-decoration: none;
            transition: all 0.3s;
            font-weight: 500;
        }

        .sidebar-menu a:hover,
        .sidebar-menu a.active {
            background: rgba(255, 255, 255, 0.1);
            border-left: 4px solid #3498db;
        }

        .sidebar-menu .icon {
            margin-right: 12px;
            font-size: 20px;
        }

        .main-content {
            margin-left: 260px;
            min-height: 100vh;
        }

        .topbar {
            background: white;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 20px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .topbar-left h1 {
            color: #2c3e50;
            font-size: 28px;
        }

        .topbar-right {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 8px 15px;
            background: #f8f9fa;
            border-radius: 10px;
        }

        .avatar {
            width: 45px;
            height: 45px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: 600;
            font-size: 18px;
        }

        .user-details {
            text-align: left;
        }

        .user-details .name {
            font-weight: 600;
            color: #2c3e50;
            display: block;
        }

        .user-details .role {
            font-size: 12px;
            color: #7f8c8d;
        }

        .btn-logout {
            padding: 10px 20px;
            background: #e74c3c;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: background 0.3s;
        }

        .btn-logout:hover {
            background: #c0392b;
        }

        .content-area {
            padding: 40px;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 25px;
            margin-bottom: 40px;
        }

        .stat-card {
            background: white;
            border-radius: 12px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .stat-icon {
            width: 60px;
            height: 60px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 28px;
        }

        .stat-icon.blue {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .stat-icon.green {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
        }

        .stat-icon.orange {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
        }

        .stat-icon.purple {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
        }

        .stat-info h3 {
            font-size: 32px;
            color: #2c3e50;
            margin-bottom: 5px;
        }

        .stat-info p {
            color: #7f8c8d;
            font-size: 14px;
        }

        .action-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 25px;
        }

        .action-card {
            background: white;
            border-radius: 12px;
            padding: 30px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            transition: transform 0.3s, box-shadow 0.3s;
            cursor: pointer;
        }

        .action-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.12);
        }

        .action-card .icon {
            width: 70px;
            height: 70px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 32px;
            margin-bottom: 20px;
        }

        .action-card h3 {
            color: #2c3e50;
            font-size: 22px;
            margin-bottom: 10px;
        }

        .action-card p {
            color: #7f8c8d;
            line-height: 1.6;
        }

        @media (max-width: 1024px) {
            .sidebar {
                width: 0;
                padding: 0;
            }

            .main-content {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>
    <aside class="sidebar">
        <div class="sidebar-logo">
            <h2>Fashion Store</h2>
            <p>Admin Panel</p>
        </div>

        <ul class="sidebar-menu">
            <li>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="active">
                    <span class="icon">📊</span>
                    Dashboard
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/users">
                    <span class="icon">👥</span>
                    Quản lý người dùng
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/products">
                    <span class="icon">📦</span>
                    Quản lý sản phẩm
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/orders">
                    <span class="icon">🛒</span>
                    Quản lý đơn hàng
                </a>
            </li>
            <li>
                <a href="#" onclick="alert('Chức năng đang phát triển'); return false;">
                    <span class="icon">💰</span>
                    Báo cáo doanh thu
                </a>
            </li>
            <li>
                <a href="#" onclick="alert('Chức năng đang phát triển'); return false;">
                    <span class="icon">⚙️</span>
                    Cài đặt
                </a>
            </li>
        </ul>
    </aside>

    <div class="main-content">
        <div class="topbar">
            <div class="topbar-left">
                <h1>Dashboard</h1>
            </div>

            <div class="topbar-right">
                <div class="user-info">
                    <div class="avatar"><%= fullName.substring(0, 1).toUpperCase() %></div>
                    <div class="user-details">
                        <span class="name"><%= fullName %></span>
                        <span class="role"><%= roleDisplay %></span>
                    </div>
                </div>
                <form method="post" action="${pageContext.request.contextPath}/logout" style="display: inline;">
                    <button type="submit" class="btn-logout">Đăng xuất</button>
                </form>
            </div>
        </div>

        <div class="content-area">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon blue">👥</div>
                    <div class="stat-info">
                        <h3>0</h3>
                        <p>Tổng người dùng</p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon green">📦</div>
                    <div class="stat-info">
                        <h3>0</h3>
                        <p>Sản phẩm</p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon orange">🛒</div>
                    <div class="stat-info">
                        <h3>0</h3>
                        <p>Đơn hàng</p>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon purple">💰</div>
                    <div class="stat-info">
                        <h3>0đ</h3>
                        <p>Doanh thu</p>
                    </div>
                </div>
            </div>

            <div class="action-cards">
                <div class="action-card" onclick="location.href='${pageContext.request.contextPath}/admin/users'">
                    <div class="icon">👥</div>
                    <h3>Quản lý người dùng</h3>
                    <p>Xem, thêm, sửa, xóa người dùng trong hệ thống</p>
                </div>

                <div class="action-card" onclick="location.href='${pageContext.request.contextPath}/admin/products'">
                    <div class="icon">📦</div>
                    <h3>Quản lý sản phẩm</h3>
                    <p>Quản lý danh mục và sản phẩm thời trang</p>
                </div>

                <div class="action-card" onclick="location.href='${pageContext.request.contextPath}/admin/orders'">
                    <div class="icon">🛒</div>
                    <h3>Quản lý đơn hàng</h3>
                    <p>Xử lý và theo dõi các đơn hàng</p>
                </div>

                <div class="action-card" onclick="alert('Chức năng đang được phát triển')">
                    <div class="icon">📊</div>
                    <h3>Báo cáo & Thống kê</h3>
                    <p>Xem báo cáo doanh thu và thống kê</p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>