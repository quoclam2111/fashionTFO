package payment.provideshipping;

public class ShippingFeeCalculator {
    private final int baseFee;
    private final int perKm;

    public ShippingFeeCalculator(int baseFee, int perKm) {
        if (baseFee < 0 || perKm < 0) throw new IllegalArgumentException("fee negative");
        this.baseFee = baseFee;
        this.perKm = perKm;
    }

    public int calculate(int distanceKm) {
        if (distanceKm < 0) throw new IllegalArgumentException("distance negative");
        return baseFee + perKm * distanceKm;
    }
}