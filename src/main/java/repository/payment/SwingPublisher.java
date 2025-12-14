package repository.payment;

import javax.swing.SwingUtilities;

import java.util.function.Consumer;

public class SwingPublisher<VM> implements Publisher<VM> {
    private final Consumer<VM> consumer;
    public SwingPublisher(Consumer<VM> consumer) { this.consumer = consumer; }
    @Override
    public void publish(VM viewModel) {
        SwingUtilities.invokeLater(() -> consumer.accept(viewModel));
    }
}