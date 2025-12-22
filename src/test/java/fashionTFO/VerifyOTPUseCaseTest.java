package fashionTFO;

/**
 * ⭐ FILE: src/test/java/fashionTFO/VerifyOTPUseCaseTest.java
 * 
 * Đặt file này cùng folder với RegisterValidationTest.java
 */

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung.dangky.VerifyOTPUseCase;
import repository.DTO.UserDTO;
import repository.user.RegisterRepoGateway;

import java.util.Optional;

/**
 * ====================================================================
 * TEST CASES CHO VERIFY OTP USE CASE
 * ====================================================================
 * 
 * Mục đích: Kiểm tra xác thực OTP sau khi đăng ký
 * 
 * Coverage:
 * - Happy path: 2 test cases
 * - Validation: 4 test cases
 * - OTP verification: 4 test cases
 * - Attempts tracking: 3 test cases
 * - Edge cases: 2 test cases
 * 
 * Total: 15 test cases
 * ====================================================================
 */
@DisplayName("Verify OTP UseCase Tests")
public class VerifyOTPUseCaseTest {

    private VerifyOTPUseCase useCase;
    private RegisterRepoGateway mockRepository;
    private TestPresenter presenter;
    private QuanLyNguoiDungRequestData request;

    private class TestPresenter implements QuanLyNguoiDungOutputBoundary {
        public QuanLyNguoiDungResponseData capturedResponse;

        @Override
        public void present(QuanLyNguoiDungResponseData response) {
            this.capturedResponse = response;
        }
    }

    @BeforeEach
    void setUp() {
        mockRepository = Mockito.mock(RegisterRepoGateway.class);
        presenter = new TestPresenter();
        useCase = new VerifyOTPUseCase(mockRepository, presenter);
        
        // Request mặc định
        request = new QuanLyNguoiDungRequestData();
        request.id = "user-123";
        request.otpCode = "123456";
    }

    // ==================== HAPPY PATH ====================

    /**
     * TEST: Verify OTP thành công
     * 
     * MỤC ĐÍCH: Verify flow xác thực OTP hoàn chỉnh
     * 
     * LUỒNG XỬ LÝ TRONG USECASE:
     * 1. User nhập OTP "123456"
     * 2. Validate input (userId, otpCode format)
     * 3. Check attempts < 5
     * 4. Verify OTP trong DB (check expires_at, verified_at)
     * 5. Mark email as verified
     * 6. Update user.status = "active"
     * 7. Return success response
     */
    @Test
    @DisplayName("Verify OTP thành công")
    void testVerifyOTPSuccess() {
        // ==================== ARRANGE ====================
        // 📝 Mock repository responses
        
        // 1. Attempts = 0 (chưa nhập sai lần nào)
        Mockito.when(mockRepository.getOTPAttempts("user-123")).thenReturn(0);
        
        // 2. OTP đúng (DB verify pass)
        // → SQL: SELECT ... WHERE user_id = ? AND otp_code = ? 
        //        AND verified_at IS NULL AND expires_at > NOW()
        // → Trả về: found → true
        Mockito.when(mockRepository.verifyOTP("user-123", "123456")).thenReturn(true);
        
        // 3. User info để hiển thị message
        UserDTO userDTO = new UserDTO();
        userDTO.id = "user-123";
        userDTO.username = "testuser";
        Mockito.when(mockRepository.findById("user-123")).thenReturn(Optional.of(userDTO));
        
        // 💡 LUỒNG SẼ CHẠY:
        // useCase.control(request)
        // → execute()
        // → validate input ✅
        // → getOTPAttempts() → 0 ✅ (< 5)
        // → verifyOTP() → true ✅
        // → markEmailAsVerified() ✅
        //    → UPDATE users SET status = 'active' WHERE user_id = ?
        //    → UPDATE email_verification SET verified_at = NOW() WHERE user_id = ?
        // → response.success = true
        // → response.otpVerified = true

        // ==================== ACT ====================
        useCase.control(request);

        // ==================== ASSERT ====================
        // ✅ Kiểm tra response
        QuanLyNguoiDungResponseData response = presenter.capturedResponse;
        
        assertTrue(response.success, "Success phải là true");
        assertTrue(response.message.contains("thành công"), "Message phải chứa 'thành công'");
        assertTrue(response.message.contains("testuser"), "Message phải chứa username");
        assertTrue(response.otpVerified, "OTP verified flag phải là true");
        assertEquals("user-123", response.userId, "User ID phải khớp");

        // ==================== VERIFY ====================
        // 🔍 Verify các repository methods được gọi
        
        // 1. verifyOTP() được gọi với đúng params
        Mockito.verify(mockRepository).verifyOTP("user-123", "123456");
        
        // 2. markEmailAsVerified() được gọi → activate account
        Mockito.verify(mockRepository).markEmailAsVerified("user-123");
        
        // 3. incrementOTPAttempts() KHÔNG được gọi (vì verify thành công)
        Mockito.verify(mockRepository, Mockito.never())
            .incrementOTPAttempts(Mockito.anyString());
        
        // 💡 WHY VERIFY NEVER?
        // → Khi OTP đúng, không tăng attempts
        // → Chỉ tăng attempts khi OTP sai
    }

