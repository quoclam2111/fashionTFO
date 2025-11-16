package quanlydonhang.delete;

import adapters.deleteorders.DeleteOrderOutputDTO;

public interface DeleteOrderOutputBoundary {
    void presentSuccess(DeleteOrderOutputDTO output);
    void presentError(String error);
}