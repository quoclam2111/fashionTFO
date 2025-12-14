package adapters.cart;

import java.util.List;

import cart.ViewCartOutputData;

public class ViewCartViewModel {
    private List<ViewCartOutputData> cartItems;

    public List<ViewCartOutputData> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<ViewCartOutputData> cartItems) {
        this.cartItems = cartItems;
    }
}