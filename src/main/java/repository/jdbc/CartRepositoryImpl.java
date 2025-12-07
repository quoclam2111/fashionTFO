package repository.jdbc;

import repository.CartRepository;
import repository.DTO.CartDTO;
import java.sql.*;
import java.util.*;

public class CartRepositoryImpl implements CartRepository {

    @Override
    public List<CartDTO> findCartItemsByUserId(String userId) throws SQLException {
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
                String productName = rs.getString("productName");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                
                list.add(new CartDTO(
                        rs.getString("productId"),
                        productName,
                        price,
                        quantity,
                        rs.getString("variantId")
                ));
            }
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

    @Override
    public int getQuantityInCart(String userId, String variantId) {
        String sql = """
            SELECT ci.quantity 
            FROM cart_items ci
            JOIN carts c ON ci.cart_id = c.cart_id
            WHERE c.user_id = ? AND ci.variant_id = ?
        """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            stmt.setString(2, variantId);
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
    public boolean addToCart(String userId, String variantId, int quantity) {
        try (Connection conn = DBConnection.getConnection()) {
            String cartId = getOrCreateCart(userId, conn);
            
            int existingQuantity = getQuantityInCart(userId, variantId);
            
            if (existingQuantity > 0) {
                String updateSql = """
                    UPDATE cart_items ci
                    JOIN carts c ON ci.cart_id = c.cart_id
                    SET ci.quantity = ci.quantity + ?
                    WHERE c.user_id = ? AND ci.variant_id = ?
                """;
                
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setInt(1, quantity);
                    stmt.setString(2, userId);
                    stmt.setString(3, variantId);
                    
                    int rowsAffected = stmt.executeUpdate();
                    return rowsAffected > 0;
                }
            } else {
                String insertSql = """
                    INSERT INTO cart_items (item_id, cart_id, variant_id, quantity)
                    VALUES (?, ?, ?, ?)
                """;
                
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setString(1, java.util.UUID.randomUUID().toString());
                    stmt.setString(2, cartId);
                    stmt.setString(3, variantId);
                    stmt.setInt(4, quantity);
                    
                    int rowsAffected = stmt.executeUpdate();
                    return rowsAffected > 0;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private String getOrCreateCart(String userId, Connection conn) throws SQLException {
        String selectSql = "SELECT cart_id FROM carts WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("cart_id");
            }
        }
        
        String cartId = java.util.UUID.randomUUID().toString();
        String insertSql = "INSERT INTO carts (cart_id, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            stmt.setString(1, cartId);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        }
        
        return cartId;
    }

    @Override
    public int countCartItems(String userId) {
        String sql = """
            SELECT COUNT(*) as total
            FROM cart_items ci
            JOIN carts c ON ci.cart_id = c.cart_id
            WHERE c.user_id = ?
        """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean clearCart(String userId) {
        String sql = """
            DELETE ci FROM cart_items ci
            JOIN carts c ON ci.cart_id = c.cart_id
            WHERE c.user_id = ?
        """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean userExists(String userId) {
        String sql = "SELECT COUNT(*) as count FROM users WHERE user_id = ? AND status = 'active'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}