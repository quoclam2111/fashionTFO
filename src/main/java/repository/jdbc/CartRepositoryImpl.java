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
                ci.quantity
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
                        rs.getInt("quantity")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}