package frameworks.desktop;

import javax.swing.*;
import java.awt.*;

public class CustomerProfileGUI extends JFrame {
    private String currentUserId;
    private String currentUsername;
    
    private JTextField txtFullName, txtEmail, txtPhone, txtAddress;
    private JPasswordField txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    private JButton btnUpdate, btnChangePassword, btnBack;
    
    public CustomerProfileGUI(String userId, String username) {
        this.currentUserId = userId;
        this.currentUsername = username;
        initComponents();
        loadUserProfile();
    }
    
    private void initComponents() {
        setTitle("Thông Tin Cá Nhân");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);
        
        // ========== HEADER ==========
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(700, 80));
        
        JLabel lblTitle = new JLabel("👤 THÔNG TIN CÁ NHÂN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        // ========== PROFILE INFO PANEL ==========
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
            "Thông Tin Tài Khoản",
            0, 0,
            new Font("Arial", Font.BOLD, 16),
            new Color(33, 150, 243)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel lblUsernameValue = new JLabel(currentUsername);
        lblUsernameValue.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsernameValue.setForeground(new Color(100, 100, 100));
        
        addProfileRow(profilePanel, gbc, lblUsername, lblUsernameValue);
        
        txtFullName = new JTextField(20);
        addProfileRow(profilePanel, gbc, new JLabel("Họ tên:"), txtFullName);
        
        txtEmail = new JTextField(20);
        addProfileRow(profilePanel, gbc, new JLabel("Email:"), txtEmail);
        
        txtPhone = new JTextField(20);
        addProfileRow(profilePanel, gbc, new JLabel("Số điện thoại:"), txtPhone);
        
        txtAddress = new JTextField(20);
        addProfileRow(profilePanel, gbc, new JLabel("Địa chỉ:"), txtAddress);
        
        btnUpdate = new JButton("💾 Cập Nhật Thông Tin");
        btnUpdate.setBackground(new Color(76, 175, 80));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 14));
        btnUpdate.setPreferredSize(new Dimension(200, 40));
        btnUpdate.addActionListener(e -> updateProfile());
        
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        profilePanel.add(btnUpdate, gbc);
        
        // ========== CHANGE PASSWORD PANEL ==========
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 152, 0), 2),
            "Đổi Mật Khẩu",
            0, 0,
            new Font("Arial", Font.BOLD, 16),
            new Color(255, 152, 0)
        ));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        txtCurrentPassword = new JPasswordField(20);
        addProfileRow(passwordPanel, gbc, new JLabel("Mật khẩu hiện tại:"), txtCurrentPassword);
        
        txtNewPassword = new JPasswordField(20);
        addProfileRow(passwordPanel, gbc, new JLabel("Mật khẩu mới:"), txtNewPassword);
        
        txtConfirmPassword = new JPasswordField(20);
        addProfileRow(passwordPanel, gbc, new JLabel("Xác nhận mật khẩu:"), txtConfirmPassword);
        
        btnChangePassword = new JButton("🔒 Đổi Mật Khẩu");
        btnChangePassword.setBackground(new Color(255, 152, 0));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setFocusPainted(false);
        btnChangePassword.setFont(new Font("Arial", Font.BOLD, 14));
        btnChangePassword.setPreferredSize(new Dimension(200, 40));
        btnChangePassword.addActionListener(e -> changePassword());
        
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        passwordPanel.add(btnChangePassword, gbc);
        
        // ========== BUTTON PANEL ==========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        btnBack = new JButton("⬅️ Quay Lại");
        btnBack.setBackground(new Color(96, 125, 139));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setPreferredSize(new Dimension(150, 40));
        btnBack.addActionListener(e -> dispose());
        
        buttonPanel.add(btnBack);
        
        // ========== CENTER PANEL ==========
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(profilePanel);
        centerPanel.add(passwordPanel);
        
        // ========== ADD TO FRAME ==========
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void addProfileRow(JPanel panel, GridBagConstraints gbc, JLabel label, JComponent field) {
        label.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panel.add(label, gbc);
        
        if (field instanceof JTextField || field instanceof JPasswordField) {
            field.setFont(new Font("Arial", Font.PLAIN, 14));
        }
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
        gbc.gridy++;
    }
    
    private void loadUserProfile() {
        // TODO: Load user data from repository
        // For now, use dummy data
        txtFullName.setText("Nguyễn Văn A");
        txtEmail.setText("nguyenvana@email.com");
        txtPhone.setText("0123456789");
        txtAddress.setText("123 Đường ABC, Quận 1, TP.HCM");
    }
    
    private void updateProfile() {
        String fullName = txtFullName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String address = txtAddress.getText();
        
        // TODO: Implement update profile use case
        JOptionPane.showMessageDialog(this,
            "Chức năng cập nhật thông tin đang được phát triển!\n" +
            "Họ tên: " + fullName + "\n" +
            "Email: " + email + "\n" +
            "Số điện thoại: " + phone + "\n" +
            "Địa chỉ: " + address,
            "Thông Báo",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void changePassword() {
        String currentPass = new String(txtCurrentPassword.getPassword());
        String newPass = new String(txtNewPassword.getPassword());
        String confirmPass = new String(txtConfirmPassword.getPassword());
        
        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng điền đầy đủ thông tin!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this,
                "Mật khẩu mới không khớp!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (newPass.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Mật khẩu mới phải có ít nhất 6 ký tự!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // TODO: Implement change password use case
        JOptionPane.showMessageDialog(this,
            "Chức năng đổi mật khẩu đang được phát triển!",
            "Thông Báo",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Clear password fields
        txtCurrentPassword.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
    }
}
