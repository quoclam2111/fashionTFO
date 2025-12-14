package quanlydonhang.delete;

import adapters.quanlydonhang.deleteorders.DeleteOrderInputDTO;

public interface DeleteOrderInputBoundary {
    void execute(DeleteOrderInputDTO input);
}