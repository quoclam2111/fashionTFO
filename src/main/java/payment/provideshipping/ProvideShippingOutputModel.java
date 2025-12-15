package payment.provideshipping;

public class ProvideShippingOutputModel {
    public final String orderId;
    public final String phone;
    public final String address;
//    public final String customer;

    public ProvideShippingOutputModel(String orderId, String phone, String address) {
        this.orderId = orderId;
        this.phone = phone;
        this.address = address;
//        this.customer = customer;
    }
}