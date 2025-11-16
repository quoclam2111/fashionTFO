package adapters.order;

import java.util.List;

import frameworks.FormGUI;
import repository.DTO.ProductVariantDTO;

public class OrderFormPayment {
    public static void openForm(List<ProductVariantDTO> cartItems) {
        FormGUI form = new FormGUI(cartItems);
        form.setVisible(true);
    }
}
