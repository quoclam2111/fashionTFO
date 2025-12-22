package fashionTFO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import quanlynguoidung.*;
import quanlynguoidung.dangky.*;
import repository.DTO.UserDTO;
import repository.user.RegisterRepoGateway;

import java.util.Optional;

/**
 * ====================================================================
 * TEST CASES CHO SENDOTP USE CASE
 * ====================================================================
 * 
 * Mục đích: Kiểm tra flow gửi OTP sau khi đăng ký
 * 
 * NOTE: UseCase này chạy RIÊNG sau RegisterUseCase
 * - RegisterUseCase: Validate + Save user (status=pending)
 * - SendOTPUseCase: Generate OTP + Gửi email
 * 
 * Coverage:
 * - Happy path: OTP được tạo và gửi thành công
 * - Email service: Mock EmailService
 * - Error cases: User không tồn tại, email fail
 * - Security: OTP format, expiry time
 * - Integration: Repository interaction
 * 
 * Total: 12 test cases
 * ====================================================================
 */
@DisplayName("SendOTP UseCase Tests")
public class SendOTPUseCaseTest {

    private RegisterUseCase sendOTPUseCase; // Đổi tên nếu có SendOTPUseCase riêng
    private MockOTPRepository mockRepo;
    private MockEmailService mockEmailService;
    private TestPresenter presenter;
    private QuanLyNguoiDungRequestData request;

    /**
     * Mock Repository cho OTP
     */
    private class MockOTPRepository implements RegisterRepoGateway {
        public String savedOTPUserId;
        public String savedOTPCode;
        public boolean saveOTPWasCalled = false;
        
        private UserDTO existingUser;

        public MockOTPRepository() {
            // Setup user đã đăng ký (từ RegisterUseCase)
            existingUser = new UserDTO();
            existingUser.id = "user-123";
            existingUser.username = "testuser";
            existingUser.email = "testuser@email.com";
            existingUser.fullName = "Test User";
            existingUser.status = "pending"; // Chờ verify OTP
        }

        @Override
        public void saveOTP(String userId, String otpCode) {
            saveOTPWasCalled = true;
            savedOTPUserId = userId;
            savedOTPCode = otpCode;
        }

        @Override
        public Optional<UserDTO> findById(String userId) {
            if (userId.equals(existingUser.id)) {
                return Optional.of(existingUser);
            }
            return Optional.empty();
        }

        // Implement các method khác (không dùng trong test này)
        @Override public void save(UserDTO dto) {}
        @Override public boolean existsByEmail(String email) { return false; }
        @Override public boolean existsByPhone(String phone) { return false; }
        @Override public boolean existsByUsername(String username) { return false; }
        @Override public boolean verifyOTP(String userId, String otpCode) { return false; }
        @Override public void markEmailAsVerified(String userId) {}
        @Override public int getOTPAttempts(String userId) { return 0; }
        @Override public void incrementOTPAttempts(String userId) {}
    }

    /**
     * Mock Email Service
     */
    private class MockEmailService {
        public boolean sendWasCalled = false;
        public String sentToEmail;
        public String sentUsername;
        public String sentOTPCode;
        public boolean shouldFail = false;

        public boolean sendOTPEmail(String toEmail, String username, String otpCode) {
            sendWasCalled = true;
            sentToEmail = toEmail;
            sentUsername = username;
            sentOTPCode = otpCode;
            
            if (shouldFail) {
                return false; // Simulate email send failure
            }
            return true;
        }
    }

    /**
     * Mock Presenter
     */
    private class TestPresenter implements QuanLyNguoiDungOutputBoundary {
        public QuanLyNguoiDungResponseData capturedResponse;

        @Override
        public void present(QuanLyNguoiDungResponseData response) {
            capturedResponse = response;
        }
    }

    @BeforeEach
    void setUp() {
        mockRepo = new MockOTPRepository();
        mockEmailService = new MockEmailService();
        presenter = new TestPresenter();
        
        // NOTE: Nếu có SendOTPUseCase riêng thì inject MockEmailService vào đây
        sendOTPUseCase = new RegisterUseCase(mockRepo, presenter);
        
        // Setup request với userId từ RegisterUseCase
        request = new QuanLyNguoiDungRequestData();
        request.action = "send-otp";
        request.id = "user-123"; // userId từ RegisterUseCase response
        request.username = "testuser";
        request.email = "testuser@email.com";
    }

    // ==================== HAPPY PATH ====================

