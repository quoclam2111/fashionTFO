package frameworks.desktop;

import adapters.sanpham.get.GetProductController;
import adapters.sanpham.get.GetProductInputDTO;
import adapters.sanpham.get.GetProductPresenter;
import adapters.sanpham.get.GetProductViewModel;
import adapters.sanpham.edit.UpdateProductController;
import adapters.sanpham.edit.UpdateProductInputDTO;
import adapters.sanpham.edit.UpdateProductPresenter;
import adapters.sanpham.edit.UpdateProductViewModel;
import quanlysanpham.get.GetProductUseCase;
import quanlysanpham.edit.UpdateProductUseCase;
import repository.jdbc.SanPhamRepositoryImpl;

import javax.swing.*;
import java.awt.*;

public class EditProductGUI extends JFrame {
    private JTextField txtProductName;
    private JTextArea txtDescription;
    private JTextField txtBrandId;
    private JTextField txtCategoryId;
    private JTextField txtDefaultImage;
    private JTextField txtPrice;
    private JTextField txtDiscountPrice;
    private JTextField txtStockQuantity;
    private JComboBox<String> cmbStatus;
    private JButton btnSave;
    private JButton btnCancel;

    private String productId;
    private final ProductUpdateListener listener;

    public EditProductGUI(String productId, ProductUpdateListener listener) {
        this.productId = productId;
        this.listener = listener;

        setTitle("Sửa sản phẩm #" + productId);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Product Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Tên sản phẩm:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtProductName = new JTextField();
        formPanel.add(txtProductName, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDescription = new JTextArea(3, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        formPanel.add(scrollDesc, gbc);

        // Brand ID
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Brand ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtBrandId = new JTextField();
        formPanel.add(txtBrandId, gbc);

        // Category ID
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Category ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCategoryId = new JTextField();
        formPanel.add(txtCategoryId, gbc);

        // Default Image
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(new JLabel("Ảnh mặc định:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDefaultImage = new JTextField();
        formPanel.add(txtDefaultImage, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        formPanel.add(new JLabel("Giá:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPrice = new JTextField();
        formPanel.add(txtPrice, gbc);

        // Discount Price
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        formPanel.add(new JLabel("Giá khuyến mãi:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDiscountPrice = new JTextField();
        formPanel.add(txtDiscountPrice, gbc);

        // Stock Quantity
        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0;
        formPanel.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtStockQuantity = new JTextField();
        formPanel.add(txtStockQuantity, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 8; gbc.weightx = 0;
        formPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbStatus = new JComboBox<>(new String[]{"PUBLISHED", "DRAFT", "ARCHIVED"});
        formPanel.add(cmbStatus, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("💾 Lưu");
        btnSave.setBackground(new Color(76, 175, 80));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);

        btnCancel = new JButton("❌ Hủy");
        btnCancel.setBackground(new Color(244, 67, 54));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Events
        btnSave.addActionListener(e -> saveProduct());
        btnCancel.addActionListener(e -> dispose());

        // Load product data
        loadProductData();

        setVisible(true);
    }

    private void loadProductData() {
        GetProductInputDTO dto = new GetProductInputDTO();
        dto.productId = this.productId;

        GetProductViewModel model = new GetProductViewModel();
        GetProductPresenter presenter = new GetProductPresenter(model);
        SanPhamRepositoryImpl repo = new SanPhamRepositoryImpl();
        GetProductUseCase uc = new GetProductUseCase(repo, presenter);
        GetProductController controller = new GetProductController(uc);

        try {
            controller.execute(dto);
            if (model.success && model.product != null) {
                // Điền dữ liệu vào form từ ViewModel
                txtProductName.setText(model.product.productName);
                txtDescription.setText(model.product.description);
                txtBrandId.setText(model.product.brandId != null ? model.product.brandId : "");
                txtCategoryId.setText(model.product.categoryId != null ? model.product.categoryId : "");
                txtDefaultImage.setText(model.product.defaultImage);
                txtPrice.setText(model.product.price != null ? model.product.price : "");
                txtDiscountPrice.setText(model.product.discountPrice != null ? model.product.discountPrice : "");
                txtStockQuantity.setText(model.product.stockQuantity != null ? String.valueOf(model.product.stockQuantity) : "0");
                cmbStatus.setSelectedItem(model.product.status);
            } else {
                JOptionPane.showMessageDialog(this,
                        model.message != null ? model.message : "Không tìm thấy sản phẩm!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void saveProduct() {
        // Validate
        if (txtProductName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập tên sản phẩm!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            txtProductName.requestFocus();
            return;
        }

        try {
            // gọi repository / usecase để update
            boolean success = updateProductInDB();
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                listener.onProductUpdated(); // thông báo cho ListProductsGUI
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    private boolean updateProductInDB() {
        // logic update sản phẩm
        UpdateProductInputDTO dto = new UpdateProductInputDTO();
        dto.productId = this.productId;
        dto.productName = txtProductName.getText().trim();
        dto.description = txtDescription.getText().trim();
        dto.brandId = txtBrandId.getText().trim();
        dto.categoryId = txtCategoryId.getText().trim();
        dto.defaultImage = txtDefaultImage.getText().trim();
        dto.price = txtPrice.getText().trim();
        dto.discountPrice = txtDiscountPrice.getText().trim();
        dto.stockQuantity = txtStockQuantity.getText().trim();
        dto.status = (String) cmbStatus.getSelectedItem();

        UpdateProductViewModel model = new UpdateProductViewModel();
        UpdateProductPresenter presenter = new UpdateProductPresenter(model);
        SanPhamRepositoryImpl repo = new SanPhamRepositoryImpl();
        UpdateProductUseCase uc = new UpdateProductUseCase(repo, presenter);
        UpdateProductController controller = new UpdateProductController(uc);
        controller.execute(dto);
        return true; // ví dụ luôn thành công
    }
}