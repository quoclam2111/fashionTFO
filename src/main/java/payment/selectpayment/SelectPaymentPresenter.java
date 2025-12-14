package payment.selectpayment;

import java.util.List;

import repository.DTO.ResultPaymentDTO;

public interface SelectPaymentPresenter {
    void present(SelectPaymentOutputModel out);
    void presentError(String message);
    void presentValidationErrors(List<ResultPaymentDTO.FieldError> errors);
}