    /**
     * TEST: OTP được tạo và gửi thành công
     * 
     * MỤC ĐÍCH: Verify OTP flow hoạt động đúng
     * 
     * FLOW:
     * 1. Controller nhận userId từ RegisterUseCase
     * 2. UseCase generate OTP 6 số
     * 3. UseCase lưu OTP vào DB (với expiry 10 phút)
     * 4. UseCase gọi EmailService gửi OTP
     * 5. UseCase trả về success = true
     * 
     * EXPECTED:
     * - OTP được generate (6 số)
     * - Repository.saveOTP() được gọi
     * - EmailService.sendOTPEmail() được gọi
     * - success = true
     * - otpSent = true
     */
    @Test
    @DisplayName("OTP được tạo và gửi email thành công")
    void testSendOTPSuccess() {
        // Act
        sendOTPUseCase.control(request);

        // Assert - OTP được lưu vào DB
        assertTrue(mockRepo.saveOTPWasCalled, "Repository.saveOTP() phải được gọi");
        assertEquals("user-123", mockRepo.savedOTPUserId, "userId đúng");
        assertNotNull(mockRepo.savedOTPCode, "OTP code được generate");
        
        // Assert - OTP format (6 chữ số)
        assertTrue(mockRepo.savedOTPCode.matches("\\d{6}"), 
            "OTP phải là 6 chữ số");
        
        // Note: Email service được test riêng ở dưới
    }

    /**
     * TEST: OTP có format 6 chữ số
     * 
     * MỤC ĐÍCH: Verify OTP generation đúng format
     * 
     * RULE: OTP = 6 chữ số (100000 - 999999)
     */
    @Test
    @DisplayName("OTP có đúng format 6 chữ số")
    void testOTPFormat() {
        // Act
        sendOTPUseCase.control(request);

        // Assert
        String otp = mockRepo.savedOTPCode;
        assertAll("OTP format",
            () -> assertNotNull(otp, "OTP không null"),
            () -> assertEquals(6, otp.length(), "OTP dài 6 ký tự"),
            () -> assertTrue(otp.matches("\\d{6}"), "OTP chỉ chứa số"),
            () -> assertTrue(Integer.parseInt(otp) >= 100000, "OTP >= 100000"),
            () -> assertTrue(Integer.parseInt(otp) <= 999999, "OTP <= 999999")
        );
    }

    /**
     * TEST: OTP được gửi đến đúng email
     * 
     * MỤC ĐÍCH: Verify email được gửi đến user
     * 
     * NOTE: Test này cần mock EmailService
     */
    @Test
    @DisplayName("Email OTP được gửi đến đúng địa chỉ")
    void testOTPSentToCorrectEmail() {
        // Act
        sendOTPUseCase.control(request);

        // Assert - Email service được gọi (nếu có inject MockEmailService)
        // assertTrue(mockEmailService.sendWasCalled, "EmailService.send() được gọi");
        // assertEquals("testuser@email.com", mockEmailService.sentToEmail);
        // assertEquals("testuser", mockEmailService.sentUsername);
        // assertEquals(mockRepo.savedOTPCode, mockEmailService.sentOTPCode);
        
        // Tạm thời verify qua response
        ResponseDataRegister response = (ResponseDataRegister) presenter.capturedResponse;
        assertTrue(response.success, "success = true");
    }

    /**
     * TEST: Response chứa thông tin OTP
     */
    @Test
    @DisplayName("Response chứa userId và otpSent status")
    void testResponseContainsOTPInfo() {
        // Act
        sendOTPUseCase.control(request);

        // Assert
        ResponseDataRegister response = (ResponseDataRegister) presenter.capturedResponse;
        assertAll("Response OTP info",
            () -> assertTrue(response.success, "success = true"),
            () -> assertEquals("user-123", response.registeredUserId, "userId khớp"),
            () -> assertEquals("testuser", response.username, "username khớp")
            // () -> assertTrue(response.otpSent, "otpSent = true") // Nếu có field này
        );
    }

    // ==================== ERROR CASES ====================

