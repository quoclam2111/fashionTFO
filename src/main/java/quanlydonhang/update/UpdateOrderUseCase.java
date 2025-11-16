package quanlydonhang.update;

import adapters.updateorder.UpdateOrderInputDTO;
import adapters.updateorder.UpdateOrderOutputDTO;
import repository.UpdateOrderRepositoryGateway;
import repository.DTO.OrderDTO;

public class UpdateOrderUseCase implements UpdateOrderInputBoundary {
    private UpdateOrderRepositoryGateway repository;
    private UpdateOrderOutputBoundary presenter;

    public UpdateOrderUseCase(
            UpdateOrderRepositoryGateway repository,
            UpdateOrderOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void execute(UpdateOrderInputDTO input) {
        // Validate
        if (input.orderId == null || input.orderId.trim().isEmpty()) {
            presenter.presentError("Order ID không được để trống!");
            return;
        }

        if (input.customerName == null || input.customerName.trim().isEmpty()) {
            presenter.presentError("Tên khách hàng không được để trống!");
            return;
        }

        if (input.customerPhone == null || input.customerPhone.trim().isEmpty()) {
            presenter.presentError("Số điện thoại không được để trống!");
            return;
        }

        if (input.totalAmount <= 0) {
            presenter.presentError("Tổng tiền phải lớn hơn 0!");
            return;
        }

        try {
            // Tạo OrderDTO để cập nhật
            OrderDTO dto = new OrderDTO();
            dto.id = input.orderId;
            dto.userId = input.userId;
            dto.customerName = input.customerName;
            dto.customerPhone = input.customerPhone;
            dto.customerAddress = input.customerAddress;
            dto.totalAmount = input.totalAmount;
            dto.status = input.status;
            dto.note = input.note;

            // Cập nhật vào database
            repository.update(dto);

            // Tạo output
            UpdateOrderOutputDTO output = new UpdateOrderOutputDTO();
            output.success = true;
            output.message = "Cập nhật đơn hàng thành công!";
            output.updatedOrderId = input.orderId;

            presenter.presentSuccess(output);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("ORDER_NOT_FOUND")) {
                presenter.presentError("Không tìm thấy đơn hàng với ID: " + input.orderId);
            } else {
                presenter.presentError("Lỗi khi cập nhật đơn hàng: " + e.getMessage());
            }
        } catch (Exception e) {
            presenter.presentError("Lỗi không xác định: " + e.getMessage());
        }
    }
}