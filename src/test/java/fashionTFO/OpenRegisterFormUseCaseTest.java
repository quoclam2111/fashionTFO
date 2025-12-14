package fashionTFO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung.dangky.OpenRegisterFormUseCase;
import quanlynguoidung.dangky.ResponseDataOpenRegisterForm;

/**
 * ====================================================================
 * TEST CASES CHO OPENREGISTERFORM USE CASE
 * ====================================================================
 * 
 * Mục đích: Kiểm tra use case mở form đăng ký hoạt động đúng
 * 
 * Use case này đơn giản nhưng quan trọng:
 * - Khởi tạo form với data mặc định
 * - Đảm bảo presenter được gọi đúng cách
 * - Xử lý edge cases (null request, v.v.)
 * 
 * Coverage:
 * - Happy path test: 2 test cases
 * - Edge case test: 1 test case
 * - Integration test: 2 test cases
 * - Timing test: 1 test case
 * 
 * Total: 6 test cases
 * ====================================================================
 */
@DisplayName("OpenRegisterForm UseCase Tests")
public class OpenRegisterFormUseCaseTest {

    private OpenRegisterFormUseCase useCase;
    private TestPresenter presenter;
    private QuanLyNguoiDungRequestData request;

    /**
     * Mock Presenter để capture response
     * 
     * LÝ DO dùng Mock:
     * - Không cần real presenter (có thể update UI)
     * - Chỉ cần capture response để verify
     * - Giúp test độc lập, không phụ thuộc UI layer
     */
    private class TestPresenter implements QuanLyNguoiDungOutputBoundary {
        public QuanLyNguoiDungResponseData capturedResponse;

        @Override
        public void present(QuanLyNguoiDungResponseData response) {
            this.capturedResponse = response;
        }
    }

    /**
     * Setup trước mỗi test
     * 
     * Khởi tạo:
     * - Mock presenter để capture response
     * - Use case với mock presenter
     * - Empty request (vì OpenForm không cần data)
     */
    @BeforeEach
    void setUp() {
        presenter = new TestPresenter();
        useCase = new OpenRegisterFormUseCase(presenter);
        request = new QuanLyNguoiDungRequestData();
    }

    /**
     * TEST: Mở form thành công (HAPPY PATH)
     * 
     * MỤC ĐÍCH: Verify use case hoạt động đúng trong trường hợp bình thường
     * 
     * FLOW:
     * 1. User click "Đăng ký" button
     * 2. Controller gọi use case
     * 3. Use case prepare data
     * 4. Use case gọi presenter
     * 5. Presenter update view model
     * 
     * KIỂM TRA:
     * - Response không null
     * - success = true
     * - message = "Form đăng ký đã sẵn sàng"
     * - timestamp được set
     * 
     * LÝ DO: Đây là flow chính, phải pass
     */
    @Test
    @DisplayName("Mở form thành công")
    void testOpenFormSuccess() {
        // Act - Thực thi use case
        useCase.control(request);

        // Assert - Verify kết quả
        assertNotNull(presenter.capturedResponse, 
            "Response không được null");
        assertTrue(presenter.capturedResponse.success, 
            "Success phải là true");
        assertEquals("Form đăng ký đã sẵn sàng", 
            presenter.capturedResponse.message,
            "Message phải khớp");
        assertNotNull(presenter.capturedResponse.timestamp,
            "Timestamp không được null");
    }

    /**
     * TEST: Response có đúng kiểu dữ liệu
     * 
     * MỤC ĐÍCH: Verify response là đúng type (type safety)
     * 
     * KIỂM TRA:
     * - Response phải là ResponseDataOpenRegisterForm
     * - Không bị downcast sai type
     * - formTitle = "Đăng ký tài khoản"
     * 
     * LÝ DO: 
     * - Type safety quan trọng trong Java
     * - Tránh ClassCastException runtime
     * - Đảm bảo polymorphism đúng
     */
    @Test
    @DisplayName("Response phải có đúng kiểu ResponseDataOpenRegisterForm")
    void testOpenFormResponseType() {
        // Act
        useCase.control(request);

        // Assert - Verify type
        assertInstanceOf(ResponseDataOpenRegisterForm.class, 
            presenter.capturedResponse,
            "Response phải là ResponseDataOpenRegisterForm");

        // Cast an toàn sau khi verify type
        ResponseDataOpenRegisterForm response =
            (ResponseDataOpenRegisterForm) presenter.capturedResponse;
        assertEquals("Đăng ký tài khoản", response.formTitle,
            "Form title phải khớp");
    }

