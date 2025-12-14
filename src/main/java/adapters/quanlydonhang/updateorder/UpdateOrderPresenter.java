package adapters.quanlydonhang.updateorder;

import quanlydonhang.update.UpdateOrderOutputBoundary;

public class UpdateOrderPresenter implements UpdateOrderOutputBoundary {
    private UpdateOrderViewModel viewModel;

    public UpdateOrderPresenter(UpdateOrderViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(UpdateOrderOutputDTO output) {
        viewModel.success = true;
        viewModel.message = "✅ Cập nhật đơn hàng thành công!";
        viewModel.updatedOrderId = output.updatedOrderId;
        viewModel.notifySubscribers();
    }

    @Override
    public void presentError(String error) {
        viewModel.success = false;
        viewModel.message = "❌ " + error;
        viewModel.notifySubscribers();
    }
}