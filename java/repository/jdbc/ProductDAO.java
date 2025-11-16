package repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import repository.ProductDAOGateway;
import repository.DTO.ProductVariantDTO;

public class ProductDAO implements ProductDAOGateway {

    // Sử dụng DBConnection để lấy kết nối
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    public List<ProductVariantDTO> getAllPublishedProductVariants() throws SQLException {
        List<ProductVariantDTO> variants = new ArrayList<>();

        String SQL = "SELECT pv.variant_id, p.product_name, c.color_name, s.size_name, pv.price, pv.quantity " +
                     "FROM product_variants pv " +
                     "JOIN products p ON pv.product_id = p.product_id " +
                     "JOIN colors c ON pv.color_id = c.color_id " +
                     "JOIN sizes s ON pv.size_id = s.size_id " +
                     "WHERE p.status = 'published' AND pv.quantity > 0";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("DAO: Đang thực thi truy vấn...");

            while (rs.next()) {
                variants.add(new ProductVariantDTO(
                    rs.getString("variant_id"),
                    rs.getString("product_name"),
                    rs.getString("color_name"),
                    rs.getString("size_name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                ));
            }

        } catch (SQLException e) {
            System.err.println("DAO Error: Lỗi truy vấn database khi lấy sản phẩm.");
            e.printStackTrace();
            throw new SQLException("Không thể lấy danh sách sản phẩm.", e);
        }
        return variants;
    }

    @Override
    public ProductVariantDTO getVariantDetailById(String variantId) throws SQLException {
        // ✅ Sửa lại câu SQL hoàn chỉnh
        String SQL = "SELECT pv.variant_id, p.product_name, c.color_name, s.size_name, pv.price, pv.quantity " +
                     "FROM product_variants pv " +
                     "JOIN products p ON pv.product_id = p.product_id " +
                     "JOIN colors c ON pv.color_id = c.color_id " +
                     "JOIN sizes s ON pv.size_id = s.size_id " +
                     "WHERE pv.variant_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

             stmt.setString(1, variantId);

             try (ResultSet rs = stmt.executeQuery()) {
                 if (rs.next()) {
                     return new ProductVariantDTO(
                        rs.getString("variant_id"),
                        rs.getString("product_name"),
                        rs.getString("color_name"),
                        rs.getString("size_name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                    );
                 }
             }
        } catch (SQLException e) {
            System.err.println("DAO Error: Lỗi truy vấn database khi lấy chi tiết sản phẩm.");
            e.printStackTrace();
            throw new SQLException("Không thể lấy chi tiết sản phẩm.", e);
        }
        return null;
    }
}