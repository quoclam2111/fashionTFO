package repository.user;

import repository.DTO.UserDTO;

public interface RegisterRepoGateway {
    void save(UserDTO dto);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUsername(String username);
}