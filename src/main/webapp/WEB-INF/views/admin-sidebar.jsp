<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String currentPage = request.getParameter("activePage");
    if (currentPage == null) {
        String uri = request.getRequestURI();
        if (uri.contains("users")) {
            currentPage = "users";
        } else if (uri.contains("products")) {
            currentPage = "products";
        } else if (uri.contains("dashboard")) {
            currentPage = "dashboard";
        }
    }
%>
<style>
    .sidebar {
        position: fixed;
        left: 0;
        top: 0;
        width: 260px;
        height: 100vh;
        background: linear-gradient(180deg, #2c3e50 0%, #34495e 100%);
        padding: 30px 0;
        overflow-y: auto;
        z-index: 1000;
    }

    .sidebar-logo {
        text-align: center;
        margin-bottom: 40px;
        padding: 0 20px;
    }

    .sidebar-logo h2 {
        color: white;
        font-size: 24px;
        margin-bottom: 5px;
    }

    .sidebar-logo p {
        color: #bdc3c7;
        font-size: 14px;
    }

    .sidebar-menu {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .sidebar-menu li {
        margin-bottom: 5px;
    }

    .sidebar-menu a {
        display: flex;
        align-items: center;
        padding: 15px 25px;
        color: #ecf0f1;
        text-decoration: none;
        transition: all 0.3s;
        font-weight: 500;
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

    @media (max-width: 1024px) {
        .sidebar {
            width: 0;
            padding: 0;
        }
    }
</style>

<aside class="sidebar">
    <div class="sidebar-logo">
        <h2>Fashion Store</h2>
        <p>Admin Panel</p>
    </div>

    <ul class="sidebar-menu">
        <li>
            <a href="${pageContext.request.contextPath}/admin/dashboard" 
               class="<%= "dashboard".equals(currentPage) ? "active" : "" %>">
                <span class="icon">📊</span>
                Dashboard
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/admin/users"
               class="<%= "users".equals(currentPage) ? "active" : "" %>">
                <span class="icon">👥</span>
                Quản lý người dùng
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/admin/products"
               class="<%= "products".equals(currentPage) ? "active" : "" %>">
                <span class="icon">📦</span>
                Quản lý sản phẩm
            </a>
        </li>
        <li>
            <a href="#" onclick="alert('Chức năng đang phát triển'); return false;">
                <span class="icon">🛒</span>
                Quản lý đơn hàng
            </a>
        </li>
        <li>
            <a href="#" onclick="alert('Chức năng đang phát triển'); return false;">
                <span class="icon">💰</span>
                Báo cáo doanh thu
            </a>
        </li>
        <li>
            <a href="#" onclick="alert('Chức năng đang phát triển'); return false;">
                <span class="icon">⚙️</span>
                Cài đặt
            </a>
        </li>
    </ul>
</aside>