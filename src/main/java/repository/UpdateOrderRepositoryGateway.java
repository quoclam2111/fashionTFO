package repository;

import java.util.Optional;
import repository.DTO.OrderDTO;

public interface UpdateOrderRepositoryGateway {
    void update(OrderDTO dto);
    Optional<OrderDTO> findById(String id);
}