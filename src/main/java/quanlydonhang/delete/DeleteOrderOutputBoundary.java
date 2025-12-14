package quanlydonhang.delete;

import adapters.quanlydonhang.deleteorders.DeleteOrderOutputDTO;

public interface DeleteOrderOutputBoundary {
    void presentSuccess(DeleteOrderOutputDTO output);
    void presentError(String error);
}