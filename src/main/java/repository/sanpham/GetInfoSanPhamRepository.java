package repository.sanpham;

import repository.DTO.ProductDTO;

import java.math.BigDecimal;
import java.util.Optional;

public interface GetInfoSanPhamRepository {
    Optional<ProductDTO> findById(String productId);
    Optional<ProductDTO> findByName(String productId);
    Optional<ProductDTO> findBySlug(String slug);
    Optional<BigDecimal> findPriceById(String productId);
    Optional<Integer> findStockById(String productId);
    Optional<BigDecimal> findDiscountPriceById(String productId);
    Optional<String> findDefaultImageById(String productId);
    Optional<String> findDescriptionById(String productId);
    Optional<String> findBrandIdById(String productId);
    Optional<String> findCategoryIdById(String productId);
    Optional<String> findByCreateAt(String productId);
    Optional<String> findByUpdateAt(String productId);
    Optional<String> findSlugById(String productId);
    // Thêm vào interface
    Optional<ProductDTO> findFirstByBrandId(String brandId);
    Optional<ProductDTO> findFirstByCategoryId(String categoryId);
}
