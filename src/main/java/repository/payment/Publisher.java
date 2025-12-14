package repository.payment;

public interface Publisher<VM> {
    void publish(VM viewModel);
}