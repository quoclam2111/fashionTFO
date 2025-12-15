<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String fullName = (String) session.getAttribute("fullName");
    if (fullName == null || !"CUSTOMER".equals(session.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng - Fashion Store</title>
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

        .navbar {
            background: white;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 15px 0;
            position: sticky;
            top: 0;
            z-index: 100;
        }

        .navbar-content {
            max-width: 1400px;
            margin: 0 auto;
            padding: 0 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            font-size: 28px;
            font-weight: 700;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            cursor: pointer;
        }

        .nav-menu {
            display: flex;
            gap: 30px;
            list-style: none;
            align-items: center;
        }

        .nav-menu a {
            text-decoration: none;
            color: #333;
            font-weight: 500;
            transition: color 0.3s;
        }

        .nav-menu a:hover {
            color: #667eea;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: 600;
        }

        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 30px;
        }

        .page-header {
            margin-bottom: 40px;
        }

        .page-header h1 {
            font-size: 36px;
            color: #333;
            margin-bottom: 10px;
        }

        .breadcrumb {
            color: #999;
            font-size: 14px;
        }

        .breadcrumb a {
            color: #667eea;
            text-decoration: none;
        }

        .cart-layout {
            display: grid;
            grid-template-columns: 1fr 400px;
            gap: 30px;
        }

        .cart-items {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        .cart-item {
            display: grid;
            grid-template-columns: 120px 1fr auto;
            gap: 20px;
            padding: 20px;
            border-bottom: 1px solid #f0f0f0;
            transition: background 0.3s;
        }

        .cart-item:hover {
            background: #f9f9f9;
        }

        .cart-item:last-child {
            border-bottom: none;
        }

        .item-image {
            width: 120px;
            height: 120px;
            border-radius: 10px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 48px;
            color: white;
            overflow: hidden;
        }

        .item-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .item-details {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }

        .item-name {
            font-size: 18px;
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
        }

        .item-variant {
            color: #999;
            font-size: 14px;
            margin-bottom: 15px;
        }

        .quantity-control {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .quantity-btn {
            width: 32px;
            height: 32px;
            border: 2px solid #e0e0e0;
            background: white;
            border-radius: 6px;
            cursor: pointer;
            font-size: 18px;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 0.3s;
        }

        .quantity-btn:hover {
            border-color: #667eea;
            color: #667eea;
        }

        .quantity-input {
            width: 50px;
            text-align: center;
            border: 2px solid #e0e0e0;
            border-radius: 6px;
            padding: 6px;
            font-size: 14px;
        }

        .item-actions {
            display: flex;
            flex-direction: column;
            align-items: flex-end;
            justify-content: space-between;
        }

        .item-price {
            font-size: 22px;
            font-weight: 700;
            color: #667eea;
        }

        .btn-remove {
            background: #fee;
            color: #e74c3c;
            border: none;
            padding: 8px 16px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.3s;
        }

        .btn-remove:hover {
            background: #e74c3c;
            color: white;
        }

        .cart-summary {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            height: fit-content;
            position: sticky;
            top: 100px;
        }

        .summary-title {
            font-size: 20px;
            font-weight: 600;
            color: #333;
            margin-bottom: 25px;
            padding-bottom: 15px;
            border-bottom: 2px solid #f0f0f0;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
            color: #666;
        }

        .summary-row.total {
            font-size: 20px;
            font-weight: 700;
            color: #333;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 2px solid #f0f0f0;
        }

        .summary-row.total .value {
            color: #667eea;
        }

        .btn-checkout {
            width: 100%;
            padding: 16px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            margin-top: 20px;
            transition: all 0.3s;
        }

        .btn-checkout:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
        }

        .btn-continue {
            width: 100%;
            padding: 14px;
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
            border-radius: 10px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            margin-top: 15px;
            transition: all 0.3s;
        }

        .btn-continue:hover {
            background: #f8f9ff;
        }

        .empty-cart {
            text-align: center;
            padding: 80px 20px;
            background: white;
            border-radius: 15px;
        }

        .empty-cart-icon {
            font-size: 80px;
            margin-bottom: 20px;
        }

        .empty-cart h2 {
            font-size: 28px;
            color: #333;
            margin-bottom: 15px;
        }

        .empty-cart p {
            color: #999;
            font-size: 16px;
            margin-bottom: 30px;
        }

        .voucher-section {
            margin-top: 25px;
            padding-top: 20px;
            border-top: 1px solid #f0f0f0;
        }

        .voucher-input {
            display: flex;
            gap: 10px;
            margin-top: 15px;
        }

        .voucher-input input {
            flex: 1;
            padding: 12px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 14px;
        }

        .voucher-input button {
            padding: 12px 20px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s;
        }

        .voucher-input button:hover {
            background: #5568d3;
        }

        @media (max-width: 1024px) {
            .cart-layout {
                grid-template-columns: 1fr;
            }

            .cart-summary {
                position: static;
            }

            .cart-item {
                grid-template-columns: 100px 1fr;
            }

            .item-actions {
                grid-column: 1 / -1;
                flex-direction: row;
                justify-content: space-between;
                margin-top: 15px;
            }
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-content">
            <div class="logo" onclick="location.href='${pageContext.request.contextPath}/home'">
                Fashion Store
            </div>

            <ul class="nav-menu">
                <li><a href="${pageContext.request.contextPath}/home">🛍️ Sản phẩm</a></li>
                <li><a href="${pageContext.request.contextPath}/cart" style="color: #667eea;">🛒 Giỏ hàng</a></li>
                <li><a href="${pageContext.request.contextPath}/customer/dashboard">👤 Tài khoản</a></li>
            </ul>

            <div class="user-info">
                <div class="avatar"><%= fullName.substring(0, 1).toUpperCase() %></div>
                <span><%= fullName %></span>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="page-header">
            <h1>🛒 Giỏ hàng của bạn</h1>
            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/home">Trang chủ</a> / Giỏ hàng
            </div>
        </div>

        <div id="cartContent"></div>
    </div>

    <script>
        let cartItems = [];

        function loadCart() {
            const saved = localStorage.getItem('cart');
            if (saved) {
                cartItems = JSON.parse(saved);
            }
            renderCart();
        }

        function saveCart() {
            localStorage.setItem('cart', JSON.stringify(cartItems));
        }

        function updateQuantity(index, delta) {
            if (cartItems[index].quantity + delta > 0) {
                cartItems[index].quantity += delta;
                saveCart();
                renderCart();
            }
        }

        function setQuantity(index, value) {
            const quantity = parseInt(value);
            if (quantity > 0) {
                cartItems[index].quantity = quantity;
                saveCart();
                renderCart();
            }
        }

        function removeItem(index) {
            if (confirm('Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?')) {
                cartItems.splice(index, 1);
                saveCart();
                renderCart();
            }
        }

        function calculateTotal() {
            return cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
        }

        function formatVND(amount) {
            return new Intl.NumberFormat('vi-VN', {
                style: 'currency',
                currency: 'VND'
            }).format(amount);
        }

        function renderCart() {
            const container = document.getElementById('cartContent');
            const contextPath = '<%= request.getContextPath() %>';
            
            if (cartItems.length === 0) {
                container.innerHTML = 
                    '<div class="empty-cart">' +
                        '<div class="empty-cart-icon">🛒</div>' +
                        '<h2>Giỏ hàng trống</h2>' +
                        '<p>Bạn chưa có sản phẩm nào trong giỏ hàng</p>' +
                        '<button class="btn-checkout" onclick="location.href=\'' + contextPath + '/home\'">Tiếp tục mua sắm</button>' +
                    '</div>';
                return;
            }

            const subtotal = calculateTotal();
            const shipping = subtotal > 500000 ? 0 : 30000;
            const total = subtotal + shipping;

            let itemsHtml = '';
            for (let i = 0; i < cartItems.length; i++) {
                const item = cartItems[i];
                itemsHtml += 
                    '<div class="cart-item">' +
                        '<div class="item-image">👔</div>' +
                        '<div class="item-details">' +
                            '<div class="item-name">' + item.name + '</div>' +
                            '<div class="item-variant">Kích thước: M | Màu: Xanh</div>' +
                            '<div class="quantity-control">' +
                                '<button class="quantity-btn" onclick="updateQuantity(' + i + ', -1)">-</button>' +
                                '<input type="number" class="quantity-input" value="' + item.quantity + '" min="1" onchange="setQuantity(' + i + ', this.value)">' +
                                '<button class="quantity-btn" onclick="updateQuantity(' + i + ', 1)">+</button>' +
                            '</div>' +
                        '</div>' +
                        '<div class="item-actions">' +
                            '<div class="item-price">' + formatVND(item.price * item.quantity) + '</div>' +
                            '<button class="btn-remove" onclick="removeItem(' + i + ')">🗑️ Xóa</button>' +
                        '</div>' +
                    '</div>';
            }

            let freeShipMsg = '';
            if (shipping !== 0) {
                freeShipMsg = '<div style="color: #28a745; font-size: 13px; margin-top: 10px;">💚 Mua thêm ' + formatVND(500000 - subtotal) + ' để được miễn phí vận chuyển</div>';
            }

            container.innerHTML = 
                '<div class="cart-layout">' +
                    '<div class="cart-items">' + itemsHtml + '</div>' +
                    '<div class="cart-summary">' +
                        '<div class="summary-title">Tổng đơn hàng</div>' +
                        '<div class="summary-row">' +
                            '<span>Tạm tính (' + cartItems.length + ' sản phẩm)</span>' +
                            '<span class="value">' + formatVND(subtotal) + '</span>' +
                        '</div>' +
                        '<div class="summary-row">' +
                            '<span>Phí vận chuyển</span>' +
                            '<span class="value">' + (shipping === 0 ? 'Miễn phí' : formatVND(shipping)) + '</span>' +
                        '</div>' +
                        freeShipMsg +
                        '<div class="voucher-section">' +
                            '<div style="font-weight: 600; color: #333; margin-bottom: 5px;">Mã giảm giá</div>' +
                            '<div class="voucher-input">' +
                                '<input type="text" placeholder="Nhập mã giảm giá" id="voucherCode">' +
                                '<button onclick="applyVoucher()">Áp dụng</button>' +
                            '</div>' +
                        '</div>' +
                        '<div class="summary-row total">' +
                            '<span>Tổng cộng</span>' +
                            '<span class="value">' + formatVND(total) + '</span>' +
                        '</div>' +
                        '<button class="btn-checkout" onclick="proceedToCheckout()">Tiến hành thanh toán</button>' +
                        '<button class="btn-continue" onclick="location.href=\'' + contextPath + '/home\'">Tiếp tục mua sắm</button>' +
                    '</div>' +
                '</div>';
        }

        function applyVoucher() {
            const code = document.getElementById('voucherCode').value.trim().toUpperCase();
            if (code === '') {
                alert('Vui lòng nhập mã giảm giá');
                return;
            }

            // Demo voucher codes
            const vouchers = {
                'WELCOME10': 10,
                'FASHION20': 20,
                'VIP30': 30
            };

            if (vouchers[code]) {
                alert(`✓ Áp dụng mã giảm ${vouchers[code]}% thành công!`);
                // TODO: Apply discount logic
            } else {
                alert('❌ Mã giảm giá không hợp lệ');
            }
        }

        function proceedToCheckout() {
            localStorage.setItem('checkoutCart', JSON.stringify(cartItems));
            location.href = '${pageContext.request.contextPath}/checkout';
        }

        // Load cart on page load
        loadCart();
    </script>
</body>
</html>