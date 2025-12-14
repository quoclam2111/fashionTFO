package quanlysanpham.get;

import quanlysanpham.ProductManageControl;
import quanlysanpham.ProductOutputBoundary;
import quanlysanpham.ProductRequestData;
import repository.DTO.ProductDTO;
import repository.sanpham.GetInfoSanPhamRepository;

import java.util.Optional;

public class GetProductUseCase extends ProductManageControl {
    private final GetInfoSanPhamRepository repository;

    public GetProductUseCase(GetInfoSanPhamRepository repository,
                             ProductOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new ResponseDataGetProduct();
    }

    @Override
    public void execute(ProductRequestData req) {
        try {
            if (req.searchBy == null || req.searchValue == null || req.searchValue.isBlank()) {
                response.success = false;
                response.message = "Thiếu tiêu chí hoặc giá trị tìm kiếm!";
                return;
            }

            // Cập nhật switch case
            Optional<ProductDTO> productOpt = switch (req.searchBy.toLowerCase()) {
                case "id" -> repository.findById(req.searchValue);
                case "name" -> repository.findByName(req.searchValue);
                case "slug" -> repository.findBySlug(req.searchValue);
                case "brandid" -> repository.findFirstByBrandId(req.searchValue);
                case "categoryid" -> repository.findFirstByCategoryId(req.searchValue);
                default -> throw new IllegalArgumentException(
                        "Tiêu chí tìm kiếm không hợp lệ! Hỗ trợ: id, name, slug, brandid, categoryid"
                );
            };

            ResponseDataGetProduct res = (ResponseDataGetProduct) response;

            if (productOpt.isPresent()) {
                ProductDTO dto = productOpt.get();

                // ✅ Convert sang ProductViewItem (Use Case model)
                GetProductItem item = convertToViewItem(dto);

                res.product = item;
                res.success = true;
                res.message = "Lấy thông tin sản phẩm thành công";
            } else {
                res.product = null;
                res.success = false;
                res.message = "Không tìm thấy sản phẩm với ID: " + req.productId;
            }

        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
    }

    private GetProductItem convertToViewItem(ProductDTO dto) {
        GetProductItem item = new GetProductItem();
        item.productId = dto.productId;
        item.productName = dto.productName;
        item.slug = dto.slug;
        item.description = dto.description;
        item.brandId = dto.brandId;
        item.categoryId = dto.categoryId;
        item.defaultImage = dto.defaultImage;
        item.price = dto.price;
        item.discountPrice = dto.discountPrice;
        item.stockQuantity = dto.stockQuantity;
        item.status = dto.status;
        item.createdAt = dto.createdAt;
        item.updatedAt = dto.updatedAt;
        return item;
    }
}