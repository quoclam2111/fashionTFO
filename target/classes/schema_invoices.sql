-- SQL Schema cho Invoice Management System
-- Tạo bảng invoices để lưu hóa đơn
CREATE TABLE IF NOT EXISTS invoices (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    item_total INT NOT NULL,
    shipping_fee INT NOT NULL,
    vat INT NOT NULL,
    final_amount INT NOT NULL,
    payment_method VARCHAR(50),
    customer_phone VARCHAR(20),
    customer_address TEXT,
    payment_status VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING, COMPLETED, FAILED
    delivery_status VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING, SHIPPING, DELIVERED, CANCELLED
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_order_id (order_id),
    INDEX idx_payment_status (payment_status),
    INDEX idx_delivery_status (delivery_status),
    INDEX idx_created_date (created_date)
);

-- Tạo bảng invoice_items để lưu chi tiết các item trong hóa đơn
CREATE TABLE IF NOT EXISTS invoice_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(255) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    unit_price INT NOT NULL,
    total INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE,
    INDEX idx_invoice_id (invoice_id)
);

-- Views hữu ích cho admin

-- View: Tóm tắt tất cả hóa đơn
CREATE OR REPLACE VIEW vw_invoice_summary AS
SELECT 
    i.id as invoice_id,
    i.order_id,
    i.user_id,
    i.created_date,
    i.final_amount,
    i.payment_status,
    i.delivery_status,
    i.customer_phone,
    COUNT(ii.id) as item_count,
    i.created_at
FROM invoices i
LEFT JOIN invoice_items ii ON i.id = ii.invoice_id
GROUP BY i.id
ORDER BY i.created_date DESC;

-- View: Hóa đơn chờ giao hàng (shipping)
CREATE OR REPLACE VIEW vw_invoices_shipping AS
SELECT 
    i.id as invoice_id,
    i.order_id,
    i.user_id,
    i.created_date,
    i.final_amount,
    i.payment_status,
    i.delivery_status,
    i.customer_phone,
    i.customer_address
FROM invoices i
WHERE i.delivery_status IN ('PENDING', 'SHIPPING')
ORDER BY i.created_date ASC;

-- View: Hóa đơn chưa thanh toán
CREATE OR REPLACE VIEW vw_invoices_unpaid AS
SELECT 
    i.id as invoice_id,
    i.order_id,
    i.user_id,
    i.created_date,
    i.final_amount,
    i.payment_status,
    i.payment_method,
    i.customer_phone
FROM invoices i
WHERE i.payment_status = 'PENDING'
AND i.payment_method IN ('BANKING', 'BANKING_QR', 'CARD')
ORDER BY i.created_date ASC;

-- View: Thống kê doanh thu theo tháng
CREATE OR REPLACE VIEW vw_invoice_revenue_monthly AS
SELECT 
    DATE_FORMAT(i.created_date, '%Y-%m') as month,
    COUNT(i.id) as invoice_count,
    SUM(i.final_amount) as total_revenue,
    AVG(i.final_amount) as avg_amount,
    SUM(CASE WHEN i.payment_status = 'COMPLETED' THEN i.final_amount ELSE 0 END) as completed_revenue
FROM invoices i
GROUP BY DATE_FORMAT(i.created_date, '%Y-%m')
ORDER BY month DESC;
