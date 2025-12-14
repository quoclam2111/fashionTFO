<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="adapters.quanlynguoidung.list.ListUsersViewModel" %>
<%@ page import="quanlynguoidung.list.UserListItem" %>
<%@ page import="java.util.List" %>
<%
    ListUsersViewModel viewModel = (ListUsersViewModel) request.getAttribute("viewModel");
    List<UserListItem> users = viewModel != null ? viewModel.users : null;
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý người dùng - Fashion Store</title>
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

        .content-area {
            padding: 40px;
        }

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }

        .page-header h2 {
            color: #2c3e50;
            font-size: 32px;
        }

        .filters {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            display: flex;
            gap: 15px;
            align-items: center;
            flex-wrap: wrap;
        }

        .filter-group {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .filter-group label {
            color: #555;
            font-weight: 500;
        }

        .filter-group select {
            padding: 8px 12px;
            border: 2px solid #e0e0e0;
            border-radius: 6px;
            font-size: 14px;
        }

        .alert {
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            border-left: 4px solid #28a745;
        }

        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border-left: 4px solid #dc3545;
        }

        .users-table {
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        thead {
            background: #f8f9fa;
        }

        th {
            padding: 15px;
            text-align: left;
            font-weight: 600;
            color: #2c3e50;
            border-bottom: 2px solid #dee2e6;
        }

        td {
            padding: 15px;
            border-bottom: 1px solid #dee2e6;
            color: #555;
        }

        tbody tr:hover {
            background: #f8f9fa;
        }

        .status-badge {
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            display: inline-block;
        }

        .status-active {
            background: #d4edda;
            color: #155724;
        }

        .status-inactive {
            background: #f8d7da;
            color: #721c24;
        }

        .action-buttons {
            display: flex;
            gap: 8px;
        }

        .btn {
            padding: 6px 12px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 13px;
            font-weight: 500;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
        }

        .btn-view {
            background: #3498db;
            color: white;
        }

        .btn-view:hover {
            background: #2980b9;
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

        .no-data {
            text-align: center;
            padding: 60px;
            color: #999;
        }

        .no-data .icon {
            font-size: 64px;
            margin-bottom: 20px;
        }

        @media (max-width: 1024px) {
            .sidebar {
                width: 0;
                padding: 0;
            }

            .main-content {
                margin-left: 0;
            }

            .users-table {
                overflow-x: auto;
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
        <div class="topbar">
            <div class="topbar-left">
                <h1>Quản lý người dùng</h1>
            </div>
        </div>

        <div class="content-area">
            <% if (request.getParameter("deleted") != null) { %>
                <div class="alert alert-success">
                    ✓ Xóa người dùng thành công!
                </div>
            <% } %>

            <% if (request.getParameter("error") != null) { %>
                <div class="alert alert-error">
                    ✕ <%= request.getParameter("error") %>
                </div>
            <% } %>

            <div class="filters">
                <form method="get" action="${pageContext.request.contextPath}/admin/users" style="display: flex; gap: 15px; flex-wrap: wrap; width: 100%;">
                    <div class="filter-group">
                        <label>Trạng thái:</label>
                        <select name="status" onchange="this.form.submit()">
                            <option value="all" <%= "all".equals(request.getParameter("status")) ? "selected" : "" %>>Tất cả</option>
                            <option value="active" <%= "active".equals(request.getParameter("status")) ? "selected" : "" %>>Đang hoạt động</option>
                            <option value="locked" <%= "locked".equals(request.getParameter("status")) ? "selected" : "" %>>Bị khóa</option>
                        </select>
                    </div>

                    <div class="filter-group">
                        <label>Sắp xếp:</label>
                        <select name="sortBy" onchange="this.form.submit()">
                            <option value="fullName" <%= "fullName".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Theo tên</option>
                            <option value="email" <%= "email".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Theo email</option>
                            <option value="username" <%= "username".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Theo username</option>
                        </select>
                    </div>

                    <div class="filter-group">
                        <label>Thứ tự:</label>
                        <select name="order" onchange="this.form.submit()">
                            <option value="asc" <%= !"desc".equals(request.getParameter("order")) ? "selected" : "" %>>Tăng dần</option>
                            <option value="desc" <%= "desc".equals(request.getParameter("order")) ? "selected" : "" %>>Giảm dần</option>
                        </select>
                    </div>
                </form>
            </div>

            <div class="users-table">
                <% if (users != null && !users.isEmpty()) { %>
                    <table>
                        <thead>
                            <tr>
                                <th>Username</th>
                                <th>Họ và tên</th>
                                <th>Email</th>
                                <th>Số điện thoại</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (UserListItem user : users) { %>
                                <tr>
                                    <td><strong><%= user.username %></strong></td>
                                    <td><%= user.fullName %></td>
                                    <td><%= user.email %></td>
                                    <td><%= user.phone %></td>
                                    <td>
                                        <span class="status-badge <%= "active".equals(user.status) ? "status-active" : "status-inactive" %>">
                                            <%= "active".equals(user.status) ? "Đang hoạt động" : "Bị khóa" %>
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/admin/users?action=view&id=<%= user.id %>" 
                                               class="btn btn-view">Xem</a>
                                            <a href="${pageContext.request.contextPath}/admin/users?action=edit&id=<%= user.id %>" 
                                               class="btn btn-edit">Sửa</a>
                                            <button onclick="confirmDelete('<%= user.id %>', '<%= user.username %>')" 
                                                    class="btn btn-delete">Xóa</button>
                                        </div>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } else { %>
                    <div class="no-data">
                        <div class="icon">📭</div>
                        <h3>Không có dữ liệu</h3>
                        <p>Chưa có người dùng nào trong hệ thống</p>
                    </div>
                <% } %>
            </div>
        </div>
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