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
    <title>Chỉnh sửa người dùng - Fashion Store</title>
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

        .btn-back {
            padding: 10px 20px;
            background: #95a5a6;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
        }

        .btn-back:hover {
            background: #7f8c8d;
        }

        .error-message {
            background: #f8d7da;
            color: #721c24;
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #dc3545;
        }

        .edit-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            padding: 40px;
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 20px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group.full-width {
            grid-column: 1 / -1;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
            font-size: 14px;
        }

        .form-group input,
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 15px;
            font-family: inherit;
            transition: all 0.3s;
        }

        .form-group textarea {
            resize: vertical;
            min-height: 100px;
        }

        .form-group input:focus,
        .form-group select:focus,
        .form-group textarea:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .form-group input[readonly] {
            background: #f5f5f5;
            cursor: not-allowed;
        }

        .form-note {
            font-size: 13px;
            color: #999;
            margin-top: 5px;
        }

        .form-actions {
            display: flex;
            gap: 15px;
            justify-content: flex-end;
            margin-top: 30px;
            padding-top: 30px;
            border-top: 2px solid #f0f0f0;
        }

        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
            transition: all 0.3s;
        }

        .btn-cancel {
            background: #95a5a6;
            color: white;
        }

        .btn-cancel:hover {
            background: #7f8c8d;
        }

        .btn-save {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-save:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.3);
        }

        @media (max-width: 1024px) {
            .sidebar {
                width: 0;
                padding: 0;
            }

            .main-content {
                margin-left: 0;
            }

            .form-row {
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
            <h1>Chỉnh sửa người dùng</h1>
            <a href="${pageContext.request.contextPath}/admin/users?action=view&id=<%= user != null ? user.id : "" %>" 
               class="btn-back">
                ← Quay lại
            </a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                ✕ <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <% if (user != null) { %>
            <div class="edit-card">
                <form method="post" action="${pageContext.request.contextPath}/admin/users">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="userId" value="<%= user.id %>">

                    <div class="form-group">
                        <label>Tên đăng nhập</label>
                        <input type="text" value="<%= user.username %>" readonly>
                        <div class="form-note">Tên đăng nhập không thể thay đổi</div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="fullName">Họ và tên *</label>
                            <input type="text" id="fullName" name="fullName" 
                                   value="<%= user.fullName %>" required>
                        </div>

                        <div class="form-group">
                            <label for="email">Email *</label>
                            <input type="email" id="email" name="email" 
                                   value="<%= user.email %>" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="phone">Số điện thoại *</label>
                            <input type="tel" id="phone" name="phone" 
                                   value="<%= user.phone %>" required>
                        </div>

                        <div class="form-group">
                            <label for="status">Trạng thái *</label>
                            <select id="status" name="status" required>
                                <option value="active" <%= "active".equals(user.status) ? "selected" : "" %>>
                                    Đang hoạt động
                                </option>
                                <option value="locked" <%= "locked".equals(user.status) ? "selected" : "" %>>
                                    Bị khóa
                                </option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group full-width">
                        <label for="address">Địa chỉ</label>
                        <textarea id="address" name="address"><%= user.address != null ? user.address : "" %></textarea>
                    </div>

                    <div class="form-group">
                        <label for="password">Mật khẩu mới</label>
                        <input type="password" id="password" name="password" 
                               placeholder="Để trống nếu không muốn đổi mật khẩu">
                        <div class="form-note">Chỉ nhập nếu muốn thay đổi mật khẩu (tối thiểu 6 ký tự)</div>
                    </div>

                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/admin/users?action=view&id=<%= user.id %>" 
                           class="btn btn-cancel">
                            Hủy
                        </a>
                        <button type="submit" class="btn btn-save">
                            💾 Lưu thay đổi
                        </button>
                    </div>
                </form>
            </div>
        <% } else { %>
            <div class="edit-card">
                <p style="text-align: center; color: #999; padding: 40px;">
                    Không tìm thấy thông tin người dùng
                </p>
            </div>
        <% } %>
    </div>
</body>
</html>