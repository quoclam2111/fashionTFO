<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String fullName = (String) session.getAttribute("fullName");
    String userId = (String) session.getAttribute("userId");
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
    <title>Thanh toán - Fashion Store</title>
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
        }

        .checkout-steps {
            display: flex;
            gap: 30px;
            align-items: center;
        }

        .step {
            display: flex;
            align-items: center;
            gap: 10px;
            color: #999;
            font-weight: 500;
        }

        .step.active {
            color: #667eea;
        }

        .step-number {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            background: #e0e0e0;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
        }

        .step.active .step-number {
            background: #667eea;
            color: white;
        }

        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 30px;
        }

        .checkout-layout {
            display: grid;
            grid-template-columns: 1fr 400px;
            gap: 30px;
        }

        .checkout-form {
            background: white;
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        .section-title {
            font-size: 22px;
            font-weight: 600;
            color: #333;
            margin-bottom: 25px;
            padding-bottom: 15px;
            border-bottom: 2px solid #f0f0f0;
        }

        .form-group {
            margin-bottom: 20px;
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
        .form-group textarea,
        .form-group select {
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
            min-height: 80px;
        }

        .form-group input:focus,
        .form-group textarea:focus,
        .form-group select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .payment-methods {
            display: grid;
            gap: 15px;
            margin-top: 20px;
        }

        .payment-method {
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            padding: 20px;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .payment-method:hover {
            border-color: #667eea;
            background: #f8f9ff;
        }

        .payment-method.selected {
            border-color: #667eea;
            background: #f8f9ff;
        }

        .payment-method input[type="radio"] {
            width: 20px;
            height: 20px;
            accent-color: #667eea;
        }

        .payment-icon {
            font-size: 32px;
        }

        .payment-info {
            flex: 1;
        }

        .payment-name {
            font-weight: 600;
            color: #333;
            margin-bottom: 5px;
        }

        .payment-desc {
            font-size: 13px;
            color: #999;
        }

        .order-summary {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            height: fit-content;
            position: sticky;
            top: 20px;
        }

        .summary-items {
            margin-bottom: 20px;
        }

        .summary-item {
            display: flex;
            gap: 15px;
            padding: 15px 0;
            border-bottom: 1px solid #f0f0f0;
        }

        .summary-item:last-child {
            border-bottom: none;
        }

        .summary-item-image {
            width: 60px;
            height: 60px;
            border-radius: 8px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            color: white;
        }

        .summary-item-info {
            flex: 1;
        }

        .summary-item-name {
            font-size: 14px;
            font-weight: 600;
            color: #333;
            margin-bottom: 5px;
        }

        .summary-item-qty {
            font-size: 13px;
            color: #999;
        }

        .summary-item-price {
            font-weight: 600;
            color: #667eea;
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

        .btn-place-order {
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

        .btn-place-order:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
        }

        .btn-place-order:disabled {
            opacity: 0.5;
            cursor: not-allowed;
        }

        .security-note {
            text-align: center;
            color: #999;
            font-size: 13px;
            margin-top: 15px;
        }

        .alert {
            padding: 15px 20px;
            border-radius: 10px;
            margin-bottom: 20px;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            border-left: 4px solid #28a745;
        }

        @media (max-width: 1024px) {
            .checkout-layout {
                grid-template-columns: 1fr;
            }

            .order-summary {
                position: static;
            }

            .form-row {
                grid-template-columns: 1fr;
            }

            .checkout-steps {
                display: none;
            }
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-content">
            <div class="logo">Fashion Store</div>

            <div class="checkout-steps">
                <div class="step active">
                    <div class="step-number">1</div>
                    <span>Thông tin</span>
                </div>
                <div class="step">
                    <div class="step-number">2</div>
                    <span>Thanh toán</span>
                </div>
                <div class="step">
                    <div class="step-number">3</div>
                    <span>Hoàn tất</span>
                </div>
            </div>

            <div style="color: #999;">🔒 Thanh toán an toàn</div>
        </div>
    </nav>

    <div class="container">
        <div class="checkout-layout">
            <div class="checkout-form">
                <form id="checkoutForm" onsubmit="handleCheckout(event)">
                    <div class="section-title">📍 Thông tin giao hàng</div>

                    <div class="form-group">
                        <label for="fullName">Họ và tên <span class="required">*</span></label>
                        <input type="text" id="fullName" name="fullName" required 
                               value="<%= fullName %>" placeholder="Nguyễn Văn A">
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="phone">Số điện thoại <span class="required">*</span></label>
                            <input type="tel" id="phone" name="phone" required 
                                   pattern="0[0-9]{9,10}" placeholder="0912345678">
                        </div>

                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" 
                                   placeholder="email@example.com">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="address">Địa chỉ giao hàng <span class="required">*</span></label>
                        <textarea id="address" name="address" required 
                                  placeholder="Số nhà, tên đường, phường/xã, quận/huyện, tỉnh/thành phố"></textarea>
                    </div>

                    <div class="form-group">
                        <label for="note">Ghi chú đơn hàng</label>
                        <textarea id="note" name="note" 
                                  placeholder="Ghi chú về đơn hàng (tùy chọn)"></textarea>
                    </div>

                    <div class="section-title" style="margin-top: 40px;">💳 Phương thức thanh toán</div>

                    <div class="payment-methods">
                        <label class="payment-method selected">
                            <input type="radio" name="paymentMethod" value="COD" checked>
                            <div class="payment-icon">💵</div>
                            <div class="payment-info">
                                <div class="payment-name">Thanh toán khi nhận hàng (COD)</div>
                                <div class="payment-desc">Thanh toán bằng tiền mặt khi nhận hàng</div>
                            </div>
                        </label>

                        <label class="payment-method">
                            <input type="radio" name="paymentMethod" value="BANKING">
                            <div class="payment-icon">🏦</div>
                            <div class="payment-info">
                                <div class="payment-name">Chuyển khoản ngân hàng</div>
                                <div class="payment-desc">Chuyển khoản qua Internet Banking</div>
                            </div>
                        </label>

                        <label class="payment-method">
                            <input type="radio" name="paymentMethod" value="QR">
                            <div class="payment-icon">📱</div>
                            <div class="payment-info">
                                <div class="payment-name">Quét mã QR</div>
                                <div class="payment-desc">Thanh toán qua VNPay, MoMo, ZaloPay</div>
                            </div>
                        </label>

                        <label class="payment-method">
                            <input type="radio" name="paymentMethod" value="CARD">
                            <div class="payment-icon">💳</div>
                            <div class="payment-info">
                                <div class="payment-name">Thẻ tín dụng/ghi nợ</div>
                                <div class="payment-desc">Visa, Mastercard, JCB</div>
                            </div>
                        </label>
                    </div>
                </form>
            </div>

            <div class="order-summary">
                <div class="section-title">Đơn hàng của bạn</div>
                <div id="orderSummary"></div>
                
                <button type="submit" form="checkoutForm" class="btn-place-order" id="btnPlaceOrder">
                    Đặt hàng
                </button>

                <div class="security-note">
                    🔒 Thông tin của bạn được bảo mật
                </div>
            </div>
        </div>
    </div>

    <script>
    let cartItems = [];

    function loadCheckoutCart() {
        const saved = localStorage.getItem('checkoutCart');
        const contextPath = '<%= request.getContextPath() %>';
        if (saved) {
            cartItems = JSON.parse(saved);
            console.log('✅ Loaded cart:', cartItems);
            renderOrderSummary();
        } else {
            alert('Giỏ hàng trống! Chuyển về trang chủ.. .');
            location.href = contextPath + '/home';
        }
    }

    function formatVND(amount) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    }

    function calculateTotal() {
        return cartItems. reduce((sum, item) => sum + (item.price * item.quantity), 0);
    }

    function renderOrderSummary() {
        const subtotal = calculateTotal();
        const shipping = subtotal > 500000 ? 0 : 30000;
        const tax = Math.round(subtotal * 0.1);
        const total = subtotal + shipping + tax;

        const container = document.getElementById('orderSummary');
        
        let itemsHtml = '';
        for (let i = 0; i < cartItems.length; i++) {
            const item = cartItems[i];
            itemsHtml += 
                '<div class="summary-item">' +
                    '<div class="summary-item-image">👔</div>' +
                    '<div class="summary-item-info">' +
                        '<div class="summary-item-name">' + item.name + '</div>' +
                        '<div class="summary-item-qty">Số lượng: ' + item. quantity + '</div>' +
                    '</div>' +
                    '<div class="summary-item-price">' + formatVND(item.price * item.quantity) + '</div>' +
                '</div>';
        }

        container.innerHTML = 
            '<div class="summary-items">' + itemsHtml + '</div>' +
            '<div class="summary-row">' +
                '<span>Tạm tính</span>' +
                '<span class="value">' + formatVND(subtotal) + '</span>' +
            '</div>' +
            '<div class="summary-row">' +
                '<span>Phí vận chuyển</span>' +
                '<span class="value">' + (shipping === 0 ? 'Miễn phí' : formatVND(shipping)) + '</span>' +
            '</div>' +
            '<div class="summary-row">' +
                '<span>VAT (10%)</span>' +
                '<span class="value">' + formatVND(tax) + '</span>' +
            '</div>' +
            '<div class="summary-row total">' +
                '<span>Tổng cộng</span>' +
                '<span class="value">' + formatVND(total) + '</span>' +
            '</div>';
    }

    function handleCheckout(event) {
        event.preventDefault();
        event.stopPropagation();
        
        const btn = document.getElementById('btnPlaceOrder');
        const originalText = btn.textContent;
        btn.disabled = true;
        btn.textContent = 'Đang xử lý... ';

        if (! cartItems || cartItems.length === 0) {
            alert('❌ Giỏ hàng trống!');
            btn.disabled = false;
            btn.textContent = originalText;
            return;
        }

        const form = document.getElementById('checkoutForm');
        const fullName = form.fullName.value. trim();
        const phone = form.phone.value.trim();
        const address = form.address. value.trim();
        const email = form.email.value.trim();
        const note = form. note.value.trim();
        const paymentMethod = form.querySelector('input[name="paymentMethod"]:checked').value;

        console.log('👤 Full Name:', fullName);
        console.log('📞 Phone:', phone);
        console.log('📍 Address:', address);
        console.log('💳 Payment:', paymentMethod);

        if (! fullName) {
            alert('❌ Vui lòng nhập họ tên!');
            btn.disabled = false;
            btn.textContent = originalText;
            return;
        }

        if (!phone) {
            alert('❌ Vui lòng nhập số điện thoại!');
            btn.disabled = false;
            btn.textContent = originalText;
            return;
        }

        if (!address) {
            alert('❌ Vui lòng nhập địa chỉ!');
            btn.disabled = false;
            btn.textContent = originalText;
            return;
        }

        const subtotal = calculateTotal();
        const shipping = subtotal > 500000 ? 0 : 30000;
        const tax = Math.round(subtotal * 0.1);
        const total = subtotal + shipping + tax;
        
        console.log('🔍 Submitting checkout...');
        console.log('📦 Cart Items:', cartItems);
        console.log('💰 Total Amount:', total);

        // ✅ TẠO URL-ENCODED STRING
        const params = new URLSearchParams();
        params.append('fullName', fullName);
        params.append('phone', phone);
        params.append('address', address);
        params.append('email', email);
        params.append('note', note);
        params.append('paymentMethod', paymentMethod);
        params.append('totalAmount', total);
        params.append('cartItems', JSON.stringify(cartItems));

        console.log('📤 Params:', params. toString());

        const contextPath = '<%= request.getContextPath() %>';

        // ✅ GỬI VỚI CONTENT-TYPE:  application/x-www-form-urlencoded
        fetch(contextPath + '/api/checkout', {
            method:  'POST',
            headers:  {
                'Content-Type':  'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: params.toString()
        })
        .then(response => {
            console.log('📡 Response status:', response.status);
            if (!response.ok) {
                throw new Error('HTTP error!  status: ' + response.status);
            }
            return response. json();
        })
        .then(data => {
            console.log('📥 Response data:', data);
            
            if (data.success) {
                console.log('✅ Order created:', data.orderId);
                localStorage.removeItem('cart');
                localStorage.removeItem('checkoutCart');
                window.location.href = contextPath + '/order-success? orderId=' + data.orderId;
            } else {
                alert('❌ ' + data.message);
                btn.disabled = false;
                btn.textContent = originalText;
            }
        })
        .catch(error => {
            console.error('❌ Error:', error);
            alert('❌ Có lỗi xảy ra:  ' + error.message);
            btn.disabled = false;
            btn.textContent = originalText;
        });
    }

    // ✅ Handle payment method selection
    document.addEventListener('DOMContentLoaded', function() {
        document.querySelectorAll('.payment-method').forEach(method => {
            method.addEventListener('click', function() {
                document.querySelectorAll('.payment-method').forEach(m => m.classList.remove('selected'));
                this.classList.add('selected');
                this.querySelector('input[type="radio"]').checked = true;
            });
        });
        
        // Load cart on page load
        loadCheckoutCart();
    });
</script>
</body>
</html>