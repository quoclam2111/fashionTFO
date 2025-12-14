package adapters.quanlydonhang.deleteorders;

import quanlydonhang.delete.DeleteOrderInputBoundary;

public class DeleteOrderController {
    private DeleteOrderInputBoundary useCase;

    public DeleteOrderController(DeleteOrderInputBoundary useCase) {
        this.useCase = useCase;
    }

    public void execute(DeleteOrderInputDTO input) {
        useCase.execute(input);
    }
}