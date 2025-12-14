<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="adapters.quanlynguoidung.get.GetUserViewModel" %>
<%@ page import="quanlynguoidung.get.UserViewItem" %>
<%
    String fullName = (String) session.getAttribute("fullName");
    if (fullName == null || !"CUSTOMER".equals(session.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    GetUserViewModel viewModel = (GetUserViewModel) request.getAttribute("viewModel");
    UserViewItem user = viewModel != null ? viewModel.user : null;
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông tin cá nhân - Fashion Store</title>
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
            cursor: pointer;
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

        .container {
            max-width: 900px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .page-header h1 {
            color: #2c3e50;
            font-size: 32px;
        }

        .btn-group {
            display: flex;
            gap: 10px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
        }

        .btn-back {
            background: #95a5a6;
            color: white;
        }

        .btn-back:hover {
            background: #7f8c8d;
        }

        .btn-edit {
            background: #f39c12;
            color: white;
        }

        .btn-edit:hover {
            background: #e67e22;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #28a745;
        }

        .profile-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }

        .avatar-large {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            background: white;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
            font-size: 48px;
            font-weight: 700;
            color: #667eea;
        }

        .user-name {
            font-size: 28px;
            margin-bottom: 5px;
        }

        .user-username {
            font-size: 16px;
            opacity: 0.9;
        }

        .card-body {
            padding: 40px;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 30px;
        }

        .info-item {
            padding: 20px;
            background: #f8f9fa;
            border-radius: 10px;
        }

        .info-label {
            color: #999;
            font-size: 14px;
            margin-bottom: 8px;
            font-weight: 500;
        }

        .info-value {
            color: #333;
            font-size: 18px;
            font-weight: 600;
        }

        .info-item.full-width {
            grid-column: 1 / -1;
        }

        @media (max-width: 768px) {
            .nav-menu {
                display: none;
            }

            .info-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-content">
            <div class="logo" onclick="location.href='${pageContext.request.contextPath}/customer/dashboard'">
                Fashion Store
            </div>
            
            <ul class="nav-menu">
                <li><a href="${pageContext.request.contextPath}/home">Sản phẩm</a></li>
                <li><a href="${pageContext.request.contextPath}/customer/dashboard">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/customer/profile">Tài khoản</a></li>
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

    <div class="container">
        <div class="page-header">
            <h1>Thông tin cá nhân</h1>
            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/customer/dashboard" class="btn btn-back">
                    ← Quay lại
                </a>
                <% if (user != null) { %>
                    <a href="${pageContext.request.contextPath}/customer/profile?action=edit" 
                       class="btn btn-edit">
                        ✎ Chỉnh sửa
                    </a>
                <% } %>
            </div>
        </div>

        <% if (request.getParameter("success") != null) { %>
            <div class="alert-success">
                ✓ Cập nhật thông tin thành công!
            </div>
        <% } %>

        <% if (user != null) { %>
            <div class="profile-card">
                <div class="card-header">
                    <div class="avatar-large">
                        <%= user.fullName.substring(0, 1).toUpperCase() %>
                    </div>
                    <h2 class="user-name"><%= user.fullName %></h2>
                    <p class="user-username">@<%= user.username %></p>
                </div>

                <div class="card-body">
                    <div class="info-grid">
                        <div class="info-item">
                            <div class="info-label">📧 Email</div>
                            <div class="info-value"><%= user.email %></div>
                        </div>

                        <div class="info-item">
                            <div class="info-label">📱 Số điện thoại</div>
                            <div class="info-value"><%= user.phone %></div>
                        </div>

                        <div class="info-item full-width">
                            <div class="info-label">📍 Địa chỉ</div>
                            <div class="info-value">
                                <%= user.address != null && !user.address.isEmpty() ? user.address : "Chưa cập nhật" %>
                            </div>
                        </div>

                        <div class="info-item">
                            <div class="info-label">🔑 User ID</div>
                            <div class="info-value"><%= user.id %></div>
                        </div>

                        <div class="info-item">
                            <div class="info-label">📊 Trạng thái tài khoản</div>
                            <div class="info-value" style="color: #28a745;">
                                ✓ Đang hoạt động
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        <% } else { %>
            <div class="profile-card">
                <div class="card-body">
                    <p style="text-align: center; color: #999; padding: 40px;">
                        Không tìm thấy thông tin người dùng
                    </p>
                </div>
            </div>
        <% } %>
    </div>
</body>
</html>