package repository;

import java.util.Optional;
import repository.DTO.OrderDTO;

public interface GetOrderRepositoryGateway {
    Optional<OrderDTO> findById(String id);
    Optional<OrderDTO> findByUserId(String userId);
    Optional<OrderDTO> findByPhone(String phone);
}