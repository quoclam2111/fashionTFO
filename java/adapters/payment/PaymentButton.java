package adapters.payment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import adapters.Home.HomeController;
import adapters.order.OrderFormPayment;
import repository.DTO.ProductVariantDTO;

public class PaymentButton extends JButton {

    private List<ProductVariantDTO> cartItems;
    private HomeController controller;

    public PaymentButton(List<ProductVariantDTO> cartItems) {
        super("Thanh toán");
        this.cartItems = cartItems;
        this.controller = controller;

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OrderFormPayment.openForm(cartItems);
            }
        });
    }
}
