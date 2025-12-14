package repository.sanpham;

import repository.DTO.ProductDTO;

import java.util.Optional;

public interface UpdateSanPhamRepostiory {
    boolean update(ProductDTO productDTO);
    // Tìm sản phẩm theo ID
    Optional<ProductDTO> findById(String productId);

    // Kiểm tra tên sản phẩm đã tồn tại (trừ sản phẩm hiện tại)
    boolean existsByNameExcludingProduct(String productName, String excludeProductId);

    // Kiểm tra slug đã tồn tại (trừ sản phẩm hiện tại)
    boolean existsBySlugExcludingProduct(String slug, String excludeProductId);
}
