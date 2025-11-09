package repository;

import java.util.List;
import java.util.Optional;

import repository.DTO.UserDTO;



public interface AddUserRepoGateway {
    void save(UserDTO dto);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
//    // Thêm các method mới
//    List<UserDTO > findAll();
//    Optional<UserDTO> findById(String id);
//    void deleteById(String id);
//    void update(UserDTO user);
    boolean existsByUsername(String username);
}
