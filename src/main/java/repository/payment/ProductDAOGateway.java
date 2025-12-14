package repository.payment;

import java.sql.SQLException;
import java.util.List;

import repository.DTO.ProductVariantDTO;

public interface ProductDAOGateway {

    List<ProductVariantDTO> getAllPublishedProductVariants() throws SQLException;

    ProductVariantDTO getVariantDetailById(String variantId) throws SQLException;
}