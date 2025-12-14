package repository.user;

import java.util.Optional;

import repository.DTO.UserDTO;

public interface GetUserRepositoryGateway {
    Optional<UserDTO> findById(String id);
    Optional<UserDTO> findByUsername(String username);
    Optional<UserDTO> findByEmail(String email);
    Optional<UserDTO> findByPhone(String phone);
}
