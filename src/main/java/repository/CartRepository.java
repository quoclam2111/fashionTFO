package repository;

import repository.DTO.CartDTO;
import java.util.List;

public interface CartRepository {
    List<CartDTO> findCartItemsByUserId(String userId);
    boolean updateCartItemQuantity(String userId, String variantId, int newQuantity);
    boolean removeCartItem(String userId, String variantId);
    int getAvailableStock(String variantId);
    String getProductNameByVariantId(String variantId);
    int getQuantityInCart(String userId, String variantId);
    boolean addToCart(String userId, String variantId, int quantity);
    int countCartItems(String userId);
    boolean clearCart(String userId);
}