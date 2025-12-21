package repository.user;

import java.util.Optional;

import repository.DTO.UserDTO;

public interface RegisterRepoGateway {
    void save(UserDTO dto);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUsername(String username);
    
    Optional<UserDTO> findById(String userId);
    
    void saveOTP(String userId, String otpCode);
    boolean verifyOTP(String userId, String otpCode);
    void markEmailAsVerified(String userId);
    int getOTPAttempts(String userId);
    void incrementOTPAttempts(String userId);
}