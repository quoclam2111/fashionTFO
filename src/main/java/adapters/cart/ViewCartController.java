package adapters.cart;

import quanlynguoidung.cart.ViewCartInputBoundary;
import quanlynguoidung.cart.ViewCartInputData;

public class ViewCartController {
    private final ViewCartInputBoundary useCase;

    public ViewCartController(ViewCartInputBoundary useCase) {
        this.useCase = useCase;
    }

    public void viewCart(String userId) {
        useCase.execute(new ViewCartInputData(userId));
    }
}
