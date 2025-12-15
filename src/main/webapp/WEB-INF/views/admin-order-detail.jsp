<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="repository.DTO.OrderDTO" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // Lấy order từ request attribute (được set bởi servlet)
    OrderDTO order = (OrderDTO) request.getAttribute("order");
    
    if (order == null) {
        response.sendRedirect(request.getContextPath() + "/admin/orders?error=Không tìm thấy đơn hàng");
        return;
    }
    
    NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết đơn hàng - Fashion Store</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5f5f5; }
        .main-content { margin-left: 260px; min-height: 100vh; padding: 40px; }
        
        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }
        
        .page-header h1 { color: #2c3e50; font-size: 32px; }
        
        .btn-group { display: flex; gap: 10px; }
        
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
        
        .btn-back { background: #95a5a6; color: white; }
        .btn-back:hover { background: #7f8c8d; }
        .btn-edit { background: #f39c12; color: white; }
        .btn-edit:hover { background: #e67e22; }
        .btn-delete { background: #e74c3c; color: white; }
        .btn-delete:hover { background: #c0392b; }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #28a745;
        }
        
        .order-detail-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        
        .order-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
        }
        
        .order-id {
            font-size: 28px;
            margin-bottom: 10px;
        }
        
        .order-date {
            font-size: 16px;
            opacity: 0.9;
        }
        
        .order-body {
            padding: 40px;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 30px;
            margin-bottom: 40px;
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
        
        .section-title {
            font-size: 20px;
            font-weight: 600;
            color: #333;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #f0f0f0;
        }
        
        .order-items {
            margin-bottom: 30px;
        }
        
        .order-item {
            display: flex;
            justify-content: space-between;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 8px;
            margin-bottom: 10px;
        }
        
        .item-info { flex: 1; }
        .item-name { font-weight: 600; color: #333; margin-bottom: 5px; }
        .item-details { font-size: 14px; color: #666; }
        .item-price { font-weight: 700; color: #667eea; font-size: 18px; }
        
        .order-summary {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
        }
        
        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #e0e0e0;
        }
        
        .summary-row:last-child { border-bottom: none; }
        
        .summary-row.total {
            font-size: 20px;
            font-weight: 700;
            color: #333;
            margin-top: 10px;
            padding-top: 15px;
            border-top: 2px solid #333;
        }
        
        .summary-row.total .value { color: #667eea; }
        
        .status-badge {
            display: inline-block;
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 600;
        }
        
        .status-pending { background: #fff3cd; color: #856404; }
        .status-confirmed { background: #cfe2ff; color: #084298; }
        .status-shipping { background: #e7f3ff; color: #055160; }
        .status-completed { background: #d4edda; color: #155724; }
        .status-cancelled { background: #f8d7da; color: #721c24; }
        
        .timeline {
            margin-top: 40px;
        }
        
        .timeline-item {
            display: flex;
            gap: 20px;
            margin-bottom: 25px;
            position: relative;
        }
        
        .timeline-item:not(:last-child)::after {
            content: "";
            position: absolute;
            left: 19px;
            top: 40px;
            width: 2px;
            height: calc(100% + 5px);
            background: #e0e0e0;
        }
        
        .timeline-icon {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 18px;
            flex-shrink: 0;
        }
        
        .timeline-content {
            flex: 1;
            padding-top: 5px;
        }
        
        .timeline-title {
            font-weight: 600;
            color: #333;
            margin-bottom: 5px;
        }
        
        .timeline-time {
            font-size: 13px;
            color: #999;
        }
        
        @media (max-width: 1024px) {
            .main-content { margin-left: 0; }
            .info-grid { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
    <jsp:include page="admin-sidebar.jsp">
        <jsp:param name="activePage" value="orders" />
    </jsp:include>

    <div class="main-content">
        <div class="page-header">
            <h1>Chi tiết đơn hàng</h1>
            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-back">
                    ← Quay lại
                </a>
                <a href="${pageContext.request.contextPath}/admin/orders?action=edit&id=<%= order.id %>" 
                   class="btn btn-edit">
                    ✎ Chỉnh sửa
                </a>
                <button onclick="confirmDelete('<%= order.id %>', '<%= order.customerName %>')" 
                        class="btn btn-delete">
                    🗑 Xóa
                </button>
            </div>
        </div>

        <% if (request.getParameter("updated") != null) { %>
            <div class="alert-success">
                ✓ Cập nhật đơn hàng thành công!
            </div>
        <% } %>

        <div class="order-detail-card">
            <div class="order-header">
                <h2 class="order-id">Đơn hàng #<%= order.id.substring(0, Math.min(8, order.id.length())) %></h2>
                <p class="order-date">Đặt lúc: <%= dateFormat.format(order.orderDate) %></p>
            </div>

            <div class="order-body">
                <!-- Thông tin khách hàng -->
                <div class="section-title">👤 Thông tin khách hàng</div>
                <div class="info-grid">
                    <div class="info-item">
                        <div class="info-label">Họ và tên</div>
                        <div class="info-value"><%= order.customerName %></div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Số điện thoại</div>
                        <div class="info-value"><%= order.customerPhone %></div>
                    </div>
                    <div class="info-item full-width">
                        <div class="info-label">Địa chỉ giao hàng</div>
                        <div class="info-value"><%= order.customerAddress %></div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">User ID</div>
                        <div class="info-value"><%= order.userId %></div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Trạng thái</div>
                        <div class="info-value">
                            <% 
                            String statusClass = "status-" + order.status;
                            String statusText = "";
                            switch(order.status) {
                                case "pending": statusText = "Chờ xác nhận"; break;
                                case "confirmed": statusText = "Đã xác nhận"; break;
                                case "shipping": statusText = "Đang giao"; break;
                                case "completed": statusText = "Hoàn thành"; break;
                                case "cancelled": statusText = "Đã hủy"; break;
                                default: statusText = order.status;
                            }
                            %>
                            <span class="status-badge <%= statusClass %>"><%= statusText %></span>
                        </div>
                    </div>
                    <% if (order.note != null && !order.note.isEmpty()) { %>
                    <div class="info-item full-width">
                        <div class="info-label">Ghi chú</div>
                        <div class="info-value"><%= order.note %></div>
                    </div>
                    <% } %>
                </div>

                <!-- Tổng tiền -->
                <div class="section-title">💰 Thông tin thanh toán</div>
                <div class="order-summary">
                    <div class="summary-row total">
                        <span>Tổng cộng</span>
                        <span class="value"><%= vndFormat.format(order.totalAmount) %></span>
                    </div>
                </div>

                <!-- Timeline -->
                <div class="timeline">
                    <div class="section-title">📋 Lịch sử đơn hàng</div>
                    
                    <div class="timeline-item">
                        <div class="timeline-icon">✓</div>
                        <div class="timeline-content">
                            <div class="timeline-title">Đơn hàng đã được tạo</div>
                            <div class="timeline-time"><%= dateFormat.format(order.orderDate) %></div>
                        </div>
                    </div>
                    
                    <% if ("pending".equals(order.status)) { %>
                        <div class="timeline-item">
                            <div class="timeline-icon" style="background: #95a5a6;">⏳</div>
                            <div class="timeline-content">
                                <div class="timeline-title">Chờ xác nhận</div>
                                <div class="timeline-time">Đang chờ xử lý...</div>
                            </div>
                        </div>
                    <% } else if ("confirmed".equals(order.status)) { %>
                        <div class="timeline-item">
                            <div class="timeline-icon">✓</div>
                            <div class="timeline-content">
                                <div class="timeline-title">Đã xác nhận</div>
                                <div class="timeline-time">Đơn hàng đã được xác nhận</div>
                            </div>
                        </div>
                    <% } else if ("shipping".equals(order.status)) { %>
                        <div class="timeline-item">
                            <div class="timeline-icon">✓</div>
                            <div class="timeline-content">
                                <div class="timeline-title">Đã xác nhận</div>
                            </div>
                        </div>
                        <div class="timeline-item">
                            <div class="timeline-icon">🚚</div>
                            <div class="timeline-content">
                                <div class="timeline-title">Đang giao hàng</div>
                                <div class="timeline-time">Đơn hàng đang được vận chuyển</div>
                            </div>
                        </div>
                    <% } else if ("completed".equals(order.status)) { %>
                        <div class="timeline-item">
                            <div class="timeline-icon">✓</div>
                            <div class="timeline-content">
                                <div class="timeline-title">Đã xác nhận</div>
                            </div>
                        </div>
                        <div class="timeline-item">
                            <div class="timeline-icon">✓</div>
                            <div class="timeline-content">
                                <div class="timeline-title">Đã giao hàng</div>
                            </div>
                        </div>
                        <div class="timeline-item">
                            <div class="timeline-icon" style="background: #28a745;">✓</div>
                            <div class="timeline-content">
                                <div class="timeline-title">Hoàn thành</div>
                                <div class="timeline-time">Đơn hàng đã hoàn thành</div>
                            </div>
                        </div>
                    <% } else if ("cancelled".equals(order.status)) { %>
                        <div class="timeline-item">
                            <div class="timeline-icon" style="background: #e74c3c;">✕</div>
                            <div class="timeline-content">
                                <div class="timeline-title">Đã hủy</div>
                                <div class="timeline-time">Đơn hàng đã bị hủy</div>
                            </div>
                        </div>
                    <% } %>
                </div>
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