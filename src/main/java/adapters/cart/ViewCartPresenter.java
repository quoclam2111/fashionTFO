package adapters.cart;

import java.util.List;

import quanlycart.ViewCartOutputBoundary;
import quanlycart.ViewCartOutputData;

public class ViewCartPresenter implements ViewCartOutputBoundary {
    private final ViewCartViewModel viewModel;
    
    public ViewCartPresenter(ViewCartViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(List<ViewCartOutputData> outputDataList) {
        viewModel.setCartItems(outputDataList);
        viewModel.setError(false);
        viewModel.setErrorCode(null);
        viewModel.setErrorMessage(null);
        viewModel.setEmpty(outputDataList.isEmpty());
    }
    
    @Override
    public void presentError(String errorCode, String errorMessage) {
        viewModel.setCartItems(new java.util.ArrayList<>());
        viewModel.setError(true);
        viewModel.setErrorCode(errorCode);
        viewModel.setErrorMessage(errorMessage);
        viewModel.setEmpty(false);
    }
    
    @Override
    public void presentEmptyCart(String message) {
        viewModel.setCartItems(new java.util.ArrayList<>());
        viewModel.setError(false);
        viewModel.setErrorCode(null);
        viewModel.setErrorMessage(message);
        viewModel.setEmpty(true);
    }
}