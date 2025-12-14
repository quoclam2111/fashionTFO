package payment.selectpayment;

import repository.payment.Publisher;
import repository.DTO.ResultPaymentDTO;

import java.util.List;

import adapters.payment.selectpayment.SelectPaymentViewModel;

public class SelectPaymentPresenterImpl implements SelectPaymentPresenter {

    private final Publisher<SelectPaymentViewModel> publisher;

    public SelectPaymentPresenterImpl(Publisher<SelectPaymentViewModel> pub) {
        this.publisher = pub;
    }

    @Override
    public void present(SelectPaymentOutputModel out) {
        publisher.publish(
            SelectPaymentViewModel.ok(
                out.orderId,
                out.paymentMethod,
                out.vat,
                out.finalAmount
            )
        );
    }

    @Override
    public void presentError(String message) {
        publisher.publish(SelectPaymentViewModel.error(message));
    }

    @Override
    public void presentValidationErrors(List<ResultPaymentDTO.FieldError> errors) {
        publisher.publish(SelectPaymentViewModel.validationErrors(errors));
    }
}