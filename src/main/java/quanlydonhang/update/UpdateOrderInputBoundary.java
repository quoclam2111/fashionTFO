package quanlydonhang.update;

import adapters.quanlydonhang.updateorder.UpdateOrderInputDTO;

public interface UpdateOrderInputBoundary {
    void execute(UpdateOrderInputDTO input);
}