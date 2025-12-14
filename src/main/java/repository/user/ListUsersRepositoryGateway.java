package repository.user;

import java.util.List;

import repository.DTO.UserDTO;

public interface ListUsersRepositoryGateway {
	List<UserDTO> findAll();
}
