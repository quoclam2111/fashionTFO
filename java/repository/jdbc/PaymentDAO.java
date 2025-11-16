package repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import repository.DTO.OrderDetailDTO;
import repository.DTO.ProductVariantDTO;


public class PaymentDAO {

    public boolean saveOrder(OrderDetailDTO order) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            
            String sqlOrder = """
                INSERT INTO orders (order_id, order_no, user_id, total_amount, payment_method, order_status)
                VALUES (?, ?, ?, ?, ?, 'Pending')
            """;
            try (PreparedStatement ps = conn.prepareStatement(sqlOrder)) {
                ps.setString(1, order.getOrderId());
                ps.setString(2, order.getOrderId());
                ps.setString(3, order.getCustomerId());
                ps.setDouble(4, order.getTotalAmount());
                ps.setString(5, order.getPaymentMethod());
                ps.executeUpdate();
            }

            String sqlItem = """
                INSERT INTO order_items (order_item_id, order_id, variant_id, quantity, subtotal)
                VALUES (?, ?, ?, ?, ?)
            """;
            try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                for (ProductVariantDTO item : order.getOrderItems()) {
                    ps.setString(1, UUID.randomUUID().toString());
                    ps.setString(2, order.getOrderId());
                    ps.setString(3, item.getVariantId());
                    ps.setInt(4, item.getQuantity());
                    ps.setDouble(5, item.getPrice() * item.getQuantity());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            System.out.println(" Lưu đơn hàng " + order.getOrderId() + " thành công!");
            return true;

        } catch (SQLException e) {
            System.err.println(" Lỗi khi lưu đơn hàng vào DB!");
            e.printStackTrace();
            return false;
        }
    }
}