package repository.hoadon;

import repository.DTO.OrderDTO;


import java.util.Optional;

public interface AddOrderRepoGateway {
    String save(OrderDTO dto);
    Optional<OrderDTO> findById(String Id);
    Optional<OrderDTO> findByUserId(String userId);
    boolean existsByOrderId(String orderId);

}