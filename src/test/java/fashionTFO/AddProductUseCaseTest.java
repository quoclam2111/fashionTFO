package fashionTFO;

import org.junit.jupiter.api.*;
import org.mockito.*;
import quanlysanpham.add.AddProductUseCase;
import quanlysanpham.add.ResponseDataAddProduct;
import repository.DTO.ProductDTO;
import repository.sanpham.AddSanPhamRepository;
import quanlysanpham.*;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Test Thêm Sản Phẩm - AddProductUseCase")
class AddProductUseCaseTest {

    @Mock
    private AddSanPhamRepository mockRepository;

    @Mock
    private ProductOutputBoundary mockPresenter;

    @InjectMocks
    private AddProductUseCase addProductUseCase;

    private ProductRequestData validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addProductUseCase = new AddProductUseCase(mockRepository, mockPresenter);

        // Tạo request hợp lệ mẫu
        validRequest = new ProductRequestData();
        validRequest.action = "add";
        validRequest.productName = "Áo thun nam basic";
        validRequest.description = "Áo thun cotton 100% cao cấp";
        validRequest.brandId = "BRAND001";
        validRequest.categoryId = "CAT001";
        validRequest.defaultImage = "image.jpg";
        validRequest.price = new BigDecimal("250000");
        validRequest.discountPrice = new BigDecimal("200000");
        validRequest.stockQuantity = 100;
        validRequest.status = "PUBLISHED";
    }

    // ==================== KỊCH BẢN 1: TÊN SẢN PHẨM KHÔNG HỢP LỆ ====================

    @Nested
    @DisplayName("Kịch bản 1: Tên sản phẩm không hợp lệ")
    class TestCase_TenSanPhamKhongHopLe {

        @Test
        @DisplayName("1.2.1: Tên sản phẩm null - Should fail")
        void testTenSanPhamNull() {
            // Given
            validRequest.productName = null;

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertFalse(response.success);
            // Slug generation xảy ra trước validate, nên message là từ generateSlug()
            assertEquals("Cannot generate slug because product name is empty", response.message);
            verify(mockRepository, never()).addSanPham(any());
            verify(mockPresenter, times(1)).present(any());
        }

        @Test
        @DisplayName("1.2.3: Tên sản phẩm rỗng - Should fail")
        void testTenSanPhamRong() {
            // Given
            validRequest.productName = "";

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertFalse(response.success);
            // Slug generation xảy ra trước validate
            assertEquals("Cannot generate slug because product name is empty", response.message);
            verify(mockRepository, never()).addSanPham(any());
        }

        @Test
        @DisplayName("1.2.4: Tên sản phẩm chỉ có khoảng trắng - Should fail")
        void testTenSanPhamChiCoKhoangTrang() {
            // Given
            validRequest.productName = "   ";

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertFalse(response.success);
            // Slug generation xảy ra trước validate
            assertEquals("Cannot generate slug because product name is empty", response.message);
        }

        @Test
        @DisplayName("1.2.5: Tên sản phẩm hợp lệ - Should pass")
        void testTenSanPhamHopLe() {
            // Given
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);
            doNothing().when(mockRepository).addSanPham(any());

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertTrue(response.success);
            assertEquals("PRODUCT ADDED SUCCESSFULLY", response.message);
            verify(mockRepository, times(1)).addSanPham(any());
        }
    }

    // ==================== KỊCH BẢN 2: TÊN SẢN PHẨM HỢP LỆ NHƯNG GIÁ KHÔNG HỢP LỆ ====================

    @Nested
    @DisplayName("Kịch bản 2: Tên sản phẩm hợp lệ nhưng giá không hợp lệ")
    class TestCase_GiaKhongHopLe {

        @Test
        @DisplayName("2.1.1: Giá null - Should fail")
        void testGiaNull() {
            // Given
            validRequest.price = null;

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertFalse(response.success);
            assertEquals("Price cannot be null or negative", response.message);
            verify(mockRepository, never()).addSanPham(any());
        }

        @Test
        @DisplayName("2.1.2.1.1: Giá âm - Should fail")
        void testGiaAm() {
            // Given
            validRequest.price = new BigDecimal("-100000");

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertFalse(response.success);
            assertEquals("Price cannot be null or negative", response.message);
        }

        @Test
        @DisplayName("2.1.2.1.2: Giá = 0 - Should pass (theo code hiện tại)")
        void testGiaBangKhong() {
            // Given
            validRequest.price = BigDecimal.ZERO;
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertTrue(response.success);
            assertEquals("PRODUCT ADDED SUCCESSFULLY", response.message);
        }

        @Test
        @DisplayName("2.1.2.1.2.1: Giá dương hợp lệ - Should pass")
        void testGiaDuongHopLe() {
            // Given
            validRequest.price = new BigDecimal("500000");
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertTrue(response.success);
            assertEquals("PRODUCT ADDED SUCCESSFULLY", response.message);
        }
    }

    // ==================== KỊCH BẢN 3: TÊN VÀ GIÁ HỢP LỆ NHƯNG SỐ LƯỢNG KHÔNG HỢP LỆ ====================

    @Nested
    @DisplayName("Kịch bản 3: Tên và giá hợp lệ nhưng số lượng không hợp lệ")
    class TestCase_SoLuongKhongHopLe {

        @Test
        @DisplayName("2.1.1.1: Số lượng âm - Should fail")
        void testSoLuongAm() {
            // Given
            validRequest.stockQuantity = -10;

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertFalse(response.success);
            assertEquals("Stock quantity cannot be negative", response.message);
            verify(mockRepository, never()).addSanPham(any());
        }

        @Test
        @DisplayName("2.1.1.2.1: Số lượng = 0 - Should pass")
        void testSoLuongBangKhong() {
            // Given
            validRequest.stockQuantity = 0;
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertTrue(response.success);
            assertEquals("PRODUCT ADDED SUCCESSFULLY", response.message);
        }

        @Test
        @DisplayName("2.1.1.2.1.1: Số lượng dương - Should pass")
        void testSoLuongDuong() {
            // Given
            validRequest.stockQuantity = 50;
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertTrue(response.success);
            assertEquals("PRODUCT ADDED SUCCESSFULLY", response.message);
            verify(mockRepository, times(1)).addSanPham(any());
        }
    }

    // ==================== KỊCH BẢN 4: CÁC GIÁ TRỊ HỢP LỆ ====================

    @Nested
    @DisplayName("Kịch bản 4: Các giá trị hợp lệ")
    class TestCase_CacGiaTriHopLe {

        @Test
        @DisplayName("1.2: Tên sản phẩm đã tồn tại - Should fail")
        void testTenSanPhamDaTonTai() {
            // Given
            when(mockRepository.existsByProductName(validRequest.productName)).thenReturn(true);

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertFalse(response.success);
            assertEquals("PRODUCT NAME ALREADY EXISTS", response.message);
            verify(mockRepository, never()).addSanPham(any());
        }

        @Test
        @DisplayName("2.1: Tất cả thông tin hợp lệ - Should pass")
        void testThemSanPhamThanhCong() {
            // Given
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);
            doNothing().when(mockRepository).addSanPham(any());

            // When
            addProductUseCase.control(validRequest);
            ResponseDataAddProduct response = (ResponseDataAddProduct) addProductUseCase.getRes();

            // Then
            assertTrue(response.success);
            assertEquals("PRODUCT ADDED SUCCESSFULLY", response.message);
            assertNotNull(response.productId);
            assertNotNull(response.productName);
            assertNotNull(response.timestamp);
            assertEquals("Áo thun nam basic", response.productName);

            // Verify repository được gọi đúng
            ArgumentCaptor<ProductDTO> dtoCaptor = ArgumentCaptor.forClass(ProductDTO.class);
            verify(mockRepository, times(1)).addSanPham(dtoCaptor.capture());

            ProductDTO capturedDTO = dtoCaptor.getValue();
            assertEquals("Áo thun nam basic", capturedDTO.productName);
            assertEquals(new BigDecimal("250000"), capturedDTO.price);
            assertEquals(100, capturedDTO.stockQuantity);

            // Verify presenter được gọi
            verify(mockPresenter, times(1)).present(any());
        }

        @Test
        @DisplayName("3.1: Hệ thống lưu dữ liệu thành công")
        void testHeThongLuuDuLieuThanhCong() {
            // Given
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);
            doNothing().when(mockRepository).addSanPham(any());

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertTrue(response.success);
            verify(mockRepository, times(1)).addSanPham(any());
        }

        @Test
        @DisplayName("Test với các trường optional null")
        void testVoiCacTruongOptionalNull() {
            // Given
            validRequest.description = null;
            validRequest.defaultImage = null;
            validRequest.discountPrice = null;
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertTrue(response.success);
            verify(mockRepository, times(1)).addSanPham(any());
        }
    }

    // ==================== TEST EXCEPTION HANDLING ====================

    @Nested
    @DisplayName("Exception Handling")
    class TestCase_ExceptionHandling {

        @Test
        @DisplayName("3: Hệ thống lưu dữ liệu không thành công")
        void testHeThongLuuDuLieuKhongThanhCong() {
            // Given
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);
            doThrow(new RuntimeException("Database error")).when(mockRepository).addSanPham(any());

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertFalse(response.success);
            assertEquals("ERROR_ADDING_PRODUCT", response.message);
        }

        @Test
        @DisplayName("Test IllegalArgumentException trong validate")
        void testIllegalArgumentException() {
            // Given
            validRequest.price = new BigDecimal("-100");

            // When
            addProductUseCase.control(validRequest);
            ProductResponseData response = addProductUseCase.getRes();

            // Then
            assertFalse(response.success);
            assertTrue(response.message.contains("Price cannot be null or negative"));
        }
    }

    // ==================== TEST AUTO-GENERATED FIELDS ====================

    @Nested
    @DisplayName("Test các trường tự động sinh")
    class TestCase_AutoGeneratedFields {

        @Test
        @DisplayName("ProductId tự động sinh UUID")
        void testProductIdTuDongSinh() {
            // Given
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);

            // When
            addProductUseCase.control(validRequest);
            ResponseDataAddProduct response = (ResponseDataAddProduct) addProductUseCase.getRes();

            // Then
            assertNotNull(response.productId);
            assertTrue(response.productId.length() > 0);
        }

        @Test
        @DisplayName("Slug tự động generate từ productName")
        void testSlugTuDongGenerate() {
            // Given
            validRequest.productName = "Áo Thun Nam Basic";
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);

            // When
            addProductUseCase.control(validRequest);

            // Then - Verify slug được tạo đúng format
            ArgumentCaptor<ProductDTO> dtoCaptor = ArgumentCaptor.forClass(ProductDTO.class);
            verify(mockRepository).addSanPham(dtoCaptor.capture());

            ProductDTO dto = dtoCaptor.getValue();
            assertNotNull(dto.slug);
            assertTrue(dto.slug.contains("ao-thun-nam-basic"));
        }

        @Test
        @DisplayName("Status mặc định là PUBLISHED")
        void testStatusMacDinhPublished() {
            // Given
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);

            // When
            addProductUseCase.control(validRequest);

            // Then
            ArgumentCaptor<ProductDTO> dtoCaptor = ArgumentCaptor.forClass(ProductDTO.class);
            verify(mockRepository).addSanPham(dtoCaptor.capture());

            assertEquals("PUBLISHED", dtoCaptor.getValue().status);
        }

        @Test
        @DisplayName("CreatedAt và UpdatedAt tự động set")
        void testTimestampTuDongSet() {
            // Given
            when(mockRepository.existsByProductName(anyString())).thenReturn(false);
            Date before = new Date();

            // When
            addProductUseCase.control(validRequest);

            // Then
            Date after = new Date();
            ArgumentCaptor<ProductDTO> dtoCaptor = ArgumentCaptor.forClass(ProductDTO.class);
            verify(mockRepository).addSanPham(dtoCaptor.capture());

            ProductDTO dto = dtoCaptor.getValue();
            assertNotNull(dto.createdAt);
            assertNotNull(dto.updatedAt);
            assertTrue(dto.createdAt.compareTo(before) >= 0);
            assertTrue(dto.createdAt.compareTo(after) <= 0);
        }
    }
}