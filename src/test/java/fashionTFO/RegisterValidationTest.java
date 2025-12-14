package fashionTFO;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import quanlynguoidung.dangky.Register;

/**
 * ====================================================================
 * TEST CASES CHO REGISTER VALIDATION
 * ====================================================================
 * 
 * Mục đích: Kiểm tra tất cả validation rules cho form đăng ký user
 * 
 * Coverage:
 * - Username validation: 3 test cases
 * - Password validation: 4 test cases  
 * - Email validation: 4 test cases
 * - Phone validation: 6 test cases
 * - Fullname validation: 3 test cases
 * - Combined validation: 2 test cases
 * 
 * Total: 22 test cases
 * ====================================================================
 */
@DisplayName("Register Validation Tests")
public class RegisterValidationTest {
    
    // ==================== USERNAME VALIDATION ====================
    
    /**
     * TEST: Validate thành công với tất cả input hợp lệ
     * 
     * MỤC ĐÍCH: Kiểm tra happy path - không có lỗi gì
     * 
     * INPUT:
     * - username: "validuser"
     * - password: "password123" (>= 6 ký tự)
     * - fullName: "Nguyen Van A"
     * - email: "test@email.com" (đúng format)
     * - phone: "0123456789" (9-12 số)
     * - address: "Ha Noi"
     * 
     * EXPECTED: Không throw exception
     */
    @Test
    @DisplayName("Validate thành công với tất cả input hợp lệ")
    void testValidateSuccess() {
        Register user = new Register(
            "validuser",
            "password123",
            "Nguyen Van A",
            "test@email.com",
            "0123456789",
            "Ha Noi"
        );
        
        // Không throw exception = test pass
        assertDoesNotThrow(() -> user.validate());
    }
    
