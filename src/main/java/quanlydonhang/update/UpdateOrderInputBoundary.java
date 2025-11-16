package quanlydonhang.update;

import adapters.updateorder.UpdateOrderInputDTO;

public interface UpdateOrderInputBoundary {
    void execute(UpdateOrderInputDTO input);
}