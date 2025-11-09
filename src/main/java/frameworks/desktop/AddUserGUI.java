package frameworks.desktop;

import javax.swing.*;

import adapters.add.AddUserController;
import adapters.add.AddUserInputDTO;
import adapters.add.AddUserPresenter;
import adapters.add.AddUserViewModel;

import java.awt.*;

import quanlynguoidung.them.AddUserInputData;
import quanlynguoidung.them.AddUserOutputBoundary;
import quanlynguoidung.them.AddUserUseCase;
import repository.jdbc.UserRepoImpl;

public class AddUserGUI extends JFrame {
    private JTextField txtUsername, txtEmail, txtFullName, txtPhone, txtAddress;
    private JPasswordField txtPassword;
    private JButton btnAdd;

    public AddUserGUI() {
        setTitle("Thêm người dùng");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        txtUsername = new JTextField(20);
        JLabel lblPassword = new JLabel("Mật khẩu:");
        txtPassword = new JPasswordField(20);
        JLabel lblFullName = new JLabel("Họ tên:");
        txtFullName = new JTextField(20);
        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField(20);
        JLabel lblPhone = new JLabel("Số điện thoại:");
        txtPhone = new JTextField(20);
        JLabel lblAddress = new JLabel("Địa chỉ:");
        txtAddress = new JTextField(20);


        addFormRow(formPanel, gbc, lblUsername, txtUsername);
        addFormRow(formPanel, gbc, lblPassword, txtPassword);
        addFormRow(formPanel, gbc, lblFullName, txtFullName);
        addFormRow(formPanel, gbc, lblEmail, txtEmail);
        addFormRow(formPanel, gbc, lblPhone, txtPhone);
        addFormRow(formPanel, gbc, lblAddress, txtAddress);

        btnAdd = new JButton("➕ Thêm người dùng");
        btnAdd.setBackground(new Color(72, 163, 255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        btnAdd.addActionListener(e -> {
        
            AddUserInputDTO dto = new AddUserInputDTO();
            dto.username = txtUsername.getText();
            dto.password = new String(txtPassword.getPassword());
            dto.fullName = txtFullName.getText();
            dto.email = txtEmail.getText();
            dto.phone = txtPhone.getText();
            dto.address = txtAddress.getText();
            
            AddUserViewModel model = new AddUserViewModel();
            AddUserPresenter presenter = new AddUserPresenter(model);
            
            UserRepoImpl repo = new UserRepoImpl();
            AddUserUseCase uc = new AddUserUseCase(repo, presenter);

            
            AddUserController controller = new AddUserController(uc);

            try {
                controller.execute(dto);
                if(model.success) {
                    // Thêm thành công → hiển thị thông báo
                	JOptionPane.showMessageDialog(this, 
                            model.message + "\nUser ID: " + model.userId +
                            "\nThời gian: " + model.timestamp,
                            "Thành công", 
                            JOptionPane.INFORMATION_MESSAGE);

                    // Sau khi người dùng bấm OK → xóa form
                    txtUsername.setText(""); 
                    txtPassword.setText(""); 
                    txtFullName.setText("");
                    txtEmail.setText(""); 
                    txtPhone.setText(""); 
                    txtAddress.setText("");
                } else {
                    // Thêm thất bại → hiển thị lỗi, giữ nguyên dữ liệu
                    JOptionPane.showMessageDialog(this, model.message,
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                // Bắt lỗi không mong muốn → giữ nguyên dữ liệu
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
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
        new AddUserGUI();
    }
}
