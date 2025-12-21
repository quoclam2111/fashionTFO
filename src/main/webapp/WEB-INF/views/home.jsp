<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="quanlysanpham.list.ProductViewItem" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util. Locale" %>
<%
    // Kiểm tra user đã login chưa
    String fullName = (String) session.getAttribute("fullName");
    boolean isLoggedIn = (fullName != null);
    
    // Lấy danh sách sản phẩm từ request
    List<ProductViewItem> products = (List<ProductViewItem>) request.getAttribute("products");
    String errorMessage = (String) request.getAttribute("errorMessage");
    
    // Format tiền VND
    NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fashion Store - Cửa hàng thời trang</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background:  #f5f5f5;
        }

        /* Navbar */
        .navbar {
            background: white;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 15px 0;
            position: sticky;
            top: 0;
            z-index:  100;
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
            font-weight:  700;
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
            display: flex;
            align-items: center;
            gap: 8px;
        }

        . nav-menu a:hover {
            color: #667eea;
        }

        .cart-badge {
            background: #e74c3c;
            color:  white;
            border-radius: 50%;
            padding: 2px 8px;
            font-size:  12px;
            font-weight: 600;
        }

        .auth-buttons {
            display: flex;
            gap: 15px;
            align-items: center;
        }

        .btn {
            padding: 10px 24px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.3s;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }

        .btn-login {
            background: transparent;
            color: #667eea;
            border: 2px solid #667eea;
        }

        .btn-login:hover {
            background: #667eea;
            color:  white;
        }

        . btn-register {
            background:  linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-register:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.3);
        }

        .btn-logout {
            background: #e74c3c;
            color: white;
        }

        .btn-logout:hover {
            background: #c0392b;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 8px 15px;
            background: #f8f9fa;
            border-radius: 10px;
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

        /* Hero Section */
        .hero {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 80px 30px;
            text-align:  center;
        }

        . hero h1 {
            font-size: 56px;
            margin-bottom:  20px;
            animation: fadeInDown 1s;
        }

        .hero p {
            font-size: 20px;
            opacity: 0.9;
            margin-bottom: 40px;
            animation: fadeInUp 1s;
        }

        . hero . btn {
            font-size: 16px;
            padding: 14px 32px;
            animation: fadeIn 1. 5s;
        }

        /* Container */
        .container {
            max-width: 1400px;
            margin: 0 auto;
            padding: 60px 30px;
        }

        . section-title {
            text-align: center;
            margin-bottom: 50px;
        }

        .section-title h2 {
            font-size: 42px;
            color: #333;
            margin-bottom: 15px;
        }

        . section-title p {
            font-size: 18px;
            color: #666;
        }

        /* Products Grid */
        .products-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 30px;
            margin-bottom: 60px;
        }

        . product-card {
            background:  white;
            border-radius:  15px;
            overflow: hidden;
            box-shadow: 0 5px 20px rgba(0,0,0,0.1);
            transition: transform 0.3s, box-shadow 0.3s;
            cursor: pointer;
        }

        .product-card:hover {
            transform: translateY(-10px);
            box-shadow:  0 10px 30px rgba(0,0,0,0.15);
        }

        .product-image {
            width: 100%;
            height: 300px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 80px;
            position: relative;
            overflow: hidden;
        }

        .product-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .product-badge {
            position: absolute;
            top: 15px;
            right: 15px;
            background: #e74c3c;
            color: white;
            padding: 6px 12px;
            border-radius: 20px;
            font-size:  12px;
            font-weight: 600;
        }

        .product-badge.sale {
            background: #e74c3c;
        }

        .product-badge. new {
            background: #2ecc71;
        }

        .product-info {
            padding: 25px;
        }

        . product-category {
            color: #999;
            font-size: 13px;
            text-transform: uppercase;
            letter-spacing: 1px;
            margin-bottom: 8px;
        }

        . product-name {
            font-size: 20px;
            font-weight:  600;
            color: #333;
            margin-bottom: 10px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .product-price {
            font-size: 24px;
            font-weight:  700;
            color: #667eea;
            margin-bottom: 15px;
        }

        .product-price del {
            font-size: 16px;
            color: #999;
            margin-left: 10px;
        }

        .product-actions {
            display: flex;
            gap: 10px;
        }

        .btn-add-cart {
            flex: 1;
            background: #667eea;
            color: white;
            border:  none;
            padding: 12px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition:  all 0.3s;
        }

        .btn-add-cart:hover {
            background:  #5568d3;
        }

        .btn-view {
            width: 45px;
            height: 45px;
            background: #f8f9fa;
            border:  none;
            border-radius: 8px;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 0.3s;
        }

        .btn-view:hover {
            background: #667eea;
            color: white;
        }

        /* Error Message */
        .error-message {
            background: #fee;
            color: #c33;
            padding: 20px;
            border-radius: 10px;
            text-align:  center;
            margin:  20px 0;
        }

        . empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }

        .empty-state h3 {
            font-size: 24px;
            margin-bottom:  10px;
        }

        /* Footer */
        .footer {
            background: #2c3e50;
            color: white;
            padding: 60px 30px 30px;
        }

        . footer-content {
            max-width: 1400px;
            margin: 0 auto;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 40px;
            margin-bottom: 40px;
        }

        .footer-section h3 {
            margin-bottom: 20px;
            font-size: 20px;
        }

        .footer-section ul {
            list-style: none;
        }

        .footer-section ul li {
            margin-bottom: 12px;
        }

        . footer-section a {
            color: #bdc3c7;
            text-decoration: none;
            transition: color 0.3s;
        }

        .footer-section a:hover {
            color: white;
        }

        .footer-bottom {
            text-align: center;
            padding-top: 30px;
            border-top:  1px solid #34495e;
            color: #bdc3c7;
        }

        /* Animations */
        @keyframes fadeInDown {
            from {
                opacity: 0;
                transform: translateY(-30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes fadeInUp {
            from {
                opacity:  0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
            }
            to {
                opacity: 1;
            }
        }

        /* Responsive */
        @media (max-width: 768px) {
            .nav-menu {
                display: none;
            }

            .hero h1 {
                font-size: 36px;
            }

            .products-grid {
                grid-template-columns: 1fr;
            }

            .auth-buttons {
                gap: 8px;
            }

            . btn {
                padding: 8px 16px;
                font-size: 13px;
            }
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar">
        <div class="navbar-content">
            <div class="logo" onclick="location.href='${pageContext.request.contextPath}/home'">
                Fashion Store
            </div>

            <ul class="nav-menu">
                <li><a href="#products">🛍️ Sản phẩm</a></li>
                <li><a href="#categories">📂 Danh mục</a></li>
                <li>
                    <a href="javascript:void(0)" onclick="handleCart()">
                        🛒 Giỏ hàng 
                        <span class="cart-badge" id="cartCount">0</span>
                    </a>
                </li>
                <% if (isLoggedIn) { %>
                    <li>
                        <a href="${pageContext.request.contextPath}/customer/dashboard">
                            👤 Tài khoản
                        </a>
                    </li>
                <% } %>
            </ul>

            <div class="auth-buttons">
                <% if (isLoggedIn) { %>
                    <div class="user-info">
                        <div class="avatar"><%= fullName. substring(0, 1).toUpperCase() %></div>
                        <span><%= fullName %></span>
                    </div>
                    <form method="post" action="${pageContext.request. contextPath}/logout" style="display: inline;">
                        <button type="submit" class="btn btn-logout">Đăng xuất</button>
                    </form>
                <% } else { %>
                    <a href="${pageContext.request. contextPath}/login" class="btn btn-login">
                        🔐 Đăng nhập
                    </a>
                    <a href="${pageContext. request.contextPath}/register" class="btn btn-register">
                        ✨ Đăng ký
                    </a>
                <% } %>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero">
        <h1>Bộ Sưu Tập Thời Trang 2025</h1>
        <p>Khám phá xu hướng mới nhất - Phong cách đẳng cấp - Giá cả hợp lý</p>
        <a href="#products" class="btn btn-register">Mua sắm ngay</a>
    </section>

    <!-- Main Content -->
    <div class="container">
        <!-- Products Section -->
        <section id="products">
            <div class="section-title">
                <h2>Sản Phẩm Nổi Bật</h2>
                <p>Những sản phẩm được yêu thích nhất</p>
            </div>

            <% if (errorMessage != null) { %>
                <div class="error-message">
                    ⚠️ <%= errorMessage %>
                </div>
            <% } %>

            <div class="products-grid">
                <% 
                if (products != null && ! products.isEmpty()) {
                    for (ProductViewItem product :  products) {
                        // Tính % giảm giá
                        int discountPercent = 0;
                        if (product.discountPrice != null && product.price != null 
                            && product.discountPrice. compareTo(BigDecimal.ZERO) > 0
                            && product.discountPrice.compareTo(product. price) < 0) {
                            
                            BigDecimal discount = product.price.subtract(product.discountPrice);
                            discountPercent = discount.multiply(new BigDecimal(100))
                                                     .divide(product.price, 0, BigDecimal.ROUND_HALF_UP)
                                                     .intValue();
                        }
                        
                        // Hiển thị giá hiện tại (ưu tiên giá sale)
                        BigDecimal currentPrice = (product.discountPrice != null 
                                                   && product.discountPrice.compareTo(BigDecimal.ZERO) > 0) 
                                                   ? product.discountPrice 
                                                   : product. price;
                %>
                    <div class="product-card">
                        <div class="product-image">
                            <% if (discountPercent > 0) { %>
                                <div class="product-badge sale">-<%= discountPercent %>%</div>
                            <% } %>
                            
                            <% if (product.defaultImage != null && ! product.defaultImage.trim().isEmpty()) { %>
                                <img src="<%= product. defaultImage %>" alt="<%= product.productName %>">
                            <% } else { %>
                                👔
                            <% } %>
                        </div>
                        
                        <div class="product-info">
                            <div class="product-category"><%= product.categoryId != null ? product.categoryId : "Thời trang" %></div>
                            <div class="product-name" title="<%= product.productName %>">
                                <%= product.productName %>
                            </div>
                            <div class="product-price">
                                <%= vndFormat.format(currentPrice) %>
                                <% if (discountPercent > 0) { %>
                                    <del><%= vndFormat.format(product.price) %></del>
                                <% } %>
                            </div>
                            <div class="product-actions">
                                <button class="btn-add-cart" 
                                        onclick="addToCart({
                                            id: '<%= product.productId %>',
                                            name: '<%= product.productName. replace("'", "\\'") %>',
                                            price: <%= currentPrice %>,
                                            image: '<%= product.defaultImage != null ? product.defaultImage : "" %>'
                                        })">
                                    Thêm vào giỏ
                                </button>
                                <button class="btn-view" 
                                        onclick="viewProduct('<%= product.productId %>')">👁️</button>
                            </div>
                        </div>
                    </div>
                <% 
                    }
                } else { 
                %>
                    <div class="empty-state" style="grid-column: 1/-1;">
                        <h3>📦 Chưa có sản phẩm nào</h3>
                        <p>Vui lòng quay lại sau! </p>
                    </div>
                <% } %>
            </div>
        </section>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-content">
            <div class="footer-section">
                <h3>Fashion Store</h3>
                <p>Cửa hàng thời trang hàng đầu Việt Nam.  Chất lượng - Uy tín - Phong cách.</p>
            </div>

            <div class="footer-section">
                <h3>Liên Kết</h3>
                <ul>
                    <li><a href="#products">Sản phẩm</a></li>
                    <li><a href="#categories">Danh mục</a></li>
                    <li><a href="${pageContext.request.contextPath}/login">Đăng nhập</a></li>
                    <li><a href="${pageContext.request.contextPath}/register">Đăng ký</a></li>
                </ul>
            </div>

            <div class="footer-section">
                <h3>Chính Sách</h3>
                <ul>
                    <li><a href="#">Chính sách đổi trả</a></li>
                    <li><a href="#">Chính sách bảo mật</a></li>
                    <li><a href="#">Điều khoản sử dụng</a></li>
                    <li><a href="#">Hướng dẫn mua hàng</a></li>
                </ul>
            </div>

            <div class="footer-section">
                <h3>Liên Hệ</h3>
                <ul>
                    <li>📍 123 Nguyễn Trãi, Q.1, TP.HCM</li>
                    <li>📞 1900 1234</li>
                    <li>✉️ contact@fashionstore.vn</li>
                    <li>🕒 8:00 - 22:00 hàng ngày</li>
                </ul>
            </div>
        </div>

        <div class="footer-bottom">
            <p>&copy; 2025 Fashion Store. All rights reserved.  | Loaded <%= products != null ? products.size() : 0 %> products</p>
        </div>
    </footer>

<script>
    const isLoggedIn = <%= isLoggedIn %>;
    let cartItems = [];

    // ✅ Tạo variantId đồng nhất từ productId (36 ký tự)
    function createVariantId(productId) {
        // Nếu productId đã là UUID 36 ký tự → dùng luôn
        if (productId && productId.length === 36 && productId.includes('-')) {
            return productId;
        }
        
        // Ngược lại → hash và padding
        let hash = 0;
        for (let i = 0; i < productId.length; i++) {
            hash = ((hash << 5) - hash) + productId.charCodeAt(i);
            hash = hash & hash;
        }
        
        const hashStr = Math.abs(hash).toString(36);
        const combined = productId + '-' + hashStr;
        
        // Cắt hoặc padding về đúng 36 ký tự
        if (combined.length > 36) {
            return combined.substring(0, 36);
        } else {
            return combined. padEnd(36, '0');
        }
    }

    // Load cart from localStorage
    function loadCart() {
        const saved = localStorage.getItem('cart');
        if (saved) {
            cartItems = JSON.parse(saved);
            updateCartBadge();
        }
    }

    // Save cart to localStorage
    function saveCart() {
        localStorage.setItem('cart', JSON.stringify(cartItems));
    }

    // Update cart badge
    function updateCartBadge() {
        const badge = document.getElementById('cartCount');
        if (badge) {
            const totalQuantity = cartItems.reduce((sum, item) => sum + (item.quantity || 0), 0);
            badge. textContent = totalQuantity;
        }
    }

    // ✅ Add to cart WITH variantId (36 ký tự)
    function addToCart(product) {
        if (!isLoggedIn) {
            if (confirm('Bạn cần đăng nhập để mua hàng.  Chuyển đến trang đăng nhập?')) {
                window.location.href = '${pageContext.request.contextPath}/login';
            }
            return;
        }

        console.log('🔍 Adding product:', product);

        // ✅ Tạo variantId đồng nhất (36 ký tự)
        const variantId = createVariantId(product.id);
        console.log('✅ Generated variantId (' + variantId. length + ' chars):', variantId);

        // ✅ Tìm trong cart theo productId HOẶC variantId
        let existingItem = cartItems.find(item => 
            item. productId === product.id || item.variantId === variantId
        );

        if (existingItem) {
            existingItem.quantity += 1;
            // ✅ Cập nhật variantId nếu cũ sai
            existingItem.variantId = variantId;
            console.log('✅ Increased quantity:', existingItem);
        } else {
            const cartItem = {
                variantId: variantId,  // ← 36 ký tự
                productId: product.id,
                name: product.name,
                price: product.price,
                quantity: 1,
                color: 'Mặc định',
                size: 'Free Size',
                image: product.image || '',
                addedAt: new Date().toISOString()
            };
            
            cartItems.push(cartItem);
            console.log('✅ Added new item:', cartItem);
        }

        saveCart();
        updateCartBadge();

        alert('✅ Đã thêm "' + product.name + '" vào giỏ hàng!');
        console.log('📦 Current cart:', cartItems);
    }

    // View product details
    function viewProduct(productId) {
        console.log('View product:', productId);
        alert('Xem chi tiết sản phẩm: ' + productId + '\n(Chức năng đang phát triển)');
    }

    // Handle cart click
    function handleCart() {
        if (!isLoggedIn) {
            if (confirm('Bạn cần đăng nhập để xem giỏ hàng.  Chuyển đến trang đăng nhập?')) {
                window.location.href = '<%=request.getContextPath()%>/login';
            }
            return;
        }
        
        window.location.href = '<%=request.getContextPath()%>/cart';
    }

    // Load cart on page load
    loadCart();

    // Smooth scroll
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor. addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
</script>
</body>
</html>