    /**
     * TEST: Username null
     * 
     * MỤC ĐÍCH: Đảm bảo không cho phép username null
     * 
     * INPUT: username = null
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Vui lòng nhập tên đăng nhập!"
     * 
     * LÝ DO: Username là required field, không được để trống
     */
    @Test
    @DisplayName("Ném exception khi username null")
    void testUsernameNull() {
        Register user = new Register(
            null,  // ❌ Username null
            "password123",
            "Nguyen Van A",
            "test@email.com",
            "0123456789",
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập tên đăng nhập!", exception.getMessage());
    }
    
    /**
     * TEST: Username rỗng
     * 
     * MỤC ĐÍCH: Đảm bảo không cho phép username rỗng
     * 
     * INPUT: username = ""
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Vui lòng nhập tên đăng nhập!"
     * 
     * LÝ DO: Username rỗng không có ý nghĩa
     */
    @Test
    @DisplayName("Ném exception khi username rỗng")
    void testUsernameEmpty() {
        Register user = new Register(
            "",  // ❌ Username rỗng
            "password123",
            "Nguyen Van A",
            "test@email.com",
            "0123456789",
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập tên đăng nhập!", exception.getMessage());
    }
    
    /**
     * TEST: Username chỉ có khoảng trắng
     * 
     * MỤC ĐÍCH: Đảm bảo username sau trim() phải có nội dung
     * 
     * INPUT: username = "   " (3 spaces)
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Vui lòng nhập tên đăng nhập!"
     * 
     * LÝ DO: Username chỉ có space = không hợp lệ
     */
    @Test
    @DisplayName("Ném exception khi username chỉ có khoảng trắng")
    void testUsernameWhitespaceOnly() {
        Register user = new Register(
            "   ",  // ❌ Username chỉ có spaces
            "password123",
            "Nguyen Van A",
            "test@email.com",
            "0123456789",
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập tên đăng nhập!", exception.getMessage());
    }
    
    // ==================== PASSWORD VALIDATION ====================
    
    /**
     * TEST: Password null
     * 
     * MỤC ĐÍCH: Đảm bảo không cho phép password null
     * 
     * INPUT: password = null
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Vui lòng nhập mật khẩu!"
     * 
     * LÝ DO: Password là required cho security
     */
    @Test
    @DisplayName("Ném exception khi password null")
    void testPasswordNull() {
        Register user = new Register(
            "validuser",
            null,  // ❌ Password null
            "Nguyen Van A",
            "test@email.com",
            "0123456789",
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập mật khẩu!", exception.getMessage());
    }
    
    /**
     * TEST: Password rỗng
     * 
     * MỤC ĐÍCH: Đảm bảo không cho phép password rỗng
     * 
     * INPUT: password = ""
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Vui lòng nhập mật khẩu!"
     * 
     * LÝ DO: Password rỗng = không bảo mật
     */
    @Test
    @DisplayName("Ném exception khi password rỗng")
    void testPasswordEmpty() {
        Register user = new Register(
            "validuser",
            "",  // ❌ Password rỗng
            "Nguyen Van A",
            "test@email.com",
            "0123456789",
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập mật khẩu!", exception.getMessage());
    }
    
    /**
     * TEST: Password quá ngắn
     * 
     * MỤC ĐÍCH: Đảm bảo password đủ mạnh (>= 6 ký tự)
     * 
     * INPUT: password = "12345" (5 ký tự)
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Mật khẩu phải có ít nhất 6 ký tự!"
     * 
     * LÝ DO: Password ngắn = dễ bị crack, không an toàn
     * RULE: Minimum 6 ký tự
     */
    @Test
    @DisplayName("Ném exception khi password quá ngắn (< 6 ký tự)")
    void testPasswordTooShort() {
        Register user = new Register(
            "validuser",
            "12345",  // ❌ Chỉ 5 ký tự (< 6)
            "Nguyen Van A",
            "test@email.com",
            "0123456789",
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Mật khẩu phải có ít nhất 6 ký tự!", exception.getMessage());
    }
    
    /**
     * TEST: Password đúng 6 ký tự (BOUNDARY TEST)
     * 
     * MỤC ĐÍCH: Test giá trị biên - 6 là minimum cho phép
     * 
     * INPUT: password = "123456" (đúng 6 ký tự)
     * 
     * EXPECTED: Không throw exception
     * 
     * LÝ DO: 6 ký tự là boundary case - phải accept
     */
    @Test
    @DisplayName("Chấp nhận password đúng 6 ký tự")
    void testPasswordExactly6Characters() {
        Register user = new Register(
            "validuser",
            "123456",  // ✅ Đúng 6 ký tự (boundary)
            "Nguyen Van A",
            "test@email.com",
            "0123456789",
            "Ha Noi"
        );
        
        assertDoesNotThrow(() -> user.validate());
    }
    
    // ==================== EMAIL VALIDATION ====================
    
    /**
     * TEST: Email null
     * 
     * MỤC ĐÍCH: Đảm bảo không cho phép email null
     * 
     * INPUT: email = null
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Vui lòng nhập email!"
     * 
     * LÝ DO: Email cần để liên lạc với user
     */
    @Test
    @DisplayName("Ném exception khi email null")
    void testEmailNull() {
        Register user = new Register(
            "validuser",
            "password123",
            "Nguyen Van A",
            null,  // ❌ Email null
            "0123456789",
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập email!", exception.getMessage());
    }
    
    /**
     * TEST: Email rỗng
     * 
     * MỤC ĐÍCH: Đảm bảo không cho phép email rỗng
     * 
     * INPUT: email = ""
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Vui lòng nhập email!"
     * 
     * LÝ DO: Email rỗng không thể gửi được
     */
    @Test
    @DisplayName("Ném exception khi email rỗng")
    void testEmailEmpty() {
        Register user = new Register(
            "validuser",
            "password123",
            "Nguyen Van A",
            "",  // ❌ Email rỗng
            "0123456789",
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập email!", exception.getMessage());
    }
    
    /**
     * TEST: Email sai định dạng
     * 
     * MỤC ĐÍCH: Đảm bảo email đúng format RFC 5322
     * 
     * TEST CASES:
     * - "notanemail" → Không có @
     * - "missing@domain" → Không có TLD (.com, .vn)
     * - "@nodomain.com" → Không có local part
     * - "no@domain@double.com" → 2 dấu @
     * - "spaces in@email.com" → Có khoảng trắng
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Email không đúng định dạng!"
     * 
     * LÝ DO: Email sai format = không gửi được
     * REGEX: ^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$
     */
    @Test
    @DisplayName("Ném exception với các định dạng email không hợp lệ")
    void testEmailInvalidFormat() {
        String[] invalidEmails = {
            "notanemail",              // ❌ Không có @
            "missing@domain",          // ❌ Không có TLD
            "@nodomain.com",           // ❌ Không có local part
            "no@domain@double.com",    // ❌ 2 dấu @
            "spaces in@email.com"      // ❌ Có space
        };
        
        for (String invalidEmail : invalidEmails) {
            Register user = new Register(
                "validuser",
                "password123",
                "Nguyen Van A",
                invalidEmail,  // ❌ Email không hợp lệ
                "0123456789",
                "Ha Noi"
            );
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, 
                () -> user.validate(),
                "Phải fail cho email: " + invalidEmail
            );
            
            assertEquals("Email không đúng định dạng!", exception.getMessage());
        }
    }
    
    /**
     * TEST: Email hợp lệ
     * 
     * MỤC ĐÍCH: Verify các format email thông dụng đều work
     * 
     * TEST CASES:
     * - "test@email.com" → Standard format
     * - "user.name@example.co.uk" → Có dấu chấm
     * - "user+tag@domain.com" → Có dấu + (Gmail alias)
     * - "user_name@test-domain.com" → Có underscore & dash
     * 
     * EXPECTED: Không throw exception
     * 
     * LÝ DO: Các format này đều hợp lệ theo RFC 5322
     */
    @Test
    @DisplayName("Chấp nhận các định dạng email hợp lệ")
    void testEmailValidFormats() {
        String[] validEmails = {
            "test@email.com",              // ✅ Standard
            "user.name@example.co.uk",     // ✅ Có dấu chấm
            "user+tag@domain.com",         // ✅ Có dấu +
            "user_name@test-domain.com"    // ✅ Underscore & dash
        };
        
        for (String validEmail : validEmails) {
            Register user = new Register(
                "validuser",
                "password123",
                "Nguyen Van A",
                validEmail,  // ✅ Email hợp lệ
                "0123456789",
                "Ha Noi"
            );
            
            assertDoesNotThrow(() -> user.validate(), 
                "Phải pass cho email: " + validEmail);
        }
    }
    
    // ==================== PHONE VALIDATION ====================
    
    /**
     * TEST: Phone null
     * 
     * MỤC ĐÍCH: Đảm bảo không cho phép phone null
     * 
     * INPUT: phone = null
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Vui lòng nhập số điện thoại!"
     * 
     * LÝ DO: Phone cần để liên lạc
     */
    @Test
    @DisplayName("Ném exception khi phone null")
    void testPhoneNull() {
        Register user = new Register(
            "validuser",
            "password123",
            "Nguyen Van A",
            "test@email.com",
            null,  // ❌ Phone null
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập số điện thoại!", exception.getMessage());
    }
    
    /**
     * TEST: Phone rỗng
     * 
     * MỤC ĐÍCH: Đảm bảo không cho phép phone rỗng
     * 
     * INPUT: phone = ""
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Vui lòng nhập số điện thoại!"
     * 
     * LÝ DO: Phone rỗng không gọi được
     */
    @Test
    @DisplayName("Ném exception khi phone rỗng")
    void testPhoneEmpty() {
        Register user = new Register(
            "validuser",
            "password123",
            "Nguyen Van A",
            "test@email.com",
            "",  // ❌ Phone rỗng
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập số điện thoại!", exception.getMessage());
    }
    
    /**
     * TEST: Phone quá ngắn
     * 
     * MỤC ĐÍCH: Đảm bảo phone >= 9 chữ số
     * 
     * INPUT: phone = "12345678" (8 chữ số)
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Số điện thoại phải từ 9-12 chữ số!"
     * 
     * LÝ DO: Phone VN tối thiểu 9 số (mobile) hoặc 10 số (landline)
     * RULE: 9-12 chữ số
     */
    @Test
    @DisplayName("Ném exception khi phone quá ngắn (< 9 số)")
    void testPhoneTooShort() {
        Register user = new Register(
            "validuser",
            "password123",
            "Nguyen Van A",
            "test@email.com",
            "12345678",  // ❌ Chỉ 8 số (< 9)
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Số điện thoại phải từ 9-12 chữ số!", exception.getMessage());
    }
    
    /**
     * TEST: Phone quá dài
     * 
     * MỤC ĐÍCH: Đảm bảo phone <= 12 chữ số
     * 
     * INPUT: phone = "1234567890123" (13 chữ số)
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Số điện thoại phải từ 9-12 chữ số!"
     * 
     * LÝ DO: Phone VN tối đa 12 số (với country code +84)
     * RULE: 9-12 chữ số
     */
    @Test
    @DisplayName("Ném exception khi phone quá dài (> 12 số)")
    void testPhoneTooLong() {
        Register user = new Register(
            "validuser",
            "password123",
            "Nguyen Van A",
            "test@email.com",
            "1234567890123",  // ❌ 13 số (> 12)
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Số điện thoại phải từ 9-12 chữ số!", exception.getMessage());
    }
    
    /**
     * TEST: Phone có độ dài hợp lệ (BOUNDARY TEST)
     * 
     * MỤC ĐÍCH: Test các boundary values 9-12 đều OK
     * 
     * TEST CASES:
     * - "123456789" → 9 số (min)
     * - "0123456789" → 10 số (standard VN mobile)
     * - "84123456789" → 11 số (với country code)
     * - "841234567890" → 12 số (max)
     * 
     * EXPECTED: Không throw exception
     * 
     * LÝ DO: Test boundary cases quan trọng
     */
    @Test
    @DisplayName("Chấp nhận phone từ 9-12 chữ số")
    void testPhoneValidLengths() {
        String[] validPhones = {
            "123456789",     // ✅ 9 số (min)
            "0123456789",    // ✅ 10 số
            "84123456789",   // ✅ 11 số
            "841234567890"   // ✅ 12 số (max)
        };
        
        for (String validPhone : validPhones) {
            Register user = new Register(
                "validuser",
                "password123",
                "Nguyen Van A",
                "test@email.com",
                validPhone,  // ✅ Phone hợp lệ
                "Ha Noi"
            );
            
            assertDoesNotThrow(() -> user.validate(),
                "Phải pass cho phone: " + validPhone);
        }
    }
    
    /**
     * TEST: Phone chứa ký tự không phải số
     * 
     * MỤC ĐÍCH: Đảm bảo phone chỉ chứa digits
     * 
     * TEST CASES:
     * - "012-345-6789" → Có dấu gạch ngang
     * - "012 345 6789" → Có khoảng trắng
     * - "+84123456789" → Có dấu +
     * - "012.345.6789" → Có dấu chấm
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Số điện thoại phải từ 9-12 chữ số!"
     * 
     * LÝ DO: DB chỉ lưu số thuần, không có format
     * REGEX: \\d{9,12}
     */
    @Test
    @DisplayName("Ném exception khi phone chứa ký tự không phải số")
    void testPhoneWithNonDigits() {
        String[] invalidPhones = {
            "012-345-6789",    // ❌ Có dấu gạch ngang
            "012 345 6789",    // ❌ Có space
            "+84123456789",    // ❌ Có dấu +
            "012.345.6789"     // ❌ Có dấu chấm
        };
        
        for (String invalidPhone : invalidPhones) {
            Register user = new Register(
                "validuser",
                "password123",
                "Nguyen Van A",
                "test@email.com",
                invalidPhone,  // ❌ Phone có ký tự đặc biệt
                "Ha Noi"
            );
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, 
                () -> user.validate(),
                "Phải fail cho phone: " + invalidPhone
            );
            
            assertEquals("Số điện thoại phải từ 9-12 chữ số!", exception.getMessage());
        }
    }
    
    // ==================== FULLNAME VALIDATION ====================
    
    /**
     * TEST: Fullname null
     * 
     * MỤC ĐÍCH: Đảm bảo không cho phép fullname null
     * 
     * INPUT: fullname = null
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Vui lòng nhập họ tên!"
     * 
     * LÝ DO: Fullname cần để nhận diện user
     */
    @Test
    @DisplayName("Ném exception khi fullname null")
    void testFullNameNull() {
        Register user = new Register(
            "validuser",
            "password123",
            null,  // ❌ Fullname null
            "test@email.com",
            "0123456789",
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập họ tên!", exception.getMessage());
    }
    
    /**
     * TEST: Fullname rỗng
     * 
     * MỤC ĐÍCH: Đảm bảo không cho phép fullname rỗng
     * 
     * INPUT: fullname = ""
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Vui lòng nhập họ tên!"
     * 
     * LÝ DO: Fullname rỗng không có ý nghĩa
     */
    @Test
    @DisplayName("Ném exception khi fullname rỗng")
    void testFullNameEmpty() {
        Register user = new Register(
            "validuser",
            "password123",
            "",  // ❌ Fullname rỗng
            "test@email.com",
            "0123456789",
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập họ tên!", exception.getMessage());
    }
    
    /**
     * TEST: Fullname chỉ có khoảng trắng
     * 
     * MỤC ĐÍCH: Đảm bảo fullname sau trim() phải có nội dung
     * 
     * INPUT: fullname = "   "
     * 
     * EXPECTED: 
     * - Throw IllegalArgumentException
     * - Message: "Vui lòng nhập họ tên!"
     * 
     * LÝ DO: Fullname chỉ có space = không hợp lệ
     */
    @Test
    @DisplayName("Ném exception khi fullname chỉ có khoảng trắng")
    void testFullNameWhitespaceOnly() {
        Register user = new Register(
            "validuser",
            "password123",
            "   ",  // ❌ Fullname chỉ có spaces
            "test@email.com",
            "0123456789",
            "Ha Noi"
        );
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập họ tên!", exception.getMessage());
    }
    
    // ==================== COMBINED VALIDATION ====================
    
    /**
     * TEST: Nhiều lỗi cùng lúc - chỉ throw lỗi đầu tiên
     * 
     * MỤC ĐÍCH: Verify validation dùng fail-fast strategy
     * 
     * INPUT: TẤT CẢ field đều lỗi
     * - username: null ❌
     * - password: "123" (< 6) ❌
     * - fullname: "" ❌
     * - email: "invalid" ❌
     * - phone: "123" (< 9) ❌
     * 
     * EXPECTED: 
     * - Chỉ throw lỗi đầu tiên (username)
     * - Message: "Vui lòng nhập tên đăng nhập!"
     * 
     * LÝ DO: Fail-fast = dừng ngay khi gặp lỗi đầu
     * → User chỉ fix 1 lỗi mỗi lần → UX tốt hơn
     */
    @Test
    @DisplayName("Nhiều lỗi cùng lúc - chỉ ném lỗi đầu tiên")
    void testMultipleValidationErrors_FirstErrorThrown() {
        Register user = new Register(
            null,      // ❌ Username lỗi (lỗi đầu tiên)
            "123",     // ❌ Password lỗi
            "",        // ❌ Fullname lỗi
            "invalid", // ❌ Email lỗi
            "123",     // ❌ Phone lỗi
            "Ha Noi"
        );
        
        // Chỉ lỗi đầu tiên được throw
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> user.validate()
        );
        
        assertEquals("Vui lòng nhập tên đăng nhập!", exception.getMessage());
    }
}
    
    /**
     * TEST: Thứ tự validation
     * 
     * MỤC ĐÍCH: Verify validation order là cố định
     * 
     * THỨ TỰ VALIDATE:
     * 1. Username
     * 2. Password
     * 3. Email
     * 4. Phone
     * 5. Fullname
     * 
     * TEST STRATEGY:
     * - Test 1: Chỉ username lỗi → Ném lỗi username
     * - Test 2: Username OK, password lỗi → Ném lỗi password
     * - Test 3: Username + password OK, email lỗi → Ném lỗi email
     * - Test 4: 3 cái OK, phone lỗi → Ném lỗi phone
     * - Test 5: 4 cái OK, fullname lỗi → Ném lỗi fullname
     * 
     * LÝ DO: Validation order phải consistent để:
     * - User biết fix lỗi theo thứ tự
     * - Không bị confuse khi fix xong lỗi này lại ra lỗi khác
     */