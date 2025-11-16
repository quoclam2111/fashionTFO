package presenter;

import view.interfaces.OutputInterface;
import model.OrderDetail;
import view.PaymentSuccessViewModel;

public class OutputPresenter implements OutputInterface {

    private final String actionType;

    // Cho phép truyền loại thông báo vào constructor
    public OutputPresenter(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public void output(OrderDetail orderDetail) {
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
