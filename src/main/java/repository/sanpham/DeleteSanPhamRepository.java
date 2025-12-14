package repository.sanpham;

import repository.DTO.ProductDTO;


import java.util.Optional;

public interface DeleteSanPhamRepository {
    void deleteById(String id);
    Optional<ProductDTO> findById(String id);
}
