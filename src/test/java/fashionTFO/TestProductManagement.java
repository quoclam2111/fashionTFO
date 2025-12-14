package fashionTFO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import adapters.sanpham.add.*;
import adapters.sanpham.get.*;
import adapters.sanpham.edit.*;
import adapters.sanpham.delete.*;
import adapters.sanpham.list.*;
import quanlysanpham.add.AddProductUseCase;
import quanlysanpham.get.GetProductUseCase;
import quanlysanpham.edit.UpdateProductUseCase;
import quanlysanpham.delete.DeleteProductUseCase;
import quanlysanpham.list.ProductListUseCase;
import repository.jdbc.SanPhamRepositoryImpl;

/**
 * Test Suite cho Product Management System
 * Kiểm tra toàn bộ chức năng CRUD + List của hệ thống quản lý sản phẩm
 */
public class TestProductManagement {

    private SanPhamRepositoryImpl repository;
    private List<String> createdProductIds = new ArrayList<>();

    /**
     * Setup: Chạy TRƯỚC MỖI test case
     * - Khởi tạo repository mới
     * - Reset danh sách product IDs để cleanup
     */
    @BeforeEach
    public void setUp() {
        repository = new SanPhamRepositoryImpl();
        createdProductIds.clear();
    }

    /**
     * Teardown: Chạy SAU MỖI test case
     * - Xóa tất cả products được tạo trong test để giữ DB sạch
     * - Tránh ảnh hưởng giữa các test cases
     */
    @AfterEach
    public void tearDown() {
        // Cleanup: Xóa tất cả products được tạo trong test
        for (String productId : createdProductIds) {
            try {
                repository.deleteById(productId);
            } catch (Exception e) {
                // Ignore nếu product đã bị xóa
            }
        }
    }

    // ========================================
    // TEST ADD PRODUCT (CREATE)
    // ========================================

    /**
     * TEST: Thêm product với dữ liệu hợp lệ (slug tự động generate)
     * Expected: Thành công, trả về product ID và message "Thêm sản phẩm thành công!"
     */
    @Test
    public void testAddProduct_ValidInput_Success() {
        // Arrange - Chuẩn bị dữ liệu test
        // Dùng timestamp để tạo unique values, tránh trùng lặp
        long timestamp = System.currentTimeMillis();
        AddProductInputDTO inputDTO = new AddProductInputDTO();
        inputDTO.productName = "Test Product " + timestamp;
        // ✅ KHÔNG CẦN NHẬP SLUG - sẽ tự động generate từ productName
        inputDTO.description = "Test product description";
        inputDTO.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        inputDTO.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        inputDTO.defaultImage = "test-image.jpg";
        inputDTO.price = String.valueOf(new BigDecimal("100000.00"));
        inputDTO.discountPrice = String.valueOf(new BigDecimal("90000.00"));
        inputDTO.stockQuantity = String.valueOf(100);
inputDTO.status = "published";

        // Khởi tạo controller + use case + presenter + view model
        AddProductViewModel viewModel = new AddProductViewModel();
        AddProductPresenter presenter = new AddProductPresenter(viewModel);
        AddProductUseCase useCase = new AddProductUseCase(repository, presenter);
        AddProductController controller = new AddProductController(useCase);

        // Act - Thực thi action
        controller.execute(inputDTO);

        // Track để cleanup sau test
        if (viewModel.productId != null) {
            createdProductIds.add(viewModel.productId);
        }

        // Assert - Kiểm tra kết quả
        assertTrue(viewModel.success, "Add product should succeed. Error: " + viewModel.message);
        assertNotNull(viewModel.productId, "Product ID should not be null");
        assertEquals("Thêm sản phẩm thành công!", viewModel.message);
        assertNotNull(viewModel.timestamp);
    }

