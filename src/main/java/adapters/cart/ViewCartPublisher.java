package adapters.cart;

import quanlynguoidung.cart.ViewCartOutputData;
import java.util.List;
import javax.swing.*;

public class ViewCartPublisher {
    private final ViewCartViewModel viewModel;

    public ViewCartPublisher(ViewCartViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void publish() {
        System.out.println("=== Giỏ hàng của bạn ===");
        viewModel.getCartItems().forEach(item -> {
            System.out.printf("- %s | Giá: %.2f | SL: %d | Tổng: %.2f%n",
                    item.getProductName(), item.getPrice(), item.getQuantity(), item.getTotal());
        });
        double total = viewModel.getCartItems().stream()
                .mapToDouble(ViewCartOutputData::getTotal).sum();
        System.out.printf("Tổng cộng: %.2f%n", total);
    }
    
    // SỬA LỖI Ở ĐÂY
    public void publishToSwing(JTable table) {
        List<ViewCartOutputData> items = viewModel.getCartItems();

        String[] columns = {"Tên sản phẩm", "Giá", "Số lượng", "Tổng"};
        Object[][] data = new Object[items.size()][4];

        for (int i = 0; i < items.size(); i++) {
            ViewCartOutputData item = items.get(i); 
            
            data[i][0] = item.getProductName();
            data[i][1] = item.getPrice();
            data[i][2] = item.getQuantity();
            data[i][3] = item.getTotal();
        }

        table.setModel(new javax.swing.table.DefaultTableModel(data, columns));
    }
}