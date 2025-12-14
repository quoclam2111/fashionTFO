package payment.createorder;

import repository.Publisher;
import repository.DTO.ResultPaymentDTO;

import java.util.List;
import java.util.stream.Collectors;

import adapter.payment.createorder.CreateOrderViewModel;



public class CreateOrderPaymentPresenterImpl implements CreateOrderPresenter {
    private final Publisher<CreateOrderViewModel> publisher;

    public CreateOrderPaymentPresenterImpl(Publisher<CreateOrderViewModel> publisher) { this.publisher = publisher; }

    @Override
    public void present(CreateOrderOutputModel output) {
        // Đã sửa: Thay vì gọi 'new CreateOrderViewModel(...)' (gây lỗi do không có constructor public 6 tham số),
        // chúng ta gọi phương thức tĩnh 'CreateOrderViewModel.ok(...)' đã được định nghĩa.
        CreateOrderViewModel vm = CreateOrderViewModel.ok( 
                output.orderId,
                output.itemsSummary,
                output.itemTotal,
                output.shippingFee,
                output.vat,
                output.finalAmount
        );
        publisher.publish(vm);
    }

    @Override
    public void presentError(String message) {
        CreateOrderViewModel vm = CreateOrderViewModel.error(message);
        publisher.publish(vm);
    }

    @Override
    public void presentValidationErrors(java.util.List<repository.DTO.ResultPaymentDTO.FieldError> errors) {
        CreateOrderViewModel vm = CreateOrderViewModel.validationErrors(errors);
        publisher.publish(vm);
    }
}