package frameworks.desktop;

import javax.swing.*;
import java.awt.*;

/**
 * Màn hình chào mừng - Entry point của ứng dụng
 * Người dùng chọn: Đăng nhập hoặc Đăng ký
 */
public class WelcomeGUI extends JFrame {
    private JButton btnLogin, btnRegister;
    
    public WelcomeGUI() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Fashion Store - Chào Mừng");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // ========== HEADER ==========
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(239, 83, 80));
        headerPanel.setPreferredSize(new Dimension(500, 200));
        headerPanel.setLayout(new GridBagLayout());
        
        JLabel lblTitle = new JLabel("🛍️ FASHION STORE");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 42));
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblSubtitle = new JLabel("Mua sắm thời trang trực tuyến");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        lblSubtitle.setForeground(new Color(255, 255, 255, 200));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 5, 0);
        headerPanel.add(lblTitle, gbc);
        
        gbc.gridy = 1;
        headerPanel.add(lblSubtitle, gbc);
        
        // ========== BUTTON PANEL ==========
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));
        
        JLabel lblWelcome = new JLabel("Chào mừng bạn đến với Fashion Store!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(lblWelcome);
        
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Login Button
        btnLogin = new JButton("🔐 Đăng Nhập");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 18));
        btnLogin.setBackground(new Color(239, 83, 80));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setMaximumSize(new Dimension(350, 55));
        btnLogin.setPreferredSize(new Dimension(350, 55));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> openLoginGUI());
        buttonPanel.add(btnLogin);
        
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Register Button
        btnRegister = new JButton("📝 Đăng Ký Tài Khoản");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 18));
        btnRegister.setBackground(Color.WHITE);
        btnRegister.setForeground(new Color(239, 83, 80));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createLineBorder(new Color(239, 83, 80), 2));
        btnRegister.setMaximumSize(new Dimension(350, 55));
        btnRegister.setPreferredSize(new Dimension(350, 55));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> openRegisterGUI());
        buttonPanel.add(btnRegister);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    /**
     * Mở màn hình đăng nhập (tự động phân luồng)
     */
    private void openLoginGUI() {
        this.dispose();
        new LoginGUI().setVisible(true);
    }
    
    /**
     * Mở màn hình đăng ký
     */
    private void openRegisterGUI() {
        new RegisterGUI().setVisible(true);
    }
    
    /**
     * Entry point của ứng dụng
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeGUI().setVisible(true));
    }
}