package frameworks.desktop;

import adapters.sanpham.get.GetProductController;
import adapters.sanpham.get.GetProductInputDTO;
import adapters.sanpham.get.GetProductPresenter;
import adapters.sanpham.get.GetProductViewModel;
import quanlysanpham.get.GetProductUseCase;
import repository.jdbc.SanPhamRepositoryImpl;

import javax.swing.*;
import java.awt.*;

public class ViewProductGUI extends JFrame implements Subscriber {
    private JTextField txtProductId;
    private JButton btnSearch;
    private JButton btnClose;

    // Display fields
    private JTextField txtProductName;
    private JTextField txtSlug;
    private JTextArea txtDescription;
    private JTextField txtBrandId;
    private JTextField txtCategoryId;
    private JTextField txtDefaultImage;
    private JTextField txtPrice;
    private JTextField txtDiscountPrice;
    private JTextField txtStockQuantity;
    private JTextField txtStatus;
    private JTextField txtCreatedAt;
    private JTextField txtUpdatedAt;

    private GetProductViewModel viewModel;

    public ViewProductGUI() {
        initializeViewModel();
        initializeUI();
    }

    public ViewProductGUI(String productId) {
        initializeViewModel();
        initializeUI();
        txtProductId.setText(productId);
        loadProductInfo();
    }

    private void initializeViewModel() {
        viewModel = new GetProductViewModel();
        // Note: GetProductViewModel doesn't extend Publisher,
        // so we'll update manually after loading
    }

    private void initializeUI() {
        setTitle("Xem Chi Tiết Sản Phẩm");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = createTitlePanel();

        // Search Panel
        JPanel searchPanel = createSearchPanel();

        // Info Panel
        JPanel infoPanel = createInfoPanel();
        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.add(searchPanel, BorderLayout.NORTH);
        centerWrapper.add(contentPanel, BorderLayout.CENTER);

        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(245, 245, 245));

        JLabel lblTitle = new JLabel("CHI TIẾT SẢN PHẨM");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(50, 50, 50));

        panel.add(lblTitle);
        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblSearch = new JLabel("Tìm Kiếm Sản Phẩm");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchControls.setBackground(Color.WHITE);

        JLabel lblProductId = new JLabel("Mã Sản Phẩm:");
        lblProductId.setFont(new Font("Arial", Font.PLAIN, 13));

        txtProductId = new JTextField(30);
        txtProductId.setPreferredSize(new Dimension(300, 32));
        txtProductId.setFont(new Font("Arial", Font.PLAIN, 13));

        btnSearch = new JButton("🔍 Tìm Kiếm");
        btnSearch.setBackground(new Color(0, 123, 255));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Arial", Font.BOLD, 13));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setPreferredSize(new Dimension(130, 35));
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchControls.add(lblProductId);
        searchControls.add(txtProductId);
        searchControls.add(btnSearch);

        panel.add(lblSearch, BorderLayout.NORTH);
        panel.add(searchControls, BorderLayout.CENTER);

        // Event
        btnSearch.addActionListener(e -> loadProductInfo());
        txtProductId.addActionListener(e -> loadProductInfo()); // Enter key support

        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblInfo = new JLabel("Thông Tin Sản Phẩm");
        lblInfo.setFont(new Font("Arial", Font.BOLD, 14));
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblInfo);
        panel.add(Box.createVerticalStrut(15));

        // Create all fields
        txtProductName = createInfoField(panel, "Tên Sản Phẩm:");
        txtSlug = createInfoField(panel, "Slug:");
        txtDescription = createInfoTextArea(panel, "Mô Tả:");
        txtBrandId = createInfoField(panel, "Mã Thương Hiệu:");
        txtCategoryId = createInfoField(panel, "Mã Danh Mục:");
        txtDefaultImage = createInfoField(panel, "Hình Ảnh:");
        txtPrice = createInfoField(panel, "Giá Gốc:");
        txtDiscountPrice = createInfoField(panel, "Giá Giảm:");
        txtStockQuantity = createInfoField(panel, "Số Lượng Tồn:");
        txtStatus = createInfoField(panel, "Trạng Thái:");
        txtCreatedAt = createInfoField(panel, "Ngày Tạo:");
        txtUpdatedAt = createInfoField(panel, "Ngày Cập Nhật:");

        return panel;
    }

    private JTextField createInfoField(JPanel parent, String labelText) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 5));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setPreferredSize(new Dimension(150, 30));

        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 13));
        textField.setPreferredSize(new Dimension(400, 32));
        textField.setEditable(false);
        textField.setBackground(new Color(248, 249, 250));

        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(textField, BorderLayout.CENTER);

        parent.add(fieldPanel);
        parent.add(Box.createVerticalStrut(8));

        return textField;
    }

    private JTextArea createInfoTextArea(JPanel parent, String labelText) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 5));
        fieldPanel.setBackground(Color.WHITE);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setPreferredSize(new Dimension(150, 30));

        JTextArea textArea = new JTextArea(3, 40);
        textArea.setFont(new Font("Arial", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setBackground(new Color(248, 249, 250));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 80));

        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(scrollPane, BorderLayout.CENTER);

        parent.add(fieldPanel);
        parent.add(Box.createVerticalStrut(8));

        return textArea;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        btnClose = new JButton("✖ Đóng");
        btnClose.setBackground(new Color(108, 117, 125));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 13));
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setPreferredSize(new Dimension(150, 40));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnClose.addActionListener(e -> dispose());

        panel.add(btnClose);

        return panel;
    }

    private void loadProductInfo() {
        String productId = txtProductId.getText().trim();

        if (productId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập mã sản phẩm!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Create DTO
            GetProductInputDTO dto = new GetProductInputDTO();
            dto.productId = productId;

            // Create Presenter with ViewModel
            GetProductPresenter presenter = new GetProductPresenter(viewModel);

            // Create Repository
            SanPhamRepositoryImpl repo = new SanPhamRepositoryImpl();

            // Create Use Case
            GetProductUseCase useCase = new GetProductUseCase(repo, presenter);

            // Create Controller
            GetProductController controller = new GetProductController(useCase);

            // Execute
            controller.execute(dto);

            // Update UI manually (since ViewModel doesn't have Publisher)
            update();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải thông tin sản phẩm: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (viewModel.success && viewModel.product != null) {
            GetProductViewModel.ProductViewData product = viewModel.product;

            txtProductName.setText(product.productName);
            txtSlug.setText(product.slug);
            txtDescription.setText(product.description);
            txtBrandId.setText(product.brandId);
            txtCategoryId.setText(product.categoryId);
            txtDefaultImage.setText(product.defaultImage);
            txtPrice.setText(product.price);
            txtDiscountPrice.setText(product.discountPrice);
            txtStockQuantity.setText(String.valueOf(product.stockQuantity));
            txtStatus.setText(product.status);
            txtCreatedAt.setText(product.createdAt);
            txtUpdatedAt.setText(product.updatedAt);

        } else {
            clearFields();
            JOptionPane.showMessageDialog(this,
                    viewModel.message,
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearFields() {
        txtProductName.setText("");
        txtSlug.setText("");
        txtDescription.setText("");
        txtBrandId.setText("");
        txtCategoryId.setText("");
        txtDefaultImage.setText("");
        txtPrice.setText("");
        txtDiscountPrice.setText("");
        txtStockQuantity.setText("");
        txtStatus.setText("");
        txtCreatedAt.setText("");
        txtUpdatedAt.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewProductGUI());
    }
}