<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="adapters.sanpham.add.AddProductInputDTO" %>
<%
    AddProductInputDTO formData = (AddProductInputDTO) request.getAttribute("formData");
    if (formData == null) formData = new AddProductInputDTO();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm sản phẩm - Fashion Store</title>
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

        .add-card {
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
    <jsp:include page="admin-sidebar.jsp">
        <jsp:param name="activePage" value="products" />
    </jsp:include>

    <div class="main-content">
        <div class="page-header">
            <h1>Thêm sản phẩm mới</h1>
            <a href="${pageContext.request.contextPath}/admin/products" class="btn-back">
                ← Quay lại
            </a>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                ✕ <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <div class="add-card">
            <form method="post" action="${pageContext.request.contextPath}/admin/products">
                <input type="hidden" name="action" value="add">

                <div class="form-group">
                    <label for="productName">Tên sản phẩm <span class="required">*</span></label>
                    <input type="text" id="productName" name="productName" required 
                           value="<%= formData.productName != null ? formData.productName : "" %>"
                           placeholder="Nhập tên sản phẩm">
                </div>

                <div class="form-group full-width">
                    <label for="description">Mô tả</label>
                    <textarea id="description" name="description" 
                              placeholder="Nhập mô tả sản phẩm"><%= formData.description != null ? formData.description : "" %></textarea>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="brandId">Thương hiệu</label>
                        <input type="text" id="brandId" name="brandId" 
                               value="<%= formData.brandId != null ? formData.brandId : "" %>"
                               placeholder="ID thương hiệu">
                    </div>

                    <div class="form-group">
                        <label for="categoryId">Danh mục</label>
                        <input type="text" id="categoryId" name="categoryId" 
                               value="<%= formData.categoryId != null ? formData.categoryId : "" %>"
                               placeholder="ID danh mục">
                    </div>
                </div>

                <div class="form-group">
                    <label for="defaultImage">Đường dẫn hình ảnh</label>
                    <input type="text" id="defaultImage" name="defaultImage" 
                           value="<%= formData.defaultImage != null ? formData.defaultImage : "" %>"
                           placeholder="https://example.com/image.jpg">
                    <div class="form-note">URL hình ảnh sản phẩm</div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="price">Giá gốc <span class="required">*</span></label>
                        <input type="number" id="price" name="price" required min="0" step="1000"
                               value="<%= formData.price != null ? formData.price : "" %>"
                               placeholder="500000">
                    </div>

                    <div class="form-group">
                        <label for="discountPrice">Giá khuyến mãi</label>
                        <input type="number" id="discountPrice" name="discountPrice" min="0" step="1000"
                               value="<%= formData.discountPrice != null ? formData.discountPrice : "" %>"
                               placeholder="450000">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="stockQuantity">Số lượng tồn kho <span class="required">*</span></label>
                        <input type="number" id="stockQuantity" name="stockQuantity" required min="0"
                               value="<%= formData.stockQuantity != null ? formData.stockQuantity : "0" %>"
                               placeholder="100">
                    </div>

                    <div class="form-group">
                        <label for="status">Trạng thái <span class="required">*</span></label>
                        <select id="status" name="status" required>
                            <option value="PUBLISHED" <%= "PUBLISHED".equals(formData.status) ? "selected" : "" %>>
                                Đã xuất bản
                            </option>
                            <option value="DRAFT" <%= "DRAFT".equals(formData.status) ? "selected" : "" %>>
                                Bản nháp
                            </option>
                            <option value="ARCHIVED" <%= "ARCHIVED".equals(formData.status) ? "selected" : "" %>>
                                Đã lưu trữ
                            </option>
                        </select>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-cancel">
                        Hủy
                    </a>
                    <button type="submit" class="btn btn-save">
                        💾 Lưu sản phẩm
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>