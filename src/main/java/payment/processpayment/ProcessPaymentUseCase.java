package payment.processpayment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import repository.payment.*;
import entity.Order;
import entity.PaymentMethod;
import repository.payment.OrderPaymentRepository;

import java.util.Optional;
import java.util.UUID;

public class ProcessPaymentUseCase {
    private final OrderPaymentRepository orderRepo;
    private final PaymentGateway gateway;
    private final ProcessPaymentPresenter presenter;
    private final Gson gson;

    public ProcessPaymentUseCase(
        OrderPaymentRepository orderRepo,
        PaymentGateway gateway,
        ProcessPaymentPresenter presenter
    ) {
        this.orderRepo = orderRepo;
        this.gateway = gateway;
        this.presenter = presenter;
        this.gson = new Gson();
    }

    public void execute(ProcessPaymentInput input) {
        if (input == null || input.orderId == null) {
            presenter.presentError("orderId required");
            return;
        }

        UUID id;
        try {
            id = UUID.fromString(input.orderId);
        } catch (Exception ex) {
            presenter.presentError("orderId invalid");
            return;
        }

        Optional<Order> maybe = orderRepo.findById(id);
        if (!maybe.isPresent()) {
            presenter.presentError("Order not found");
            return;
        }

        Order order = maybe.get();
        PaymentMethod pm = order.getPaymentMethod();

        if (pm == null) {
            presenter.presentError("Payment method not selected");
            return;
        }

        // COD: không cần gateway
        if (pm.isCOD()) {
            presenter.present(new ProcessPaymentOutputModel(
                order.getId().toString(),
                false,  // needGatewayRedirect = false
                "Đơn hàng sẽ thanh toán khi nhận hàng (COD)",
                null    // gatewayInfo = null
            ));
            return;
        }

        // Banking/QR: gọi gateway để tạo QR code
        try {
            String gatewayResponse = gateway.requestPayment(
                order.getId().toString(),
                order.getFinalAmount()
            );

            // Parse JSON response để kiểm tra success
            JsonObject jsonResponse = gson.fromJson(gatewayResponse, JsonObject.class);
            
            // Kiểm tra nếu gateway trả về lỗi
            if (jsonResponse.has("success") && !jsonResponse.get("success").getAsBoolean()) {
                String errorMsg = jsonResponse.has("error") 
                    ? jsonResponse.get("error").getAsString() 
                    : "Unknown error from payment gateway";
                presenter.presentError("Không thể tạo mã thanh toán: " + errorMsg);
                return;
            }

            // Thành công - trả về gatewayInfo
            presenter.present(new ProcessPaymentOutputModel(
                order.getId().toString(),
                true,   // needGatewayRedirect = true
                "Vui lòng quét mã QR để thanh toán",
                gatewayResponse  // JSON string chứa: qrCode, accountNo, accountName, etc.
            ));

        } catch (Exception ex) {
            presenter.presentError("Lỗi hệ thống: " + ex.getMessage());
        }
    }
}