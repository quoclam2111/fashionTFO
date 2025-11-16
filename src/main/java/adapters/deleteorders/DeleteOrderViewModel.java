package adapters.deleteorders;

import frameworks.desktop.Subscriber;
import java.util.ArrayList;
import java.util.List;

public class DeleteOrderViewModel {
    public boolean success;
    public String message;
    public String deletedOrderId;

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