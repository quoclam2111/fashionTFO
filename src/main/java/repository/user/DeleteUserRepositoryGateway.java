package repository.user;

import java.util.Optional;

import repository.DTO.UserDTO;

public interface DeleteUserRepositoryGateway {
    void deleteById(String id);
    Optional<UserDTO> findById(String id); // để check tồn tại trước khi xóa
}
