package frameworks.desktop;

import adapters.cart.*;
import javax.swing.*;
import java.awt.*;

public class ViewCartGUI extends JFrame {

    private JTable table;
    private ViewCartViewModel viewModel;
    private ViewCartController controller;
    private ViewCartPublisher publisher;

    public ViewCartGUI(ViewCartController controller, ViewCartViewModel viewModel, ViewCartPublisher publisher) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.publisher = publisher;

        setTitle("Giỏ hàng của bạn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnLoad = new JButton("Xem giỏ hàng");
        btnLoad.addActionListener(e -> {
            controller.viewCart("user-uuid-here");
            publisher.publishToSwing(table);
        });
        add(btnLoad, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // Kết nối các tầng theo Clean Architecture
        ViewCartViewModel viewModel = new ViewCartViewModel();
        ViewCartPresenter presenter = new ViewCartPresenter(viewModel);
        repository.jdbc.CartRepositoryImpl repo = new repository.jdbc.CartRepositoryImpl();
        cart.ViewCartUseCase useCase = new cart.ViewCartUseCase(repo, presenter);
        ViewCartController controller = new ViewCartController(useCase);
        ViewCartPublisher publisher = new ViewCartPublisher(viewModel);

        SwingUtilities.invokeLater(() -> new ViewCartGUI(controller, viewModel, publisher).setVisible(true));
    }
}
