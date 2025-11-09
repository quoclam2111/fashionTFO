package repository;

import java.util.Optional;

import repository.DTO.UserDTO;

public interface UpdateUserRepositoryGateway {
    void update(UserDTO dto);
    boolean existsByEmailExcludingUser(String email, String userId);
    boolean existsByPhoneExcludingUser(String phone, String userId);
    boolean existsByUsername(String username);
    Optional<UserDTO> findById(String id); // để check tồn tại
}