    /**
     * TEST: User status được update thành active
     */
    @Test
    @DisplayName("User status được chuyển sang active sau verify")
    void testUserStatusActivated() {
        // Arrange
        Mockito.when(mockRepository.getOTPAttempts("user-123")).thenReturn(0);
        Mockito.when(mockRepository.verifyOTP("user-123", "123456")).thenReturn(true);
        
        UserDTO userDTO = new UserDTO();
        userDTO.id = "user-123";
        userDTO.username = "testuser";
        Mockito.when(mockRepository.findById("user-123")).thenReturn(Optional.of(userDTO));

        // Act
        useCase.control(request);

        // Assert
        assertTrue(presenter.capturedResponse.success);
        Mockito.verify(mockRepository).markEmailAsVerified("user-123");
    }

    // ==================== VALIDATION TESTS ====================

    /**
     * TEST: User ID null
     */
    @Test
    @DisplayName("Từ chối khi user ID null")
    void testUserIdNull() {
        // Arrange
        request.id = null;

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("User ID"));
    }

    /**
     * TEST: User ID rỗng
     */
    @Test
    @DisplayName("Từ chối khi user ID rỗng")
    void testUserIdEmpty() {
        // Arrange
        request.id = "   ";

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("User ID"));
    }

    /**
     * TEST: OTP code null
     */
    @Test
    @DisplayName("Từ chối khi OTP code null")
    void testOTPCodeNull() {
        // Arrange
        request.otpCode = null;

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertEquals("Vui lòng nhập mã OTP!", presenter.capturedResponse.message);
    }

    /**
     * TEST: OTP code không đúng format (không phải 6 chữ số)
     */
    @Test
    @DisplayName("Từ chối khi OTP không phải 6 chữ số")
    void testOTPCodeInvalidFormat() {
        // Arrange - Test nhiều format không hợp lệ
        String[] invalidOTPs = {
            "12345",      // 5 chữ số
            "1234567",    // 7 chữ số
            "abc123",     // Có chữ cái
            "12-34-56"    // Có ký tự đặc biệt
        };

        for (String invalidOTP : invalidOTPs) {
            request.otpCode = invalidOTP;

            // Act
            useCase.control(request);

            // Assert
            assertFalse(presenter.capturedResponse.success, 
                "Phải fail với OTP: " + invalidOTP);
            assertEquals("Mã OTP phải gồm 6 chữ số!", 
                presenter.capturedResponse.message);
        }
    }

    // ==================== OTP VERIFICATION TESTS ====================

    /**
     * TEST: OTP sai → Tăng attempts
     * 
     * MỤC ĐÍCH: Verify tracking số lần nhập sai
     * 
     * LUỒNG XỬ LÝ:
     * 1. User đã nhập sai 2 lần trước đó (attempts = 2)
     * 2. Lần này nhập OTP sai tiếp
     * 3. Tăng attempts lên 3
     * 4. Tính remaining = 5 - 3 = 2 lần
     * 5. Báo lỗi với số lần còn lại
     */
    @Test
    @DisplayName("OTP sai → tăng số lần thử và hiện remaining attempts")
    void testOTPIncorrect() {
        // ==================== ARRANGE ====================
        // 📝 Mock: User đã nhập sai 2 lần trước
        Mockito.when(mockRepository.getOTPAttempts("user-123")).thenReturn(2);
        
        // 📝 Mock: OTP lần này cũng sai
        // → DB không tìm thấy row matching (user_id, otp_code, not expired, not verified)
        Mockito.when(mockRepository.verifyOTP("user-123", "123456")).thenReturn(false);
        
        // 💡 LUỒNG SẼ CHẠY:
        // useCase.control(request)
        // → execute()
        // → getOTPAttempts() → 2 ✅ (< 5, vẫn cho thử)
        // → verifyOTP() → false ❌
        //    → Không gọi markEmailAsVerified()
        //    → incrementOTPAttempts() ✅ Tăng attempts từ 2 → 3
        //       → UPDATE email_verification SET attempts = attempts + 1 WHERE user_id = ?
        //    → remainingAttempts = 5 - (2 + 1) = 2
        //    → response.success = false
        //    → response.remainingAttempts = 2

        // ==================== ACT ====================
        useCase.control(request);

        // ==================== ASSERT ====================
        // ✅ Check response báo lỗi
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("không chính xác"));
        
        // ✅ Remaining attempts phải đúng
        // Formula: remaining = 5 - (currentAttempts + 1)
        //                   = 5 - (2 + 1) = 2
        assertEquals(2, presenter.capturedResponse.remainingAttempts);
        
        // ✅ OTP verified flag phải false
        assertFalse(presenter.capturedResponse.otpVerified);

        // ==================== VERIFY ====================
        // 🔍 Verify repository methods được gọi đúng sequence
        
        // 1. incrementOTPAttempts() được gọi (vì OTP sai)
        Mockito.verify(mockRepository).incrementOTPAttempts("user-123");
        
        // 2. markEmailAsVerified() KHÔNG được gọi (vì OTP sai)
        Mockito.verify(mockRepository, Mockito.never())
            .markEmailAsVerified(Mockito.anyString());
        
        // 💡 WHY THIS MATTERS?
        // → Đảm bảo không activate account khi OTP sai
        // → Chỉ tăng attempts counter để track
    }

    /**
     * TEST: OTP hết hạn (expires_at)
     */
    @Test
    @DisplayName("OTP hết hạn sau 10 phút")
    void testOTPExpired() {
        // Arrange
        Mockito.when(mockRepository.getOTPAttempts("user-123")).thenReturn(0);
        Mockito.when(mockRepository.verifyOTP("user-123", "123456"))
            .thenReturn(false); // DB trả về false vì đã hết hạn

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("không chính xác"));
    }

    /**
     * TEST: OTP đúng nhưng đã verify rồi
     */
    @Test
    @DisplayName("Không thể verify OTP đã được dùng")
    void testOTPAlreadyVerified() {
        // Arrange - OTP đã verify (verified_at != NULL)
        Mockito.when(mockRepository.getOTPAttempts("user-123")).thenReturn(0);
        Mockito.when(mockRepository.verifyOTP("user-123", "123456"))
            .thenReturn(false); // SQL WHERE verified_at IS NULL sẽ fail

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
    }

    /**
     * TEST: Verify với OTP hợp lệ ở lần thử cuối
     */
    @Test
    @DisplayName("Verify thành công ở lần thử thứ 5 (cuối cùng)")
    void testVerifyAtLastAttempt() {
        // Arrange
        Mockito.when(mockRepository.getOTPAttempts("user-123")).thenReturn(4); // Đã thử 4 lần
        Mockito.when(mockRepository.verifyOTP("user-123", "123456")).thenReturn(true);
        
        UserDTO userDTO = new UserDTO();
        userDTO.id = "user-123";
        userDTO.username = "testuser";
        Mockito.when(mockRepository.findById("user-123")).thenReturn(Optional.of(userDTO));

        // Act
        useCase.control(request);

        // Assert
        assertTrue(presenter.capturedResponse.success, "Phải cho verify ở lần thử thứ 5");
        assertTrue(presenter.capturedResponse.otpVerified);
    }

    // ==================== ATTEMPTS TRACKING ====================

    /**
     * TEST: Quá 5 lần thử → Từ chối
     * 
     * MỤC ĐÍCH: Verify rate limiting - không cho nhập vô hạn
     * 
     * LUỒNG XỬ LÝ:
     * 1. User đã nhập sai 5 lần (attempts = 5)
     * 2. Check attempts >= 5 → DỪNG NGAY
     * 3. Không cho verify OTP nữa
     * 4. Yêu cầu đăng ký lại
     * 
     * ⚠️ BẢO MẬT:
     * - Ngăn brute-force attack (thử OTP liên tục)
     * - Max 5 lần = 1,000,000 / 5 = 200,000 combinations có thể thử
     * - Sau 5 lần → phải đăng ký lại (OTP mới)
     */
    @Test
    @DisplayName("Từ chối khi đã nhập sai OTP quá 5 lần")
    void testMaxAttemptsExceeded() {
        // ==================== ARRANGE ====================
        // 📝 Mock: User đã nhập sai 5 lần
        Mockito.when(mockRepository.getOTPAttempts("user-123")).thenReturn(5);
        
        // 💡 LUỒNG SẼ CHẠY:
        // useCase.control(request)
        // → execute()
        // → getOTPAttempts() → 5
        // → if (attempts >= 5) ❌ DỪNG NGAY
        //    → response.success = false
        //    → response.message = "Bạn đã nhập sai OTP quá 5 lần! Vui lòng đăng ký lại."
        //    → response.remainingAttempts = 0
        //    → return (KHÔNG gọi verifyOTP())
        
        // 🚫 KHÔNG ĐƯỢC CHẠY:
        // → verifyOTP() KHÔNG được gọi
        // → incrementOTPAttempts() KHÔNG được gọi
        // → markEmailAsVerified() KHÔNG được gọi

        // ==================== ACT ====================
        useCase.control(request);

        // ==================== ASSERT ====================
        // ✅ Check response báo lỗi
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("quá 5 lần"));
        assertTrue(presenter.capturedResponse.message.contains("đăng ký lại"));
        
        // ✅ Remaining phải = 0
        assertEquals(0, presenter.capturedResponse.remainingAttempts);

        // ==================== VERIFY ====================
        // 🔍 QUAN TRỌNG: Verify KHÔNG gọi verifyOTP
        // → Vì đã hết lượt, không cần waste resource query DB
        Mockito.verify(mockRepository, Mockito.never())
            .verifyOTP(Mockito.anyString(), Mockito.anyString());
        
        // 💡 WHY NEVER?
        // → Early return để tối ưu performance
        // → Không query DB khi đã biết chắc fail
        // → Bảo mật: không cho attacker tiếp tục thử
    }

    /**
     * TEST: Tracking remaining attempts chính xác
     */
    @Test
    @DisplayName("Hiển thị số lần thử còn lại chính xác")
    void testRemainingAttemptsTracking() {
        // Test với các attempts khác nhau
        int[][] testCases = {
            {0, 4}, // attempts=0 → remaining=4
            {1, 3}, // attempts=1 → remaining=3
            {2, 2}, // attempts=2 → remaining=2
            {3, 1}, // attempts=3 → remaining=1
            {4, 0}  // attempts=4 → remaining=0
        };

        for (int[] testCase : testCases) {
            int currentAttempts = testCase[0];
            int expectedRemaining = testCase[1];

            // Arrange
            Mockito.when(mockRepository.getOTPAttempts("user-123")).thenReturn(currentAttempts);
            Mockito.when(mockRepository.verifyOTP("user-123", "123456")).thenReturn(false);

            // Act
            useCase.control(request);

            // Assert
            assertEquals(expectedRemaining, presenter.capturedResponse.remainingAttempts,
                String.format("Với attempts=%d, remaining phải là %d", 
                    currentAttempts, expectedRemaining));
        }
    }

    /**
     * TEST: Reset attempts sau verify thành công
     */
    @Test
    @DisplayName("Không tăng attempts khi verify thành công")
    void testNoIncrementOnSuccess() {
        // Arrange
        Mockito.when(mockRepository.getOTPAttempts("user-123")).thenReturn(2);
        Mockito.when(mockRepository.verifyOTP("user-123", "123456")).thenReturn(true);
        
        UserDTO userDTO = new UserDTO();
        userDTO.id = "user-123";
        userDTO.username = "testuser";
        Mockito.when(mockRepository.findById("user-123")).thenReturn(Optional.of(userDTO));

        // Act
        useCase.control(request);

        // Assert
        assertTrue(presenter.capturedResponse.success);
        
        // Verify KHÔNG gọi incrementOTPAttempts khi thành công
        Mockito.verify(mockRepository, Mockito.never()).incrementOTPAttempts(Mockito.anyString());
    }

    // ==================== EDGE CASES ====================

    /**
     * TEST: User không tồn tại (edge case)
     */
    @Test
    @DisplayName("Xử lý khi user không tồn tại trong DB")
    void testUserNotFound() {
        // Arrange
        Mockito.when(mockRepository.getOTPAttempts("user-999")).thenReturn(0);
        Mockito.when(mockRepository.verifyOTP("user-999", "123456")).thenReturn(true);
        Mockito.when(mockRepository.findById("user-999")).thenReturn(Optional.empty());

        request.id = "user-999";

        // Act
        useCase.control(request);

        // Assert - Vẫn success nhưng message generic
        assertTrue(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("Người dùng"));
    }

    /**
     * TEST: Xử lý lỗi DB
     */
    @Test
    @DisplayName("Xử lý graceful khi DB lỗi")
    void testDatabaseError() {
        // Arrange
        Mockito.when(mockRepository.getOTPAttempts("user-123"))
            .thenThrow(new RuntimeException("DB Connection Failed"));

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("Lỗi hệ thống"));
    }
}