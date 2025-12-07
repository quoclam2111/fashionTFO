package adapters.cart;

import quanlycart.ViewCartInputBoundary;
import quanlycart.ViewCartInputData;

public class ViewCartController {
    private final ViewCartInputBoundary useCase;

    public ViewCartController(ViewCartInputBoundary useCase) {
        this.useCase = useCase;
    }

    public void viewCart(String userId) {
        useCase.execute(new ViewCartInputData(userId));
    }
}
