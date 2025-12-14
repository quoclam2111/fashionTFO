package fashionTFO;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quanlydonhang.QuanLyDonHangOutputBoundary;
import quanlydonhang.QuanLyDonHangRequestData;
import quanlydonhang.QuanLyDonHangResponseData;
import quanlydonhang.them.AddOrderUseCase;
import quanlydonhang.them.ResponseDataAddOrder;
import repository.AddOrderRepoGateway;
import repository.DTO.OrderDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@DisplayName("Test Add Order Use Case - ThemHoaDonUseCase")
class AddOrderUseCaseTest {

    private AddOrderRepoGateway mockRepository;
    private QuanLyDonHangOutputBoundary mockPresenter;
    private AddOrderUseCase useCase;

    @BeforeEach
    void setUp() {
        mockRepository = mock(AddOrderRepoGateway.class);
        mockPresenter = mock(QuanLyDonHangOutputBoundary.class);
        useCase = new AddOrderUseCase(mockRepository, mockPresenter);
    }

    // ========== KỊCH BẢN 1: Admin nhập các thông tin hợp lệ ==========
    @Test
    @DisplayName("Kịch bản 1 - Tất cả thông tin hợp lệ (1,2,3,4,5)")
    void testScenario1_AllValidInputs() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, times(1)).save(any(OrderDTO.class));
        verify(mockPresenter, times(1)).present(any(QuanLyDonHangResponseData.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertTrue(response.success);
        assertEquals("Tạo đơn hàng thành công!", response.message);

        ResponseDataAddOrder addResponse = (ResponseDataAddOrder) response;
        assertNotNull(addResponse.addedOrderId);
        assertEquals("pending", addResponse.orderStatus);
    }

    // ========== KỊCH BẢN 2: Admin không nhập User ID ==========
    @Test
    @DisplayName("Kịch bản 2 - User ID null (1, 2.1, 3.1, 4, 5)")
    void testScenario2_UserIdNull() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.userId = null;

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    @Test
    @DisplayName("Kịch bản 2 - User ID empty (1, 2.1, 3.1, 4, 5)")
    void testScenario2_UserIdEmpty() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.userId = "";

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    // ========== KỊCH BẢN 3: Admin nhập User ID không hợp lệ ==========
    // TODO: Cần implement validation ký tự đặc biệt trong Order.checkUserId()
    /*
    @Test
    @DisplayName("Kịch bản 3 - User ID có ký tự đặc biệt (1, 2.1.1, 3.1, 4, 5)")
    void testScenario3_UserIdWithSpecialChars() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.userId = "user@123!";

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }
    */

    // ========== KỊCH BẢN 4: Admin nhập User ID nhưng không nhập Tên khách hàng ==========
    @Test
    @DisplayName("Kịch bản 4 - Customer Name null (1, 2.1.1.1, 2.1.1.2, 3.1, 4, 5)")
    void testScenario4_CustomerNameNull() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerName = null;

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    @Test
    @DisplayName("Kịch bản 4 - Customer Name empty (1, 2.1.1.1, 2.1.1.2, 3.1, 4, 5)")
    void testScenario4_CustomerNameEmpty() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerName = "";

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    // ========== KỊCH BẢN 5: Admin nhập User ID, Tên KH nhưng không nhập SĐT ==========
    @Test
    @DisplayName("Kịch bản 5 - Phone null (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2, 3.1, 4, 5)")
    void testScenario5_PhoneNull() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerPhone = null;

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    @Test
    @DisplayName("Kịch bản 5 - Phone empty (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2, 3.1, 4, 5)")
    void testScenario5_PhoneEmpty() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerPhone = "";

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    // ========== KỊCH BẢN 6: Admin nhập User ID, Tên KH nhưng SĐT không hợp lệ ==========
    @Test
    @DisplayName("Kịch bản 6 - SĐT không đúng 10-11 số (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1, 3.1, 4, 5)")
    void testScenario6_PhoneInvalidLength() {
        // Arrange - Test với SĐT 9 số
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerPhone = "012345678";

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    @Test
    @DisplayName("Kịch bản 6 - SĐT không bắt đầu bằng số 0 (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1, 3.1, 4, 5)")
    void testScenario6_PhoneNotStartWithZero() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerPhone = "1234567890";

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    @Test
    @DisplayName("Kịch bản 6 - SĐT có ký tự không phải số (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1, 3.1, 4, 5)")
    void testScenario6_PhoneWithNonNumeric() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerPhone = "0123abc789";

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    // ========== KỊCH BẢN 7: Admin nhập User ID, Tên KH, SĐT nhưng không nhập địa chỉ ==========
    @Test
    @DisplayName("Kịch bản 7 - Address null (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1.1, 2.1.1.2.1.2.1.2, 3.1, 4, 5)")
    void testScenario7_AddressNull() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerAddress = null;

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    @Test
    @DisplayName("Kịch bản 7 - Address empty (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1.1, 2.1.1.2.1.2.1.2, 3.1, 4, 5)")
    void testScenario7_AddressEmpty() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerAddress = "";

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    // ========== KỊCH BẢN 8: Admin nhập đầy đủ thông tin nhưng không nhập tổng tiền ==========
    @Test
    @DisplayName("Kịch bản 8 - Total amount = 0 (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1.1, 2.1.1.2.1.2.1.1.1, 2.1.1.2.1.2.1.2.2, 3.1, 4, 5)")
    void testScenario8_TotalAmountZero() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.totalAmount = 0;

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    @Test
    @DisplayName("Kịch bản 8 - Total amount âm (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1.1, 2.1.1.2.1.2.1.1.1, 2.1.1.2.1.2.1.2.2, 3.1, 4, 5)")
    void testScenario8_TotalAmountNegative() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.totalAmount = -100;

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, never()).save(any(OrderDTO.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    // ========== KỊCH BẢN 9: Admin nhập đầy đủ thông tin hợp lệ (bao gồm note) ==========
    @Test
    @DisplayName("Kịch bản 9 - Tất cả thông tin hợp lệ + note (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1.1, 2.1.1.2.1.2.1.1.1, 2.1.1.2.1.2.1.2.2.1, 3.1, 4, 5)")
    void testScenario9_AllValidInputsWithNote() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.note = "Giao hàng giờ hành chính";

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, times(1)).save(argThat(dto ->
                dto.note != null && dto.note.equals("Giao hàng giờ hành chính")
        ));
        verify(mockPresenter, times(1)).present(any(QuanLyDonHangResponseData.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertTrue(response.success);
        assertEquals("Tạo đơn hàng thành công!", response.message);

        ResponseDataAddOrder addResponse = (ResponseDataAddOrder) response;
        assertNotNull(addResponse.addedOrderId);
        assertEquals("pending", addResponse.orderStatus);
    }

    @Test
    @DisplayName("Kịch bản 9 - Tất cả thông tin hợp lệ, note null (optional) (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1.1, 2.1.1.2.1.2.1.1.1, 2.1.1.2.1.2.1.2.2.1, 3.1, 4, 5)")
    void testScenario9_AllValidInputsWithNullNote() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.note = null;

        // Act
        useCase.control(request);

        // Assert
        verify(mockRepository, times(1)).save(any(OrderDTO.class));
        verify(mockPresenter, times(1)).present(any(QuanLyDonHangResponseData.class));

        QuanLyDonHangResponseData response = useCase.getRes();
        assertTrue(response.success);
        assertEquals("Tạo đơn hàng thành công!", response.message);
    }

    // Helper method để tạo request hợp lệ
    private QuanLyDonHangRequestData createValidRequest() {
        QuanLyDonHangRequestData request = new QuanLyDonHangRequestData();
        request.action = "add";
        request.userId = "user123";
        request.customerName = "Nguyễn Văn A";
        request.customerPhone = "0123456789";
        request.customerAddress = "123 Đường ABC, Quận 1, TP.HCM";
        request.totalAmount = 500000;
        request.note = null;
        return request;
    }
}