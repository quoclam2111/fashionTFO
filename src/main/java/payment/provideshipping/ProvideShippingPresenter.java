package payment.provideshipping;

import java.util.List;

import repository.DTO.ResultPaymentDTO;

public interface ProvideShippingPresenter {
    void present(ProvideShippingOutputModel out);
    void presentError(String message);
    void presentValidationErrors(List<ResultPaymentDTO.FieldError> errors);
}