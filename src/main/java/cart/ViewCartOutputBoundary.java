package cart;

import java.util.List;

public interface ViewCartOutputBoundary {
    void present(List<ViewCartOutputData> cartItems);
}
