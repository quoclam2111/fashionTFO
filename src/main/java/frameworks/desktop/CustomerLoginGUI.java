//package frameworks.desktop;
//
//import javax.swing.*;
//import adapters.quanlynguoidung.dangnhap.*;
//import quanlynguoidung.dangnhap.LoginUseCase;
//import repository.jdbc.LoginRepoImpl;
//import java.awt.*;
//
///**
// * Form đăng nhập dành riêng cho KHÁCH HÀNG
// * Chặn nhân viên đăng nhập ở đây
// */
//public class CustomerLoginGUI extends JFrame {
//    private JTextField txtUsername;
//    private JPasswordField txtPassword;
//    private JButton btnLogin, btnBack;
//    
//    public CustomerLoginGUI() {
//        initComponents();
//    }
//    
//    private void initComponents() {
//        setTitle("Fashion Store - Đăng Nhập Khách Hàng");
//        setSize(500, 600);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.setBackground(Color.WHITE);
//        
//        // ========== HEADER ==========
//        JPanel headerPanel = new JPanel();
//        headerPanel.setBackground(new Color(239, 83, 80));
//        headerPanel.setPreferredSize(new Dimension(500, 150));
//        headerPanel.setLayout(new GridBagLayout());
//        
//        JLabel lblTitle = new JLabel("👤 ĐĂNG NHẬP");
//        lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
//        lblTitle.setForeground(Color.WHITE);
//        
//        JLabel lblSubtitle = new JLabel("Dành cho Khách hàng");
//        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));
//        lblSubtitle.setForeground(new Color(255, 255, 255, 200));
//        
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.insets = new Insets(5, 0, 5, 0);
//        headerPanel.add(lblTitle, gbc);
//        
//        gbc.gridy = 1;
//        headerPanel.add(lblSubtitle, gbc);
//        
//        // ========== LOGIN FORM ==========
//        JPanel formPanel = new JPanel();
//        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
//        formPanel.setBackground(Color.WHITE);
//        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
//        
//        // Username
//        JLabel lblUsername = new JLabel("Tên đăng nhập");
//        lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
//        lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
//        formPanel.add(lblUsername);
//        
//        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
//        
//        txtUsername = new JTextField();
//        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
//        txtUsername.setMaximumSize(new Dimension(350, 40));
//        txtUsername.setPreferredSize(new Dimension(350, 40));
//        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
//        formPanel.add(txtUsername);
//        
//        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
//        
//        // Password
//        JLabel lblPassword = new JLabel("Mật khẩu");
//        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
//        lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
//        formPanel.add(lblPassword);
//        
//        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
//        
//        txtPassword = new JPasswordField();
//        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
//        txtPassword.setMaximumSize(new Dimension(350, 40));
//        txtPassword.setPreferredSize(new Dimension(350, 40));
//        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
//        formPanel.add(txtPassword);
//        
//        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
//        
//        // Login Button
//        btnLogin = new JButton("Đăng Nhập");
//        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
//        btnLogin.setBackground(new Color(239, 83, 80));
//        btnLogin.setForeground(Color.WHITE);
//        btnLogin.setFocusPainted(false);
//        btnLogin.setMaximumSize(new Dimension(350, 45));
//        btnLogin.setPreferredSize(new Dimension(350, 45));
//        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
//        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        btnLogin.addActionListener(e -> handleLogin());
//        formPanel.add(btnLogin);
//        
//        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
//        
//        // Back Button
//        btnBack = new JButton("⬅️ Quay lại");
//        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
//        btnBack.setBackground(Color.WHITE);
//        btnBack.setForeground(new Color(100, 100, 100));
//        btnBack.setFocusPainted(false);
//        btnBack.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
//        btnBack.setMaximumSize(new Dimension(350, 45));
//        btnBack.setPreferredSize(new Dimension(350, 45));
//        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
//        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        btnBack.addActionListener(e -> dispose());
//        formPanel.add(btnBack);
//        
//        mainPanel.add(headerPanel, BorderLayout.NORTH);
//        mainPanel.add(formPanel, BorderLayout.CENTER);
//        
//        add(mainPanel);
//    }
//    
//    private void handleLogin() {
//        String username = txtUsername.getText();
//        String password = new String(txtPassword.getPassword());
//        
//        if (username.isEmpty() || password.isEmpty()) {
//            JOptionPane.showMessageDialog(this,
//                "Vui lòng nhập đầy đủ thông tin!",
//                "Lỗi",
//                JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        
//        // Gọi LoginUseCase
//        LoginInputDTO dto = new LoginInputDTO();
//        dto.username = username;
//        dto.password = password;
//        
//        LoginViewModel model = new LoginViewModel();
//        LoginPresenter presenter = new LoginPresenter(model);
//        
//        LoginRepoImpl repo = new LoginRepoImpl();
//        LoginUseCase uc = new LoginUseCase(repo, presenter);
//        
//        LoginController controller = new LoginController(uc);
//        
//        try {
//            controller.executeWithDTO(dto);
//            
//            if (model.success) {
//                // Kiểm tra loại tài khoản
//                if ("NHANVIEN".equals(model.accountType)) {
//                    // ❌ Chặn nhân viên đăng nhập ở đây
//                    JOptionPane.showMessageDialog(this,
//                        "Tài khoản nhân viên không thể đăng nhập ở đây!\n" +
//                        "Vui lòng sử dụng trang đăng nhập dành cho Nhân viên.",
//                        "Không có quyền truy cập",
//                        JOptionPane.WARNING_MESSAGE);
//                    
//                } else {
//                    // ✅ Khách hàng đăng nhập thành công
//                    JOptionPane.showMessageDialog(this,
//                        "Chào mừng: " + model.fullName,
//                        "Đăng nhập thành công",
//                        JOptionPane.INFORMATION_MESSAGE);
//                    
//                    this.dispose();
//                    
//                    // Truyền UUID thật từ database
//                    CustomerMainMenuGUI customerMenu = new CustomerMainMenuGUI(
//                        model.userId,    // UUID từ database
//                        model.username
//                    );
//                    customerMenu.setVisible(true);
//                }
//                
//            } else {
//                JOptionPane.showMessageDialog(this,
//                    model.message,
//                    "Lỗi đăng nhập",
//                    JOptionPane.ERROR_MESSAGE);
//            }
//            
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this,
//                "Lỗi: " + ex.getMessage(),
//                "Lỗi",
//                JOptionPane.ERROR_MESSAGE);
//        }
//    }
//}