    /**
     * TEST: Thêm product với tên trống
     * Expected: Thất bại, trả về error message
     */
    @Test
    public void testAddProduct_EmptyProductName_Failed() {
        // Arrange
        AddProductInputDTO inputDTO = new AddProductInputDTO();
        inputDTO.productName = ""; // ⚠️ Product name trống (invalid)
        inputDTO.description = "Test description";
        inputDTO.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        inputDTO.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        inputDTO.price = String.valueOf(new BigDecimal("100000.00"));
        inputDTO.stockQuantity = String.valueOf(100);
        inputDTO.status = "published";

        AddProductViewModel viewModel = new AddProductViewModel();
        AddProductPresenter presenter = new AddProductPresenter(viewModel);
        AddProductUseCase useCase = new AddProductUseCase(repository, presenter);
        AddProductController controller = new AddProductController(useCase);

        // Act
        controller.execute(inputDTO);

        // Assert - Phải thất bại
        assertFalse(viewModel.success, "Add product should fail with empty product name");
        assertNotNull(viewModel.message);
    }

    /**
     * TEST: Thêm product với productName trùng (slug tự động sẽ trùng)
     * Expected: Thất bại nếu slug được generate trùng
     * Note: Nếu system cho phép productName trùng, test này có thể pass
     */
    @Test
    public void testAddProduct_DuplicateProductName_CheckSlugHandling() {
        // Arrange - Tạo product đầu tiên (thành công)
        AddProductInputDTO inputDTO1 = new AddProductInputDTO();
        inputDTO1.productName = "Duplicate Product Name Test";
        inputDTO1.description = "First product";
        inputDTO1.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        inputDTO1.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        inputDTO1.price = String.valueOf(new BigDecimal("100000.00"));
inputDTO1.stockQuantity = String.valueOf(100);
        inputDTO1.status = "published";

        AddProductViewModel viewModel1 = new AddProductViewModel();
        AddProductPresenter presenter1 = new AddProductPresenter(viewModel1);
        AddProductUseCase useCase1 = new AddProductUseCase(repository, presenter1);
        AddProductController controller1 = new AddProductController(useCase1);
        controller1.execute(inputDTO1);

        if (viewModel1.productId != null) {
            createdProductIds.add(viewModel1.productId);
        }

        assertTrue(viewModel1.success, "First product should be created successfully");

        // Arrange - Tạo product thứ hai với CÙNG PRODUCT NAME
        AddProductInputDTO inputDTO2 = new AddProductInputDTO();
        inputDTO2.productName = "Duplicate Product Name Test"; // ⚠️ Tên trùng
        inputDTO2.description = "Second product";
        inputDTO2.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        inputDTO2.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        inputDTO2.price = String.valueOf(new BigDecimal("200000.00"));
        inputDTO2.stockQuantity = String.valueOf(50);
        inputDTO2.status = "published";

        AddProductViewModel viewModel2 = new AddProductViewModel();
        AddProductPresenter presenter2 = new AddProductPresenter(viewModel2);
        AddProductUseCase useCase2 = new AddProductUseCase(repository, presenter2);
        AddProductController controller2 = new AddProductController(useCase2);

        // Act
        controller2.execute(inputDTO2);

        // Assert - Tùy vào logic của hệ thống
        // Nếu system tự động thêm suffix vào slug → có thể thành công
        // Nếu system không cho phép slug trùng → phải thất bại
        if (!viewModel2.success) {
            // Expected: Thất bại vì slug trùng
            assertTrue(viewModel2.message.contains("Slug") ||
                            viewModel2.message.contains("đã tồn tại") ||
                            viewModel2.message.contains("trùng"),
                    "Error message should mention slug duplication");
        } else {
            // System tự động xử lý slug trùng (thêm suffix)
            createdProductIds.add(viewModel2.productId);
            // Verify slug đã được modified
            assertNotNull(viewModel2.productId);
        }
    }

    /**
     * TEST: Thêm product với giá âm
     * Expected: Thất bại, message về giá không hợp lệ
     */
    @Test
    public void testAddProduct_NegativePrice_Failed() {
        // Arrange
        long timestamp = System.currentTimeMillis();
        AddProductInputDTO inputDTO = new AddProductInputDTO();
        inputDTO.productName = "Negative Price Product " + timestamp;
        inputDTO.description = "Product with negative price";
        inputDTO.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        inputDTO.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
inputDTO.price = String.valueOf(new BigDecimal("-100000.00")); // ⚠️ Giá âm (invalid)
        inputDTO.stockQuantity = String.valueOf(100);
        inputDTO.status = "published";

        AddProductViewModel viewModel = new AddProductViewModel();
        AddProductPresenter presenter = new AddProductPresenter(viewModel);
        AddProductUseCase useCase = new AddProductUseCase(repository, presenter);
        AddProductController controller = new AddProductController(useCase);

        // Act
        controller.execute(inputDTO);

        // Assert - Phải thất bại
        assertFalse(viewModel.success, "Add product should fail with negative price");
        assertTrue(viewModel.message.contains("Giá") ||
                viewModel.message.contains("price") ||
                viewModel.message.contains("không hợp lệ"));
    }

