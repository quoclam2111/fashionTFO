package frameworks.desktop;

import javax.swing.*;
import java.awt.*;

public class CustomerMainMenuGUI extends JFrame {
    private String currentUserId;
    private String currentUsername;
    
    public CustomerMainMenuGUI(String userId, String username) {
        this.currentUserId = userId;
        this.currentUsername = username;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Fashion Store - Trang Khách Hàng");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // ========== HEADER ==========
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(239, 83, 80)); // Red color
        headerPanel.setPreferredSize(new Dimension(900, 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        
        // Left side - Store name
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("🛍️ FASHION STORE");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        leftHeader.add(lblTitle);
        
        // Right side - User info
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHeader.setOpaque(false);
        JLabel lblWelcome = new JLabel("Xin chào, " + currentUsername + " 👤");
        lblWelcome.setFont(new Font("Arial", Font.PLAIN, 16));
        lblWelcome.setForeground(Color.WHITE);
        rightHeader.add(lblWelcome);
        
        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        
        // ========== MAIN PANEL ==========
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(250, 250, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(createMenuButton("🛒 Mua Sắm", 
            "Khám phá sản phẩm mới",
            new Color(244, 67, 54), 
            e -> openShopping()), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(createMenuButton("📦 Đơn Hàng Của Tôi", 
            "Theo dõi đơn hàng",
            new Color(255, 152, 0), 
            e -> openMyOrders()), gbc);
        
        gbc.gridx = 2;
        mainPanel.add(createMenuButton("🛍️ Giỏ Hàng", 
            "Xem giỏ hàng",
            new Color(156, 39, 176), 
            e -> openCart()), gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(createMenuButton("👤 Thông Tin Cá Nhân", 
            "Quản lý tài khoản",
            new Color(33, 150, 243), 
            e -> openProfile()), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(createMenuButton("❤️ Yêu Thích", 
            "Sản phẩm đã lưu",
            new Color(233, 30, 99), 
            e -> openWishlist()), gbc);
        
        gbc.gridx = 2;
        mainPanel.add(createMenuButton("🚪 Đăng Xuất", 
            "Thoát tài khoản",
            new Color(96, 125, 139), 
            e -> logout()), gbc);
        
        // ========== FOOTER ==========
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setPreferredSize(new Dimension(900, 50));
        
        JLabel lblFooter = new JLabel("© 2025 Fashion Store - Mua sắm thời trang trực tuyến");
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 13));
        lblFooter.setForeground(new Color(100, 100, 100));
        footerPanel.add(lblFooter);
        
        // ========== ADD TO FRAME ==========
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JButton createMenuButton(String title, String subtitle, Color bgColor, 
                                     java.awt.event.ActionListener action) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(10, 10));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 160));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Title
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        
        // Subtitle
        JLabel lblSubtitle = new JLabel(subtitle, SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(255, 255, 255, 200));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(lblTitle);
        textPanel.add(lblSubtitle);
        
        button.add(textPanel, BorderLayout.CENTER);
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        button.addActionListener(action);
        return button;
    }
    
    private void openShopping() {
        JOptionPane.showMessageDialog(this, 
            "Chức năng Mua Sắm đang được phát triển!\n" +
            "Sẽ hiển thị danh sách sản phẩm có sẵn.", 
            "Thông Báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openMyOrders() {
        JOptionPane.showMessageDialog(this, 
            "Chức năng Đơn Hàng Của Tôi đang được phát triển!\n" +
            "Sẽ hiển thị lịch sử và trạng thái đơn hàng.", 
            "Thông Báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openCart() {
        JOptionPane.showMessageDialog(this, 
            "Chức năng Giỏ Hàng đang được phát triển!\n" +
            "Sẽ hiển thị các sản phẩm trong giỏ.", 
            "Thông Báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openProfile() {
        this.setVisible(false);
        
        CustomerProfileGUI profileGUI = new CustomerProfileGUI(currentUserId, currentUsername);
        profileGUI.setVisible(true);
        
        profileGUI.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                CustomerMainMenuGUI.this.setVisible(true);
            }
        });
    }
    
    private void openWishlist() {
        JOptionPane.showMessageDialog(this, 
            "Chức năng Yêu Thích đang được phát triển!\n" +
            "Sẽ hiển thị các sản phẩm đã lưu.", 
            "Thông Báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn đăng xuất?", 
            "Xác Nhận Đăng Xuất", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            // Quay về màn hình login
            new WelcomeGUI().setVisible(true);
        }
    }
}