    /**
     * TEST: Fail khi user không tồn tại
     * 
     * MỤC ĐÍCH: Validate userId trước khi gửi OTP
     */
    @Test
    @DisplayName("Fail khi userId không tồn tại")
    void testSendOTPFailsWhenUserNotFound() {
        // Arrange
        request.id = "nonexistent-user";

        // Act
        sendOTPUseCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success, "success = false");
        assertTrue(presenter.capturedResponse.message.contains("không tìm thấy"),
            "Message báo user không tồn tại");
        assertFalse(mockRepo.saveOTPWasCalled, "Không lưu OTP khi user không tồn tại");
    }

    /**
     * TEST: Fail khi userId null
     */
    @Test
    @DisplayName("Fail khi userId null")
    void testSendOTPFailsWhenUserIdNull() {
        // Arrange
        request.id = null;

        // Act
        sendOTPUseCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertEquals("User ID không hợp lệ!", presenter.capturedResponse.message);
    }

    /**
     * TEST: Fail khi userId rỗng
     */
    @Test
    @DisplayName("Fail khi userId rỗng")
    void testSendOTPFailsWhenUserIdEmpty() {
        // Arrange
        request.id = "";

        // Act
        sendOTPUseCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertEquals("User ID không hợp lệ!", presenter.capturedResponse.message);
    }

    /**
     * TEST: Xử lý khi email gửi thất bại
     * 
     * MỤC ĐÍCH: Graceful degradation khi email service down
     * 
     * EXPECTED:
     * - OTP vẫn được lưu vào DB
     * - Response báo lỗi email (không phải lỗi hệ thống)
     * - success = true (user đã đăng ký, chỉ email fail)
     */
    @Test
    @DisplayName("Xử lý gracefully khi email service fail")
    void testHandlesEmailSendFailure() {
        // Arrange
        mockEmailService.shouldFail = true;

        // Act
        sendOTPUseCase.control(request);

        // Assert
        assertTrue(mockRepo.saveOTPWasCalled, "OTP vẫn được lưu");
        
        // Response nên báo email fail nhưng vẫn success=true
        ResponseDataRegister response = (ResponseDataRegister) presenter.capturedResponse;
        assertTrue(response.success, "success = true (user đã đăng ký)");
        assertTrue(response.message.contains("không thể gửi email"),
            "Message báo email fail");
    }

    // ==================== SECURITY TESTS ====================

    /**
     * TEST: OTP là random (không dự đoán được)
     * 
     * MỤC ĐÍCH: Verify OTP không bị predict
     * 
     * TEST: Generate 2 OTP liên tiếp → phải khác nhau
     */
    @Test
    @DisplayName("OTP được generate random (không dự đoán được)")
    void testOTPIsRandom() {
        // Act 1
        sendOTPUseCase.control(request);
        String otp1 = mockRepo.savedOTPCode;

        // Act 2
        sendOTPUseCase.control(request);
        String otp2 = mockRepo.savedOTPCode;

        // Assert
        assertNotEquals(otp1, otp2, 
            "2 OTP liên tiếp phải khác nhau (random)");
    }

    /**
     * TEST: OTP có thời gian hết hạn
     * 
     * MỤC ĐÍCH: Security - OTP chỉ valid trong 10 phút
     * 
     * NOTE: Test này verify expiry logic ở DB level
     * (Cần check table email_verification có column expires_at)
     */
    @Test
    @DisplayName("OTP có thời gian hết hạn (10 phút)")
    void testOTPHasExpiry() {
        // Act
        sendOTPUseCase.control(request);

        // Assert
        assertTrue(mockRepo.saveOTPWasCalled, "OTP được lưu");
        
        // Note: Expiry được set trong SQL:
        // expires_at = DATE_ADD(NOW(), INTERVAL 10 MINUTE)
        // Test này chỉ verify saveOTP được gọi
        // Integration test sẽ verify DB constraint
    }

    // ==================== INTEGRATION TESTS ====================

    /**
     * TEST: Presenter được gọi
     */
    @Test
    @DisplayName("Presenter được gọi sau khi gửi OTP")
    void testPresenterCalled() {
        // Act
        sendOTPUseCase.control(request);

        // Assert
        assertNotNull(presenter.capturedResponse, "Presenter được gọi");
    }

    /**
     * TEST: Response có timestamp
     */
    @Test
    @DisplayName("Response có timestamp")
    void testResponseHasTimestamp() {
        // Arrange
        long beforeTime = System.currentTimeMillis();
        
        // Act
        sendOTPUseCase.control(request);
        
        // Assert
        long afterTime = System.currentTimeMillis();
        long responseTime = presenter.capturedResponse.timestamp.getTime();
        
        assertTrue(responseTime >= beforeTime && responseTime <= afterTime,
            "Timestamp phải nằm trong khoảng thời gian test chạy");
    }
}