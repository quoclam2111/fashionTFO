package fashionTFO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung.dangky.RegisterUseCase;
import quanlynguoidung.dangky.ResponseDataRegister;
import repository.DTO.UserDTO;
import repository.user.RegisterRepoGateway;

import java.util.Optional;

/**
 * ====================================================================
 * TEST CASES CHO REGISTER USE CASE
 * ====================================================================
 * 
 * Mục đích: Kiểm tra đăng ký user có gửi OTP
 * 
 * Coverage:
 * - Happy path: 3 test cases
 * - Validation: 5 test cases
 * - Duplicate check: 3 test cases
 * - OTP flow: 2 test cases
 * - Error handling: 2 test cases
 * 
 * Total: 15 test cases
 * ====================================================================
 */
@DisplayName("Register UseCase Tests")
public class RegisterUseCaseTest {

    private RegisterUseCase useCase;
    private RegisterRepoGateway mockRepository;
    private TestPresenter presenter;
    private QuanLyNguoiDungRequestData request;

    /**
     * Mock Presenter để capture response
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
     */
    @BeforeEach
    void setUp() {
        mockRepository = Mockito.mock(RegisterRepoGateway.class);
        presenter = new TestPresenter();
        useCase = new RegisterUseCase(mockRepository, presenter);
        
        // Request mặc định hợp lệ
        request = new QuanLyNguoiDungRequestData();
        request.username = "testuser";
        request.password = "Password@123";
        request.fullName = "Test User";
        request.email = "test@email.com";
        request.phone = "0123456789";
        request.address = "Ha Noi";
    }

    // ==================== HAPPY PATH ====================

    /**
     * TEST: Đăng ký thành công và gửi OTP
     * 
     * MỤC ĐÍCH: Verify flow đăng ký hoàn chỉnh
     * 
     * FLOW THỰC TẾ TRONG USECASE:
     * 1. User nhập thông tin hợp lệ
     * 2. Validate pass (Register.validate())
     * 3. Check không trùng username/email/phone
     * 4. Save vào DB
     * 5. Generate OTP
     * 6. Gửi email OTP
     * 7. Return success response
     */
    @Test
    @DisplayName("Đăng ký thành công và gửi OTP")
    void testRegisterSuccess() {
        // ==================== ARRANGE ====================
        // 📝 Chuẩn bị: Mock repository để giả lập DB
        // → Khi useCase gọi existsByUsername() → trả về false (không trùng)
        Mockito.when(mockRepository.existsByUsername("testuser")).thenReturn(false);
        Mockito.when(mockRepository.existsByEmail("test@email.com")).thenReturn(false);
        Mockito.when(mockRepository.existsByPhone("0123456789")).thenReturn(false);
        
        // 💡 LUỒNG SẼ CHẠY:
        // request (đã setup ở setUp()) 
        // → useCase.control(request)
        // → execute() trong RegisterUseCase
        // → new Register(...) → validate() ✅ Pass
        // → existsByUsername/Email/Phone ✅ Pass (mock trả về false)
        // → repository.save() ✅ Lưu DB
        // → OTPUtil.generateOTP() → "123456"
        // → repository.saveOTP() ✅ Lưu OTP
        // → EmailService.sendOTPEmail() ✅ Gửi email
        // → response.success = true
        // → presenter.present(response) → capture vào presenter.capturedResponse

        // ==================== ACT ====================
        // 🚀 Thực thi use case
        useCase.control(request);

        // ==================== ASSERT ====================
        // ✅ Kiểm tra kết quả
        ResponseDataRegister response = (ResponseDataRegister) presenter.capturedResponse;
        
        // 1. Response phải được tạo
        assertNotNull(response, "Response không được null");
        
        // 2. Success flag phải true
        assertTrue(response.success, "Success phải là true");
        
        // 3. Message phải thông báo thành công
        assertTrue(response.message.contains("thành công"), "Message phải chứa 'thành công'");
        
        // 4. User ID phải được generate (UUID)
        assertNotNull(response.registeredUserId, "User ID phải được set");
        
        // 5. Username phải khớp với input
        assertEquals("testuser", response.username, "Username phải khớp");

        // ==================== VERIFY ====================
        // 🔍 Verify các method trong repository ĐÃ được gọi
        // → Đảm bảo use case thực sự tương tác với DB
        
        // 1. save() phải được gọi với UserDTO
        Mockito.verify(mockRepository).save(Mockito.any(UserDTO.class));
        
        // 2. saveOTP() phải được gọi với userId và otpCode
        Mockito.verify(mockRepository).saveOTP(Mockito.anyString(), Mockito.anyString());
        
        // 💡 WHY VERIFY?
        // → Unit test không chỉ check output, mà còn check behavior
        // → Đảm bảo use case gọi đúng repository methods
    }

