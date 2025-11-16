package adapters.updateorder;

import quanlydonhang.update.UpdateOrderInputBoundary;

public class UpdateOrderController {
    private UpdateOrderInputBoundary useCase;

    public UpdateOrderController(UpdateOrderInputBoundary useCase) {
        this.useCase = useCase;
    }

    public void execute(UpdateOrderInputDTO input) {
        useCase.execute(input);
    }
}