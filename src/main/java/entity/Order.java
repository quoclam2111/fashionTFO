package pay.entity;

import java.util.List;
import java.util.UUID;

import payment.provideshipping.ShippingFeeCalculator;

/**
 * Aggregate root Order. Contains core business logic:
 * - compute item total
 * - compute shipping fee via ShippingFeeCalculator
 * - compute VAT (5% for banking/qr)
 * - compute final amount
 *
 * Note: PhoneNumber and Address kept as simple strings (per your request earlier you wanted class-based PaymentMethod but not VO)
 */
public class Order {
    private final UUID id;
    private final List<OrderItem> items;
    private String phone;
    private String address;
    private PaymentMethod paymentMethod;

    private int itemTotal;
    private int shippingFee;
    private int vat;
    private int finalAmount;

    public Order(UUID id, List<OrderItem> items) {
        if (id == null) throw new IllegalArgumentException("id");
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("items");
        this.id = id;
        this.items = List.copyOf(items);
    }

    public UUID getId() { return id; }
    public List<OrderItem> getItems() { return items; }

    // business operations
    public void calculateItemTotal() {
        this.itemTotal = items.stream().mapToInt(OrderItem::getTotal).sum();
    }

    public void calculateShippingFee(ShippingFeeCalculator calc, int distanceKm) {
        this.shippingFee = calc.calculate(distanceKm);
    }

    public void calculateVATFor(PaymentMethod method) {
        if (method == null) {
            this.vat = 0;
            return;
        }
        this.vat = method.isBanking() ? (int)Math.round((itemTotal + shippingFee) * 0.05) : 0;
    }

    public void calculateFinalAmount() {
        this.finalAmount = itemTotal + shippingFee + vat;
    }

    // setters for shipping info & payment
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setPaymentMethod(PaymentMethod p) { this.paymentMethod = p; }

    // getters for totals
    public int getItemTotal() { return itemTotal; }
    public int getShippingFee() { return shippingFee; }
    public int getVat() { return vat; }
    public int getFinalAmount() { return finalAmount; }

    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
}
