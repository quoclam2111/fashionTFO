package payment.selectpayment;

public class SelectPaymentOutputModel {
    public final String orderId;
    public final String paymentMethod;
    public final int vat;
    public final int finalAmount;

    public SelectPaymentOutputModel(String orderId, String paymentMethod, int vat, int finalAmount) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.vat = vat;
        this.finalAmount = finalAmount;
    }
}
