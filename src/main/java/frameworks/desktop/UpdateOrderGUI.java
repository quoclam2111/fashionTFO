package frameworks.desktop;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Optional;

import javax.swing.*;

import adapters.quanlydonhang.updateorder.UpdateOrderController;
import adapters.quanlydonhang.updateorder.UpdateOrderInputDTO;
import adapters.quanlydonhang.updateorder.UpdateOrderPresenter;
import adapters.quanlydonhang.updateorder.UpdateOrderViewModel;
import quanlydonhang.update.UpdateOrderUseCase;
import repository.DTO.OrderDTO;
import repository.jdbc.OrderRepoImpl;


public class UpdateOrderGUI extends JFrame implements Subscriber {
    private String orderId;
    private JTextField txtUserId;
    private JTextField txtCustomerName;
    private JTextField txtCustomerPhone;
    private JTextField txtCustomerAddress;
    private JTextField txtTotalAmount;
    private JComboBox<String> cmbStatus;
    private JTextField txtNote;
    private JButton btnUpdate;
    private UpdateOrderViewModel viewModel;

    public UpdateOrderGUI(String orderId) {
        this.orderId = orderId;

        setTitle("Sửa đơn hàng - " + orderId);
        setSize(600, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // User ID
        mainPanel.add(new JLabel("User ID:"));
        txtUserId = new JTextField();
        mainPanel.add(txtUserId);

        // Tên khách hàng
        mainPanel.add(new JLabel("Tên khách hàng:"));
        txtCustomerName = new JTextField();
        mainPanel.add(txtCustomerName);

        // Số điện thoại
        mainPanel.add(new JLabel("Số điện thoại:"));
        txtCustomerPhone = new JTextField();
        mainPanel.add(txtCustomerPhone);

        // Địa chỉ
        mainPanel.add(new JLabel("Địa chỉ:"));
        txtCustomerAddress = new JTextField();
        mainPanel.add(txtCustomerAddress);

        // Tổng tiền
        mainPanel.add(new JLabel("Tổng tiền:"));
        txtTotalAmount = new JTextField();
        mainPanel.add(txtTotalAmount);

        // Trạng thái
        mainPanel.add(new JLabel("Trạng thái:"));
        cmbStatus = new JComboBox<>(new String[]{
                "pending", "confirmed", "shipping", "completed", "cancelled"
        });
        mainPanel.add(cmbStatus);

        // Ghi chú
        mainPanel.add(new JLabel("Ghi chú:"));
        txtNote = new JTextField();
        mainPanel.add(txtNote);

        // Nút cập nhật
        btnUpdate = new JButton("💾 Cập nhật đơn hàng");
        btnUpdate.setBackground(new Color(255, 149, 0));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(new JLabel());
        mainPanel.add(btnUpdate);

        add(mainPanel);

        // Load dữ liệu đơn hàng
        loadOrderData();

        // Event
        btnUpdate.addActionListener(e -> updateOrder());

        setVisible(true);
    }

    private void loadOrderData() {
        try {
            OrderRepoImpl repo = new OrderRepoImpl();
            Optional<OrderDTO> orderOpt = repo.findById(orderId);

            if (orderOpt.isPresent()) {
                OrderDTO order = orderOpt.get();
                txtUserId.setText(order.userId != null ? order.userId : "");
                txtCustomerName.setText(order.customerName != null ? order.customerName : "");
                txtCustomerPhone.setText(order.customerPhone != null ? order.customerPhone : "");
                txtCustomerAddress.setText(order.customerAddress != null ? order.customerAddress : "");
                txtTotalAmount.setText(String.valueOf(order.totalAmount));
                cmbStatus.setSelectedItem(order.status != null ? order.status : "pending");
                txtNote.setText(order.note != null ? order.note : "");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy đơn hàng!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void updateOrder() {
        UpdateOrderInputDTO input = new UpdateOrderInputDTO();
        input.orderId = this.orderId;
        input.userId = txtUserId.getText().trim();
        input.customerName = txtCustomerName.getText().trim();
        input.customerPhone = txtCustomerPhone.getText().trim();
        input.customerAddress = txtCustomerAddress.getText().trim();
        input.status = (String) cmbStatus.getSelectedItem();
        input.note = txtNote.getText().trim();

        try {
            input.totalAmount = Double.parseDouble(txtTotalAmount.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Tổng tiền phải là số!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        viewModel = new UpdateOrderViewModel();
        viewModel.addSubscriber(this);
        UpdateOrderPresenter presenter = new UpdateOrderPresenter(viewModel);
        OrderRepoImpl repo = new OrderRepoImpl();
        UpdateOrderUseCase uc = new UpdateOrderUseCase(repo, presenter);
        UpdateOrderController controller = new UpdateOrderController(uc);

        controller.execute(input);
    }

    @Override
    public void update() {
        if (viewModel.success) {
            JOptionPane.showMessageDialog(this,
                    viewModel.message,
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Đóng form sau khi cập nhật thành công
        } else {
            JOptionPane.showMessageDialog(this,
                    viewModel.message,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}