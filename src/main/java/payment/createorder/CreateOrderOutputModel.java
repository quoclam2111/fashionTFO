package payment.createorder;
import java.util.List;

public class CreateOrderOutputModel {
    public final String orderId;
    public final List<String> itemsSummary;
    public final int itemTotal;
    public final int shippingFee;
    public final int vat;
    public final int finalAmount;

    public CreateOrderOutputModel(String orderId, List<String> itemsSummary, int itemTotal, int shippingFee, int vat, int finalAmount) {
        this.orderId = orderId;
        this.itemsSummary = itemsSummary;
        this.itemTotal = itemTotal;
        this.shippingFee = shippingFee;
        this.vat = vat;
        this.finalAmount = finalAmount;
    }
}