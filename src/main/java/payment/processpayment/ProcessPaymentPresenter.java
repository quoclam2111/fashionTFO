package payment.processpayment;

import java.util.List;

import repository.DTO.ResultPaymentDTO;

public interface ProcessPaymentPresenter {
    void present(ProcessPaymentOutputModel output);
    void presentError(String message);
    void presentValidationErrors(List<ResultPaymentDTO.FieldError> errors);
}
