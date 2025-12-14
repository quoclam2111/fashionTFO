package quanlydonhang.delete;

import adapters.quanlydonhang.deleteorders.DeleteOrderInputDTO;
import adapters.quanlydonhang.deleteorders.DeleteOrderOutputDTO;
import repository.hoadon.DeleteOrderRepositoryGateway;

public class DeleteOrderUseCase implements DeleteOrderInputBoundary {
    private DeleteOrderRepositoryGateway repository;
    private DeleteOrderOutputBoundary presenter;

    public DeleteOrderUseCase(
            DeleteOrderRepositoryGateway repository,
            DeleteOrderOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteOrderInputDTO input) {
        // Validate
        if (input.orderId == null || input.orderId.trim().isEmpty()) {
            presenter.presentError("Order ID không được để trống!");
            return;
        }

        try {
            // ⭐ Xóa order items trước, sau đó xóa order
            repository.deleteById(input.orderId);

            // Tạo output
            DeleteOrderOutputDTO output = new DeleteOrderOutputDTO();
            output.success = true;
            output.message = "Xóa đơn hàng thành công!";
            output.deletedOrderId = input.orderId;

            presenter.presentSuccess(output);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("ORDER_NOT_FOUND")) {
                presenter.presentError("Không tìm thấy đơn hàng với ID: " + input.orderId);
            } else {
                presenter.presentError("Lỗi khi xóa đơn hàng: " + e.getMessage());
            }
        } catch (Exception e) {
            presenter.presentError("Lỗi không xác định: " + e.getMessage());
        }
    }
}