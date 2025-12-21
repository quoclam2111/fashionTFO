package config;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenUtil {
    
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();
    
    /**
     * Generate token ngẫu nhiên an toàn để xác thực email
     */
    public static String generateVerificationToken() {
        byte[] randomBytes = new byte[32]; // 256 bits
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}