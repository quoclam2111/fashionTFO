package repository.user;

import repository.DTO.UserDTO;

public interface RegisterRepoGateway {
    void save(UserDTO dto);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUsername(String username);
    
    
    void saveOTP(String userId, String otpCode);
    boolean verifyOTP(String userId, String otpCode);
    void markEmailAsVerified(String userId);
    int getOTPAttempts(String userId);
    void incrementOTPAttempts(String userId);
}