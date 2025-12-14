package payment.createorder;

import java.util.Objects;

public class CreateOrderInput {
    public final String userId;
    public final int distanceKm;

    public CreateOrderInput(String userId, int distanceKm) {
        this.userId = Objects.requireNonNull(userId);
        this.distanceKm = distanceKm;
    }
}