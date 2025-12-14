package quanlysanpham.edit;

import java.util.Optional;

import quanlysanpham.ProductManageControl;
import quanlysanpham.ProductOutputBoundary;
import quanlysanpham.ProductRequestData;
import quanlysanpham.Product;

import repository.DTO.ProductDTO;
import repository.sanpham.UpdateSanPhamRepostiory;

public class UpdateProductUseCase extends ProductManageControl {
    private final UpdateSanPhamRepostiory repository;

    public UpdateProductUseCase(UpdateSanPhamRepostiory repository,
                                ProductOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new ResponseDataUpdateProduct();
    }

    @Override
    public void execute(ProductRequestData request) {
        try {
            // 1. Convert request → UpdateProduct entity và validate
            UpdateProduct product = convertToBusinessObject(request);
            product.validate();

            // 2. Kiểm tra product có tồn tại không
            Optional<ProductDTO> existingProductOpt = repository.findById(request.productId);

            if (!existingProductOpt.isPresent()) {
                response.success = false;
                response.message = "Không tìm thấy sản phẩm với ID: " + request.productId;
                return;
            }

            ProductDTO dto = existingProductOpt.get();

            // 3. Kiểm tra tên sản phẩm mới đã được dùng chưa (nếu thay đổi)
            if (request.productName != null && !request.productName.equals(dto.productName)) {
                if (repository.existsByNameExcludingProduct(request.productName, request.productId)) {
                    response.success = false;
                    response.message = "Tên sản phẩm này đã tồn tại!";
                    return;
                }
            }

            // 4. Kiểm tra slug mới đã được dùng chưa (nếu thay đổi)
            if (request.slug != null && !request.slug.equals(dto.slug)) {
                if (repository.existsBySlugExcludingProduct(request.slug, request.productId)) {
                    response.success = false;
                    response.message = "Slug này đã được sử dụng!";
                    return;
                }
            }

            // 5. Cập nhật DTO từ request
            updateDTOFromRequest(dto, request);

            // 6. Lưu vào database
            repository.update(dto);

            // 7. Set response
            ResponseDataUpdateProduct updateResponse = (ResponseDataUpdateProduct) response;
            updateResponse.updatedProduct = convertToViewItem(dto);
            updateResponse.success = true;
            updateResponse.message = "Cập nhật sản phẩm thành công!";

        } catch (IllegalArgumentException ex) {
            response.success = false;
            response.message = ex.getMessage();
        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
    }

    /**
     * Convert Request → UpdateProduct Entity
     */
    private UpdateProduct convertToBusinessObject(ProductRequestData request) {
        return new UpdateProduct(
                request.productId,
                request.productName,
                request.slug,
                request.description,
                request.brandId,
                request.categoryId,
                request.defaultImage,
                request.price,
                request.discountPrice,
                request.stockQuantity,
                request.status != null ? Product.Status.toEnum(request.status) : null,
                null, // createdAt - không update
                new java.util.Date() // updatedAt - set thời gian hiện tại
        );
    }

    /**
     * Cập nhật DTO từ request (chỉ update các field không null)
     */
    private void updateDTOFromRequest(ProductDTO dto, ProductRequestData request) {
        if (request.productName != null && !request.productName.trim().isEmpty()) {
            dto.productName = request.productName;
        }

        if (request.slug != null && !request.slug.trim().isEmpty()) {
            dto.slug = request.slug;
        }

        if (request.description != null) {
            dto.description = request.description;
        }

        if (request.brandId != null && !request.brandId.trim().isEmpty()) {
            dto.brandId = request.brandId;
        }

        if (request.categoryId != null && !request.categoryId.trim().isEmpty()) {
            dto.categoryId = request.categoryId;
        }

        if (request.defaultImage != null) {
            dto.defaultImage = request.defaultImage;
        }

        if (request.price != null) {
            dto.price = request.price;
        }

        if (request.discountPrice != null) {
            dto.discountPrice = request.discountPrice;
        }

        if (request.stockQuantity >= 0) {
            dto.stockQuantity = request.stockQuantity;
        }

        if (request.status != null) {
            dto.status = request.status;
        }

        // Cập nhật thời gian
        dto.updatedAt = new java.util.Date();
    }

    /**
     * Convert ProductDTO → ProductUpdateViewItem
     */
    private ProductUpdateViewItem convertToViewItem(ProductDTO dto) {
        ProductUpdateViewItem viewItem = new ProductUpdateViewItem();
        viewItem.productId = dto.productId;
        viewItem.productName = dto.productName;
        viewItem.slug = dto.slug;
        viewItem.description = dto.description;
        viewItem.brandId = dto.brandId;
        viewItem.categoryId = dto.categoryId;
        viewItem.defaultImage = dto.defaultImage;
        viewItem.price = dto.price;
        viewItem.discountPrice = dto.discountPrice;
        viewItem.stockQuantity = dto.stockQuantity;
        viewItem.status = dto.status;
        viewItem.createdAt = dto.createdAt;
        viewItem.updatedAt = dto.updatedAt;
        return viewItem;
    }
}