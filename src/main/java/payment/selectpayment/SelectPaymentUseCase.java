package payment.selectpayment;

import entity.Order;
import entity.PaymentMethod;
import repository.DTO.ResultPaymentDTO;
import repository.payment.OrderPaymentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SelectPaymentUseCase {
    private final OrderPaymentRepository orderRepo;

    public SelectPaymentUseCase(OrderPaymentRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public void execute(SelectPaymentInput input, SelectPaymentPresenter presenter) {
        List<ResultPaymentDTO.FieldError> errors = new ArrayList<>();
        if (input == null) { presenter.presentError("Input missing"); return; }
        if (input.method == null || input.method.isBlank()) errors.add(new ResultPaymentDTO.FieldError("method","Phương thức thanh toán bắt buộc"));

        if (!errors.isEmpty()) { presenter.presentValidationErrors(errors); return; }

        UUID id;
        try { id = UUID.fromString(input.orderId); } catch (Exception ex) { presenter.presentError("OrderId invalid"); return; }
        Optional<Order> maybe = orderRepo.findById(id);
        if (!maybe.isPresent()) { presenter.presentError("Order not found"); return; }

        Order order = maybe.get();
        try {
            PaymentMethod pm = new PaymentMethod(input.method);
            order.setPaymentMethod(pm);

            // recalc VAT and final
            order.calculateVATFor(pm);
            order.calculateFinalAmount();
            orderRepo.save(order);

            SelectPaymentOutputModel out = new SelectPaymentOutputModel(order.getId().toString(), pm.getCode(), order.getVat(), order.getFinalAmount());
            presenter.present(out);
        } catch (IllegalArgumentException ex) {
            presenter.presentError("Invalid payment method");
        } catch (Exception ex) {
            presenter.presentError("Unexpected error: " + ex.getMessage());
        }
    }
}
