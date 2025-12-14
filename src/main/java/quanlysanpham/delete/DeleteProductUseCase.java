package quanlysanpham.delete;

import quanlysanpham.ProductManageControl;
import quanlysanpham.ProductOutputBoundary;
import quanlysanpham.ProductRequestData;
import repository.DTO.ProductDTO;
import repository.DTO.UserDTO;
import repository.sanpham.DeleteSanPhamRepository;

import java.util.Optional;

public class DeleteProductUseCase extends ProductManageControl {
    private final DeleteSanPhamRepository deleteSanPhamRepository;

    public DeleteProductUseCase(DeleteSanPhamRepository deleteSanPhamRepository, ProductOutputBoundary presenter) {
        super(presenter);
        this.deleteSanPhamRepository = deleteSanPhamRepository;
        this.response = new ResponseDataDeleteProduct();
    }

    @Override
    public void execute(ProductRequestData req) {
        try{
            // 1. Validate input
            if (req.productId == null || req.productId.trim().isEmpty()) {
                response.success = false;
                response.message = "Vui lòng cung cấp ID sản phẩm cần xóa!";
                return;
            }

            Optional<ProductDTO> existingProductOpt = deleteSanPhamRepository.findById(req.productId);
            if (!existingProductOpt.isPresent()) {
                response.success = false;
                response.message = "Không tìm thấy sản phẩm với ID: " + req.productId;
                return;
            }
            ProductDTO productToDelete = existingProductOpt.get();

            // 3. Xóa user
            deleteSanPhamRepository.deleteById(req.productId);

            // 4. Set response
            ResponseDataDeleteProduct deleteResponse = (ResponseDataDeleteProduct) response;
            deleteResponse.success = true;
            deleteResponse.message = "Xóa người dùng thành công!";
            deleteResponse.deletedProductId = req.productId;
            deleteResponse.deletedProductName = productToDelete.productName;

        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
    }
}
