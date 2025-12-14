package repository.jdbc;

import repository.DTO.ProductDTO;
import repository.sanpham.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SanPhamRepositoryImpl implements AddSanPhamRepository, ListProductrepository, UpdateSanPhamRepostiory, GetInfoSanPhamRepository, DeleteSanPhamRepository {
    @Override
    public void addSanPham(ProductDTO sanPham) {
        String sql = "INSERT INTO products (" +
                "product_id, product_name, slug, description, brand_id, category_id, " +
                "default_image, price, sale_price, stock_quantity, status, created_at, updated_at" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (var conn = DBConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {

            // ✅ Generate UUID if not exists
            if (sanPham.productId == null || sanPham.productId.isBlank()) {
                sanPham.productId = UUID.randomUUID().toString();
            }
            ps.setString(1, sanPham.productId);
            ps.setString(2, sanPham.productName);
            ps.setString(3, sanPham.slug);
            ps.setString(4, sanPham.description);
            ps.setString(5, sanPham.brandId);
            ps.setString(6, sanPham.categoryId);
            ps.setString(7, sanPham.defaultImage);
            ps.setBigDecimal(8, sanPham.price);
            ps.setBigDecimal(9, sanPham.discountPrice);
            ps.setInt(10, sanPham.stockQuantity);
            ps.setString(11, sanPham.status);
            ps.setTimestamp(12, new java.sql.Timestamp(sanPham.createdAt.getTime()));
            ps.setTimestamp(13, new java.sql.Timestamp(sanPham.updatedAt.getTime()));
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Product added successfully!");
            } else {
                System.out.println("⚠️ Product not added!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByProductName(String productName) {
        String sql = "SELECT 1 FROM products WHERE product_name = ?";
        try (var conn = DBConnection.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, productName);
            var rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProductDTO> findAllProducts() {
        List<ProductDTO> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductDTO dto = mapResultSetToDTO(rs);
                products.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
        }

        return products;
    }

    @Override
    public Optional<ProductDTO> findById(String productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ProductDTO dto = mapResultSetToDTO(rs);
                return Optional.of(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm sản phẩm: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public boolean update(ProductDTO dto) {
        String sql = """
            UPDATE products
            SET product_name = ?, slug = ?, description = ?, brand_id = ?, category_id = ?, default_image = ?, 
                price = ?, sale_price = ?, stock_quantity = ?, status = ?, updated_at = CURRENT_TIMESTAMP
            WHERE product_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dto.productName);
            ps.setString(2, dto.slug);
            ps.setString(3, dto.description);
            ps.setString(4, dto.brandId);
            ps.setString(5, dto.categoryId);
            ps.setString(6, dto.defaultImage);
            ps.setBigDecimal(7, dto.price);
            ps.setBigDecimal(8, dto.discountPrice);
            ps.setInt(9, dto.stockQuantity);
            ps.setString(10, dto.status);
            ps.setString(11, dto.productId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Product updated successfully: " + dto.productName);
                return true;
            } else {
                System.out.println("⚠️ Product not found with id: " + dto.productId);
                throw new RuntimeException("PRODUCT_NOT_FOUND");
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        }
    }

    @Override
    public boolean existsByNameExcludingProduct(String productName, String excludeProductId) {
        String sql = "SELECT 1 FROM products WHERE product_name = ? AND product_id != ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productName);
            ps.setString(2, excludeProductId);
            ResultSet rs = ps.executeQuery();

            boolean exists = rs.next();

            if (exists) {
                System.out.println("⚠️ Tên sản phẩm '" + productName + "' đã tồn tại!");
            }

            return exists;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi kiểm tra tên sản phẩm: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra slug đã tồn tại (loại trừ sản phẩm hiện tại)
     * @param slug - Slug cần kiểm tra
     * @param excludeProductId - ID sản phẩm cần loại trừ (chính nó)
     * @return true nếu đã tồn tại, false nếu chưa
     */
    @Override
    public boolean existsBySlugExcludingProduct(String slug, String excludeProductId) {
        String sql = "SELECT 1 FROM products WHERE slug = ? AND product_id != ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, slug);
            ps.setString(2, excludeProductId);
            ResultSet rs = ps.executeQuery();

            boolean exists = rs.next();

            if (exists) {
                System.out.println("⚠️ Slug '" + slug + "' đã tồn tại!");
            }

            return exists;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi kiểm tra slug: " + e.getMessage());
        }
    }

    // ===================== Lấy từng trường =====================

    @Override
    public Optional<BigDecimal> findPriceById(String productId) {
        return findBigDecimalFieldById(productId, "price");
    }

    @Override
    public Optional<BigDecimal> findDiscountPriceById(String productId) {
        return findBigDecimalFieldById(productId, "sale_price");
    }

    @Override
    public Optional<Integer> findStockById(String productId) {
        return findIntFieldById(productId, "stock_quantity");
    }

    @Override
    public Optional<String> findDescriptionById(String productId) {
        return findStringFieldById(productId, "description");
    }

    @Override
    public Optional<String> findDefaultImageById(String productId) {
        return findStringFieldById(productId, "default_image");
    }

    @Override
    public Optional<String> findBrandIdById(String productId) {
        return findStringFieldById(productId, "brand_id");
    }

    @Override
    public Optional<String> findCategoryIdById(String productId) {
        return findStringFieldById(productId, "category_id");
    }

    // ===================== Helper chung =====================
    private Optional<String> findStringFieldById(String productId, String fieldName) {
        String sql = "SELECT " + fieldName + " FROM products WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(rs.getString(fieldName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private Optional<BigDecimal> findBigDecimalFieldById(String productId, String fieldName) {
        String sql = "SELECT " + fieldName + " FROM products WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(rs.getBigDecimal(fieldName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private Optional<Integer> findIntFieldById(String productId, String fieldName) {
        String sql = "SELECT " + fieldName + " FROM products WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(rs.getInt(fieldName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(String productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Product deleted successfully with id: " + productId);
            } else {
                System.out.println("⚠️ Product not found with id: " + productId);
                throw new RuntimeException("PRODUCT_NOT_FOUND");
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
    }


    @Override
    public List<ProductDTO> findAllOrderByName() {
        return findAllSorted("product_name");
    }

    // Lấy tất cả sản phẩm sắp xếp theo giá
    @Override
    public List<ProductDTO> findAllOrderByPrice() {
        return findAllSorted("price");
    }

    // Lấy tất cả sản phẩm sắp xếp theo giá khuyến mãi
    @Override
    public List<ProductDTO> findAllOrderByDiscountPrice() {
        return findAllSorted("sale_price");
    }

    // Lấy tất cả sản phẩm sắp xếp theo số lượng
    @Override
    public List<ProductDTO> findAllOrderByStockQuantity() {
        return findAllSorted("stock_quantity");
    }

    @Override
    public List<ProductDTO> findAllOrderByCreatedAt() {
        return findAllSorted("created_at");
    }

    @Override
    public List<ProductDTO> findAllOrderByUpdatedAt() {
        return findAllSorted("updated_at");
    }

    // Phương thức chung để tránh lặp code
    private List<ProductDTO> findAllSorted(String column) {
        List<ProductDTO> products = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY " + column + " ASC"; // ASC mặc định

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapResultSetToDTO(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
        }

        return products;
    }



    private ProductDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        ProductDTO dto = new ProductDTO();
        dto.productId = rs.getString("product_id");
        dto.productName = rs.getString("product_name");
        dto.slug = rs.getString("slug");
        dto.description = rs.getString("description");
        dto.brandId = rs.getString("brand_id");
        dto.categoryId = rs.getString("category_id");
        dto.defaultImage = rs.getString("default_image");

        // BigDecimal
        dto.price = rs.getBigDecimal("price");
        dto.discountPrice = rs.getBigDecimal("sale_price");

        dto.stockQuantity = rs.getInt("stock_quantity");
        dto.status = rs.getString("status");

        // Date
        java.sql.Timestamp createdTs = rs.getTimestamp("created_at");
        dto.createdAt = createdTs != null ? new Date(createdTs.getTime()) : null;

        java.sql.Timestamp updatedTs = rs.getTimestamp("updated_at");
        dto.updatedAt = updatedTs != null ? new Date(updatedTs.getTime()) : null;

        return dto;
    }

    @Override
    public Optional<String> findByCreateAt(String productId) {
        return findDateFieldById(productId, "created_at");
    }

    /**
     * Lấy thời gian cập nhật (updated_at) theo product ID
     */
    @Override
    public Optional<String> findByUpdateAt(String productId) {
        return findDateFieldById(productId, "updated_at");
    }

// ===================== Helper method cho Date fields =====================

    private Optional<String> findDateFieldById(String productId, String fieldName) {
        String sql = "SELECT " + fieldName + " FROM products WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                java.sql.Timestamp timestamp = rs.getTimestamp(fieldName);
                if (timestamp != null) {
                    // Format: yyyy-MM-dd HH:mm:ss
                    String formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(timestamp);
                    return Optional.of(formattedDate);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy " + fieldName + ": " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<ProductDTO> findByName(String productName) {
        String sql = "SELECT * FROM products WHERE product_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ProductDTO dto = mapResultSetToDTO(rs);
                return Optional.of(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm sản phẩm theo tên: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Tìm sản phẩm theo slug
     */
    @Override
    public Optional<ProductDTO> findBySlug(String slug) {
        String sql = "SELECT * FROM products WHERE slug = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, slug);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ProductDTO dto = mapResultSetToDTO(rs);
                return Optional.of(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm sản phẩm theo slug: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Tìm slug theo product ID
     */
    @Override
    public Optional<String> findSlugById(String productId) {
        return findStringFieldById(productId, "slug");
    }

    // Implementation
    @Override
    public Optional<ProductDTO> findFirstByBrandId(String brandId) {
        String sql = "SELECT * FROM products WHERE brand_id = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, brandId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDTO(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm sản phẩm theo brand: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<ProductDTO> findFirstByCategoryId(String categoryId) {
        String sql = "SELECT * FROM products WHERE category_id = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoryId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDTO(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm sản phẩm theo category: " + e.getMessage());
        }

        return Optional.empty();
    }
}
