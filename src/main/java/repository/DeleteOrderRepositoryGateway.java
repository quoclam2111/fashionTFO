package repository;

import java.util.Optional;
import repository.DTO.OrderDTO;

public interface DeleteOrderRepositoryGateway {
    void deleteById(String id);
    Optional<OrderDTO> findById(String id);
}