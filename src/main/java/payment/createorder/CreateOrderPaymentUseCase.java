package payment.createorder;

import application.port.CartRepository;
import pay.entity.Order;
import pay.entity.OrderItem;
import pay.entity.PaymentMethod;
import payment.provideshipping.ShippingFeeCalculator;
import repository.DTO.ResultPaymentDTO;
import repository.payment.OrderPaymentRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use Case: create order from cart and prepare data for payment form.
 * It uses Presenter to send output. No UI code here.
 */
public class CreateOrderPaymentUseCase {
    private final CartRepository cartRepo;
    private final OrderPaymentRepository orderRepo;
    private final ShippingFeeCalculator shippingCalc;

    public CreateOrderPaymentUseCase(CartRepository cartRepo, OrderPaymentRepository orderRepo, ShippingFeeCalculator shippingCalc) {
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.shippingCalc = shippingCalc;
    }

    public void execute(CreateOrderInput input, CreateOrderPresenter presenter) {
        // 1. basic validation
        if (input == null) {
            presenter.presentError("Input missing");
            return;
        }
        if (input.userId == null || input.userId.isBlank()) {
            presenter.presentError("UserId required");
            return;
        }

        List<OrderItem> items = cartRepo.getCartForUser(input.userId);
        if (items == null || items.isEmpty()) {
            presenter.presentError("Cart is empty");
            return;
        }

        try {
            Order order = new Order(UUID.randomUUID(), items);
            order.calculateItemTotal();
            order.calculateShippingFee(shippingCalc, input.distanceKm);

            // default payment method none -> vat 0
            order.calculateVATFor(null);
            order.calculateFinalAmount();

            orderRepo.save(order);

            List<String> summary = items.stream().map(i -> i.getName() + " x" + i.getQuantity() + " = " + i.getTotal()).collect(Collectors.toList());
            CreateOrderOutputModel out = new CreateOrderOutputModel(order.getId().toString(), summary, order.getItemTotal(), order.getShippingFee(), order.getVat(), order.getFinalAmount());
            presenter.present(out);
        } catch (Exception ex) {
            presenter.presentError("Unexpected error: " + ex.getMessage());
        }
    }
}