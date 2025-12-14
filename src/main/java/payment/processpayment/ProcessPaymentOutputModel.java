package payment.processpayment;

public class ProcessPaymentOutputModel {
    public final String orderId;
    public final boolean needGatewayRedirect;
    public final String message;
    public final String gatewayInfo;

    public ProcessPaymentOutputModel(
        String orderId,
        boolean needGatewayRedirect,
        String message,
        String gatewayInfo
    ) {
        this.orderId = orderId;
        this.needGatewayRedirect = needGatewayRedirect;
        this.message = message;
        this.gatewayInfo = gatewayInfo;
    }

    public boolean needRedirect() {
        return gatewayInfo != null;
    }
}