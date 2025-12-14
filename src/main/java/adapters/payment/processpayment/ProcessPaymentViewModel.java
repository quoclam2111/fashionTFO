package adapters.payment.processpayment;

import java.util.List;

import repository.DTO.ResultPaymentDTO;

public class ProcessPaymentViewModel {

    public final boolean success;
    public final boolean needRedirect;
    public final String redirectInfo;
    public final String paymentMethod;
    public final String message;
    public final List<ResultPaymentDTO.FieldError> validationErrors;

    private ProcessPaymentViewModel(
            boolean success,
            boolean needRedirect,
            String redirectInfo,
            String paymentMethod,
            String message,
            List<ResultPaymentDTO.FieldError> validationErrors
    ) {
        this.success = success;
        this.needRedirect = needRedirect;
        this.redirectInfo = redirectInfo;
        this.paymentMethod = paymentMethod;
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public static ProcessPaymentViewModel redirect(String info) {
        return new ProcessPaymentViewModel(
            true, true, info, null, "Redirect to payment gateway", null
        );
    }

    public static ProcessPaymentViewModel paid(String message) {
        return new ProcessPaymentViewModel(
            true, false, null, null, message, null
        );
    }

    // FIX: Thêm method error
    public static ProcessPaymentViewModel error(String message) {
        return new ProcessPaymentViewModel(
            false, false, null, null, message, null
        );
    }

    // FIX: Thêm method validationErrors
    public static ProcessPaymentViewModel validationErrors(List<ResultPaymentDTO.FieldError> errors) {
        return new ProcessPaymentViewModel(
            false, false, null, null, "Validation failed", errors
        );
    }
}