    /**
     * TEST: Response có đầy đủ thông tin
     */
    @Test
    @DisplayName("Response chứa đầy đủ thông tin user")
    void testRegisterResponseFields() {
        // Arrange
        Mockito.when(mockRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByPhone(Mockito.anyString())).thenReturn(false);

        // Act
        useCase.control(request);

        // Assert
        ResponseDataRegister response = (ResponseDataRegister) presenter.capturedResponse;
        assertAll("Response fields",
            () -> assertNotNull(response.success, "Success field phải được set"),
            () -> assertNotNull(response.message, "Message phải được set"),
            () -> assertNotNull(response.registeredUserId, "User ID phải được set"),
            () -> assertNotNull(response.username, "Username phải được set"),
            () -> assertNotNull(response.timestamp, "Timestamp phải được set"),
            () -> assertTrue(response.otpSent, "OTP sent flag phải là true")
        );
    }

    /**
     * TEST: Password được hash trước khi lưu
     * 
     * MỤC ĐÍCH: Verify security - password phải được mã hóa BCrypt
     * 
     * LUỒNG XỬ LÝ:
     * 1. User nhập password plaintext: "Password@123"
     * 2. Register.validate() 
     *    → this.password = PasswordUtil.hashPassword(plainPassword)
     *    → "Password@123" → "$2a$10$xxx..." (BCrypt hash)
     * 3. Lưu vào DB với password đã hash
     * 
     * ⚠️ BẢO MẬT:
     * - KHÔNG BAO GIỜ lưu plaintext password vào DB
     * - Dùng BCrypt để hash (one-way, không decrypt được)
     * - Mỗi lần hash cùng password → hash khác nhau (salt random)
     */
    @Test
    @DisplayName("Password được hash trước khi lưu vào DB")
    void testPasswordIsHashed() {
        // ==================== ARRANGE ====================
        Mockito.when(mockRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByPhone(Mockito.anyString())).thenReturn(false);

        String plainPassword = request.password; // "password123"
        
        // 💡 ĐIỀU GÌ SẼ XẢY RA:
        // useCase.control(request)
        // → Register user = new Register(...) 
        //    → Constructor lưu: this.plainPassword = "password123"
        // → user.validate()
        //    → this.password = PasswordUtil.hashPassword("password123")
        //    → this.password = "$2a$10$AbC..." ✅ BCrypt hash
        // → convertToDTO(user)
        //    → dto.password = user.getPassword() → "$2a$10$AbC..."
        // → repository.save(dto) → Lưu hash vào DB

        // ==================== ACT ====================
        useCase.control(request);

        // ==================== ASSERT & VERIFY ====================
        // 🔍 Dùng argThat() để intercept argument của save()
        Mockito.verify(mockRepository).save(Mockito.argThat(dto -> {
            // 1. Password trong DTO phải KHÁC plaintext
            assertNotEquals(plainPassword, dto.password, 
                "Password phải được hash, không được lưu plaintext!");
            
            // 2. BCrypt hash luôn bắt đầu bằng $2a$ hoặc $2b$
            assertTrue(dto.password.startsWith("$2"), 
                "Password phải là BCrypt hash (bắt đầu với $2a$ hoặc $2b$)");
            
            // 3. BCrypt hash luôn dài ~60 ký tự
            assertTrue(dto.password.length() >= 50, 
                "BCrypt hash phải có độ dài ~60 ký tự");
            
            return true; // argThat phải return boolean
        }));
        
        // 💡 WHY argThat()?
        // → Cho phép kiểm tra chi tiết argument được pass vào method
        // → Không chỉ verify method được gọi, mà còn verify DATA đúng
    }

