package payment.processpayment;

import repository.Publisher;
import repository.DTO.ResultPaymentDTO;

import java.util.List;

import adapter.payment.processpayment.ProcessPaymentViewModel;

public class ProcessPaymentPresenterImpl implements ProcessPaymentPresenter {

    private final Publisher<ProcessPaymentViewModel> publisher;

    public ProcessPaymentPresenterImpl(Publisher<ProcessPaymentViewModel> publisher) {
        this.publisher = publisher;
    }

    @Override
    public void present(ProcessPaymentOutputModel out) {
        if (out.needGatewayRedirect) {
            publisher.publish(
                ProcessPaymentViewModel.redirect(out.gatewayInfo)
            );
        } else {
            publisher.publish(
                ProcessPaymentViewModel.paid(out.message)
            );
        }
    }

    @Override
    public void presentError(String message) {
        publisher.publish(
            ProcessPaymentViewModel.error(message)
        );
    }

    @Override
    public void presentValidationErrors(List<ResultPaymentDTO.FieldError> errors) {
        publisher.publish(
            ProcessPaymentViewModel.validationErrors(errors)
        );
    }
}