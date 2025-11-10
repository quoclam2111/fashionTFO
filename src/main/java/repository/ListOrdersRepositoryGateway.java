package repository;

import java.util.List;
import java.util.Optional;

import repository.DTO.OrderDTO;

public interface ListOrdersRepositoryGateway {
    List<OrderDTO> findAll();
    Optional<OrderDTO> findByUserId(String userId);
}