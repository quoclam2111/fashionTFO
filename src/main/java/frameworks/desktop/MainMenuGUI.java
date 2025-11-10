package frameworks.desktop;

import javax.swing.*;
import java.awt.*;

public class MainMenuGUI extends JFrame {
    
    public MainMenuGUI() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Cửa Hàng Thời Trang");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // ========== HEADER ==========
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 58, 64));
        headerPanel.setPreferredSize(new Dimension(800, 80));
        
        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        // ========== MAIN PANEL ==========
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(248, 249, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(createMenuButton("👥 Quản Lý Người Dùng", 
            new Color(0, 123, 255), 
            e -> openUserManagement()), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(createMenuButton("📦 Quản Lý Sản Phẩm", 
            new Color(40, 167, 69), 
            e -> openProductManagement()), gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(createMenuButton("🛒 Quản Lý Đơn Hàng", 
            new Color(255, 193, 7), 
            e -> openOrderManagement()), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(createMenuButton("📊 Thống Kê & Báo Cáo", 
            new Color(23, 162, 184), 
            e -> openStatistics()), gbc);
        
        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(createMenuButton("⚙️ Cài Đặt Hệ Thống", 
            new Color(108, 117, 125), 
            e -> openSettings()), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(createMenuButton("🚪 Đăng Xuất", 
            new Color(220, 53, 69), 
            e -> logout()), gbc);
        
        // ========== FOOTER ==========
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(233, 236, 239));
        footerPanel.setPreferredSize(new Dimension(800, 40));
        
        JLabel lblFooter = new JLabel("© 2025 Fashion Store Management System");
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 12));
        lblFooter.setForeground(new Color(108, 117, 125));
        footerPanel.add(lblFooter);
        
        // ========== ADD TO FRAME ==========
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JButton createMenuButton(String text, Color bgColor, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(300, 120));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        button.addActionListener(action);
        return button;
    }
    
    private void openUserManagement() {
        UserManagementGUI userGUI = new UserManagementGUI();
        userGUI.setVisible(true);
    }
    
    private void openProductManagement() {
        JOptionPane.showMessageDialog(this, 
            "Chức năng Quản Lý Sản Phẩm đang được phát triển!", 
            "Thông Báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openOrderManagement() {
        JOptionPane.showMessageDialog(this, 
            "Chức năng Quản Lý Đơn Hàng đang được phát triển!", 
            "Thông Báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openStatistics() {
        JOptionPane.showMessageDialog(this, 
            "Chức năng Thống Kê & Báo Cáo đang được phát triển!", 
            "Thông Báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openSettings() {
        JOptionPane.showMessageDialog(this, 
            "Chức năng Cài Đặt Hệ Thống đang được phát triển!", 
            "Thông Báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn đăng xuất?", 
            "Xác Nhận Đăng Xuất", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuGUI mainMenu = new MainMenuGUI();
            mainMenu.setVisible(true);
        });
    }
}