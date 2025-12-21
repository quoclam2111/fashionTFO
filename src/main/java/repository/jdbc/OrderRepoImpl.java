package repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import repository.DTO.OrderDTO;
import repository.DTO.OrderItemDTO;
import repository.hoadon.AddOrderRepoGateway;
import repository.hoadon.DeleteOrderRepositoryGateway;
import repository.hoadon.GetOrderRepositoryGateway;
import repository.hoadon.ListOrdersRepositoryGateway;
import repository.hoadon.UpdateOrderRepositoryGateway;

public class OrderRepoImpl implements AddOrderRepoGateway,
        ListOrdersRepositoryGateway,
        GetOrderRepositoryGateway,
        UpdateOrderRepositoryGateway,
        DeleteOrderRepositoryGateway {

    // ============================================
    // IMPLEMENT AddOrderRepoGateway
    // ============================================
    @Override
    public String save(OrderDTO dto) {
        String sql = """
            INSERT INTO orders (order_id, user_id, customer_name, customer_phone,
                                customer_address, total_amount, status, order_date, note)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dto.id == null ? UUID.randomUUID().toString() : dto.id);
            ps.setString(2, dto.userId);
            ps.setString(3, dto.customerName);
            ps.setString(4, dto.customerPhone);
            ps.setString(5, dto.customerAddress);
            ps.setDouble(6, dto.totalAmount);
            ps.setString(7, dto.status);
            ps.setTimestamp(8, new java.sql.Timestamp(dto.orderDate.getTime()));
            ps.setString(9, dto.note);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Order saved successfully!");
            } else {
                System.out.println("⚠️ Order not saved!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu đơn hàng: " + e.getMessage());
        }
        return sql;
    }

    @Override
    public boolean existsByOrderId(String orderId) {
        String sql = "SELECT 1 FROM orders WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ============================================
    // IMPLEMENT ListOrdersRepositoryGateway
    // ============================================
    @Override
    public List<OrderDTO> findAll() {
        List<OrderDTO> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrderDTO dto = mapResultSetToDTO(rs);
                orders.add(dto);
            }

            System.out.println("✅ Loaded " + orders.size() + " orders");

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách đơn hàng: " + e.getMessage());
        }

        return orders;
    }


    // ============================================
    // IMPLEMENT GetOrderRepositoryGateway
    // ============================================
    @Override
    public Optional<OrderDTO> findById(String id) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                OrderDTO dto = mapResultSetToDTO(rs);
                // ⭐ THÊM:  Load order items
                dto.items = getOrderItems(id);
                System.out.println("✅ Order found:  " + dto.id);
                return Optional.of(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm đơn hàng: " + e.getMessage());
        }

        return Optional. empty();
    }
    
    @Override
    public Optional<OrderDTO> findByUserId(String userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                OrderDTO dto = mapResultSetToDTO(rs);
                return Optional.of(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm đơn hàng theo userId: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<OrderDTO> findByPhone(String phone) {
        String sql = "SELECT * FROM orders WHERE customer_phone = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                OrderDTO dto = mapResultSetToDTO(rs);
                return Optional.of(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm đơn hàng theo phone: " + e.getMessage());
        }

        return Optional.empty();
    }

    // ============================================
    // IMPLEMENT UpdateOrderRepositoryGateway
    // ============================================
    @Override
    public void update(OrderDTO dto) {
        String sql = """
            UPDATE orders
            SET user_id = ?, customer_name = ?, customer_phone = ?,
                customer_address = ?, total_amount = ?, status = ?, note = ?
            WHERE order_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dto.userId);
            ps.setString(2, dto.customerName);
            ps.setString(3, dto.customerPhone);
            ps.setString(4, dto.customerAddress);
            ps.setDouble(5, dto.totalAmount);
            ps.setString(6, dto.status);
            ps.setString(7, dto.note);
            ps.setString(8, dto.id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Order updated successfully: " + dto.id);
            } else {
                System.out.println("⚠️ Order not found with id: " + dto.id);
                throw new RuntimeException("ORDER_NOT_FOUND");
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật đơn hàng: " + e.getMessage());
        }
    }

    // ============================================
    // IMPLEMENT DeleteOrderRepositoryGateway
    // ============================================
    @Override
    public void deleteById(String id) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bật transaction

            // BƯỚC 1: Xóa shipping (thông tin giao hàng)
            String sqlDeleteShipping = "DELETE FROM shipping WHERE order_id = ?";
            try (PreparedStatement psShipping = conn.prepareStatement(sqlDeleteShipping)) {
                psShipping.setString(1, id);
                int shippingDeleted = psShipping.executeUpdate();
                System.out.println("🚚 Đã xóa " + shippingDeleted + " shipping records");
            }

            // BƯỚC 2: Xóa order_items (chi tiết sản phẩm)
            String sqlDeleteItems = "DELETE FROM order_items WHERE order_id = ?";
            try (PreparedStatement psItems = conn.prepareStatement(sqlDeleteItems)) {
                psItems.setString(1, id);
                int itemsDeleted = psItems.executeUpdate();
                System.out.println("🗑️ Đã xóa " + itemsDeleted + " order items");
            }

            // BƯỚC 3: Xóa orders (đơn hàng chính)
            String sqlDeleteOrder = "DELETE FROM orders WHERE order_id = ?";
            try (PreparedStatement psOrder = conn.prepareStatement(sqlDeleteOrder)) {
                psOrder.setString(1, id);
                int rows = psOrder.executeUpdate();

                if (rows > 0) {
                    conn.commit(); // Commit transaction
                    System.out.println("✅ Đã xóa đơn hàng thành công: " + id);
                } else {
                    conn.rollback(); // Rollback nếu không tìm thấy
                    System.out.println("⚠️ Không tìm thấy đơn hàng với ID: " + id);
                    throw new RuntimeException("ORDER_NOT_FOUND");
                }
            }

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu có lỗi
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException("Lỗi khi xóa đơn hàng: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Khôi phục auto-commit
                    conn.close();
                } catch (Exception closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    // ============================================
    // HELPER METHOD
    // ============================================
    private OrderDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        OrderDTO dto = new OrderDTO();
        dto.id = rs.getString("order_id");
        dto.orderNo = rs.getString("order_no");
        dto.userId = rs. getString("user_id");
        dto.customerName = rs.getString("customer_name");
        dto.customerPhone = rs.getString("customer_phone");
        dto.customerAddress = rs.getString("customer_address");
        dto.totalAmount = rs.getDouble("total_amount");
        dto.paymentMethod = rs. getString("payment_method");
        dto.paymentStatus = rs. getString("payment_status");
        dto.status = rs.getString("order_status");
        dto.orderDate = rs.getTimestamp("order_date");
        dto.note = rs.getString("note");
        return dto;
    }
 // ============================================
 // ⭐ THÊM METHOD CHECKOUT (MỚI)
 // ============================================

 /**
  * Tạo đơn hàng từ checkout (có cart items)
  */
    /**
     * Tạo đơn hàng mới với order items
     */
    public String createOrder(OrderDTO order) throws java.sql.SQLException {
        Connection conn = null;
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // 1. Tạo order_id (UUID hoặc custom format)
            String orderId = java.util.UUID.randomUUID().toString();
            String orderNo = "ORD" + System.currentTimeMillis();
            
            System.out.println("🆕 Creating order: " + orderNo + " (ID: " + orderId + ")");
            
            // 2. Chuẩn hóa payment_method về chữ thường
            String paymentMethod = (order.paymentMethod != null ?  order.paymentMethod : "COD").toLowerCase();
            
            // 3. Insert vào bảng orders
            String sqlOrder = """
                INSERT INTO orders (
                    order_id, order_no, user_id, customer_name, customer_phone, 
                    customer_address, total_amount, order_date, note, 
                    payment_method, payment_status, order_status, created_at, updated_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?, 'Unpaid', 'Pending', NOW(), NOW())
            """;
            
            try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder)) {
                psOrder.setString(1, orderId);
                psOrder.setString(2, orderNo);
                psOrder.setString(3, order.userId);
                psOrder.setString(4, order. customerName);
                psOrder. setString(5, order.customerPhone);
                psOrder.setString(6, order.customerAddress);
                psOrder.setDouble(7, order.totalAmount);
                psOrder.setString(8, order.note != null ? order.note : "");
                psOrder.setString(9, paymentMethod);
                
                int rows = psOrder.executeUpdate();
                if (rows == 0) {
                    throw new RuntimeException("Không thể tạo đơn hàng!");
                }
                
                System.out.println("✅ Order created:  " + orderNo);
            }
            
            // 4. Insert vào bảng order_items
            if (order.items != null && ! order.items.isEmpty()) {
                String sqlItems = """
                    INSERT INTO order_items (order_id, variant_id, quantity, subtotal)
                    VALUES (?, ?, ?, ?)
                """;
                
                try (PreparedStatement psItems = conn.prepareStatement(sqlItems)) {
                    int itemCount = 0;
                    
                    for (repository. DTO.OrderItemDTO item : order. items) {
                        // ✅ Validate variantId
                        if (item.variantId == null || item.variantId.trim().isEmpty()) {
                            System.err.println("⚠️ Item không có variantId, bỏ qua!");
                            continue;
                        }
                        
                        // ✅ Kiểm tra variant có tồn tại không
                        if (!variantExists(conn, item.variantId)) {
                            System.err.println("⚠️ variantId không tồn tại: " + item.variantId);
                            System.err.println("   Đang tạo variant mặc định.. .");
                            
                            // Tạo variant mặc định
                            boolean created = createDefaultVariant(conn, item.variantId);
                            if (! created) {
                                System. err.println("❌ Không thể tạo variant, bỏ qua item này!");
                                continue;
                            }
                        }
                        
                        psItems.setString(1, orderId);
                        psItems.setString(2, item.variantId);
                        psItems.setInt(3, item.quantity);
                        psItems.setBigDecimal(4, item.subtotal);
                        psItems.addBatch();
                        itemCount++;
                    }
                    
                    if (itemCount > 0) {
                        int[] results = psItems.executeBatch();
                        System.out.println("✅ Đã thêm " + results. length + " items vào đơn hàng");
                    } else {
                        throw new RuntimeException("Không có item hợp lệ nào để thêm vào đơn hàng!");
                    }
                }
            } else {
                throw new RuntimeException("Đơn hàng phải có ít nhất 1 sản phẩm!");
            }
            
            conn.commit();
            System.out.println("✅ Đơn hàng " + orderNo + " đã được tạo thành công!");
            
            return orderNo;  // Trả về order_no thay vì order_id
            
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("❌ Rollback transaction do lỗi: " + e. getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Lỗi khi tạo đơn hàng:  " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn. setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Kiểm tra variant_id có tồn tại không
     */
    private boolean variantExists(Connection conn, String variantId) throws java.sql.SQLException {
        String sql = "SELECT 1 FROM product_variants WHERE variant_id = ?";
        try (PreparedStatement ps = conn. prepareStatement(sql)) {
            ps.setString(1, variantId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs. next();
            }
        }
    }

    /**
     * Tạo variant mặc định từ variantId
     */
    /**
     * Tạo variant mặc định từ variantId
     */
    private boolean createDefaultVariant(Connection conn, String variantId) {
        try {
            // Trích xuất productId từ variantId
            String productId = extractProductIdFromVariantId(variantId);
            
            System.out.println("🔍 Trying to create variant for productId: " + productId);
            
            // ✅ Lấy color_id và size_id đầu tiên từ database
            String colorId = null;
            String sizeId = null;
            
            String sqlGetIds = """
                SELECT 
                    (SELECT color_id FROM colors LIMIT 1) as color_id,
                    (SELECT size_id FROM sizes LIMIT 1) as size_id
            """;
            
            try (PreparedStatement ps = conn.prepareStatement(sqlGetIds);
                 ResultSet rs = ps. executeQuery()) {
                if (rs.next()) {
                    colorId = rs.getString("color_id");
                    sizeId = rs.getString("size_id");
                }
            }
            
            if (colorId == null || sizeId == null) {
                System.err.println("❌ Không tìm thấy color_id hoặc size_id trong database!");
                return false;
            }
            
            // ✅ Kiểm tra product có tồn tại không
            String sqlCheckProduct = "SELECT product_id, price, sale_price FROM products WHERE product_id = ?  LIMIT 1";
            try (PreparedStatement ps = conn. prepareStatement(sqlCheckProduct)) {
                ps.setString(1, productId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Product tồn tại, tạo variant
                        String realProductId = rs.getString("product_id");
                        double price = rs.getDouble("price");
                        Double salePrice = rs.getObject("sale_price") != null ? rs.getDouble("sale_price") : null;
                        
                        // ✅ Insert variant với color_id và size_id
                        String sqlInsert = """
                            INSERT INTO product_variants (variant_id, product_id, color_id, size_id, quantity, price, sale_price)
                            VALUES (?, ?, ?, ?, ?, ?, ?)
                        """;
                        
                        try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                            psInsert.setString(1, variantId);
                            psInsert.setString(2, realProductId);
                            psInsert.setString(3, colorId);  // ← color_id thay vì color
                            psInsert.setString(4, sizeId);   // ← size_id thay vì size
                            psInsert.setInt(5, 100);
                            psInsert.setDouble(6, price);
                            
                            if (salePrice != null) {
                                psInsert.setDouble(7, salePrice);
                            } else {
                                psInsert.setNull(7, java. sql.Types.DECIMAL);
                            }
                            
                            int rows = psInsert.executeUpdate();
                            if (rows > 0) {
                                System.out.println("✅ Đã tạo variant:   " + variantId + " cho product: " + realProductId);
                                return true;
                            }
                        }
                    } else {
                        System.err.println("❌ Không tìm thấy product với ID: " + productId);
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tạo variant: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    /**
     * Trích xuất productId từ variantId
     * Format: var-1766332535492-xxx → trả về productId đầu tiên tìm thấy
     */
    private String extractProductIdFromVariantId(String variantId) {
        try {
            // Nếu format: var-timestamp-random
            if (variantId.startsWith("var-")) {
                String[] parts = variantId. split("-");
                if (parts.length >= 2) {
                    // Thử tìm product đầu tiên trong database
                    Connection conn = DBConnection.getConnection();
                    String sql = "SELECT product_id FROM products LIMIT 1";
                    try (PreparedStatement ps = conn.prepareStatement(sql);
                         ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return rs.getString("product_id");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Fallback: return variantId
        return variantId;
    }

 private String generateOrderNo() {
     String prefix = "ORD";
     String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
     return prefix + timestamp;
 }

 /**
  * Lấy chi tiết sản phẩm trong đơn hàng
  */
 public List<OrderItemDTO> getOrderItems(String orderId) {
     List<OrderItemDTO> items = new ArrayList<>();
     String sql = "SELECT oi.*, p.product_name, p.default_image, " +
                 "c.color_name, s.size_name, pv.price " +
                 "FROM order_items oi " +
                 "JOIN product_variants pv ON oi.variant_id = pv.variant_id " +
                 "JOIN products p ON pv.product_id = p.product_id " +
                 "JOIN colors c ON pv.color_id = c. color_id " +
                 "JOIN sizes s ON pv.size_id = s.size_id " +
                 "WHERE oi. order_id = ? ";
     
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         
         ps.setString(1, orderId);
         ResultSet rs = ps.executeQuery();
         
         while (rs. next()) {
             OrderItemDTO item = new OrderItemDTO();
             item.orderItemId = rs.getString("order_item_id");
             item.orderId = rs.getString("order_id");
             item.variantId = rs.getString("variant_id");
             item.quantity = rs.getInt("quantity");
             item.subtotal = rs.getBigDecimal("subtotal");
             item.productName = rs.getString("product_name");
             item.productImage = rs.getString("default_image");
             item.colorName = rs.getString("color_name");
             item.sizeName = rs. getString("size_name");
             item.unitPrice = rs.getBigDecimal("price");
             items.add(item);
         }
         
     } catch (SQLException e) {
         throw new RuntimeException("Lỗi khi lấy chi tiết đơn hàng: " + e.getMessage(), e);
     } catch (Exception e) {
         throw new RuntimeException("Không thể kết nối DB: " + e.getMessage(), e);
     }
     
     return items;
 }
}