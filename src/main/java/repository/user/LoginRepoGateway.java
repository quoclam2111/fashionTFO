package repository.user;

import repository.DTO.NhanVienDTO;
import repository.DTO.UserDTO;

public interface LoginRepoGateway {
    NhanVienDTO findNhanVienByUsername(String username);
    UserDTO findUserByUsername(String username);
}