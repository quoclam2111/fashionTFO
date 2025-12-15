<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="repository.DTO.OrderDTO" %>
<%
    // Lấy order từ request attribute (được set bởi servlet)
    OrderDTO order = (OrderDTO) request.getAttribute("order");
    
    if (order == null) {
        response.sendRedirect(request.getContextPath() + "/admin/orders?error=Không tìm thấy đơn hàng");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa đơn hàng - Fashion Store</title>
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
        
        .btn-back:hover { background: #7f8c8d; }
        
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
        
        .form-group label .required {
            color: #e74c3c;
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
            text-decoration: none;
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
        
        .section-title {
            font-size: 18px;
            font-weight: 600;
            color: #333;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #f0f0f0;
        }
        
        @media (max-width: 1024px) {
            .main-content { margin-left: 0; }
            .form-row { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
    <jsp:include page="admin-sidebar.jsp">
        <jsp:param name="activePage" value="orders" />
    </jsp:include>

    <div class="main-content">
        <div class="page-header">
            <h1>Chỉnh sửa đơn hàng</h1>
            <a href="${pageContext.request.contextPath}/admin/orders?action=view&id=<%= order.id %>" 
               class="btn-back">
                ← Quay lại
            </a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                ✕ <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <div class="edit-card">
            <form method="post" action="${pageContext.request.contextPath}/admin/orders">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="orderId" value="<%= order.id %>">

                <div class="section-title">📋 Thông tin đơn hàng</div>

                <div class="form-group">
                    <label>Mã đơn hàng</label>
                    <input type="text" value="#<%= order.id.substring(0, Math.min(8, order.id.length())) %>" readonly>
                    <div class="form-note">Mã đơn hàng không thể thay đổi</div>
                </div>

                <div class="form-group">
                    <label for="userId">User ID <span class="required">*</span></label>
                    <input type="text" id="userId" name="userId" 
                           value="<%= order.userId %>" required>
                </div>

                <div class="section-title">👤 Thông tin khách hàng</div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="customerName">Họ và tên <span class="required">*</span></label>
                        <input type="text" id="customerName" name="customerName" 
                               value="<%= order.customerName %>" required
                               placeholder="Nhập họ tên khách hàng">
                    </div>

                    <div class="form-group">
                        <label for="customerPhone">Số điện thoại <span class="required">*</span></label>
                        <input type="tel" id="customerPhone" name="customerPhone" 
                               value="<%= order.customerPhone %>" required
                               pattern="0[0-9]{9,10}"
                               placeholder="0912345678">
                    </div>
                </div>

                <div class="form-group full-width">
                    <label for="customerAddress">Địa chỉ giao hàng <span class="required">*</span></label>
                    <textarea id="customerAddress" name="customerAddress" required
                               placeholder="Nhập địa chỉ giao hàng"><%= order.customerAddress %></textarea>
                </div>

                <div class="section-title">💰 Thông tin thanh toán</div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="totalAmount">Tổng tiền <span class="required">*</span></label>
                        <input type="number" id="totalAmount" name="totalAmount" 
                               value="<%= order.totalAmount %>" required min="0" step="1000"
                               placeholder="1500000">
                    </div>

                    <div class="form-group">
                        <label for="status">Trạng thái <span class="required">*</span></label>
                        <select id="status" name="status" required>
                            <option value="pending" <%= "pending".equals(order.status) ? "selected" : "" %>>
                                Chờ xác nhận
                            </option>
                            <option value="confirmed" <%= "confirmed".equals(order.status) ? "selected" : "" %>>
                                Đã xác nhận
                            </option>
                            <option value="shipping" <%= "shipping".equals(order.status) ? "selected" : "" %>>
                                Đang giao
                            </option>
                            <option value="completed" <%= "completed".equals(order.status) ? "selected" : "" %>>
                                Hoàn thành
                            </option>
                            <option value="cancelled" <%= "cancelled".equals(order.status) ? "selected" : "" %>>
                                Đã hủy
                            </option>
                        </select>
                    </div>
                </div>

                <div class="form-group full-width">
                    <label for="note">Ghi chú đơn hàng</label>
                    <textarea id="note" name="note" 
                              placeholder="Ghi chú về đơn hàng (tùy chọn)"><%= order.note != null ? order.note : "" %></textarea>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/admin/orders?action=view&id=<%= order.id %>" 
                       class="btn btn-cancel">
                        Hủy
                    </a>
                    <button type="submit" class="btn btn-save">
                        💾 Lưu thay đổi
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>