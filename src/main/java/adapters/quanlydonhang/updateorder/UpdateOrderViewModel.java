package adapters.quanlydonhang.updateorder;

import java.util.ArrayList;
import java.util.List;

import frameworks.desktop.Subscriber;

public class UpdateOrderViewModel {
    public boolean success;
    public String message;
    public String updatedOrderId;

    private List<Subscriber> subscribers = new ArrayList<>();

    public void addSubscriber(Subscriber s) {
        subscribers.add(s);
    }

    public void notifySubscribers() {
        for (Subscriber s : subscribers) {
            s.update();
        }
    }
}