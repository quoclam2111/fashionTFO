package view;

import java.util.List;

import model.ProductVariantDTO;

public class OrderFormPayment {
    public static void openForm(List<ProductVariantDTO> cartItems) {
        FormGUI form = new FormGUI(cartItems);
        form.setVisible(true);
    }
}
