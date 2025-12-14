package fashionTFO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import quanlydonhang.QuanLyDonHangOutputBoundary;
import quanlydonhang.QuanLyDonHangRequestData;
import quanlydonhang.QuanLyDonHangResponseData;
import quanlydonhang.add.AddOrderUseCase;
import quanlydonhang.add.ResponseDataAddOrder;
import repository.hoadon.AddOrderRepoGateway;
import repository.DTO.OrderDTO;

import java.util.Optional;

/**
 * ====================================================================
 * TEST CASES CHO ADD ORDER USE CASE
 * ====================================================================
 *
 * Mục đích: Kiểm tra tất cả validation rules và business logic khi tạo đơn hàng mới
 *
 * Coverage:
 * - Happy path: 2 test cases (có/không note)
 * - User ID validation: 2 test cases
 * - Customer name validation: 2 test cases
 * - Phone validation: 5 test cases
 * - Address validation: 2 test cases
 * - Total amount validation: 2 test cases
 *
 * Total: 15 test cases
 * ====================================================================
 */
@DisplayName("Add Order Use Case Tests")
public class AddOrderUseCaseTest {

    private AddOrderUseCase useCase;
    private TestRepository testRepository;
    private TestPresenter testPresenter;

    /**
     * Setup method - chạy trước mỗi test
     * Khởi tạo use case với test doubles (không dùng mock framework)
     */
    @BeforeEach
    void setUp() {
        testRepository = new TestRepository();
        testPresenter = new TestPresenter();
        useCase = new AddOrderUseCase(testRepository, testPresenter);
    }

    // ==================== HAPPY PATH - TẠO ĐƠN HÀNG THÀNH CÔNG ====================

    /**
     * TEST: Tạo đơn hàng thành công với tất cả thông tin hợp lệ (không có note)
     *
     * MỤC ĐÍCH: Kiểm tra flow chính khi admin tạo đơn hàng với đầy đủ thông tin bắt buộc
     *
     * KỊCH BẢN: Admin nhập các thông tin hợp lệ (1,2,3,4,5)
     *
     * INPUT:
     * - userId: "user123"
     * - customerName: "Nguyễn Văn A"
     * - customerPhone: "0123456789" (10 số, bắt đầu bằng 0)
     * - customerAddress: "123 Đường ABC, Quận 1, TP.HCM"
     * - totalAmount: 500000 (> 0)
     * - note: null (optional)
     *
     * EXPECTED:
     * - Repository.save() được gọi (savedOrder != null)
     * - Presenter.present() được gọi
     * - Response success = true
     * - Response message = "Tạo đơn hàng thành công!"
     * - Response có addedOrderId (not null)
     * - Order status = "pending"
     *
     * LÝ DO: Đây là happy path - tất cả input hợp lệ nên đơn hàng được tạo thành công
     */
    @Test
    @DisplayName("Tạo đơn hàng thành công với tất cả thông tin hợp lệ")
    void testCreateOrderSuccess_WithoutNote() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();

        // Act
        useCase.control(request);

        // Assert
        assertNotNull(testRepository.savedOrder, "Order phải được lưu vào repository");
        assertNotNull(testPresenter.presentedResponse, "Response phải được present");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertTrue(response.success, "Response success phải là true");
        assertEquals("Tạo đơn hàng thành công!", response.message);

