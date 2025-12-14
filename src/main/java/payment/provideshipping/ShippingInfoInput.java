package payment.provideshipping;

public class ShippingInfoInput {
    public final String orderId;
    public final String phone;
    public final String address;

    public ShippingInfoInput(String orderId, String phone, String address) {
        this.orderId = orderId;
        this.phone = phone;
        this.address = address;
    }
}