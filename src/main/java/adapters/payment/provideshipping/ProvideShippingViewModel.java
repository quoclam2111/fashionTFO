package adapter.payment.provideshipping;

import java.util.List;

import repository.DTO.ResultPaymentDTO;

public class ProvideShippingViewModel {
    public final boolean success;
    public final String orderId;
    public final String phone;
    public final String address;
    public final String message;
    public final List<ResultPaymentDTO.FieldError> fieldErrors;

    private ProvideShippingViewModel(boolean success, String orderId, String phone, 
                                     String address, String message, 
                                     List<ResultPaymentDTO.FieldError> fieldErrors) {
        this.success = success;
        this.orderId = orderId;
        this.phone = phone;
        this.address = address;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public static ProvideShippingViewModel ok(String orderId, String phone, String address) {
        return new ProvideShippingViewModel(true, orderId, phone, address, null, null);
    }

    public static ProvideShippingViewModel error(String message) {
        return new ProvideShippingViewModel(false, null, null, null, message, null);
    }

    public static ProvideShippingViewModel validationErrors(List<ResultPaymentDTO.FieldError> errors) {
        return new ProvideShippingViewModel(false, null, null, null, null, errors);
    }
}