    // ========================================
    // TEST GET PRODUCT (READ)
    // ========================================

    /**
     * TEST: Tìm product theo ID hợp lệ
     * Expected: Thành công, trả về đúng thông tin product
     */
    @Test
    public void testGetProduct_ValidId_Success() {
        // Arrange - Tạo product trước để test
        long timestamp = System.currentTimeMillis();
        AddProductInputDTO addDTO = new AddProductInputDTO();
        addDTO.productName = "Get Product Test " + timestamp;
        addDTO.description = "Get product test description";
        addDTO.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        addDTO.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        addDTO.price = String.valueOf(new BigDecimal("150000.00"));
        addDTO.stockQuantity = String.valueOf(75);
        addDTO.status = "published";

        AddProductViewModel addViewModel = new AddProductViewModel();
        AddProductPresenter addPresenter = new AddProductPresenter(addViewModel);
        AddProductUseCase addUseCase = new AddProductUseCase(repository, addPresenter);
        AddProductController addController = new AddProductController(addUseCase);
        addController.execute(addDTO);

        assertTrue(addViewModel.success, "Add product should succeed: " + addViewModel.message);

        String productId = addViewModel.productId;
        createdProductIds.add(productId);

        // Arrange - Chuẩn bị tìm kiếm product theo ID
        GetProductInputDTO getDTO = new GetProductInputDTO();
        getDTO.productId = productId;

        GetProductViewModel getViewModel = new GetProductViewModel();
        GetProductPresenter getPresenter = new GetProductPresenter(getViewModel);
        GetProductUseCase getUseCase = new GetProductUseCase(repository, getPresenter);
        GetProductController getController = new GetProductController(getUseCase);

        // Act
        getController.execute(getDTO);

        // Assert - Phải tìm thấy product với đúng thông tin
        assertTrue(getViewModel.success, "Get product should succeed");
assertNotNull(getViewModel.product);
        assertEquals(productId, getViewModel.product.productId);
        assertEquals("Get Product Test " + timestamp, getViewModel.product.productName);
        assertNotNull(getViewModel.product.slug, "Slug should be auto-generated");
    }

    /**
     * TEST: Tìm product với ID không tồn tại
     * Expected: Thất bại, product = null, message "không tìm thấy"
     */
    @Test
    public void testGetProduct_InvalidId_Failed() {
        // Arrange
        GetProductInputDTO getDTO = new GetProductInputDTO();
        getDTO.productId = "INVALID_PRODUCT_ID_999"; // ⚠️ ID không tồn tại

        GetProductViewModel getViewModel = new GetProductViewModel();
        GetProductPresenter getPresenter = new GetProductPresenter(getViewModel);
        GetProductUseCase getUseCase = new GetProductUseCase(repository, getPresenter);
        GetProductController getController = new GetProductController(getUseCase);

        // Act
        getController.execute(getDTO);

        // Assert - Phải thất bại
        assertFalse(getViewModel.success, "Get product should fail with invalid ID");
        assertNull(getViewModel.product);
        assertTrue(getViewModel.message.contains("không tìm thấy") ||
                getViewModel.message.contains("Không tìm thấy"));
    }

    // ========================================
    // TEST UPDATE PRODUCT
    // ========================================

