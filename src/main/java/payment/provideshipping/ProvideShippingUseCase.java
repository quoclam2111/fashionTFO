package payment.provideshipping;

import pay.entity.Order;
import repository.DTO.ResultPaymentDTO;
import repository.payment.OrderPaymentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProvideShippingUseCase {
    private final OrderPaymentRepository orderRepo;

    public ProvideShippingUseCase(OrderPaymentRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public void execute(ShippingInfoInput input, ProvideShippingPresenter presenter) {
        // validate input => collect field errors
        List<ResultPaymentDTO.FieldError> errors = new ArrayList<>();
        if (input == null) {
            presenter.presentError("Input missing");
            return;
        }
        if (input.phone == null || !input.phone.matches("^[0-9]{10,11}$")) {
            errors.add(new ResultPaymentDTO.FieldError("phone", "Số điện thoại không hợp lệ"));
        }
        if (input.address == null || input.address.isBlank()) {
            errors.add(new ResultPaymentDTO.FieldError("address", "Địa chỉ không được để trống"));
        }
        if (!errors.isEmpty()) {
            presenter.presentValidationErrors(errors);
            return;
        }

        // parse orderId
        UUID id;
        try {
            id = UUID.fromString(input.orderId);
        } catch (Exception ex) {
            presenter.presentError("OrderId format invalid");
            return;
        }

        Optional<Order> maybe = orderRepo.findById(id);
        if (!maybe.isPresent()) {
            presenter.presentError("Order not found");
            return;
        }

        Order order = maybe.get();
        order.setPhone(input.phone);
        order.setAddress(input.address);
        orderRepo.save(order);

        ProvideShippingOutputModel out = new ProvideShippingOutputModel(order.getId().toString(), order.getPhone(), order.getAddress());
        presenter.present(out);
    }
}
