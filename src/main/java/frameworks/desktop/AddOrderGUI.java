package frameworks.desktop;

import javax.swing.*;
import adapters.addorder.AddOrderController;
import adapters.addorder.AddOrderInputDTO;
import adapters.addorder.AddOrderPresenter;
import adapters.addorder.AddOrderViewModel;
import quanlydonhang.them.AddOrderUseCase;
import repository.jdbc.OrderRepoImpl;
import java.awt.*;

public class AddOrderGUI extends JFrame implements Subscriber {
    private JTextField txtUserId, txtCustomerName, txtCustomerPhone, txtCustomerAddress, txtTotalAmount, txtNote;
    private JButton btnAdd;
    private AddOrderViewModel model;

    public AddOrderGUI() {
        setTitle("Thêm đơn hàng");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblUserId = new JLabel("User ID:");
        txtUserId = new JTextField(20);
        JLabel lblCustomerName = new JLabel("Tên khách hàng:");
        txtCustomerName = new JTextField(20);
        JLabel lblCustomerPhone = new JLabel("SĐT khách hàng:");
        txtCustomerPhone = new JTextField(20);
        JLabel lblCustomerAddress = new JLabel("Địa chỉ:");
        txtCustomerAddress = new JTextField(20);
        JLabel lblTotalAmount = new JLabel("Tổng tiền:");
        txtTotalAmount = new JTextField(20);
        JLabel lblNote = new JLabel("Ghi chú:");
        txtNote = new JTextField(20);

        addFormRow(formPanel, gbc, lblUserId, txtUserId);
        addFormRow(formPanel, gbc, lblCustomerName, txtCustomerName);
        addFormRow(formPanel, gbc, lblCustomerPhone, txtCustomerPhone);
        addFormRow(formPanel, gbc, lblCustomerAddress, txtCustomerAddress);
        addFormRow(formPanel, gbc, lblTotalAmount, txtTotalAmount);
        addFormRow(formPanel, gbc, lblNote, txtNote);

        btnAdd = new JButton("➕ Thêm đơn hàng");
        btnAdd.setBackground(new Color(52, 199, 89));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        btnAdd.addActionListener(e -> {
            AddOrderInputDTO dto = new AddOrderInputDTO();
            dto.userId = txtUserId.getText();
            dto.customerName = txtCustomerName.getText();
            dto.customerPhone = txtCustomerPhone.getText();
            dto.customerAddress = txtCustomerAddress.getText();
            dto.note = txtNote.getText();

            try {
                dto.totalAmount = Double.parseDouble(txtTotalAmount.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Tổng tiền phải là số!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            model = new AddOrderViewModel();
            model.addSubscriber(this);
            AddOrderPresenter presenter = new AddOrderPresenter(model);

            OrderRepoImpl repo = new OrderRepoImpl();
            AddOrderUseCase uc = new AddOrderUseCase(repo, presenter);

            AddOrderController controller = new AddOrderController(uc);

            try {
                controller.execute(dto);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    @Override
    public void update() {
        if (model.success) {
            JOptionPane.showMessageDialog(this,
                    model.message + "\nOrder ID: " + model.orderId +
                            "\nTrạng thái: " + model.orderStatus +
                            "\nThời gian: " + model.timestamp,
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

            // Xóa form sau khi thêm thành công
            txtUserId.setText("");
            txtCustomerName.setText("");
            txtCustomerPhone.setText("");
            txtCustomerAddress.setText("");
            txtTotalAmount.setText("");
            txtNote.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                    model.message,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, JLabel label, JComponent field) {
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
        gbc.gridy++;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddOrderGUI());
    }
}