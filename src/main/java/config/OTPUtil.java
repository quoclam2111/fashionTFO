package config;

import java.security.SecureRandom;

public class OTPUtil {
    
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * Generate OTP 6 số ngẫu nhiên
     */
    public static String generateOTP() {
        int otp = 100000 + random.nextInt(900000); // 100000 - 999999
        return String.valueOf(otp);
    }
}