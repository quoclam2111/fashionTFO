package repository.sanpham;

import repository.DTO.ProductDTO;

import java.util.List;

public interface ListProductrepository {
    List<ProductDTO> findAllProducts();
    List<ProductDTO> findAllOrderByPrice();
    List<ProductDTO> findAllOrderByName();
    List<ProductDTO> findAllOrderByStockQuantity();
    List<ProductDTO> findAllOrderByDiscountPrice();
    List<ProductDTO> findAllOrderByCreatedAt();
    List<ProductDTO> findAllOrderByUpdatedAt();
}
