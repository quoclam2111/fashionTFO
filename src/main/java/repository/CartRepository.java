package repository;

import java.util.List;
import repository.DTO.CartDTO;

public interface CartRepository {
    List<CartDTO> findCartItemsByUserId(String userId);
}
