<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="adapters.sanpham.list.ListProductViewModel" %>
<%@ page import="quanlysanpham.list.ProductViewItem" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%
    ListProductViewModel viewModel = (ListProductViewModel) request.getAttribute("viewModel");
    List<ProductViewItem> products = viewModel != null ? viewModel.products : null;
    NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý sản phẩm - Fashion Store</title>
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

        .btn-add {
            padding: 12px 24px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 15px;
            font-weight: 600;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: all 0.3s;
        }

        .btn-add:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.3);
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

        .products-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 25px;
        }

        .product-card {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .product-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 20px rgba(0,0,0,0.12);
        }

        .product-image {
            width: 100%;
            height: 200px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 60px;
            color: white;
            position: relative;
        }

        .product-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .product-badge {
            position: absolute;
            top: 10px;
            right: 10px;
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 11px;
            font-weight: 600;
            color: white;
        }

        .badge-published {
            background: #28a745;
        }

        .badge-draft {
            background: #ffc107;
        }

        .badge-archived {
            background: #6c757d;
        }

        .product-info {
            padding: 20px;
        }

        .product-name {
            font-size: 16px;
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .product-price {
            font-size: 20px;
            font-weight: 700;
            color: #667eea;
            margin-bottom: 8px;
        }

        .product-stock {
            font-size: 13px;
            color: #666;
            margin-bottom: 15px;
        }

        .product-actions {
            display: flex;
            gap: 8px;
        }

        .btn {
            flex: 1;
            padding: 8px 12px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 13px;
            font-weight: 500;
            text-decoration: none;
            display: inline-block;
            text-align: center;
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
            grid-column: 1/-1;
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
        }
    </style>
</head>
<body>
    <jsp:include page="admin-sidebar.jsp">
        <jsp:param name="activePage" value="products" />
    </jsp:include>

    <div class="main-content">
        <div class="topbar">
            <div class="topbar-left">
                <h1>Quản lý sản phẩm</h1>
            </div>
        </div>

        <div class="content-area">
            <% if (request.getParameter("added") != null) { %>
                <div class="alert alert-success">
                    ✓ Thêm sản phẩm thành công!
                </div>
            <% } %>

            <% if (request.getParameter("deleted") != null) { %>
                <div class="alert alert-success">
                    ✓ Xóa sản phẩm thành công!
                </div>
            <% } %>

            <% if (request.getParameter("error") != null) { %>
                <div class="alert alert-error">
                    ✕ <%= request.getParameter("error") %>
                </div>
            <% } %>

            <div class="page-header">
                <h2>Danh sách sản phẩm</h2>
                <a href="${pageContext.request.contextPath}/admin/products?action=add" class="btn-add">
                    ➕ Thêm sản phẩm mới
                </a>
            </div>

            <div class="filters">
                <form method="get" action="${pageContext.request.contextPath}/admin/products" style="display: flex; gap: 15px; flex-wrap: wrap; width: 100%;">
                    <div class="filter-group">
                        <label>Trạng thái:</label>
                        <select name="status" onchange="this.form.submit()">
                            <option value="all" <%= "all".equals(request.getParameter("status")) ? "selected" : "" %>>Tất cả</option>
                            <option value="PUBLISHED" <%= "PUBLISHED".equals(request.getParameter("status")) ? "selected" : "" %>>Đã xuất bản</option>
                            <option value="DRAFT" <%= "DRAFT".equals(request.getParameter("status")) ? "selected" : "" %>>Bản nháp</option>
                            <option value="ARCHIVED" <%= "ARCHIVED".equals(request.getParameter("status")) ? "selected" : "" %>>Đã lưu trữ</option>
                        </select>
                    </div>

                    <div class="filter-group">
                        <label>Sắp xếp:</label>
                        <select name="sortBy" onchange="this.form.submit()">
                            <option value="createdAt" <%= "createdAt".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Ngày tạo</option>
                            <option value="name" <%= "name".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Tên sản phẩm</option>
                            <option value="price" <%= "price".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Giá</option>
                            <option value="stock" <%= "stock".equals(request.getParameter("sortBy")) ? "selected" : "" %>>Tồn kho</option>
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

            <div class="products-grid">
                <% if (products != null && !products.isEmpty()) { 
                    for (ProductViewItem product : products) { 
                        String statusClass = "";
                        String statusText = "";
                        if ("PUBLISHED".equals(product.status)) {
                            statusClass = "badge-published";
                            statusText = "Đã xuất bản";
                        } else if ("DRAFT".equals(product.status)) {
                            statusClass = "badge-draft";
                            statusText = "Bản nháp";
                        } else {
                            statusClass = "badge-archived";
                            statusText = "Đã lưu trữ";
                        }
                %>
                    <div class="product-card">
                        <div class="product-image">
                            <span class="product-badge <%= statusClass %>"><%= statusText %></span>
                            <% if (product.defaultImage != null && !product.defaultImage.trim().isEmpty()) { %>
                                <img src="<%= product.defaultImage %>" alt="<%= product.productName %>">
                            <% } else { %>
                                👔
                            <% } %>
                        </div>
                        <div class="product-info">
                            <div class="product-name" title="<%= product.productName %>">
                                <%= product.productName %>
                            </div>
                            <div class="product-price">
                                <%= vndFormat.format(product.price) %>
                            </div>
                            <div class="product-stock">
                                📦 Tồn kho: <%= product.stockQuantity %>
                            </div>
                            <div class="product-actions">
                                <a href="${pageContext.request.contextPath}/admin/products?action=view&id=<%= product.productId %>" 
                                   class="btn btn-view">Xem</a>
                                <a href="${pageContext.request.contextPath}/admin/products?action=edit&id=<%= product.productId %>" 
                                   class="btn btn-edit">Sửa</a>
                                <button onclick="confirmDelete('<%= product.productId %>', '<%= product.productName %>')" 
                                        class="btn btn-delete">Xóa</button>
                            </div>
                        </div>
                    </div>
                <% } 
                } else { %>
                    <div class="no-data">
                        <div class="icon">📭</div>
                        <h3>Không có dữ liệu</h3>
                        <p>Chưa có sản phẩm nào trong hệ thống</p>
                    </div>
                <% } %>
            </div>
        </div>
    </div>

    <script>
        function confirmDelete(productId, productName) {
            if (confirm('Bạn có chắc chắn muốn xóa sản phẩm "' + productName + '"?')) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '${pageContext.request.contextPath}/admin/products';
                
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'delete';
                
                const productIdInput = document.createElement('input');
                productIdInput.type = 'hidden';
                productIdInput.name = 'productId';
                productIdInput.value = productId;
                
                form.appendChild(actionInput);
                form.appendChild(productIdInput);
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
</body>
</html>