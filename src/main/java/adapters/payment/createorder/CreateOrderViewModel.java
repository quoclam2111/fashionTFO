package adapters.payment.createorder;

import java.util.List;

import repository.DTO.ResultPaymentDTO;

public class CreateOrderViewModel {
    public final boolean success;
    public final String orderId;
    public final List<String> items;
    public final int itemTotal;
    public final int shippingFee;
    public final int vat;
    public final int finalAmount;
    public final String message;
    public final List<ResultPaymentDTO.FieldError> fieldErrors;

    private CreateOrderViewModel(boolean success, String orderId, List<String> items, int itemTotal, int shippingFee, int vat, int finalAmount, String message, List<ResultPaymentDTO.FieldError> fieldErrors) {
        this.success = success;
        this.orderId = orderId;
        this.items = items;
        this.itemTotal = itemTotal;
        this.shippingFee = shippingFee;
        this.vat = vat;
        this.finalAmount = finalAmount;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public static CreateOrderViewModel ok(String orderId, List<String> items, int itemTotal, int shippingFee, int vat, int finalAmount) {
        return new CreateOrderViewModel(true, orderId, items, itemTotal, shippingFee, vat, finalAmount, null, null);
    }

    public static CreateOrderViewModel error(String msg) {
        return new CreateOrderViewModel(false, null, null, 0,0,0,0, msg, null);
    }

    public static CreateOrderViewModel validationErrors(java.util.List<ResultPaymentDTO.FieldError> errors) {
        return new CreateOrderViewModel(false, null, null, 0,0,0,0, null, errors);
    }
}
