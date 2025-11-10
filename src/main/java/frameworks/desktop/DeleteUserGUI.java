//package frameworks.desktop;
//
//import javax.swing.*;
//import java.awt.*;
//import adapters.delete.*;
//import frameworks.desktop.Subscriber;
//import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
//import quanlynguoidung.delete.DeleteUserUseCase;
//import repository.jdbc.UserRepoImpl;
//
//public class DeleteUserGUI extends JFrame implements Subscriber {
//    private DeleteUserController controller;
//    private DeleteUserViewModel viewModel;
//    private DeleteUserPublisher publisher;
//
//    // Input fields
//    private JTextField txtUserId;
//    
//    // Buttons
//    private JButton btnDelete;
//    private JButton btnClear;
//    
//    // Result display
//    private JTextArea txtResult;
//
//    public DeleteUserGUI() {
//        initComponents();
//        setupMVC();
//    }
//
//    private void initComponents() {
//        setTitle("Xóa Người Dùng");
//        setSize(600, 450);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setLayout(new BorderLayout(10, 10));
//
//        // ========== MAIN PANEL ==========
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        // ========== PANEL INPUT ==========
//        JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
//        panelInput.setBorder(BorderFactory.createTitledBorder("Thông Tin Xóa"));
//        panelInput.setMaximumSize(new Dimension(580, 80));
//        
//        panelInput.add(new JLabel("User ID: *"));
//        txtUserId = new JTextField(35);
//        panelInput.add(txtUserId);
//        
//        mainPanel.add(panelInput);
//        mainPanel.add(Box.createVerticalStrut(10));
//
//        // ========== PANEL BUTTONS ==========
//        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
//        panelButtons.setMaximumSize(new Dimension(580, 80));
//        
//        btnDelete = new JButton("Xóa Người Dùng");
//        btnDelete.setPreferredSize(new Dimension(200, 45));
//        btnDelete.setFont(new Font("Arial", Font.BOLD, 14));
//        btnDelete.setBackground(new Color(220, 53, 69));
//        btnDelete.setForeground(Color.WHITE);
//        btnDelete.setFocusPainted(false);
//        btnDelete.addActionListener(e -> handleDelete());
//        
//        btnClear = new JButton("Xóa Trắng");
//        btnClear.setPreferredSize(new Dimension(180, 45));
//        btnClear.setFont(new Font("Arial", Font.BOLD, 14));
//        btnClear.setBackground(new Color(108, 117, 125));
//        btnClear.setForeground(Color.WHITE);
//        btnClear.setFocusPainted(false);
//        btnClear.addActionListener(e -> clearFields());
//        
//        panelButtons.add(btnDelete);
//        panelButtons.add(btnClear);
//        
//        mainPanel.add(panelButtons);
//        mainPanel.add(Box.createVerticalStrut(10));
//
//        // ========== PANEL RESULT ==========
//        JPanel panelResult = new JPanel(new BorderLayout());
//        panelResult.setBorder(BorderFactory.createTitledBorder("Kết Quả"));
//        
//        txtResult = new JTextArea(10, 40);
//        txtResult.setEditable(false);
//        txtResult.setFont(new Font("Monospaced", Font.PLAIN, 12));
//        JScrollPane scrollPane = new JScrollPane(txtResult);
//        panelResult.add(scrollPane, BorderLayout.CENTER);
//        
//        mainPanel.add(panelResult);
//        
//        add(mainPanel, BorderLayout.CENTER);
//    }
//
//    private void setupMVC() {
//        // Setup Model-View-Controller
//        viewModel = new DeleteUserViewModel();
//        publisher = new DeleteUserPublisher();
//        publisher.addSubscriber(this);
//
//        UserRepoImpl repository = new UserRepoImpl();
//        QuanLyNguoiDungOutputBoundary presenter = new DeleteUserPresenter(viewModel);
//        DeleteUserUseCase useCase = new DeleteUserUseCase(repository, presenter);
//        controller = new DeleteUserController(useCase);
//    }
//
//    private void handleDelete() {
//        try {
//            // Validate User ID
//            String userId = txtUserId.getText().trim();
//            if (userId.isEmpty()) {
//                JOptionPane.showMessageDialog(this, 
//                    "Vui lòng nhập User ID!", 
//                    "Lỗi", 
//                    JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            // Confirm trước khi xóa
//            int confirm = JOptionPane.showConfirmDialog(this, 
//                "Bạn có chắc chắn muốn xóa người dùng với ID: " + userId + "?\n" +
//                "Hành động này không thể hoàn tác!", 
//                "Xác Nhận Xóa", 
//                JOptionPane.YES_NO_OPTION,
//                JOptionPane.WARNING_MESSAGE);
//
//            if (confirm != JOptionPane.YES_OPTION) {
//                return;
//            }
//
//            // Tạo DTO
//            DeleteUserInputDTO input = new DeleteUserInputDTO();
//            input.userId = userId;
//
//            // Gọi controller
//            controller.execute(input);
//            
//            // Notify để update UI
//            publisher.notifySubscribers();
//
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, 
//                "Lỗi: " + ex.getMessage(), 
//                "Lỗi", 
//                JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void clearFields() {
//        txtUserId.setText("");
//        txtResult.setText("");
//    }
//
//    @Override
//    public void update() {
//        // Hiển thị kết quả từ ViewModel
//        StringBuilder result = new StringBuilder();
//        result.append("=== KẾT QUẢ XÓA ===\n");
//        result.append("Trạng thái: ").append(viewModel.success ? "✓ Thành công" : "✗ Thất bại").append("\n");
//        result.append("Thông báo: ").append(viewModel.message).append("\n");
//        result.append("Thời gian: ").append(viewModel.timestamp).append("\n");
//
//        if (viewModel.success) {
//            result.append("\n--- THÔNG TIN ĐÃ XÓA ---\n");
//            result.append("User ID: ").append(viewModel.deletedUserId).append("\n");
//            result.append("Username: ").append(viewModel.deletedUsername).append("\n");
//        }
//
//        txtResult.setText(result.toString());
//        
//        // Hiển thị dialog thông báo
//        if (viewModel.success) {
//            JOptionPane.showMessageDialog(this, 
//                viewModel.message, 
//                "Thành công", 
//                JOptionPane.INFORMATION_MESSAGE);
//            // Clear field sau khi xóa thành công
//            txtUserId.setText("");
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            DeleteUserGUI gui = new DeleteUserGUI();
//            gui.setVisible(true);
//        });
//    }
//}


