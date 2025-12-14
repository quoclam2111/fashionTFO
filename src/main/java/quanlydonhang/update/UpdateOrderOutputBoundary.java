package quanlydonhang.update;

import adapters.quanlydonhang.updateorder.UpdateOrderOutputDTO;

public interface UpdateOrderOutputBoundary {
    void presentSuccess(UpdateOrderOutputDTO output);
    void presentError(String error);
}