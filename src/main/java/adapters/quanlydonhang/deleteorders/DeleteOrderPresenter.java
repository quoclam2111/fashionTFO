package adapters.quanlydonhang.deleteorders;
import quanlydonhang.delete.DeleteOrderOutputBoundary;

public class DeleteOrderPresenter implements DeleteOrderOutputBoundary {
    private DeleteOrderViewModel viewModel;

    public DeleteOrderPresenter(DeleteOrderViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(DeleteOrderOutputDTO output) {
        viewModel.success = true;
        viewModel.message = "✅ Xóa đơn hàng thành công!";
        viewModel.deletedOrderId = output.deletedOrderId;
        viewModel.notifySubscribers();
    }

    @Override
    public void presentError(String error) {
        viewModel.success = false;
        viewModel.message = "❌ " + error;
        viewModel.notifySubscribers();
    }
}