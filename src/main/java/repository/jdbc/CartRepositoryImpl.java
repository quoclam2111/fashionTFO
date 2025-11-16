package repository.jdbc;

import repository.CartRepository;
import repository.DTO.CartDTO;
import java.sql.*;
import java.util.*;

public class CartRepositoryImpl implements CartRepository {

    @Override
    public List<CartDTO> findCartItemsByUserId(String userId) {
        List<CartDTO> list = new ArrayList<>();
        String sql = """
            SELECT 
                p.product_id AS productId, 
                p.product_name AS productName, 
                p.price, 
                ci.quantity,
                pv.variant_id AS variantId
            FROM carts c
            JOIN cart_items ci ON c.cart_id = ci.cart_id
            JOIN product_variants pv ON ci.variant_id = pv.variant_id
            JOIN products p ON pv.product_id = p.product_id
            WHERE c.user_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new CartDTO(
                        rs.getString("productId"),
                        rs.getString("productName"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("variantId")  // ← Thêm tham số thứ 5
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateCartItemQuantity(String userId, String variantId, int newQuantity) {
        String sql = """
            UPDATE cart_items ci
            JOIN carts c ON ci.cart_id = c.cart_id
            SET ci.quantity = ?
            WHERE c.user_id = ? AND ci.variant_id = ?
        """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, newQuantity);
            stmt.setString(2, userId);
            stmt.setString(3, variantId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeCartItem(String userId, String variantId) {
        String sql = """
            DELETE ci FROM cart_items ci
            JOIN carts c ON ci.cart_id = c.cart_id
            WHERE c.user_id = ? AND ci.variant_id = ?
        """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            stmt.setString(2, variantId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getAvailableStock(String variantId) {
        String sql = "SELECT quantity FROM product_variants WHERE variant_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, variantId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("quantity");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String getProductNameByVariantId(String variantId) {
        String sql = """
            SELECT p.product_name 
            FROM products p
            JOIN product_variants pv ON p.product_id = pv.product_id
            WHERE pv.variant_id = ?
        """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, variantId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("product_name");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Product";
    }
}