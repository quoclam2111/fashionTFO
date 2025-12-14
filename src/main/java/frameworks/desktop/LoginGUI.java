package frameworks.desktop;

import javax.swing.*;
import adapters.quanlynguoidung.dangnhap.*;
import quanlynguoidung.dangnhap.LoginUseCase;
import repository.jdbc.LoginRepoImpl;
import java.awt.*;

/**
 * Form đăng nhập duy nhất - Tự động phân luồng theo loại tài khoản:
 * - NHANVIEN (Admin/Manager/Staff) → MainMenuGUI
 * - USER (Customer) → CustomerMainMenuGUI
 */
public class LoginGUI extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;
    
    public LoginGUI() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Fashion Store - Đăng Nhập");
        setSize(500, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // ========== HEADER ==========
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(239, 83, 80));
        headerPanel.setPreferredSize(new Dimension(500, 150));
        headerPanel.setLayout(new GridBagLayout());
        
        JLabel lblTitle = new JLabel("🛍️ FASHION STORE");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblSubtitle = new JLabel("Mua sắm thời trang trực tuyến");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitle.setForeground(new Color(255, 255, 255, 200));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 5, 0);
        headerPanel.add(lblTitle, gbc);
        
        gbc.gridy = 1;
        headerPanel.add(lblSubtitle, gbc);
        
        // ========== LOGIN FORM ==========
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        JLabel lblLogin = new JLabel("Đăng Nhập");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 24));
        lblLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(lblLogin);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Username
        JLabel lblUsername = new JLabel("Tên đăng nhập");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
        lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(lblUsername);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsername.setMaximumSize(new Dimension(350, 40));
        txtUsername.setPreferredSize(new Dimension(350, 40));
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(txtUsername);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Password
        JLabel lblPassword = new JLabel("Mật khẩu");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(lblPassword);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setMaximumSize(new Dimension(350, 40));
        txtPassword.setPreferredSize(new Dimension(350, 40));
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(txtPassword);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Login Button
        btnLogin = new JButton("Đăng Nhập");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setBackground(new Color(239, 83, 80));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setMaximumSize(new Dimension(350, 45));
        btnLogin.setPreferredSize(new Dimension(350, 45));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> handleLogin());
        formPanel.add(btnLogin);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Register Button
        btnRegister = new JButton("Đăng Ký Tài Khoản");
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 14));
        btnRegister.setBackground(Color.WHITE);
        btnRegister.setForeground(new Color(239, 83, 80));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createLineBorder(new Color(239, 83, 80), 2));
        btnRegister.setMaximumSize(new Dimension(350, 45));
        btnRegister.setPreferredSize(new Dimension(350, 45));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> openRegister());
        formPanel.add(btnRegister);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // ========== TEST ACCOUNTS INFO ==========
        JPanel testAccountsPanel = createTestAccountsPanel();
        formPanel.add(testAccountsPanel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createTestAccountsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 248, 225));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 193, 7), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(350, 250));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTestTitle = new JLabel("🔑 TÀI KHOẢN TEST");
        lblTestTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTestTitle.setForeground(new Color(255, 152, 0));
        lblTestTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTestTitle);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        addAccountInfo(panel, "👨‍💼 ADMIN", "admin / admin123", new Color(33, 150, 243));
        addAccountInfo(panel, "📊 MANAGER", "manager / manager123", new Color(76, 175, 80));
        addAccountInfo(panel, "👤 CUSTOMER", "khachhang / kh123", new Color(239, 83, 80));
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel lblNote = new JLabel("<html><center><i>* Chạy SQL để tạo tài khoản test</i></center></html>");
        lblNote.setFont(new Font("Arial", Font.ITALIC, 10));
        lblNote.setForeground(new Color(150, 150, 150));
        lblNote.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblNote);
        
        return panel;
    }
    
    private void addAccountInfo(JPanel panel, String role, String credentials, Color color) {
        JLabel lblRole = new JLabel(role);
        lblRole.setFont(new Font("Arial", Font.BOLD, 12));
        lblRole.setForeground(color);
        panel.add(lblRole);
        
        JLabel lblCred = new JLabel(credentials);
        lblCred.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(lblCred);
        
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }
    
    /**
     * Xử lý đăng nhập - TỰ ĐỘNG PHÂN LUỒNG theo accountType
     */
    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ thông tin!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Gọi LoginUseCase
        LoginInputDTO dto = new LoginInputDTO();
        dto.username = username;
        dto.password = password;
        
        LoginViewModel model = new LoginViewModel();
        LoginPresenter presenter = new LoginPresenter(model);
        
        LoginRepoImpl repo = new LoginRepoImpl();
        LoginUseCase uc = new LoginUseCase(repo, presenter);
        
        LoginController controller = new LoginController(uc);
        
        try {
            controller.executeWithDTO(dto);
            
            if (model.success) {
                // ✅ TỰ ĐỘNG PHÂN LUỒNG THEO ROLE
                if ("NHANVIEN".equals(model.accountType)) {
                    // 👨‍💼 NHÂN VIÊN - Phân quyền theo role
                    String role = model.role.toUpperCase();
                    
                    JOptionPane.showMessageDialog(this,
                        "Chào mừng " + model.role + ": " + model.fullName,
                        "Đăng nhập thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    this.dispose();
                    
                    if ("ADMIN".equals(role)) {
                        // 🔴 ADMIN → MainMenuGUI (Full quyền)
                        MainMenuGUI adminMenu = new MainMenuGUI();
                        adminMenu.setVisible(true);
                        
                    } else if ("MANAGER".equals(role)) {
                        // 🟠 MANAGER → MainMenuGUI (hoặc ManagerMenuGUI nếu có)
                        MainMenuGUI managerMenu = new MainMenuGUI();
                        managerMenu.setVisible(true);
                        
                    } else if ("STAFF".equals(role)) {
                        // 🟡 STAFF → MainMenuGUI (hoặc StaffMenuGUI nếu có)
                        MainMenuGUI staffMenu = new MainMenuGUI();
                        staffMenu.setVisible(true);
                        
                    } else {
                        // Role không hợp lệ
                        JOptionPane.showMessageDialog(this,
                            "Role không hợp lệ: " + model.role,
                            "Lỗi phân quyền",
                            JOptionPane.ERROR_MESSAGE);
                        new WelcomeGUI().setVisible(true);
                    }
                    
                } else {
                    // 👤 CUSTOMER → CustomerMainMenuGUI
                    JOptionPane.showMessageDialog(this,
                        "Chào mừng: " + model.fullName,
                        "Đăng nhập thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    this.dispose();
                    // ✅ Truyền UUID thật từ database
                    CustomerMainMenuGUI customerMenu = new CustomerMainMenuGUI(
                        model.userId,    // UUID thật từ database
                        model.username
                    );
                    customerMenu.setVisible(true);
                }
                
            } else {
                JOptionPane.showMessageDialog(this,
                    model.message,
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openRegister() {
        new RegisterGUI().setVisible(true);
    }
}