<!-- admin-product-detail.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="adapters.sanpham.get.GetProductViewModel" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    GetProductViewModel viewModel = (GetProductViewModel) request.getAttribute("viewModel");
    GetProductViewModel.ProductViewData product = viewModel != null ? viewModel.product : null;
    NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết sản phẩm - Fashion Store</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5f5f5; }
        .main-content { margin-left: 260px; min-height: 100vh; padding: 40px; }
        .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
        .page-header h1 { color: #2c3e50; font-size: 32px; }
        .btn-group { display: flex; gap: 10px; }
        .btn { padding: 10px 20px; border: none; border-radius: 8px; cursor: pointer; font-size: 14px; font-weight: 600; text-decoration: none; display: inline-block; transition: all 0.3s; }
        .btn-back { background: #95a5a6; color: white; }
        .btn-back:hover { background: #7f8c8d; }
        .btn-edit { background: #f39c12; color: white; }
        .btn-edit:hover { background: #e67e22; }
        .btn-delete { background: #e74c3c; color: white; }
        .btn-delete:hover { background: #c0392b; }
        .alert-success { background: #d4edda; color: #155724; padding: 15px 20px; border-radius: 8px; margin-bottom: 20px; border-left: 4px solid #28a745; }
        .product-detail-card { background: white; border-radius: 15px; box-shadow: 0 5px 20px rgba(0,0,0,0.1); overflow: hidden; }
        .product-header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 40px; text-align: center; }
        .product-image-large { width: 200px; height: 200px; margin: 0 auto 20px; border-radius: 50%; background: white; display: flex; align-items: center; justify-content: center; font-size: 80px; overflow: hidden; }
        .product-image-large img { width: 100%; height: 100%; object-fit: cover; }
        .product-name-large { font-size: 32px; margin-bottom: 10px; }
        .product-slug { font-size: 16px; opacity: 0.9; }
        .product-body { padding: 40px; }
        .info-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 30px; }
        .info-item { padding: 20px; background: #f8f9fa; border-radius: 10px; }
        .info-label { color: #999; font-size: 14px; margin-bottom: 8px; font-weight: 500; }
        .info-value { color: #333; font-size: 18px; font-weight: 600; }
        .info-item.full-width { grid-column: 1 / -1; }
        .status-badge { display: inline-block; padding: 6px 16px; border-radius: 20px; font-size: 14px; font-weight: 600; }
        .status-published { background: #d4edda; color: #155724; }
        .status-draft { background: #fff3cd; color: #856404; }
        .status-archived { background: #d6d8db; color: #383d41; }
        @media (max-width: 1024px) { .main-content { margin-left: 0; } .info-grid { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
    <jsp:include page="admin-sidebar.jsp"><jsp:param name="activePage" value="products" /></jsp:include>
    <div class="main-content">
        <div class="page-header">
            <h1>Chi tiết sản phẩm</h1>
            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-back">← Quay lại</a>
                <% if (product != null) { %>
                    <a href="${pageContext.request.contextPath}/admin/products?action=edit&id=<%= product.productId %>" class="btn btn-edit">✎ Chỉnh sửa</a>
                    <button onclick="confirmDelete('<%= product.productId %>', '<%= product.productName %>')" class="btn btn-delete">🗑 Xóa</button>
                <% } %>
            </div>
        </div>
        <% if (request.getParameter("success") != null) { %>
            <div class="alert-success">✓ Cập nhật sản phẩm thành công!</div>
        <% } %>
        <% if (product != null) { %>
            <div class="product-detail-card">
                <div class="product-header">
                    <div class="product-image-large">
                        <% if (product.defaultImage != null && !product.defaultImage.trim().isEmpty()) { %>
                            <img src="<%= product.defaultImage %>" alt="<%= product.productName %>">
                        <% } else { %>👔<% } %>
                    </div>
                    <h2 class="product-name-large"><%= product.productName %></h2>
                    <p class="product-slug"><%= product.slug %></p>
                </div>
                <div class="product-body">
                    <div class="info-grid">
                        <div class="info-item">
                            <div class="info-label">💰 Giá gốc</div>
                            <div class="info-value"><%= product.price != null && !product.price.isEmpty() ? vndFormat.format(new java.math.BigDecimal(product.price)) : "N/A" %></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">🏷️ Giá khuyến mãi</div>
                            <div class="info-value"><%= product.discountPrice != null && !product.discountPrice.isEmpty() ? vndFormat.format(new java.math.BigDecimal(product.discountPrice)) : "Không có" %></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">📦 Tồn kho</div>
                            <div class="info-value"><%= product.stockQuantity != null ? product.stockQuantity : 0 %> sản phẩm</div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">📊 Trạng thái</div>
                            <div class="info-value">
                                <% String statusClass = "PUBLISHED".equals(product.status) ? "status-published" : "DRAFT".equals(product.status) ? "status-draft" : "status-archived"; 
                                   String statusText = "PUBLISHED".equals(product.status) ? "Đã xuất bản" : "DRAFT".equals(product.status) ? "Bản nháp" : "Đã lưu trữ"; %>
                                <span class="status-badge <%= statusClass %>"><%= statusText %></span>
                            </div>
                        </div>
                        <% if (product.description != null && !product.description.trim().isEmpty()) { %>
                        <div class="info-item full-width">
                            <div class="info-label">📝 Mô tả</div>
                            <div class="info-value"><%= product.description %></div>
                        </div>
                        <% } %>
                        <div class="info-item">
                            <div class="info-label">🏢 Thương hiệu</div>
                            <div class="info-value"><%= product.brandId != null ? product.brandId : "Chưa có" %></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">📂 Danh mục</div>
                            <div class="info-value"><%= product.categoryId != null ? product.categoryId : "Chưa có" %></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">📅 Ngày tạo</div>
                            <div class="info-value"><%= product.createdAt != null ? product.createdAt : "N/A" %></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">🔄 Cập nhật lần cuối</div>
                            <div class="info-value"><%= product.updatedAt != null ? product.updatedAt : "N/A" %></div>
                        </div>
                    </div>
                </div>
            </div>
        <% } else { %>
            <div class="product-detail-card"><div class="product-body"><p style="text-align: center; color: #999; padding: 40px;">Không tìm thấy thông tin sản phẩm</p></div></div>
        <% } %>
    </div>
    <script>
        function confirmDelete(productId, productName) {
            if (confirm('Bạn có chắc chắn muốn xóa sản phẩm "' + productName + '"?')) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '${pageContext.request.contextPath}/admin/products';
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden'; actionInput.name = 'action'; actionInput.value = 'delete';
                const productIdInput = document.createElement('input');
                productIdInput.type = 'hidden'; productIdInput.name = 'productId'; productIdInput.value = productId;
                form.appendChild(actionInput); form.appendChild(productIdInput);
                document.body.appendChild(form); form.submit();
            }
        }
    </script>
</body>
</html>