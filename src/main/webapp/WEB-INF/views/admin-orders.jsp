<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="adapters.quanlydonhang.listorders.ListOrdersViewModel" %>
<%@ page import="repository.DTO.OrderDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    ListOrdersViewModel viewModel = (ListOrdersViewModel) request.getAttribute("viewModel");
    List<OrderDTO> orders = viewModel != null ? viewModel.orders : null;
    NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý đơn hàng - Fashion Store</title>
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

        .orders-table {
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

        .status-pending {
            background: #fff3cd;
            color: #856404;
        }

        .status-confirmed {
            background: #cfe2ff;
            color: #084298;
        }

        .status-shipping {
            background: #e7f3ff;
            color: #055160;
        }

        .status-completed {
            background: #d4edda;
            color: #155724;
        }

        .status-cancelled {
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
            .main-content {
                margin-left: 0;
            }

            .orders-table {
                overflow-x: auto;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="admin-sidebar.jsp">
        <jsp:param name="activePage" value="orders" />
    </jsp:include>

    <div class="main-content">
        <div class="topbar">
            <div class="topbar-left">
                <h1>Quản lý đơn hàng</h1>
            </div>
        </div>

        <div class="content-area">
            <% if (request.getParameter("updated") != null) { %>
                <div class="alert alert-success">
                    ✓ Cập nhật đơn hàng thành công!
                </div>
            <% } %>

            <% if (request.getParameter("deleted") != null) { %>
                <div class="alert alert-success">
                    ✓ Xóa đơn hàng thành công!
                </div>
            <% } %>

            <% if (request.getParameter("error") != null) { %>
                <div class="alert alert-error">
                    ✕ <%= request.getParameter("error") %>
                </div>
            <% } %>

            <div class="page-header">
                <h2>Danh sách đơn hàng</h2>
            </div>

            <div class="filters">
                <form method="get" action="${pageContext.request.contextPath}/admin/orders" style="display: flex; gap: 15px; flex-wrap: wrap; width: 100%;">
                    <div class="filter-group">
                        <label>Trạng thái:</label>
                        <select name="status" onchange="this.form.submit()">
                            <option value="all" <%= "all".equals(request.getParameter("status")) ? "selected" : "" %>>Tất cả</option>
                            <option value="pending" <%= "pending".equals(request.getParameter("status")) ? "selected" : "" %>>Chờ xác nhận</option>
                            <option value="confirmed" <%= "confirmed".equals(request.getParameter("status")) ? "selected" : "" %>>Đã xác nhận</option>
                            <option value="shipping" <%= "shipping".equals(request.getParameter("status")) ? "selected" : "" %>>Đang giao</option>
                            <option value="completed" <%= "completed".equals(request.getParameter("status")) ? "selected" : "" %>>Hoàn thành</option>
                            <option value="cancelled" <%= "cancelled".equals(request.getParameter("status")) ? "selected" : "" %>>Đã hủy</option>
                        </select>
                    </div>

                    <div class="filter-group">
                        <label>Sắp xếp:</label>
                        <select name="sortBy" onchange="this.form.submit()">
                            <option value="orderDate" <%= "orderDate".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Ngày đặt</option>
                            <option value="totalAmount" <%= "totalAmount".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Tổng tiền</option>
                            <option value="customerName" <%= "customerName".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Tên khách hàng</option>
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

            <div class="orders-table">
                <% if (orders != null && !orders.isEmpty()) { %>
                    <table>
                        <thead>
                            <tr>
                                <th>Mã đơn</th>
                                <th>Khách hàng</th>
                                <th>Số điện thoại</th>
                                <th>Tổng tiền</th>
                                <th>Ngày đặt</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (OrderDTO order : orders) { 
                                String statusClass = "";
                                String statusText = "";
                                
                                switch(order.status) {
                                    case "pending":
                                        statusClass = "status-pending";
                                        statusText = "Chờ xác nhận";
                                        break;
                                    case "confirmed":
                                        statusClass = "status-confirmed";
                                        statusText = "Đã xác nhận";
                                        break;
                                    case "shipping":
                                        statusClass = "status-shipping";
                                        statusText = "Đang giao";
                                        break;
                                    case "completed":
                                        statusClass = "status-completed";
                                        statusText = "Hoàn thành";
                                        break;
                                    case "cancelled":
                                        statusClass = "status-cancelled";
                                        statusText = "Đã hủy";
                                        break;
                                    default:
                                        statusClass = "status-pending";
                                        statusText = order.status;
                                }
                            %>
                                <tr>
                                    <td><strong>#<%= order.id.substring(0, 8) %></strong></td>
                                    <td><%= order.customerName %></td>
                                    <td><%= order.customerPhone %></td>
                                    <td><strong style="color: #667eea;"><%= vndFormat.format(order.totalAmount) %></strong></td>
                                    <td><%= dateFormat.format(order.orderDate) %></td>
                                    <td>
                                        <span class="status-badge <%= statusClass %>">
                                            <%= statusText %>
                                        </span>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/admin/orders?action=view&id=<%= order.id %>" 
                                               class="btn btn-view">Xem</a>
                                            <a href="${pageContext.request.contextPath}/admin/orders?action=edit&id=<%= order.id %>" 
                                               class="btn btn-edit">Sửa</a>
                                            <button onclick="confirmDelete('<%= order.id %>', '<%= order.customerName %>')" 
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
                        <h3>Không có đơn hàng nào</h3>
                        <p>Chưa có đơn hàng nào trong hệ thống</p>
                    </div>
                <% } %>
            </div>
        </div>
    </div>

    <script>
        function confirmDelete(orderId, customerName) {
            if (confirm('Bạn có chắc chắn muốn xóa đơn hàng của "' + customerName + '"?')) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '${pageContext.request.contextPath}/admin/orders';
                
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'delete';
                
                const orderIdInput = document.createElement('input');
                orderIdInput.type = 'hidden';
                orderIdInput.name = 'orderId';
                orderIdInput.value = orderId;
                
                form.appendChild(actionInput);
                form.appendChild(orderIdInput);
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
</body>
</html>