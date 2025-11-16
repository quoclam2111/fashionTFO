package quanlydonhang.delete;

import adapters.deleteorders.DeleteOrderInputDTO;

public interface DeleteOrderInputBoundary {
    void execute(DeleteOrderInputDTO input);
}