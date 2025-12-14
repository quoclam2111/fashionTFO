<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="adapters.quanlynguoidung.get.GetUserViewModel" %>
<%@ page import="quanlynguoidung.get.UserViewItem" %>
<%
    GetUserViewModel viewModel = (GetUserViewModel) request.getAttribute("viewModel");
    UserViewItem user = viewModel != null ? viewModel.user : null;
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết người dùng - Fashion Store</title>
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
        }

        .sidebar-logo {
            text-align: center;
            margin-bottom: 40px;
            padding: 0 20px;
        }

        .sidebar-logo h2 {
            color: white;
            font-size: 24px;
        }

        .sidebar-menu {
            list-style: none;
        }

        .sidebar-menu a {
            display: flex;
            align-items: center;
            padding: 15px 25px;
            color: #ecf0f1;
            text-decoration: none;
            transition: all 0.3s;
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
            padding: 40px;
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

        .btn-delete {
            background: #e74c3c;
            color: white;
        }

        .btn-delete:hover {
            background: #c0392b;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #28a745;
        }

        .user-detail-card {
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

        .status-badge {
            display: inline-block;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 600;
        }

        .status-active {
            background: #d4edda;
            color: #155724;
        }

        .status-inactive {
            background: #f8d7da;
            color: #721c24;
        }

        @media (max-width: 1024px) {
            .sidebar {
                width: 0;
                padding: 0;
            }

            .main-content {
                margin-left: 0;
            }

            .info-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <aside class="sidebar">
        <div class="sidebar-logo">
            <h2>Fashion Store</h2>
        </div>

        <ul class="sidebar-menu">
            <li>
                <a href="${pageContext.request.contextPath}/admin/dashboard">
                    <span class="icon">📊</span>
                    Dashboard
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/users" class="active">
                    <span class="icon">👥</span>
                    Quản lý người dùng
                </a>
            </li>
        </ul>
    </aside>

    <div class="main-content">
        <div class="page-header">
            <h1>Chi tiết người dùng</h1>
            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-back">
                    ← Quay lại
                </a>
                <% if (user != null) { %>
                    <a href="${pageContext.request.contextPath}/admin/users?action=edit&id=<%= user.id %>" 
                       class="btn btn-edit">
                        ✎ Chỉnh sửa
                    </a>
                    <button onclick="confirmDelete('<%= user.id %>', '<%= user.username %>')" 
                            class="btn btn-delete">
                        🗑 Xóa
                    </button>
                <% } %>
            </div>
        </div>

        <% if (request.getParameter("success") != null) { %>
            <div class="alert-success">
                ✓ Cập nhật thông tin thành công!
            </div>
        <% } %>

        <% if (user != null) { %>
            <div class="user-detail-card">
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
                            <div class="info-label">📊 Trạng thái</div>
                            <div class="info-value">
                                <span class="status-badge <%= "active".equals(user.status) ? "status-active" : "status-inactive" %>">
                                    <%= "active".equals(user.status) ? "Đang hoạt động" : "Bị khóa" %>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        <% } else { %>
            <div class="user-detail-card">
                <div class="card-body">
                    <p style="text-align: center; color: #999; padding: 40px;">
                        Không tìm thấy thông tin người dùng
                    </p>
                </div>
            </div>
        <% } %>
    </div>

    <script>
        function confirmDelete(userId, username) {
            if (confirm('Bạn có chắc chắn muốn xóa người dùng "' + username + '"?')) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '${pageContext.request.contextPath}/admin/users';
                
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'delete';
                
                const userIdInput = document.createElement('input');
                userIdInput.type = 'hidden';
                userIdInput.name = 'userId';
                userIdInput.value = userId;
                
                form.appendChild(actionInput);
                form.appendChild(userIdInput);
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
</body>
</html>