    /**
     * TEST: Xử lý được khi request null (EDGE CASE)
     * 
     * MỤC ĐÍCH: Verify robustness - không crash với bad input
     * 
     * SCENARIO:
     * - Developer lỡ truyền null request
     * - Hoặc có bug ở controller layer
     * - Use case không được crash
     * 
     * KIỂM TRA:
     * - Không throw NullPointerException
     * - Vẫn trả về response (không null)
     * 
     * LÝ DO: 
     * - Defensive programming
     * - App không crash vì 1 lỗi nhỏ
     * - Graceful degradation
     */
    @Test
    @DisplayName("Xử lý được khi request null")
    void testOpenFormWithNullRequest() {
        // Act - Truyền null request
        useCase.control(null);

        // Assert - Không crash và vẫn có response
        assertNotNull(presenter.capturedResponse,
            "Response không được null ngay cả khi request null");
    }

    /**
     * TEST: Tất cả các field trong response được khởi tạo
     * 
     * MỤC ĐÍCH: Verify không có field nào bị null unintended
     * 
     * KIỂM TRA:
     * - success field khởi tạo
     * - message field khởi tạo
     * - formTitle field khởi tạo
     * - timestamp field khởi tạo
     * 
     * LÝ DO:
     * - Tránh NullPointerException ở presenter/view
     * - Đảm bảo không quên set field nào
     * - View có đủ data để render
     * 
     * KỸ THUẬT: Dùng assertAll để check hết trong 1 test
     * → Nếu fail thấy được tất cả field nào lỗi, không chỉ cái đầu
     */
    @Test
    @DisplayName("Tất cả các field trong response được khởi tạo")
    void testOpenFormResponseInitialization() {
        // Act
        useCase.control(request);

        // Assert - Check tất cả fields cùng lúc
        ResponseDataOpenRegisterForm response =
            (ResponseDataOpenRegisterForm) presenter.capturedResponse;

        assertAll("Response fields",
            () -> assertNotNull(response.success, 
                "Success field phải được khởi tạo"),
            () -> assertNotNull(response.message, 
                "Message field phải được khởi tạo"),
            () -> assertNotNull(response.formTitle, 
                "FormTitle field phải được khởi tạo"),
            () -> assertNotNull(response.timestamp, 
                "Timestamp field phải được khởi tạo")
        );
    }

    /**
     * TEST: Presenter được gọi đúng 1 lần (INTEGRATION TEST)
     * 
     * MỤC ĐÍCH: Verify flow MVC đúng
     * 
     * MVC FLOW:
     * Controller → Use Case → Presenter → View Model → View
     *                   ↑
     *              Test ở đây
     * 
     * KIỂM TRA:
     * - present() được gọi đúng 1 lần
     * - Không gọi nhiều lần (duplicate update)
     * - Không quên gọi (view không update)
     * 
     * LÝ DO:
     * - Đảm bảo separation of concerns
     * - Use case PHẢI gọi presenter
     * - Không bypass presenter để update view trực tiếp
     * 
     * KỸ THUẬT: Dùng counter trong spy presenter
     */
    @Test
    @DisplayName("Presenter được gọi đúng 1 lần")
    void testOpenFormPresenterCalled() {
        // Arrange - Tạo SpyPresenter với counter
        final int[] callCount = {0};  // Array để có thể modify trong inner class
        
        TestPresenter spyPresenter = new TestPresenter() {
            @Override
            public void present(QuanLyNguoiDungResponseData response) {
                callCount[0]++;  // Đếm số lần gọi
                super.present(response);
            }
        };

        OpenRegisterFormUseCase spyUseCase = new OpenRegisterFormUseCase(spyPresenter);

        // Act - Chạy use case
        spyUseCase.control(request);

        // Assert - Verify được gọi đúng 1 lần
        assertEquals(1, callCount[0],
            "Presenter phải được gọi đúng 1 lần");
    }
    
    /**
     * TEST: Timestamp được set với giá trị hiện tại (TIMING TEST)
     * 
     * MỤC ĐÍCH: Verify timestamp phản ánh thời gian thực
     * 
     * KIỂM TRA:
     * - Timestamp nằm trong khoảng [trước test, sau test]
     * - Không phải hardcoded value
     * - Không phải old timestamp
     * 
     * LÝ DO:
     * - Timestamp dùng để audit log
     * - Cần chính xác cho tracking
     * - Debug dễ hơn khi biết thời điểm
     * 
     * KỸ THUẬT: So sánh với system time trước/sau
     */
    @Test
    @DisplayName("Timestamp được set với giá trị hiện tại")
    void testTimestampIsSet() {
        // Arrange - Ghi nhận time trước khi chạy
        long beforeTime = System.currentTimeMillis();
        
        // Act - Chạy use case
        useCase.control(request);
        
        // Arrange - Ghi nhận time sau khi chạy
        long afterTime = System.currentTimeMillis();
        long responseTime = presenter.capturedResponse.timestamp.getTime();
        
        // Assert - Timestamp phải nằm trong khoảng [before, after]
        assertTrue(responseTime >= beforeTime && responseTime <= afterTime,
            "Timestamp phải nằm trong khoảng thời gian test chạy");
    }
}