package adapters.Home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import adapters.payment.PaymentButton;
import Pay.IHomeView;

public class HomeView extends JFrame implements IHomeView {
    private HomeController controller;

    // Components
    private JPanel headerPanel;
    private JPanel menuPanel;
    private JPanel contentPanel;
    private JLabel userInfoLabel;

    // Menu Buttons - Guest
    private JButton btnViewProducts;
    private JButton btnViewCart;
    private JButton btnLogin;
    private JButton btnRegister;

    // Menu Buttons - Customer
    private JButton btnCheckout;
    private JButton btnLogout;

    // Menu Buttons - Admin
    private JButton btnManageProducts;
    private JButton btnManageUsers;
    private JButton btnManageOrders;
    private JButton btnSearch;

    public HomeView(HomeController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Fashion Store - Trang Chủ");
        setSize(1200, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // Header
        createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Menu (Left Sidebar)
        createMenu();
        add(menuPanel, BorderLayout.WEST);

        // Content Area
        createContent();
        add(contentPanel, BorderLayout.CENTER);
    }

    private void createHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(1200, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Logo and Title
        JLabel titleLabel = new JLabel("FASHION STORE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        // User Info
        userInfoLabel = new JLabel("Guest");
        userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userInfoLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userInfoLabel, BorderLayout.EAST);
    }

    private void createMenu() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(44, 62, 80));
        menuPanel.setPreferredSize(new Dimension(250, 720));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Menu Title
        JLabel menuTitle = new JLabel("MENU");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 20));
        menuTitle.setForeground(Color.WHITE);
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(menuTitle);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Guest Buttons (Always visible)
        btnViewProducts = createMenuButton("Xem Sản Phẩm", new Color(52, 152, 219));
        btnViewProducts.addActionListener(e -> controller.handleViewProducts());

        btnViewCart = createMenuButton("Giỏ Hàng", new Color(46, 204, 113));
        btnViewCart.addActionListener(e -> controller.handleViewCart());

        btnLogin = createMenuButton("Đăng Nhập", new Color(155, 89, 182));
        btnLogin.addActionListener(e -> controller.handleLogin());

        btnRegister = createMenuButton("Đăng Ký", new Color(52, 73, 94));
        btnRegister.addActionListener(e -> controller.handleRegister());

        menuPanel.add(btnViewProducts);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnViewCart);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(btnLogin);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnRegister);

