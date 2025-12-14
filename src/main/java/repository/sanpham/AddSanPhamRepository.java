package repository.sanpham;

import repository.DTO.ProductDTO;

public interface AddSanPhamRepository {
    void addSanPham(ProductDTO sanPham);
    boolean existsByProductName(String name);
}