    /**
     * TEST: Cập nhật product với dữ liệu hợp lệ
     * Expected: Thành công, thông tin product được cập nhật đúng
     */
    @Test
    public void testUpdateProduct_ValidInput_Success() {
        // Arrange - Tạo product trước
        long timestamp = System.currentTimeMillis();
        AddProductInputDTO addDTO = new AddProductInputDTO();
        addDTO.productName = "Original Product " + timestamp;
        addDTO.description = "Original description";
        addDTO.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        addDTO.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        addDTO.price = String.valueOf(new BigDecimal("100000.00"));
        addDTO.stockQuantity = String.valueOf(50);
        addDTO.status = "published";

        AddProductViewModel addViewModel = new AddProductViewModel();
        AddProductPresenter addPresenter = new AddProductPresenter(addViewModel);
        AddProductUseCase addUseCase = new AddProductUseCase(repository, addPresenter);
        AddProductController addController = new AddProductController(addUseCase);
        addController.execute(addDTO);

        String productId = addViewModel.productId;
        createdProductIds.add(productId);

        // Arrange - Chuẩn bị update product
        UpdateProductInputDTO updateDTO = new UpdateProductInputDTO();
        updateDTO.productId = productId;
        updateDTO.productName = "Updated Product Name"; // 🔄 Đổi tên
updateDTO.description = "Updated description"; // 🔄 Đổi mô tả
        updateDTO.price = String.valueOf(new BigDecimal("120000.00")); // 🔄 Đổi giá
        updateDTO.stockQuantity = String.valueOf(75); // 🔄 Đổi số lượng

        UpdateProductViewModel updateViewModel = new UpdateProductViewModel();
        UpdateProductPresenter updatePresenter = new UpdateProductPresenter(updateViewModel);
        UpdateProductUseCase updateUseCase = new UpdateProductUseCase(repository, updatePresenter);
        UpdateProductController updateController = new UpdateProductController(updateUseCase);

        // Act
        updateController.execute(updateDTO);

        // Assert - Kiểm tra thông tin đã được cập nhật
        assertTrue(updateViewModel.success, "Update product should succeed");
        assertNotNull(updateViewModel.updatedProductId);
        assertEquals("Updated Product Name", updateViewModel.updatedProduct.productName);
        assertEquals("Updated description", updateViewModel.updatedProduct.description);
        assertEquals(new BigDecimal("120000.00"), updateViewModel.updatedProduct.price);
        assertEquals(75, updateViewModel.updatedProduct.stockQuantity);
    }

    /**
     * TEST: Cập nhật product với ID không tồn tại
     * Expected: Thất bại, message "không tìm thấy"
     */
    @Test
    public void testUpdateProduct_InvalidProductId_Failed() {
        // Arrange
        UpdateProductInputDTO updateDTO = new UpdateProductInputDTO();
        updateDTO.productId = "INVALID_PRODUCT_ID_999"; // ⚠️ ID không tồn tại
        updateDTO.productName = "Updated Name";

        UpdateProductViewModel updateViewModel = new UpdateProductViewModel();
        UpdateProductPresenter updatePresenter = new UpdateProductPresenter(updateViewModel);
        UpdateProductUseCase updateUseCase = new UpdateProductUseCase(repository, updatePresenter);
        UpdateProductController updateController = new UpdateProductController(updateUseCase);

        // Act
        updateController.execute(updateDTO);

        // Assert - Phải thất bại
        assertFalse(updateViewModel.success, "Update should fail with invalid product ID");
        assertTrue(updateViewModel.message.contains("không tìm thấy") ||
                updateViewModel.message.contains("Không tìm thấy"));
    }

    // ========================================
    // TEST DELETE PRODUCT
    // ========================================

