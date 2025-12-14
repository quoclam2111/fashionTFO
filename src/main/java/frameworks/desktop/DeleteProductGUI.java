package frameworks.desktop;

import javax.swing.*;
import java.awt.*;
import adapters.sanpham.delete.*;
import quanlysanpham.delete.DeleteProductUseCase;
import repository.jdbc.SanPhamRepositoryImpl;

public class DeleteProductGUI extends JFrame implements Subscriber {
    private DeleteProductController controller;
    private DeleteProductViewModel viewModel;
    private DeleteProductPublisher publisher;

    private ProductUpdateListener updateListener;

    // Input fields
    private JTextField txtProductId;

    // Buttons
    private JButton btnDelete;
    private JButton btnClear;

    // Result display
    private JTextArea txtResult;

    public DeleteProductGUI() {
        initComponents();
        setupMVC();
    }

    // ✅ Constructor với listener
    public DeleteProductGUI(ProductUpdateListener listener) {
        this.updateListener = listener;
        initComponents();
        setupMVC();
    }

    // ✅ Method để set Product ID từ bên ngoài
    public void setProductId(String productId) {
        txtProductId.setText(productId);
    }

    private void initComponents() {
        setTitle("Xóa Sản Phẩm");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ========== MAIN PANEL ==========
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ========== PANEL INPUT ==========
        JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder("Thông Tin Xóa"));
        panelInput.setMaximumSize(new Dimension(580, 80));

        panelInput.add(new JLabel("Mã Sản Phẩm: *"));
        txtProductId = new JTextField(35);
        panelInput.add(txtProductId);

        mainPanel.add(panelInput);
        mainPanel.add(Box.createVerticalStrut(10));

        // ========== PANEL BUTTONS ==========
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelButtons.setMaximumSize(new Dimension(580, 80));

        btnDelete = new JButton("Xóa Sản Phẩm");
        btnDelete.setPreferredSize(new Dimension(200, 45));
        btnDelete.setFont(new Font("Arial", Font.BOLD, 14));
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> handleDelete());

        btnClear = new JButton("Xóa Trắng");
        btnClear.setPreferredSize(new Dimension(180, 45));
        btnClear.setFont(new Font("Arial", Font.BOLD, 14));
        btnClear.setBackground(new Color(108, 117, 125));
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(e -> clearFields());

        panelButtons.add(btnDelete);
        panelButtons.add(btnClear);

        mainPanel.add(panelButtons);
        mainPanel.add(Box.createVerticalStrut(10));

        // ========== PANEL RESULT ==========
        JPanel panelResult = new JPanel(new BorderLayout());
        panelResult.setBorder(BorderFactory.createTitledBorder("Kết Quả"));

        txtResult = new JTextArea(10, 40);
        txtResult.setEditable(false);
        txtResult.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtResult);
        panelResult.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(panelResult);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupMVC() {
        // Setup Model-View-Controller
        viewModel = new DeleteProductViewModel();
        publisher = new DeleteProductPublisher();
        publisher.addSubscriber(this);

        SanPhamRepositoryImpl repository = new SanPhamRepositoryImpl();
        DeleteProductPresenter presenter = new DeleteProductPresenter(viewModel);
        DeleteProductUseCase useCase = new DeleteProductUseCase(repository, presenter);
        controller = new DeleteProductController(useCase);
    }

    private void handleDelete() {
        try {
            // Validate Product ID
            String productId = txtProductId.getText().trim();
            if (productId.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập Mã Sản Phẩm!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Confirm trước khi xóa
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa sản phẩm với Mã: " + productId + "?\n" +
                            "Hành động này không thể hoàn tác!",
                    "Xác Nhận Xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Tạo DTO
            DeleteProductInputDTO input = new DeleteProductInputDTO();
            input.productId = productId;

            // Gọi controller
            controller.execute(input);

            // Notify để update UI
            publisher.notifySubscribers();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        txtProductId.setText("");
        txtResult.setText("");
    }

    @Override
    public void update() {
        // Hiển thị kết quả từ ViewModel
        StringBuilder result = new StringBuilder();
        result.append("=== KẾT QUẢ XÓA ===\n");
        result.append("Trạng thái: ").append(viewModel.success ? "✓ Thành công" : "✗ Thất bại").append("\n");
        result.append("Thông báo: ").append(viewModel.message).append("\n");
        result.append("Thời gian: ").append(viewModel.timestamp).append("\n");

        if (viewModel.success) {
            result.append("\n--- THÔNG TIN ĐÃ XÓA ---\n");
            result.append("Mã Sản Phẩm: ").append(viewModel.deletedProductId).append("\n");
            result.append("Tên Sản Phẩm: ").append(viewModel.deleteProductName).append("\n");
        }

        txtResult.setText(result.toString());

        // Hiển thị dialog thông báo
        if (viewModel.success) {
            JOptionPane.showMessageDialog(this,
                    viewModel.message,
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear field sau khi xóa thành công
            txtProductId.setText("");

            // ⭐ Notify listener để refresh danh sách
            if (updateListener != null) {
                updateListener.onProductUpdated();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DeleteProductGUI gui = new DeleteProductGUI();
            gui.setVisible(true);
        });
    }
}