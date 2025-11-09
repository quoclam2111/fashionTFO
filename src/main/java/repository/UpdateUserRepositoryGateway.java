package repository;

import java.util.Optional;

import repository.DTO.UserDTO;

public interface UpdateUserRepositoryGateway {
    void update(UserDTO dto);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUsername(String username);
    Optional<UserDTO> findById(String id); // để check tồn tại
}
