package adapter.payment.selectpayment;

import java.util.List;

import repository.DTO.ResultPaymentDTO;

public class SelectPaymentViewModel {
    public final boolean success;
    public final String orderId;
    public final String paymentMethod;
    public final int vat;
    public final int finalAmount;
    public final String message;
    public final List<ResultPaymentDTO.FieldError> fieldErrors;

    private SelectPaymentViewModel(boolean success, String orderId, String paymentMethod, int vat, int finalAmount, String message, List<ResultPaymentDTO.FieldError> fieldErrors) {
        this.success = success; this.orderId = orderId; this.paymentMethod = paymentMethod; this.vat = vat; this.finalAmount = finalAmount; this.message = message; this.fieldErrors = fieldErrors;
    }
    public static SelectPaymentViewModel ok(String orderId, String method, int vat, int finalAmount) { return new SelectPaymentViewModel(true, orderId, method, vat, finalAmount, null, null); }
    public static SelectPaymentViewModel error(String msg) { return new SelectPaymentViewModel(false, null, null,0,0,msg, null); }
    public static SelectPaymentViewModel validationErrors(List<ResultPaymentDTO.FieldError> errors) { return new SelectPaymentViewModel(false, null, null,0,0,null, errors); }
}