package payment.provideshipping;

import repository.payment.Publisher;
import repository.DTO.ResultPaymentDTO;

import java.util.List;

import adapters.payment.provideshipping.ProvideShippingViewModel;

public class ProvideShippingPresenterImpl implements ProvideShippingPresenter {
    private final Publisher<ProvideShippingViewModel> publisher;

    public ProvideShippingPresenterImpl(Publisher<ProvideShippingViewModel> publisher) {
        this.publisher = publisher;
    }

    @Override
    public void present(ProvideShippingOutputModel out) {
        publisher.publish(
            ProvideShippingViewModel.ok(out.orderId, out.phone, out.address)
        );
    }

    @Override
    public void presentError(String message) {
        publisher.publish(ProvideShippingViewModel.error(message));
    }

    @Override
    public void presentValidationErrors(List<ResultPaymentDTO.FieldError> errors) {
        publisher.publish(ProvideShippingViewModel.validationErrors(errors));
    }
}
