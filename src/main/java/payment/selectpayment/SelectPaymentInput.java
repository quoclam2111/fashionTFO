package payment.selectpayment;

public class SelectPaymentInput {
    public final String orderId;
    public final String method; // "COD" or "BANKING_QR"
    public final int distanceKm; // maybe needed to recalc shipping if desired

    public SelectPaymentInput(String orderId, String method, int distanceKm) {
        this.orderId = orderId;
        this.method = method;
        this.distanceKm = distanceKm;
    }
}