package frameworks.desktop;

import javax.swing.*;
import java.awt.*;
import adapters.delete.*;
import frameworks.desktop.Subscriber;
import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.delete.DeleteUserUseCase;
import repository.jdbc.UserRepoImpl;

public class DeleteUserGUI extends JFrame implements Subscriber {
    private DeleteUserController controller;
    private DeleteUserViewModel viewModel;
    private DeleteUserPublisher publisher;

    // Input fields
    private JTextField txtUserId;
    
    // Buttons
    private JButton btnDelete;
    private JButton btnClear;
    
    // Result display
    private JTextArea txtResult;

    public DeleteUserGUI() {
        initComponents();
        setupMVC();
    }
    
    // ✅ Method để set User ID từ bên ngoài
    public void setUserId(String userId) {
        txtUserId.setText(userId);
    }

    private void initComponents() {
        setTitle("Xóa Người Dùng");
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
        
        panelInput.add(new JLabel("User ID: *"));
        txtUserId = new JTextField(35);
        panelInput.add(txtUserId);
        
        mainPanel.add(panelInput);
        mainPanel.add(Box.createVerticalStrut(10));

        // ========== PANEL BUTTONS ==========
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelButtons.setMaximumSize(new Dimension(580, 80));
        
        btnDelete = new JButton("Xóa Người Dùng");
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
        viewModel = new DeleteUserViewModel();
        publisher = new DeleteUserPublisher();
        publisher.addSubscriber(this);

        UserRepoImpl repository = new UserRepoImpl();
        QuanLyNguoiDungOutputBoundary presenter = new DeleteUserPresenter(viewModel);
        DeleteUserUseCase useCase = new DeleteUserUseCase(repository, presenter);
        controller = new DeleteUserController(useCase);
    }

    private void handleDelete() {
        try {
            // Validate User ID
            String userId = txtUserId.getText().trim();
            if (userId.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng nhập User ID!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Confirm trước khi xóa
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa người dùng với ID: " + userId + "?\n" +
                "Hành động này không thể hoàn tác!", 
                "Xác Nhận Xóa", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Tạo DTO
            DeleteUserInputDTO input = new DeleteUserInputDTO();
            input.userId = userId;

            // Gọi controller
            controller.execute(input);
            
            // Notify để update UI
            publisher.notifySubscribers();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtUserId.setText("");
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
            result.append("User ID: ").append(viewModel.deletedUserId).append("\n");
            result.append("Username: ").append(viewModel.deletedUsername).append("\n");
        }

        txtResult.setText(result.toString());
        
        // Hiển thị dialog thông báo
        if (viewModel.success) {
            JOptionPane.showMessageDialog(this, 
                viewModel.message, 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            // Clear field sau khi xóa thành công
            txtUserId.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DeleteUserGUI gui = new DeleteUserGUI();
            gui.setVisible(true);
        });
    }
}