//        btnCheckout = createMenuButton("Thanh Toán", new Color(230, 126, 34));
//        btnCheckout.addActionListener(e -> {
//            PaymentButton paymentButton = new PaymentButton(getCartItemsMock());
//            paymentButton.doClick();
//        });  Cái này sau khi có giỏ hàng thì sửa lại sau. Còn hiện tại tao để nó vầy nha Khoa
        btnCheckout = createMenuButton("Thanh Toán", new Color(230, 126, 34));
        btnCheckout.addActionListener(e -> new PaymentButton(null).doClick());

        btnLogout = createMenuButton("Đăng Xuất", new Color(231, 76, 60));
        btnLogout.addActionListener(e -> controller.handleLogout());
        btnLogout.setVisible(false);

        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnCheckout);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnLogout);

        // Admin Buttons (Hidden by default)
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel adminTitle = new JLabel("ADMIN");
        adminTitle.setFont(new Font("Arial", Font.BOLD, 18));
        adminTitle.setForeground(new Color(231, 76, 60));
        adminTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminTitle.setVisible(false);
        menuPanel.add(adminTitle);

        btnSearch = createMenuButton("Tìm Kiếm", new Color(52, 152, 219));
        btnSearch.addActionListener(e -> controller.handleSearch());
        btnSearch.setVisible(false);

        btnManageProducts = createMenuButton("Quản Lý SP", new Color(231, 76, 60));
        btnManageProducts.addActionListener(e -> controller.handleManageProducts());
        btnManageProducts.setVisible(false);

        btnManageUsers = createMenuButton("Quản Lý User", new Color(231, 76, 60));
        btnManageUsers.addActionListener(e -> controller.handleManageUsers());
        btnManageUsers.setVisible(false);

        btnManageOrders = createMenuButton("Quản Lý ĐH", new Color(231, 76, 60));
        btnManageOrders.addActionListener(e -> controller.handleManageOrders());
        btnManageOrders.setVisible(false);

        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnSearch);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnManageProducts);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnManageUsers);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnManageOrders);
    }

    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(230, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private void createContent() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome Message
        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h1>Chào mừng đến Fashion Store!</h1>" +
                "<p>Hệ thống quản lý bán hàng trực tuyến</p>" +
                "</div></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        contentPanel.add(welcomeLabel, BorderLayout.CENTER);
    }

    @Override
    public void updateUserInterface(User user) {
        // Update user info label
        String userText = "";
        if (user.isGuest()) {
            userText += "Guest (Khách)";
        } else {
            userText += user.getFullName() + " (" + user.getRole() + ")";
        }
        userInfoLabel.setText(userText);

        // Show/Hide buttons based on user role
        boolean isLoggedIn = !user.isGuest();
        boolean isAdmin = user.isAdmin();

        // Guest buttons
        btnLogin.setVisible(!isLoggedIn);
        btnRegister.setVisible(!isLoggedIn);

        // Customer buttons
        btnCheckout.setVisible(isLoggedIn);
        btnLogout.setVisible(isLoggedIn);

        // Admin buttons
        btnSearch.setVisible(isAdmin);
        btnManageProducts.setVisible(isAdmin);
        btnManageUsers.setVisible(isAdmin);
        btnManageOrders.setVisible(isAdmin);

        menuPanel.revalidate();
        menuPanel.repaint();
    }

    @Override
    public void showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Đăng Nhập", true);
        loginDialog.setSize(400, 300);
        loginDialog.setLocationRelativeTo(this);
        loginDialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        JPasswordField passwordField = new JPasswordField();

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton loginBtn = new JButton("Đăng Nhập");
        JButton cancelBtn = new JButton("Hủy");

        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            controller.performLogin(email, password);
            loginDialog.dispose();
        });

        cancelBtn.addActionListener(e -> loginDialog.dispose());

        buttonPanel.add(loginBtn);
        buttonPanel.add(cancelBtn);

        loginDialog.add(formPanel, BorderLayout.CENTER);
        loginDialog.add(buttonPanel, BorderLayout.SOUTH);
        loginDialog.setVisible(true);
    }

    @Override
    public void showRegisterDialog() {
        JDialog registerDialog = new JDialog(this, "Đăng Ký Tài Khoản", true);
        registerDialog.setSize(450, 450);
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        JPasswordField passwordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Xác nhận MK:");
        JPasswordField confirmPasswordField = new JPasswordField();
        JLabel fullNameLabel = new JLabel("Họ tên:");
        JTextField fullNameField = new JTextField();
        JLabel phoneLabel = new JLabel("Số điện thoại:");
        JTextField phoneField = new JTextField();
        JLabel addressLabel = new JLabel("Địa chỉ:");
        JTextField addressField = new JTextField();

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(confirmPasswordLabel);
        formPanel.add(confirmPasswordField);
        formPanel.add(fullNameLabel);
        formPanel.add(fullNameField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(addressLabel);
        formPanel.add(addressField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton registerBtn = new JButton("Đăng Ký");
        JButton cancelBtn = new JButton("Hủy");

        registerBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String fullName = fullNameField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            controller.performRegister(email, password, fullName, phone, address);
            registerDialog.dispose();
        });

        cancelBtn.addActionListener(e -> registerDialog.dispose());

        buttonPanel.add(registerBtn);
        buttonPanel.add(cancelBtn);

        registerDialog.add(formPanel, BorderLayout.CENTER);
        registerDialog.add(buttonPanel, BorderLayout.SOUTH);
        registerDialog.setVisible(true);
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}