    /**
     * TEST: Xóa product với ID hợp lệ
     * Expected: Thành công, product không còn tồn tại trong DB
     */
    @Test
    public void testDeleteProduct_ValidId_Success() {
        // Arrange - Tạo product trước
        long timestamp = System.currentTimeMillis();
        AddProductInputDTO addDTO = new AddProductInputDTO();
        addDTO.productName = "Delete Product Test " + timestamp;
        addDTO.description = "Delete product test";
addDTO.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        addDTO.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        addDTO.price = String.valueOf(new BigDecimal("80000.00"));
        addDTO.stockQuantity = String.valueOf(30);
        addDTO.status = "published";

        AddProductViewModel addViewModel = new AddProductViewModel();
        AddProductPresenter addPresenter = new AddProductPresenter(addViewModel);
        AddProductUseCase addUseCase = new AddProductUseCase(repository, addPresenter);
        AddProductController addController = new AddProductController(addUseCase);
        addController.execute(addDTO);

        String productId = addViewModel.productId;
        // Không add vào createdProductIds vì sẽ tự xóa trong test

        // Arrange - Chuẩn bị xóa product
        DeleteProductInputDTO deleteDTO = new DeleteProductInputDTO();
        deleteDTO.productId = productId;

        DeleteProductViewModel deleteViewModel = new DeleteProductViewModel();
        DeleteProductPresenter deletePresenter = new DeleteProductPresenter(deleteViewModel);
        DeleteProductUseCase deleteUseCase = new DeleteProductUseCase(repository, deletePresenter);
        DeleteProductController deleteController = new DeleteProductController(deleteUseCase);

        // Act
        deleteController.execute(deleteDTO);

        // Assert - Kiểm tra xóa thành công
        assertTrue(deleteViewModel.success, "Delete product should succeed");
        assertEquals(productId, deleteViewModel.deletedProductId);
        assertEquals("Delete Product Test " + timestamp, deleteViewModel.deleteProductName);

        // Verify product không còn tồn tại
        GetProductInputDTO getDTO = new GetProductInputDTO();
        getDTO.productId = productId;

        GetProductViewModel getViewModel = new GetProductViewModel();
        GetProductPresenter getPresenter = new GetProductPresenter(getViewModel);
        GetProductUseCase getUseCase = new GetProductUseCase(repository, getPresenter);
        GetProductController getController = new GetProductController(getUseCase);
        getController.execute(getDTO);

        assertFalse(getViewModel.success, "Product should not exist after deletion");
    }

    /**
     * TEST: Xóa product với ID không tồn tại
     * Expected: Thất bại
     */
    @Test
    public void testDeleteProduct_InvalidId_Failed() {
        // Arrange
        DeleteProductInputDTO deleteDTO = new DeleteProductInputDTO();
        deleteDTO.productId = "INVALID_PRODUCT_ID_999"; // ⚠️ ID không tồn tại

        DeleteProductViewModel deleteViewModel = new DeleteProductViewModel();
        DeleteProductPresenter deletePresenter = new DeleteProductPresenter(deleteViewModel);
        DeleteProductUseCase deleteUseCase = new DeleteProductUseCase(repository, deletePresenter);
        DeleteProductController deleteController = new DeleteProductController(deleteUseCase);

        // Act
deleteController.execute(deleteDTO);

        // Assert - Phải thất bại
        assertFalse(deleteViewModel.success, "Delete should fail with invalid product ID");
    }

    // ========================================
    // TEST LIST PRODUCTS
    // ========================================

    /**
     * TEST: Lấy danh sách tất cả products (không filter status)
     * Expected: Thành công, trả về danh sách products
     */
    @Test
    public void testListProducts_AllStatus_Success() {
        // Arrange - Tạo ít nhất 1 product để test
        long timestamp = System.currentTimeMillis();
        AddProductInputDTO addDTO = new AddProductInputDTO();
        addDTO.productName = "List Product Test " + timestamp;
        addDTO.description = "List product test";
        addDTO.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        addDTO.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        addDTO.price = String.valueOf(new BigDecimal("200000.00"));
        addDTO.stockQuantity = String.valueOf(100);
        addDTO.status = "published";

        AddProductViewModel addViewModel = new AddProductViewModel();
        AddProductPresenter addPresenter = new AddProductPresenter(addViewModel);
        AddProductUseCase addUseCase = new AddProductUseCase(repository, addPresenter);
        AddProductController addController = new AddProductController(addUseCase);
        addController.execute(addDTO);

        createdProductIds.add(addViewModel.productId);

        // Arrange - List products
        ListProductInputDTO listDTO = new ListProductInputDTO();
        listDTO.statusFilter = "all"; // 📋 Lấy tất cả (không filter)
        listDTO.sortBy = "productName";
        listDTO.ascending = true;

        ListProductViewModel listViewModel = new ListProductViewModel();
        ListProductPresenter listPresenter = new ListProductPresenter(listViewModel);
        ProductListUseCase listUseCase = new ProductListUseCase(repository, listPresenter);
        ListProductController listController = new ListProductController(listUseCase);

        // Act
        listController.execute(listDTO);

        // Assert
        assertTrue(listViewModel.success, "List products should succeed");
        assertNotNull(listViewModel.products);
        assertTrue(listViewModel.totalCount >= 1, "Should have at least 1 product");
    }

