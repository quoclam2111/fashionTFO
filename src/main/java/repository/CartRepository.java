package repository;

import java.util.List;
import repository.DTO.CartDTO;

public interface CartRepository {
    List<CartDTO> findCartItemsByUserId(String userId);
    boolean updateCartItemQuantity(String userId, String variantId, int newQuantity);
    boolean removeCartItem(String userId, String variantId);
    int getAvailableStock(String variantId);
    String getProductNameByVariantId(String variantId);
}
