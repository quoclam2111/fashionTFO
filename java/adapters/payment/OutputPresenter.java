package adapters.payment;

import Pay.PaymentOutputBoundary;
import repository.DTO.OrderDetailDTO;

public class OutputPresenter implements PaymentOutputBoundary {

    private final String actionType;

    // Cho phép truyền loại thông báo vào constructor
    public OutputPresenter(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public void output(OrderDetailDTO orderDetail) {
        String message = switch (actionType.toLowerCase()) {
            case "order" -> "Đặt hàng thành công!";
            case "payment" -> "Thanh toán thành công!";
            default -> "Thao tác hoàn tất!";
        };

        PaymentSuccessViewModel viewModel = new PaymentSuccessViewModel(message);
        viewModel.showSuccessMessage();
//        viewModel.destroy();
    }
}