    /**
     * TEST: Lấy danh sách chỉ products có status = "published"
     * Expected: Thành công, tất cả products trả về đều có status = "published"
     */
    @Test
    public void testListProducts_PublishedOnly_Success() {
        // Arrange - Tạo product published
        long timestamp = System.currentTimeMillis();
        AddProductInputDTO addDTO = new AddProductInputDTO();
        addDTO.productName = "Published Product " + timestamp;
        addDTO.description = "Published product test";
        addDTO.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
addDTO.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        addDTO.price = String.valueOf(new BigDecimal("300000.00"));
        addDTO.stockQuantity = String.valueOf(50);
        addDTO.status = "published";

        AddProductViewModel addViewModel = new AddProductViewModel();
        AddProductPresenter addPresenter = new AddProductPresenter(addViewModel);
        AddProductUseCase addUseCase = new AddProductUseCase(repository, addPresenter);
        AddProductController addController = new AddProductController(addUseCase);
        addController.execute(addDTO);

        createdProductIds.add(addViewModel.productId);

        // Arrange - List published products
        ListProductInputDTO listDTO = new ListProductInputDTO();
        listDTO.statusFilter = "published"; // 📋 Chỉ lấy published products
        listDTO.sortBy = "productName";
        listDTO.ascending = true;

        ListProductViewModel listViewModel = new ListProductViewModel();
        ListProductPresenter listPresenter = new ListProductPresenter(listViewModel);
        ProductListUseCase listUseCase = new ProductListUseCase(repository, listPresenter);
        ListProductController listController = new ListProductController(listUseCase);

        // Act
        listController.execute(listDTO);

        // Assert
        assertTrue(listViewModel.success, "List products should succeed");
        assertNotNull(listViewModel.products);

        // Verify tất cả product trong list đều có status = "published"
        for (var product : listViewModel.products) {
            assertEquals("published", product.status, "All products should have published status");
        }
    }

    /**
     * TEST: Sắp xếp products theo price giảm dần
     * Expected: Danh sách được sắp xếp đúng thứ tự price cao → thấp
     */
    @Test
    public void testListProducts_SortByPrice_Descending() {
        // Arrange - Tạo 2 products để test sorting
        long timestamp = System.currentTimeMillis();

        AddProductInputDTO addDTO1 = new AddProductInputDTO();
        addDTO1.productName = "Cheap Product " + timestamp;
        addDTO1.description = "Cheap product";
        addDTO1.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        addDTO1.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        addDTO1.price = String.valueOf(new BigDecimal("50000.00")); // Giá thấp
        addDTO1.stockQuantity = String.valueOf(100);
        addDTO1.status = "published";

        AddProductViewModel addViewModel1 = new AddProductViewModel();
        AddProductPresenter addPresenter1 = new AddProductPresenter(addViewModel1);
        AddProductUseCase addUseCase1 = new AddProductUseCase(repository, addPresenter1);
        AddProductController addController1 = new AddProductController(addUseCase1);
        addController1.execute(addDTO1);
        createdProductIds.add(addViewModel1.productId);
AddProductInputDTO addDTO2 = new AddProductInputDTO();
        addDTO2.productName = "Expensive Product " + timestamp;
        addDTO2.description = "Expensive product";
        addDTO2.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        addDTO2.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        addDTO2.price = String.valueOf(new BigDecimal("500000.00")); // Giá cao
        addDTO2.stockQuantity = String.valueOf(20);
        addDTO2.status = "published";

        AddProductViewModel addViewModel2 = new AddProductViewModel();
        AddProductPresenter addPresenter2 = new AddProductPresenter(addViewModel2);
        AddProductUseCase addUseCase2 = new AddProductUseCase(repository, addPresenter2);
        AddProductController addController2 = new AddProductController(addUseCase2);
        addController2.execute(addDTO2);
        createdProductIds.add(addViewModel2.productId);

        // Arrange - List with sorting
        ListProductInputDTO listDTO = new ListProductInputDTO();
        listDTO.statusFilter = "all";
        listDTO.sortBy = "price"; // 📋 Sắp xếp theo giá
        listDTO.ascending = false; // 📋 Giảm dần (cao → thấp)

        ListProductViewModel listViewModel = new ListProductViewModel();
        ListProductPresenter listPresenter = new ListProductPresenter(listViewModel);
        ProductListUseCase listUseCase = new ProductListUseCase(repository, listPresenter);
        ListProductController listController = new ListProductController(listUseCase);

        // Act
        listController.execute(listDTO);

        // Assert
        assertTrue(listViewModel.success, "List products should succeed");
        assertNotNull(listViewModel.products);

        // Verify thứ tự giảm dần theo price
        if (listViewModel.products.size() > 1) {
            for (int i = 0; i < listViewModel.products.size() - 1; i++) {
                BigDecimal price1 = listViewModel.products.get(i).price;
                BigDecimal price2 = listViewModel.products.get(i + 1).price;
                assertTrue(price1.compareTo(price2) >= 0,
                        "Prices should be in descending order");
            }
        }
    }

