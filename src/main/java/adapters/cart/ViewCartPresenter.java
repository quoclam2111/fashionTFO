package adapters.cart;

import java.util.List;

import cart.ViewCartOutputBoundary;
import cart.ViewCartOutputData;

public class ViewCartPresenter implements ViewCartOutputBoundary {
    private final ViewCartViewModel viewModel;

    public ViewCartPresenter(ViewCartViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(List<ViewCartOutputData> cartItems) {
        viewModel.setCartItems(cartItems);
    }
}
