package quanlysanpham.list;

import quanlysanpham.ProductManageControl;
import quanlysanpham.ProductOutputBoundary;
import quanlysanpham.ProductRequestData;
import repository.DTO.ProductDTO;
import repository.sanpham.ListProductrepository;

import java.util.List;
import java.util.stream.Collectors;

public class ProductListUseCase extends ProductManageControl {
    private final ListProductrepository repository;

    public ProductListUseCase(ListProductrepository repository,
                              ProductOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new ResponseDataProductList();
    }

    @Override
    public void execute(ProductRequestData request) {
        try {
            // 1. Lấy tất cả products (từ DB → DTO)
            List<ProductDTO> allProducts = repository.findAllProducts();

            // 2. Lọc nâng cao (bao gồm cả status, brand, category, stock)
            List<ProductDTO> filteredProducts = advancedFilter(allProducts, request);

            // 3. Sắp xếp
            sortProducts(filteredProducts, request.sortBy, request.ascending);

            // 4. Chuyển ProductDTO → ProductViewItem (Use Case model)
            List<ProductViewItem> items = filteredProducts.stream().map(dto -> {
                ProductViewItem item = new ProductViewItem();
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
            }).collect(Collectors.toList());

            // 5. Set response
            ResponseDataProductList res = (ResponseDataProductList) this.response;
            res.success = true;
            res.message = "Lấy danh sách sản phẩm thành công!";
            res.products = items;
            res.totalCount = allProducts.size();
            res.filteredCount = items.size();

        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
    }

    /**
     * Lọc sản phẩm nâng cao theo nhiều tiêu chí
     */
    private List<ProductDTO> advancedFilter(List<ProductDTO> products, ProductRequestData request) {
        return products.stream()
                // Lọc theo status
                .filter(p -> request.statusFilter == null
                        || request.statusFilter.isEmpty()
                        || request.statusFilter.equalsIgnoreCase("all")
                        || p.status.equalsIgnoreCase(request.statusFilter))

                // Lọc theo brand
                .filter(p -> request.brandId == null
                        || request.brandId.isEmpty()
                        || p.brandId.equals(request.brandId))

                // Lọc theo category
                .filter(p -> request.categoryId == null
                        || request.categoryId.isEmpty()
                        || p.categoryId.equals(request.categoryId))

                // Lọc sản phẩm có tồn kho >= giá trị yêu cầu
                .filter(p -> request.stockQuantity <= 0
                        || p.stockQuantity >= request.stockQuantity)

                .collect(Collectors.toList());
    }

    /**
     * Sắp xếp danh sách sản phẩm
     */
    private void sortProducts(List<ProductDTO> products, String sortBy, boolean ascending) {
        if (sortBy == null || sortBy.isEmpty()) return;

        products.sort((a, b) -> {
            int result = 0;

            switch (sortBy.toLowerCase()) {
                case "name":
                    result = a.productName.compareTo(b.productName);
                    break;

                case "price":
                    result = a.price.compareTo(b.price);
                    break;

                case "discountprice":
                case "saleprice":
                    result = compareNullable(a.discountPrice, b.discountPrice);
                    break;

                case "stock":
                case "stockquantity":
                    result = Integer.compare(a.stockQuantity, b.stockQuantity);
                    break;

                case "createdat":
                    result = a.createdAt.compareTo(b.createdAt);
                    break;

                case "updatedat":
                    result = a.updatedAt.compareTo(b.updatedAt);
                    break;

                case "slug":
                    result = a.slug.compareTo(b.slug);
                    break;

                default:
                    // Mặc định sắp xếp theo tên
                    result = a.productName.compareTo(b.productName);
            }

            return ascending ? result : -result;
        });
    }

    /**
     * So sánh 2 giá trị BigDecimal có thể null
     */
    private int compareNullable(java.math.BigDecimal a, java.math.BigDecimal b) {
        if (a == null && b == null) return 0;
        if (a == null) return 1;  // null được coi là lớn hơn
        if (b == null) return -1;
        return a.compareTo(b);
    }
}