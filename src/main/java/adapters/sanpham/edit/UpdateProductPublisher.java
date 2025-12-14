package adapters.sanpham.edit;

import frameworks.desktop.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class UpdateProductPublisher {
    private List<Subscriber> subscribers = new ArrayList<>();
    
    public void addSubscriber(Subscriber sub) {
        subscribers.add(sub);
    }
    
    public void removeSubscriber(Subscriber sub) {
        subscribers.remove(sub);
    }
    
    public void notifySubscribers() {
        for (Subscriber subscriber : subscribers) {
            subscriber.update();
        }
    }
}