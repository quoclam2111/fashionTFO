package repository;

public interface Publisher<VM> {
    void publish(VM viewModel);
}