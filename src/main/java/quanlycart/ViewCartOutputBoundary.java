package quanlycart;

import java.util.List;

public interface ViewCartOutputBoundary {
    void present(List<ViewCartOutputData> outputDataList);
    void presentError(String errorCode, String errorMessage);
    void presentEmptyCart(String message);
}