package frameworks.desktop;

import adapters.sanpham.add.AddProductController;
import adapters.sanpham.add.AddProductInputDTO;
import adapters.sanpham.add.AddProductPresenter;
import adapters.sanpham.add.AddProductViewModel;
import quanlysanpham.ProductManageControl;
import quanlysanpham.add.AddProductUseCase;
import repository.jdbc.SanPhamRepositoryImpl;

import javax.swing.*;
import java.awt.*;

public class AddProductUi extends JFrame {
    private JTextField txtProductName, txtDescription, txtBrandId, txtCategoryId,
            txtDefaultImage, txtPrice, txtDiscountPrice, txtStockQuantity, txtStatus;
    private JButton btnAdd, btnCancel;
    private ProductUpdateListener listener;

    public AddProductUi(ProductUpdateListener listener) {
        this.listener = listener;

        setTitle("Thêm sản phẩm");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout(10, 10));

        // ===== Tiêu đề chính giữa =====
        JLabel lblTitle = new JLabel("➕ Thêm sản phẩm", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblTitle, BorderLayout.NORTH);

        // ===== Form =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblProductName = new JLabel("Tên sản phẩm:");
        txtProductName = new JTextField(20);
        JLabel lblDescription = new JLabel("Mô tả:");
        txtDescription = new JTextField(20);
        JLabel lblBrandId = new JLabel("Brand ID:");
        txtBrandId = new JTextField(20);
        JLabel lblCategoryId = new JLabel("Category ID:");
        txtCategoryId = new JTextField(20);
        JLabel lblDefaultImage = new JLabel("Ảnh mặc định:");
        txtDefaultImage = new JTextField(20);
        JLabel lblPrice = new JLabel("Giá:");
        txtPrice = new JTextField(20);
        JLabel lblDiscountPrice = new JLabel("Giá khuyến mãi:");
        txtDiscountPrice = new JTextField(20);
        JLabel lblStockQuantity = new JLabel("Số lượng:");
        txtStockQuantity = new JTextField(20);

        addFormRow(formPanel, gbc, lblProductName, txtProductName);
        addFormRow(formPanel, gbc, lblDescription, txtDescription);
        addFormRow(formPanel, gbc, lblBrandId, txtBrandId);
        addFormRow(formPanel, gbc, lblCategoryId, txtCategoryId);
        addFormRow(formPanel, gbc, lblDefaultImage, txtDefaultImage);
        addFormRow(formPanel, gbc, lblPrice, txtPrice);
        addFormRow(formPanel, gbc, lblDiscountPrice, txtDiscountPrice);
        addFormRow(formPanel, gbc, lblStockQuantity, txtStockQuantity);

        panel.add(formPanel, BorderLayout.CENTER);

        // ===== Button Panel =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnAdd = new JButton("➕ Thêm sản phẩm");
        btnAdd.setBackground(new Color(76, 175, 80));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdd.setPreferredSize(new Dimension(150, 35));

        btnCancel = new JButton("✖ Hủy");
        btnCancel.setBackground(new Color(244, 67, 54));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.setPreferredSize(new Dimension(150, 35));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCancel);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // ===== Xử lý sự kiện =====
        btnAdd.addActionListener(e -> addProduct());
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void addProduct() {
        AddProductInputDTO dto = new AddProductInputDTO();
        dto.productName = txtProductName.getText();
        dto.description = txtDescription.getText();
        dto.brandId = txtBrandId.getText();
        dto.categoryId = txtCategoryId.getText();
        dto.defaultImage = txtDefaultImage.getText();
        dto.price = txtPrice.getText();
        dto.discountPrice = txtDiscountPrice.getText();
        dto.stockQuantity = txtStockQuantity.getText();

        AddProductViewModel model = new AddProductViewModel();
        AddProductPresenter presenter = new AddProductPresenter(model);

        SanPhamRepositoryImpl repo = new SanPhamRepositoryImpl();
        AddProductUseCase uc = new AddProductUseCase(repo, presenter);

        AddProductController controller = new AddProductController(uc);

        try {
            controller.execute(dto);

            if(model.success) {
                JOptionPane.showMessageDialog(this,
                        model.message + "\nID sản phẩm: " + model.productId,
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                // Thông báo cho listener để refresh danh sách
                if (listener != null) {
                    listener.onProductUpdated();
                }

                // Đóng form sau khi thêm thành công
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, model.message,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        // Test standalone
        new AddProductUi(null);
    }
}