    // ==================== VALIDATION TESTS ====================

    /**
     * TEST: Username null
     */
    @Test
    @DisplayName("Ném exception khi username null")
    void testUsernameNull() {
        // Arrange
        request.username = null;

        // Act
        useCase.control(request);

        // Assert
        QuanLyNguoiDungResponseData response = presenter.capturedResponse;
        assertFalse(response.success);
        assertEquals("Vui lòng nhập tên đăng nhập!", response.message);
    }

    /**
     * TEST: Password quá ngắn
     * 
     * RULE: Password phải >= 6 ký tự
     */
    @Test
    @DisplayName("Ném exception khi password < 6 ký tự")
    void testPasswordTooShort() {
        // Arrange
        Mockito.when(mockRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByPhone(Mockito.anyString())).thenReturn(false);
        
        request.password = "Pa1!"; // ❌ Chỉ 4 ký tự (< 6)

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("6 ký tự"));
    }

    /**
     * TEST: Password không có chữ HOA
     * 
     * RULE: Password phải có ít nhất 1 chữ cái viết HOA
     */
    @Test
    @DisplayName("Ném exception khi password không có chữ hoa")
    void testPasswordNoUpperCase() {
        // Arrange
        Mockito.when(mockRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByPhone(Mockito.anyString())).thenReturn(false);
        
        request.password = "password@123"; // ❌ Không có chữ HOA

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("chữ cái viết hoa"));
    }

    /**
     * TEST: Password không có ký tự đặc biệt
     * 
     * RULE: Password phải có ít nhất 1 ký tự đặc biệt (!@#$%^&*...)
     */
    @Test
    @DisplayName("Ném exception khi password không có ký tự đặc biệt")
    void testPasswordNoSpecialChar() {
        // Arrange
        Mockito.when(mockRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByPhone(Mockito.anyString())).thenReturn(false);
        
        request.password = "Password123"; // ❌ Không có ký tự đặc biệt

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("ký tự đặc biệt"));
    }

    /**
     * TEST: Email sai format
     * 
     * RULE: Email phải đúng format RFC 5322
     */
    @Test
    @DisplayName("Ném exception khi email sai định dạng")
    void testEmailInvalidFormat() {
        // Arrange
        Mockito.when(mockRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByPhone(Mockito.anyString())).thenReturn(false);
        
        request.email = "invalid-email"; // ❌ Không có @domain.com

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("Email"));
    }

    // ==================== DUPLICATE CHECK TESTS ====================

    /**
     * TEST: Username đã tồn tại
     * 
     * MỤC ĐÍCH: Verify use case từ chối duplicate username
     * 
     * LUỒNG XỬ LÝ:
     * 1. User nhập username "testuser"
     * 2. Validate() pass ✅
     * 3. existsByUsername("testuser") → true ❌
     * 4. → Dừng ngay, không save DB
     * 5. → Return response.success = false
     */
    @Test
    @DisplayName("Từ chối khi username đã tồn tại")
    void testUsernameDuplicate() {
        // ==================== ARRANGE ====================
        // 📝 Mock: Username đã tồn tại trong DB
        Mockito.when(mockRepository.existsByUsername("testuser")).thenReturn(true);
        
        // 💡 ĐIỀU GÌ SẼ XẢY RA:
        // useCase.control(request)
        // → Register user = new Register(...) ✅
        // → user.validate() ✅ Pass (format OK)
        // → if (repository.existsByUsername()) ❌ TRUE
        //    → response.success = false
        //    → response.message = "Tên đăng nhập đã được sử dụng!"
        //    → return (DỪNG, không gọi save())

        // ==================== ACT ====================
        useCase.control(request);

        // ==================== ASSERT ====================
        // ✅ Check response báo lỗi
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("Tên đăng nhập"));
        assertTrue(presenter.capturedResponse.message.contains("đã được sử dụng"));
        
        // ==================== VERIFY ====================
        // 🔍 QUAN TRỌNG: Verify save() KHÔNG được gọi
        // → Vì đã fail ở bước check duplicate
        // → Không được phép lưu user trùng vào DB
        Mockito.verify(mockRepository, Mockito.never()).save(Mockito.any());
        
        // 💡 WHY NEVER()?
        // → Đảm bảo use case có early return
        // → Không lãng phí resource save DB khi đã biết lỗi
    }

    /**
     * TEST: Email đã tồn tại
     */
    @Test
    @DisplayName("Từ chối khi email đã tồn tại")
    void testEmailDuplicate() {
        // Arrange
        Mockito.when(mockRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByEmail("test@email.com")).thenReturn(true);

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("Email"));
        Mockito.verify(mockRepository, Mockito.never()).save(Mockito.any());
    }

    /**
     * TEST: Phone đã tồn tại
     */
    @Test
    @DisplayName("Từ chối khi phone đã tồn tại")
    void testPhoneDuplicate() {
        // Arrange
        Mockito.when(mockRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByPhone("0123456789")).thenReturn(true);

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("Số điện thoại"));
        Mockito.verify(mockRepository, Mockito.never()).save(Mockito.any());
    }

    // ==================== OTP FLOW TESTS ====================

    /**
     * TEST: OTP được tạo và lưu
     */
    @Test
    @DisplayName("OTP được generate và lưu vào DB")
    void testOTPGenerated() {
        // Arrange
        Mockito.when(mockRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByPhone(Mockito.anyString())).thenReturn(false);

        // Act
        useCase.control(request);

        // Assert
        ResponseDataRegister response = (ResponseDataRegister) presenter.capturedResponse;
        assertTrue(response.success);
        
        // Verify saveOTP được gọi với userId và OTP code (6 chữ số)
        Mockito.verify(mockRepository).saveOTP(
            Mockito.anyString(), // userId
            Mockito.argThat(otp -> otp.matches("\\d{6}")) // OTP phải là 6 chữ số
        );
    }

    /**
     * TEST: User status = pending cho đến khi verify email
     */
    @Test
    @DisplayName("User được tạo với status = pending")
    void testUserStatusPending() {
        // Arrange
        Mockito.when(mockRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(mockRepository.existsByPhone(Mockito.anyString())).thenReturn(false);

        // Act
        useCase.control(request);

        // Assert - Verify save() được gọi với status = "pending"
        Mockito.verify(mockRepository).save(Mockito.argThat(dto -> {
            assertEquals("pending", dto.status, "Status phải là pending");
            return true;
        }));
    }

    // ==================== ERROR HANDLING ====================

    /**
     * TEST: Xử lý lỗi DB
     */
    @Test
    @DisplayName("Xử lý graceful khi DB lỗi")
    void testDatabaseError() {
        // Arrange
        Mockito.when(mockRepository.existsByUsername(Mockito.anyString()))
            .thenThrow(new RuntimeException("DB Connection Failed"));

        // Act
        useCase.control(request);

        // Assert
        assertFalse(presenter.capturedResponse.success);
        assertTrue(presenter.capturedResponse.message.contains("Lỗi hệ thống"));
    }

    /**
     * TEST: Nhiều lỗi validation - chỉ hiện lỗi đầu tiên
     */
    @Test
    @DisplayName("Nhiều lỗi validation - fail fast ở lỗi đầu")
    void testMultipleValidationErrors() {
        // Arrange - TẤT CẢ field đều lỗi
        request.username = null;          // ❌ Lỗi đầu tiên
        request.password = "123";         // ❌ Quá ngắn
        request.email = "invalid";        // ❌ Sai format
        request.phone = "123";            // ❌ Quá ngắn

        // Act
        useCase.control(request);

        // Assert - Chỉ lỗi username được báo (fail-fast)
        assertFalse(presenter.capturedResponse.success);
        assertEquals("Vui lòng nhập tên đăng nhập!", presenter.capturedResponse.message);
    }
}