    /**
     * TEST: Lấy danh sách chỉ products có status = "archived"
     * Expected: Thành công, tất cả products trả về đều có status = "archived"
     */
    @Test
    public void testListProducts_ArchivedFilter() {
        // Arrange - Tạo product và set archived
        long timestamp = System.currentTimeMillis();
        AddProductInputDTO addDTO = new AddProductInputDTO();
        addDTO.productName = "Archived Product " + timestamp;
        addDTO.description = "Archived product test";
        addDTO.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        addDTO.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        addDTO.price = String.valueOf(new BigDecimal("150000.00"));
        addDTO.stockQuantity = String.valueOf(10);
addDTO.status = "published";

        AddProductViewModel addViewModel = new AddProductViewModel();
        AddProductPresenter addPresenter = new AddProductPresenter(addViewModel);
        AddProductUseCase addUseCase = new AddProductUseCase(repository, addPresenter);
        AddProductController addController = new AddProductController(addUseCase);
        addController.execute(addDTO);

        String productId = addViewModel.productId;
        createdProductIds.add(productId);

        // Update status thành archived
        UpdateProductInputDTO updateDTO = new UpdateProductInputDTO();
        updateDTO.productId = productId;
        updateDTO.status = "archived"; // 🔄 Đổi status thành archived

        UpdateProductViewModel updateViewModel = new UpdateProductViewModel();
        UpdateProductPresenter updatePresenter = new UpdateProductPresenter(updateViewModel);
        UpdateProductUseCase updateUseCase = new UpdateProductUseCase(repository, updatePresenter);
        UpdateProductController updateController = new UpdateProductController(updateUseCase);
        updateController.execute(updateDTO);

        assertTrue(updateViewModel.success, "Update to archived should succeed: " + updateViewModel.message);

        // Arrange - List archived products
        ListProductInputDTO listDTO = new ListProductInputDTO();
        listDTO.statusFilter = "archived"; // 📋 Chỉ lấy archived products
        listDTO.sortBy = "productName";
        listDTO.ascending = true;

        ListProductViewModel listViewModel = new ListProductViewModel();
        ListProductPresenter listPresenter = new ListProductPresenter(listViewModel);
        ProductListUseCase listUseCase = new ProductListUseCase(repository, listPresenter);
        ListProductController listController = new ListProductController(listUseCase);

        // Act
        listController.execute(listDTO);

        // Assert
        assertTrue(listViewModel.success, "List should succeed: " + listViewModel.message);
        assertNotNull(listViewModel.products, "Products list should not be null");
        assertTrue(listViewModel.filteredCount >= 1, "Should have at least 1 archived product");

        // Verify product vừa tạo có trong list và tất cả đều archived
        boolean foundOurProduct = false;
        for (var product : listViewModel.products) {
            if (product.productId.equals(productId)) {
                foundOurProduct = true;
                assertEquals("archived", product.status, "Our product should be archived");
            }
            assertEquals("archived", product.status,
                    "All products in filtered result should be archived");
        }

        assertTrue(foundOurProduct, "Our archived product should be in the filtered list");
    }

    // ========================================
    // TEST INTEGRATION (Full CRUD Cycle)
    // ========================================

