package payment.createorder;

import java.util.List;

import repository.DTO.ResultPaymentDTO;

public interface CreateOrderPresenter {
    void present(CreateOrderOutputModel output);
    void presentError(String message);
    void presentValidationErrors(List<ResultPaymentDTO.FieldError> errors);
}