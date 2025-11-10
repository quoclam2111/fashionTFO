package repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import repository.*;
import repository.DTO.OrderDTO;

public class OrderRepoImpl implements AddOrderRepoGateway,
        ListOrdersRepositoryGateway,
        GetOrderRepositoryGateway,
        UpdateOrderRepositoryGateway,
        DeleteOrderRepositoryGateway {

    // ============================================
    // IMPLEMENT AddOrderRepoGateway
    // ============================================
    @Override
    public void save(OrderDTO dto) {
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
                System.out.println("✅ Order found: " + dto.id);
                return Optional.of(dto);
            } else {
                System.out.println("⚠️ Order not found with id: " + id);
                return Optional.empty();
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm đơn hàng: " + e.getMessage());
        }
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
        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Order deleted successfully with id: " + id);
            } else {
                System.out.println("⚠️ Order not found with id: " + id);
                throw new RuntimeException("ORDER_NOT_FOUND");
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa đơn hàng: " + e.getMessage());
        }
    }

    // ============================================
    // HELPER METHOD
    // ============================================
    private OrderDTO mapResultSetToDTO(ResultSet rs) throws Exception {
        OrderDTO dto = new OrderDTO();
        dto.id = rs.getString("order_id");
        dto.userId = rs.getString("user_id");
        dto.customerName = rs.getString("customer_name");
        dto.customerPhone = rs.getString("customer_phone");
        dto.customerAddress = rs.getString("customer_address");
        dto.totalAmount = rs.getDouble("total_amount");
        dto.status = rs.getString("status");
        dto.orderDate = rs.getTimestamp("order_date");
        dto.note = rs.getString("note");
        return dto;
    }
}