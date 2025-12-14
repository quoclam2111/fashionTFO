package frameworks.desktop;

import javax.swing.*;
import adapters.quanlynguoidung.dangky.*;
import quanlynguoidung.dangky.RegisterUseCase;
import repository.jdbc.UserRepoImpl;
import java.awt.*;

public class RegisterGUI extends JFrame {
    private JTextField txtUsername, txtEmail, txtFullName, txtPhone, txtAddress;
    private JPasswordField txtPassword;
    private JButton btnRegister;

    public RegisterGUI() {
        setTitle("Đăng ký tài khoản");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout(10, 10));

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(72, 163, 255));
        titlePanel.add(lblTitle);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblUsername = new JLabel("Tên đăng nhập: *");
        txtUsername = new JTextField(20);
        JLabel lblPassword = new JLabel("Mật khẩu: *");
        txtPassword = new JPasswordField(20);
        JLabel lblFullName = new JLabel("Họ tên: *");
        txtFullName = new JTextField(20);
        JLabel lblEmail = new JLabel("Email: *");
        txtEmail = new JTextField(20);
        JLabel lblPhone = new JLabel("Số điện thoại: *");
        txtPhone = new JTextField(20);
        JLabel lblAddress = new JLabel("Địa chỉ:");
        txtAddress = new JTextField(20);

        addFormRow(formPanel, gbc, lblUsername, txtUsername);
        addFormRow(formPanel, gbc, lblPassword, txtPassword);
        addFormRow(formPanel, gbc, lblFullName, txtFullName);
        addFormRow(formPanel, gbc, lblEmail, txtEmail);
        addFormRow(formPanel, gbc, lblPhone, txtPhone);
        addFormRow(formPanel, gbc, lblAddress, txtAddress);

        btnRegister = new JButton("📝 Đăng ký");
        btnRegister.setBackground(new Color(72, 163, 255));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setPreferredSize(new Dimension(150, 35));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnRegister);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        btnRegister.addActionListener(e -> handleRegister());

        setVisible(true);
    }

    private void handleRegister() {
        RegisterInputDTO dto = new RegisterInputDTO();
        dto.username = txtUsername.getText();
        dto.password = new String(txtPassword.getPassword());
        dto.fullName = txtFullName.getText();
        dto.email = txtEmail.getText();
        dto.phone = txtPhone.getText();
        dto.address = txtAddress.getText();
        
        RegisterViewModel model = new RegisterViewModel();
        RegisterPresenter presenter = new RegisterPresenter(model);
        
        UserRepoImpl repo = new UserRepoImpl();
        RegisterUseCase uc = new RegisterUseCase(repo, presenter);
        
        RegisterController controller = new RegisterController(uc);

        try {
            controller.executeWithDTO(dto);
            if(model.success) {
                // Đăng ký thành công
                JOptionPane.showMessageDialog(this, 
                    model.message + 
                    "\nTên đăng nhập: " + model.username +
                    "\nUser ID: " + model.userId +
                    "\nThời gian: " + model.timestamp,
                    "Đăng ký thành công", 
                    JOptionPane.INFORMATION_MESSAGE);

                // Xóa form và đóng
                clearForm();
                dispose();
                
            } else {
                // Đăng ký thất bại
                JOptionPane.showMessageDialog(this, model.message,
                    "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtUsername.setText(""); 
        txtPassword.setText(""); 
        txtFullName.setText("");
        txtEmail.setText(""); 
        txtPhone.setText(""); 
        txtAddress.setText("");
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, JLabel label, JComponent field) {
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
        gbc.gridy++;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterGUI());
    }
}