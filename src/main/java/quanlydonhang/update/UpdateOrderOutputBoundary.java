package quanlydonhang.update;

import adapters.updateorder.UpdateOrderOutputDTO;

public interface UpdateOrderOutputBoundary {
    void presentSuccess(UpdateOrderOutputDTO output);
    void presentError(String error);
}