package quanlysanpham.add;

import quanlysanpham.Product;
import quanlysanpham.ProductManageControl;
import quanlysanpham.ProductOutputBoundary;
import quanlysanpham.ProductRequestData;
import repository.DTO.ProductDTO;
import repository.sanpham.AddSanPhamRepository;

public class AddProductUseCase extends ProductManageControl {
    private final AddSanPhamRepository addSanPhamRepository;

    public AddProductUseCase(AddSanPhamRepository addSanPhamRepository, ProductOutputBoundary presenter) {
        super(presenter);
        this.addSanPhamRepository = addSanPhamRepository;
        this.response = new ResponseDataAddProduct();
    }
    @Override
    public void execute(ProductRequestData req) {
        try {

            AddProduct newProduct = convertToBusiness(req);
            newProduct.validate();
            if(addSanPhamRepository.existsByProductName(req.productName)){
                response.success = false;
                response.message = "PRODUCT NAME ALREADY EXISTS";
                return;
            }

            ProductDTO addProductDTO = mapRequestToDTO(newProduct);

            addSanPhamRepository.addSanPham(addProductDTO);
            ResponseDataAddProduct res = (ResponseDataAddProduct) response;
            response.success = true;
            response.message = "PRODUCT ADDED SUCCESSFULLY";
            res.productId = newProduct.getProductId();
            res.productName = newProduct.getProductName();

        } catch (IllegalArgumentException ex) {
            response.success = false;
            response.message = ex.getMessage();
        } catch (Exception ex) {
            response.success = false;
            response.message = "ERROR_ADDING_PRODUCT";
        }
    }


    private AddProduct convertToBusiness(ProductRequestData req) {
        return new AddProduct(
            null,
            req.productName,
            null,
            req.description,
            req.brandId,
            req.categoryId,
            req.defaultImage,
            req.price,
            req.discountPrice,
            req.stockQuantity,
                Product.Status.PUBLISHED,
            null,
            null
        );
    }

    private ProductDTO mapRequestToDTO(Product req) {
        ProductDTO dto = new ProductDTO();
        dto.productId = req.getProductId();
        dto.productName = req.getProductName();
        dto.slug = req.getSlug();
        dto.description = req.getDescription();
        dto.brandId = req.getBrandId();
        dto.categoryId = req.getCategoryId();
        dto.defaultImage = req.getDefaultImage();
        dto.price = req.getPrice();
        dto.discountPrice = req.getDiscountPrice();
        dto.stockQuantity = req.getStockQuantity();
        dto.status = String.valueOf(req.getStatus());
        dto.createdAt = req.getCreatedAt();
        dto.updatedAt = req.getUpdatedAt();
        return dto;
    }
}