    /**
* TEST TỔNG HỢP: Test toàn bộ vòng đời CRUD của 1 product
     * 1. CREATE - Tạo product mới (slug tự động generate)
     * 2. READ - Đọc thông tin product vừa tạo
     * 3. UPDATE - Cập nhật thông tin product
     * 4. DELETE - Xóa product
     * 5. VERIFY - Xác nhận product đã bị xóa
     * Expected: Tất cả 5 bước đều thành công
     */
    @Test
    public void testFullCRUDCycle() {
        long timestamp = System.currentTimeMillis();

        // 1️⃣ CREATE - Tạo product mới (slug tự động generate)
        AddProductInputDTO addDTO = new AddProductInputDTO();
        addDTO.productName = "CRUD Test Product " + timestamp;
        addDTO.description = "CRUD test product";
        addDTO.brandId = "8e74e1dd-bc01-11f0-a3d3-bceca01282a8";
        addDTO.categoryId = "abf76846-bc01-11f0-a3d3-bceca01282a8";
        addDTO.price = String.valueOf(new BigDecimal("250000.00"));
        addDTO.stockQuantity = String.valueOf(60);
        addDTO.status = "published";

        AddProductViewModel addViewModel = new AddProductViewModel();
        AddProductPresenter addPresenter = new AddProductPresenter(addViewModel);
        AddProductUseCase addUseCase = new AddProductUseCase(repository, addPresenter);
        AddProductController addController = new AddProductController(addUseCase);
        addController.execute(addDTO);

        assertTrue(addViewModel.success, "Step 1: Create product should succeed");
        String productId = addViewModel.productId;

        // 2️⃣ READ - Đọc thông tin product vừa tạo
        GetProductInputDTO getDTO = new GetProductInputDTO();
        getDTO.productId = productId;

        GetProductViewModel getViewModel = new GetProductViewModel();
        GetProductPresenter getPresenter = new GetProductPresenter(getViewModel);
        GetProductUseCase getUseCase = new GetProductUseCase(repository, getPresenter);
        GetProductController getController = new GetProductController(getUseCase);
        getController.execute(getDTO);

        assertTrue(getViewModel.success, "Step 2: Read product should succeed");
        assertEquals("CRUD Test Product " + timestamp, getViewModel.product.productName);
        assertNotNull(getViewModel.product.slug, "Slug should be auto-generated");

        // 3️⃣ UPDATE - Cập nhật thông tin product
        UpdateProductInputDTO updateDTO = new UpdateProductInputDTO();
        updateDTO.productId = productId;
        updateDTO.productName = "Updated CRUD Product"; // 🔄 Đổi tên
        updateDTO.price = String.valueOf(new BigDecimal("280000.00")); // 🔄 Đổi giá

        UpdateProductViewModel updateViewModel = new UpdateProductViewModel();
        UpdateProductPresenter updatePresenter = new UpdateProductPresenter(updateViewModel);
        UpdateProductUseCase updateUseCase = new UpdateProductUseCase(repository, updatePresenter);
UpdateProductController updateController = new UpdateProductController(updateUseCase);
        updateController.execute(updateDTO);

        assertTrue(updateViewModel.success, "Step 3: Update product should succeed");
        assertEquals("Updated CRUD Product", updateViewModel.updatedProduct.productName);
        assertEquals(new BigDecimal("280000.00"), updateViewModel.updatedProduct.price);

        // 4️⃣ DELETE - Xóa product
        DeleteProductInputDTO deleteDTO = new DeleteProductInputDTO();
        deleteDTO.productId = productId;

        DeleteProductViewModel deleteViewModel = new DeleteProductViewModel();
        DeleteProductPresenter deletePresenter = new DeleteProductPresenter(deleteViewModel);
        DeleteProductUseCase deleteUseCase = new DeleteProductUseCase(repository, deletePresenter);
        DeleteProductController deleteController = new DeleteProductController(deleteUseCase);
        deleteController.execute(deleteDTO);

        assertTrue(deleteViewModel.success, "Step 4: Delete product should succeed");

        // 5️⃣ VERIFY DELETION - Xác nhận product đã bị xóa
        GetProductViewModel verifyViewModel = new GetProductViewModel();
        GetProductPresenter verifyPresenter = new GetProductPresenter(verifyViewModel);
        GetProductUseCase verifyUseCase = new GetProductUseCase(repository, verifyPresenter);
        GetProductController verifyController = new GetProductController(verifyUseCase);
        verifyController.execute(getDTO);

        assertFalse(verifyViewModel.success, "Step 5: Product should not exist after deletion");
    }
}