        ResponseDataAddOrder addResponse = (ResponseDataAddOrder) response;
        assertNotNull(addResponse.addedOrderId, "Order ID phải được tạo");
        assertEquals("pending", addResponse.orderStatus);
    }

    /**
     * TEST: Tạo đơn hàng thành công với note
     *
     * MỤC ĐÍCH: Kiểm tra việc lưu note (optional field) khi có
     *
     * KỊCH BẢN: Admin nhập đầy đủ thông tin hợp lệ + note (1,2,3,4,5)
     *
     * INPUT: Giống test trên + note: "Giao hàng giờ hành chính"
     *
     * EXPECTED:
     * - Repository.save() được gọi với OrderDTO có note đúng
     * - Response success = true
     * - Response message = "Tạo đơn hàng thành công!"
     *
     * LÝ DO: Note là optional field nhưng khi có thì phải được lưu đúng
     */
    @Test
    @DisplayName("Tạo đơn hàng thành công với note")
    void testCreateOrderSuccess_WithNote() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.note = "Giao hàng giờ hành chính";  // ✅ Note optional

        // Act
        useCase.control(request);

        // Assert - Verify note được lưu đúng
        assertNotNull(testRepository.savedOrder, "Order phải được lưu");
        assertEquals("Giao hàng giờ hành chính", testRepository.savedOrder.note,
                "Note phải được lưu đúng");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertTrue(response.success);
        assertEquals("Tạo đơn hàng thành công!", response.message);

        ResponseDataAddOrder addResponse = (ResponseDataAddOrder) response;
        assertNotNull(addResponse.addedOrderId);
        assertEquals("pending", addResponse.orderStatus);
    }

    // ==================== USER ID VALIDATION ====================

    /**
     * TEST: User ID null
     *
     * MỤC ĐÍCH: Đảm bảo không cho phép tạo đơn hàng không có user ID
     *
     * KỊCH BẢN: Admin không nhập User ID (1, 2.1, 3.1, 4, 5)
     *
     * INPUT: userId = null
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi (savedOrder = null)
     * - Response success = false
     * - Response có message lỗi (not null)
     *
     * LÝ DO: User ID cần để xác định khách hàng, không được để trống
     */
    @Test
    @DisplayName("Ném lỗi khi User ID null")
    void testUserIdNull() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.userId = null;  // ❌ User ID null

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi userId null");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success, "Response success phải là false");
        assertNotNull(response.message, "Phải có message lỗi");
    }

    /**
     * TEST: User ID rỗng
     *
     * MỤC ĐÍCH: Đảm bảo không cho phép user ID rỗng
     *
     * KỊCH BẢN: Admin không nhập User ID (1, 2.1, 3.1, 4, 5)
     *
     * INPUT: userId = ""
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi
     * - Response success = false
     * - Response có message lỗi
     *
     * LÝ DO: User ID rỗng = không xác định được khách hàng
     */
    @Test
    @DisplayName("Ném lỗi khi User ID rỗng")
    void testUserIdEmpty() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.userId = "";  // ❌ User ID rỗng

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi userId rỗng");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    // ==================== CUSTOMER NAME VALIDATION ====================

    /**
     * TEST: Customer name null
     *
     * MỤC ĐÍCH: Đảm bảo không cho phép tên khách hàng null
     *
     * KỊCH BẢN: Admin nhập User ID nhưng không nhập tên khách hàng
     *           (1, 2.1.1.1, 2.1.1.2, 3.1, 4, 5)
     *
     * INPUT: customerName = null
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi
     * - Response success = false
     * - Response có message lỗi
     *
     * LÝ DO: Cần tên khách hàng để giao hàng và liên hệ
     */
    @Test
    @DisplayName("Ném lỗi khi tên khách hàng null")
    void testCustomerNameNull() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerName = null;  // ❌ Tên khách hàng null

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi customerName null");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    /**
     * TEST: Customer name rỗng
     *
     * MỤC ĐÍCH: Đảm bảo không cho phép tên khách hàng rỗng
     *
     * KỊCH BẢN: Admin nhập User ID nhưng không nhập tên khách hàng
     *           (1, 2.1.1.1, 2.1.1.2, 3.1, 4, 5)
     *
     * INPUT: customerName = ""
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi
     * - Response success = false
     * - Response có message lỗi
     *
     * LÝ DO: Tên khách hàng rỗng không có ý nghĩa
     */
    @Test
    @DisplayName("Ném lỗi khi tên khách hàng rỗng")
    void testCustomerNameEmpty() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerName = "";  // ❌ Tên khách hàng rỗng

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi customerName rỗng");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    // ==================== PHONE VALIDATION ====================

    /**
     * TEST: Phone null
     *
     * MỤC ĐÍCH: Đảm bảo không cho phép số điện thoại null
     *
     * KỊCH BẢN: Admin nhập User ID, Tên KH nhưng không nhập SĐT
     *           (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2, 3.1, 4, 5)
     *
     * INPUT: customerPhone = null
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi
     * - Response success = false
     * - Response có message lỗi
     *
     * LÝ DO: SĐT cần để liên hệ giao hàng
     */
    @Test
    @DisplayName("Ném lỗi khi số điện thoại null")
    void testPhoneNull() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerPhone = null;  // ❌ Phone null

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi phone null");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    /**
     * TEST: Phone rỗng
     *
     * MỤC ĐÍCH: Đảm bảo không cho phép số điện thoại rỗng
     *
     * KỊCH BẢN: Admin nhập User ID, Tên KH nhưng không nhập SĐT
     *           (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2, 3.1, 4, 5)
     *
     * INPUT: customerPhone = ""
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi
     * - Response success = false
     * - Response có message lỗi
     *
     * LÝ DO: SĐT rỗng không thể liên hệ được
     */
    @Test
    @DisplayName("Ném lỗi khi số điện thoại rỗng")
    void testPhoneEmpty() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerPhone = "";  // ❌ Phone rỗng

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi phone rỗng");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    /**
     * TEST: Phone không đúng độ dài (10-11 số)
     *
     * MỤC ĐÍCH: Đảm bảo SĐT phải có độ dài hợp lệ theo chuẩn VN
     *
     * KỊCH BẢN: Admin nhập User ID, Tên KH nhưng SĐT không hợp lệ
     *           (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1, 3.1, 4, 5)
     *
     * INPUT: customerPhone = "012345678" (9 số)
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi
     * - Response success = false
     * - Response có message lỗi
     *
     * LÝ DO: SĐT Việt Nam phải có 10-11 số
     * RULE: 10 số (mobile) hoặc 11 số (với mã vùng)
     */
    @Test
    @DisplayName("Ném lỗi khi SĐT không đúng 10-11 số")
    void testPhoneInvalidLength() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerPhone = "012345678";  // ❌ Chỉ 9 số (< 10)

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi phone length invalid");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    /**
     * TEST: Phone không bắt đầu bằng số 0
     *
     * MỤC ĐÍCH: Đảm bảo SĐT theo format VN (bắt đầu bằng 0)
     *
     * KỊCH BẢN: Admin nhập SĐT không đúng format VN
     *           (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1, 3.1, 4, 5)
     *
     * INPUT: customerPhone = "1234567890" (10 số nhưng không bắt đầu bằng 0)
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi
     * - Response success = false
     * - Response có message lỗi
     *
     * LÝ DO: SĐT Việt Nam phải bắt đầu bằng số 0
     * RULE: Format 0XXXXXXXXX (10-11 chữ số)
     */
    @Test
    @DisplayName("Ném lỗi khi SĐT không bắt đầu bằng số 0")
    void testPhoneNotStartWithZero() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerPhone = "1234567890";  // ❌ Không bắt đầu bằng 0

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi phone không bắt đầu bằng 0");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    /**
     * TEST: Phone chứa ký tự không phải số
     *
     * MỤC ĐÍCH: Đảm bảo SĐT chỉ chứa chữ số
     *
     * KỊCH BẢN: Admin nhập SĐT có ký tự không hợp lệ
     *           (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1, 3.1, 4, 5)
     *
     * INPUT: customerPhone = "0123abc789" (có chữ cái)
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi
     * - Response success = false
     * - Response có message lỗi
     *
     * LÝ DO: SĐT chỉ được chứa chữ số, không có chữ cái hay ký tự đặc biệt
     * INVALID: "012-345-6789", "012 345 6789", "0123abc789"
     */
    @Test
    @DisplayName("Ném lỗi khi SĐT có ký tự không phải số")
    void testPhoneWithNonNumeric() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerPhone = "0123abc789";  // ❌ Có chữ cái

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi phone có ký tự không phải số");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    // ==================== ADDRESS VALIDATION ====================

    /**
     * TEST: Address null
     *
     * MỤC ĐÍCH: Đảm bảo không cho phép địa chỉ null
     *
     * KỊCH BẢN: Admin nhập User ID, Tên KH, SĐT nhưng không nhập địa chỉ
     *           (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1.1, 2.1.1.2.1.2.1.2, 3.1, 4, 5)
     *
     * INPUT: customerAddress = null
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi
     * - Response success = false
     * - Response có message lỗi
     *
     * LÝ DO: Địa chỉ cần để giao hàng
     */
    @Test
    @DisplayName("Ném lỗi khi địa chỉ null")
    void testAddressNull() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerAddress = null;  // ❌ Địa chỉ null

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi address null");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    /**
     * TEST: Address rỗng
     *
     * MỤC ĐÍCH: Đảm bảo không cho phép địa chỉ rỗng
     *
     * KỊCH BẢN: Admin nhập User ID, Tên KH, SĐT nhưng không nhập địa chỉ
     *           (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1.1, 2.1.1.2.1.2.1.2, 3.1, 4, 5)
     *
     * INPUT: customerAddress = ""
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi
     * - Response success = false
     * - Response có message lỗi
     *
     * LÝ DO: Địa chỉ rỗng không thể giao hàng được
     */
    @Test
    @DisplayName("Ném lỗi khi địa chỉ rỗng")
    void testAddressEmpty() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.customerAddress = "";  // ❌ Địa chỉ rỗng

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi address rỗng");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    // ==================== TOTAL AMOUNT VALIDATION ====================

    /**
     * TEST: Total amount bằng 0
     *
     * MỤC ĐÍCH: Đảm bảo không cho phép tổng tiền = 0
     *
     * KỊCH BẢN: Admin nhập đầy đủ thông tin nhưng không nhập tổng tiền
     *           (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1.1, 2.1.1.2.1.2.1.1.1,
     *            2.1.1.2.1.2.1.2.2, 3.1, 4, 5)
     *
     * INPUT: totalAmount = 0
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi
     * - Response success = false
     * - Response có message lỗi
     *
     * LÝ DO: Đơn hàng phải có giá trị > 0
     */
    @Test
    @DisplayName("Ném lỗi khi tổng tiền = 0")
    void testTotalAmountZero() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.totalAmount = 0;  // ❌ Tổng tiền = 0

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi totalAmount = 0");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    /**
     * TEST: Total amount âm
     *
     * MỤC ĐÍCH: Đảm bảo không cho phép tổng tiền âm
     *
     * KỊCH BẢN: Admin nhập tổng tiền không hợp lệ (âm)
     *           (1, 2.1.1.1, 2.1.1.2.1.1, 2.1.1.2.1.2.1.1, 2.1.1.2.1.2.1.1.1,
     *            2.1.1.2.1.2.1.2.2, 3.1, 4, 5)
     *
     * INPUT: totalAmount = -100
     *
     * EXPECTED:
     * - Repository.save() KHÔNG được gọi
     * - Response success = false
     * - Response có message lỗi
     *
     * LÝ DO: Tổng tiền không thể âm (không logic)
     * RULE: totalAmount phải > 0
     */
    @Test
    @DisplayName("Ném lỗi khi tổng tiền âm")
    void testTotalAmountNegative() {
        // Arrange
        QuanLyDonHangRequestData request = createValidRequest();
        request.totalAmount = -100;  // ❌ Tổng tiền âm

        // Act
        useCase.control(request);

        // Assert - Không lưu đơn hàng
        assertNull(testRepository.savedOrder, "Order không được lưu khi totalAmount âm");

        QuanLyDonHangResponseData response = useCase.getRes();
        assertFalse(response.success);
        assertNotNull(response.message);
    }

    // ==================== HELPER METHOD ====================

    /**
     * Helper method: Tạo request hợp lệ cho test
     *
     * MỤC ĐÍCH: Tái sử dụng để tạo base request, sau đó modify các field cần test
     *
     * RETURN: QuanLyDonHangRequestData với tất cả field hợp lệ
     * - action: "add"
     * - userId: "user123"
     * - customerName: "Nguyễn Văn A"
     * - customerPhone: "0123456789" (10 số hợp lệ)
     * - customerAddress: "123 Đường ABC, Quận 1, TP.HCM"
     * - totalAmount: 500000 (> 0)
     * - note: null (optional)
     */
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

    // ==================== TEST DOUBLES (thay thế Mock) ====================

    /**
     * Test Repository - thay thế Mock Repository
     * Lưu order vào biến để verify sau này
     */
    private static class TestRepository implements AddOrderRepoGateway {
        public OrderDTO savedOrder = null;

        @Override
        public String save(OrderDTO order) {
            this.savedOrder = order;
            return "ORDER_" + System.currentTimeMillis(); // Fake ID
        }

        @Override
        public Optional<OrderDTO> findById(String Id) {
            return Optional.empty();
        }

        @Override
        public Optional<OrderDTO> findByUserId(String userId) {
            return Optional.empty();
        }

        @Override
        public boolean existsByOrderId(String orderId) {
            return false;
        }
    }

    /**
     * Test Presenter - thay thế Mock Presenter
     * Lưu response để verify sau này
     */
    private static class TestPresenter implements QuanLyDonHangOutputBoundary {
        public QuanLyDonHangResponseData presentedResponse = null;

        @Override
        public void present(QuanLyDonHangResponseData response) {
            this.presentedResponse = response;
        }
    }
}