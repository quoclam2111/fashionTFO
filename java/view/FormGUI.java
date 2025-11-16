package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import Controller.OderController;
import model.OrderDetail;
import model.ProductVariantDTO;

public class FormGUI extends JFrame {
    private List<ProductVariantDTO> cartItems;
    private JLabel orderInfoLabel;
    private OderController oderController;
    private OrderDetail currentOrderDetail;

    public FormGUI(List<ProductVariantDTO> cartItems) {
        this.cartItems = cartItems;
        this.oderController = new OderController();

        setTitle("Thông tin đơn hàng");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Danh sách sản phẩm trong giỏ hàng", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Bảng hiển thị sản phẩm
        String[] colNames = {"Tên sản phẩm", "Màu", "Size", "SL", "Giá", "Thành tiền"};
        String[][] data = new String[this.cartItems.size()][6];
        double total = 0;
        for (int i = 0; i < this.cartItems.size(); i++) {
            ProductVariantDTO p = this.cartItems.get(i);
            data[i][0] = p.getProductName();
            data[i][1] = p.getColorName();
            data[i][2] = p.getSizeName();
            data[i][3] = String.valueOf(p.getQuantity());
            data[i][4] = String.format("%.2f", p.getPrice());
            data[i][5] = String.format("%.2f", p.getPrice() * p.getQuantity());
            total += p.getPrice() * p.getQuantity();
        }

        JTable table = new JTable(data, colNames);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel nút
        JButton btnCreateOrder = new JButton("Tạo đơn hàng");
        JButton btnConfirm = new JButton("Xác nhận thanh toán");

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnCreateOrder);
        btnPanel.add(btnConfirm);

        orderInfoLabel = new JLabel("Chưa có đơn hàng nào được tạo", JLabel.CENTER);
        add(orderInfoLabel, BorderLayout.SOUTH);
        add(btnPanel, BorderLayout.NORTH);

        // Nút tạo đơn hàng
        btnCreateOrder.addActionListener((ActionEvent e) -> {
            String customerId = "KH001"; // có thể lấy từ login
            String paymentMethod = "";    // chưa chọn

            // Tạo OrderDetail thông qua controller
            currentOrderDetail = oderController.handleCreateOrder(customerId, paymentMethod, this.cartItems);
            if (currentOrderDetail != null) {
                orderInfoLabel.setText("<html>Mã đơn hàng: <b>" + currentOrderDetail.getOrderId() +
                        "</b> | Tổng tiền: " + currentOrderDetail.getTotalAmount() + " VND</html>");
            }
        });

        // Nút xác nhận thanh toán
        btnConfirm.addActionListener((ActionEvent e) -> {
            if (currentOrderDetail != null) {
                // Mở form chọn phương thức thanh toán
                PaymentMethodForm methodForm = new PaymentMethodForm(currentOrderDetail);
                methodForm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng tạo đơn hàng trước!");
            }
        });
    }
}
