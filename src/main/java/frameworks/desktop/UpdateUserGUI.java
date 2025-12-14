//package frameworks.desktop;
//
//import javax.swing.*;
//import java.awt.*;
//import adapters.update.*;
//import frameworks.desktop.Subscriber;
//import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
//import quanlynguoidung.update.UpdateUserUseCase;
//import repository.jdbc.UserRepoImpl;
//
//public class UpdateUserGUI extends JFrame implements Subscriber {
//    private UpdateUserController controller;
//    private UpdateUserViewModel viewModel;
//    private UpdateUserPublisher publisher;
//
//    // Input fields
//    private JTextField txtUserId;
//    private JTextField txtFullName;
//    private JTextField txtEmail;
//    private JTextField txtPhone;
//    private JTextField txtAddress;
//    private JComboBox<String> cboStatus;
//    private JPasswordField txtPassword;
//    
//    // Buttons
//    private JButton btnUpdate;
//    private JButton btnClear;
//    
//    // Result display
//    private JTextArea txtResult;
//
//    public UpdateUserGUI() {
//        initComponents();
//        setupMVC();
//    }
//
//    private void initComponents() {
//        setTitle("Cập Nhật Người Dùng");
//        setSize(600, 550);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setLayout(new BorderLayout(10, 10));
//
//        // ========== PANEL INPUT ==========
//        JPanel panelInput = new JPanel(new GridBagLayout());
//        panelInput.setBorder(BorderFactory.createTitledBorder("Thông Tin Cập Nhật"));
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 5, 5, 5);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        // User ID (bắt buộc)
//        gbc.gridx = 0; gbc.gridy = 0;
//        panelInput.add(new JLabel("User ID: *"), gbc);
//        gbc.gridx = 1;
//        txtUserId = new JTextField(20);
//        panelInput.add(txtUserId, gbc);
//
//        // Full Name
//        gbc.gridx = 0; gbc.gridy = 1;
//        panelInput.add(new JLabel("Họ Tên:"), gbc);
//        gbc.gridx = 1;
//        txtFullName = new JTextField(20);
//        panelInput.add(txtFullName, gbc);
//
//        // Email
//        gbc.gridx = 0; gbc.gridy = 2;
//        panelInput.add(new JLabel("Email:"), gbc);
//        gbc.gridx = 1;
//        txtEmail = new JTextField(20);
//        panelInput.add(txtEmail, gbc);
//
//        // Phone
//        gbc.gridx = 0; gbc.gridy = 3;
//        panelInput.add(new JLabel("Số ĐT:"), gbc);
//        gbc.gridx = 1;
//        txtPhone = new JTextField(20);
//        panelInput.add(txtPhone, gbc);
//
//        // Address
//        gbc.gridx = 0; gbc.gridy = 4;
//        panelInput.add(new JLabel("Địa Chỉ:"), gbc);
//        gbc.gridx = 1;
//        txtAddress = new JTextField(20);
//        panelInput.add(txtAddress, gbc);
//
//        // Status
//        gbc.gridx = 0; gbc.gridy = 5;
//        panelInput.add(new JLabel("Trạng Thái:"), gbc);
//        gbc.gridx = 1;
//        cboStatus = new JComboBox<>(new String[]{"", "active", "inactive"});
//        panelInput.add(cboStatus, gbc);
//
//        // Password (optional)
//        gbc.gridx = 0; gbc.gridy = 6;
//        panelInput.add(new JLabel("Mật Khẩu Mới:"), gbc);
//        gbc.gridx = 1;
//        txtPassword = new JPasswordField(20);
//        panelInput.add(txtPassword, gbc);
//
//        add(panelInput, BorderLayout.NORTH);
//
//        // ========== PANEL BUTTONS ==========
//        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
//        
//        btnUpdate = new JButton("Cập Nhật");
//        btnUpdate.setPreferredSize(new Dimension(120, 35));
//        btnUpdate.addActionListener(e -> handleUpdate());
//        
//        btnClear = new JButton("Xóa Trắng");
//        btnClear.setPreferredSize(new Dimension(120, 35));
//        btnClear.addActionListener(e -> clearFields());
//        
//        panelButtons.add(btnUpdate);
//        panelButtons.add(btnClear);
//        add(panelButtons, BorderLayout.CENTER);
//
//        // ========== PANEL RESULT ==========
//        JPanel panelResult = new JPanel(new BorderLayout());
//        panelResult.setBorder(BorderFactory.createTitledBorder("Kết Quả"));
//        
//        txtResult = new JTextArea(8, 40);
//        txtResult.setEditable(false);
//        txtResult.setFont(new Font("Monospaced", Font.PLAIN, 12));
//        JScrollPane scrollPane = new JScrollPane(txtResult);
//        panelResult.add(scrollPane, BorderLayout.CENTER);
//        
//        add(panelResult, BorderLayout.SOUTH);
//    }
//
//    private void setupMVC() {
//        // Setup Model-View-Controller
//        viewModel = new UpdateUserViewModel();
//        publisher = new UpdateUserPublisher();
//        publisher.addSubscriber(this);
//
//        UserRepoImpl repository = new UserRepoImpl();
//        QuanLyNguoiDungOutputBoundary presenter = new UpdateUserPresenter(viewModel);
//        UpdateUserUseCase useCase = new UpdateUserUseCase(repository, presenter);
//        controller = new UpdateUserController(useCase);
//    }
//
//    private void handleUpdate() {
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
//            // Tạo DTO
//            UpdateUserInputDTO input = new UpdateUserInputDTO();
//            input.userId = userId;
//            
//            // Chỉ set các field không empty
//            String fullName = txtFullName.getText().trim();
//            if (!fullName.isEmpty()) input.fullName = fullName;
//            
//            String email = txtEmail.getText().trim();
//            if (!email.isEmpty()) input.email = email;
//            
//            String phone = txtPhone.getText().trim();
//            if (!phone.isEmpty()) input.phone = phone;
//            
//            String address = txtAddress.getText().trim();
//            if (!address.isEmpty()) input.address = address;
//            
//            String status = (String) cboStatus.getSelectedItem();
//            if (status != null && !status.isEmpty()) input.status = status;
//            
//            String password = new String(txtPassword.getPassword()).trim();
//            if (!password.isEmpty()) input.password = password;
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
//        txtFullName.setText("");
//        txtEmail.setText("");
//        txtPhone.setText("");
//        txtAddress.setText("");
//        cboStatus.setSelectedIndex(0);
//        txtPassword.setText("");
//        txtResult.setText("");
//    }
//
//    @Override
//    public void update() {
//        // Hiển thị kết quả từ ViewModel
//        StringBuilder result = new StringBuilder();
//        result.append("=== KẾT QUẢ CẬP NHẬT ===\n");
//        result.append("Trạng thái: ").append(viewModel.success ? "✓ Thành công" : "✗ Thất bại").append("\n");
//        result.append("Thông báo: ").append(viewModel.message).append("\n");
//        result.append("Thời gian: ").append(viewModel.timestamp).append("\n");
//
//        if (viewModel.success && viewModel.updatedUser != null) {
//            result.append("\n--- THÔNG TIN ĐÃ CẬP NHẬT ---\n");
//            result.append("ID: ").append(viewModel.updatedUser.id).append("\n");
//            result.append("Username: ").append(viewModel.updatedUser.username).append("\n");
//            result.append("Họ Tên: ").append(viewModel.updatedUser.fullName).append("\n");
//            result.append("Email: ").append(viewModel.updatedUser.email).append("\n");
//            result.append("Số ĐT: ").append(viewModel.updatedUser.phone).append("\n");
//            result.append("Địa Chỉ: ").append(viewModel.updatedUser.address).append("\n");
//            result.append("Trạng Thái: ").append(viewModel.updatedUser.status).append("\n");
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
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            UpdateUserGUI gui = new UpdateUserGUI();
//            gui.setVisible(true);
//        });
//    }
//}


package frameworks.desktop;

import javax.swing.*;
import java.awt.*;

import adapters.quanlynguoidung.update.UpdateUserController;
import adapters.quanlynguoidung.update.UpdateUserInputDTO;
import adapters.quanlynguoidung.update.UpdateUserPresenter;
import adapters.quanlynguoidung.update.UpdateUserPublisher;
import adapters.quanlynguoidung.update.UpdateUserViewModel;

import frameworks.desktop.Subscriber;
import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.update.UpdateUserUseCase;
import repository.jdbc.UserRepoImpl;

public class UpdateUserGUI extends JFrame implements Subscriber {
    private UpdateUserController controller;
    private UpdateUserViewModel viewModel;
    private UpdateUserPublisher publisher;

    // Input fields
    private JTextField txtUserId;
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JComboBox<String> cboStatus;
    private JPasswordField txtPassword;
    
    // Buttons
    private JButton btnUpdate;
    private JButton btnClear;
    
    // Result display
    private JTextArea txtResult;

    public UpdateUserGUI() {
        initComponents();
        setupMVC();
    }
    
    // ✅ Method để set User ID từ bên ngoài
    public void setUserId(String userId) {
        txtUserId.setText(userId);
    }

    private void initComponents() {
        setTitle("Cập Nhật Người Dùng");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ========== PANEL INPUT ==========
        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setBorder(BorderFactory.createTitledBorder("Thông Tin Cập Nhật"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // User ID (bắt buộc)
        gbc.gridx = 0; gbc.gridy = 0;
        panelInput.add(new JLabel("User ID: *"), gbc);
        gbc.gridx = 1;
        txtUserId = new JTextField(20);
        panelInput.add(txtUserId, gbc);

        // Full Name
        gbc.gridx = 0; gbc.gridy = 1;
        panelInput.add(new JLabel("Họ Tên:"), gbc);
        gbc.gridx = 1;
        txtFullName = new JTextField(20);
        panelInput.add(txtFullName, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        panelInput.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        panelInput.add(txtEmail, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 3;
        panelInput.add(new JLabel("Số ĐT:"), gbc);
        gbc.gridx = 1;
        txtPhone = new JTextField(20);
        panelInput.add(txtPhone, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 4;
        panelInput.add(new JLabel("Địa Chỉ:"), gbc);
        gbc.gridx = 1;
        txtAddress = new JTextField(20);
        panelInput.add(txtAddress, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 5;
        panelInput.add(new JLabel("Trạng Thái:"), gbc);
        gbc.gridx = 1;
        cboStatus = new JComboBox<>(new String[]{"", "active", "inactive"});
        panelInput.add(cboStatus, gbc);

        // Password (optional)
        gbc.gridx = 0; gbc.gridy = 6;
        panelInput.add(new JLabel("Mật Khẩu Mới:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        panelInput.add(txtPassword, gbc);

        add(panelInput, BorderLayout.NORTH);

        // ========== PANEL BUTTONS ==========
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        
        btnUpdate = new JButton("Cập Nhật");
        btnUpdate.setPreferredSize(new Dimension(180, 45));
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 14));
        btnUpdate.setBackground(new Color(40, 167, 69));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.addActionListener(e -> handleUpdate());
        
        btnClear = new JButton("Xóa Trắng");
        btnClear.setPreferredSize(new Dimension(180, 45));
        btnClear.setFont(new Font("Arial", Font.BOLD, 14));
        btnClear.setBackground(new Color(108, 117, 125));
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(e -> clearFields());
        
        panelButtons.add(btnUpdate);
        panelButtons.add(btnClear);
        add(panelButtons, BorderLayout.CENTER);

        // ========== PANEL RESULT ==========
        JPanel panelResult = new JPanel(new BorderLayout());
        panelResult.setBorder(BorderFactory.createTitledBorder("Kết Quả"));
        
        txtResult = new JTextArea(8, 40);
        txtResult.setEditable(false);
        txtResult.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtResult);
        panelResult.add(scrollPane, BorderLayout.CENTER);
        
        add(panelResult, BorderLayout.SOUTH);
    }

    private void setupMVC() {
        // Setup Model-View-Controller
        viewModel = new UpdateUserViewModel();
        publisher = new UpdateUserPublisher();
        publisher.addSubscriber(this);

        UserRepoImpl repository = new UserRepoImpl();
        QuanLyNguoiDungOutputBoundary presenter = new UpdateUserPresenter(viewModel);
        UpdateUserUseCase useCase = new UpdateUserUseCase(repository, presenter);
        controller = new UpdateUserController(useCase);
    }

    private void handleUpdate() {
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

            // Tạo DTO
            UpdateUserInputDTO input = new UpdateUserInputDTO();
            input.userId = userId;
            
            // Chỉ set các field không empty
            String fullName = txtFullName.getText().trim();
            if (!fullName.isEmpty()) input.fullName = fullName;
            
            String email = txtEmail.getText().trim();
            if (!email.isEmpty()) input.email = email;
            
            String phone = txtPhone.getText().trim();
            if (!phone.isEmpty()) input.phone = phone;
            
            String address = txtAddress.getText().trim();
            if (!address.isEmpty()) input.address = address;
            
            String status = (String) cboStatus.getSelectedItem();
            if (status != null && !status.isEmpty()) input.status = status;
            
            String password = new String(txtPassword.getPassword()).trim();
            if (!password.isEmpty()) input.password = password;

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
        txtFullName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        cboStatus.setSelectedIndex(0);
        txtPassword.setText("");
        txtResult.setText("");
    }

    @Override
    public void update() {
        // Hiển thị kết quả từ ViewModel
        StringBuilder result = new StringBuilder();
        result.append("=== KẾT QUẢ CẬP NHẬT ===\n");
        result.append("Trạng thái: ").append(viewModel.success ? "✓ Thành công" : "✗ Thất bại").append("\n");
        result.append("Thông báo: ").append(viewModel.message).append("\n");
        result.append("Thời gian: ").append(viewModel.timestamp).append("\n");

        if (viewModel.success && viewModel.updatedUser != null) {
            result.append("\n--- THÔNG TIN ĐÃ CẬP NHẬT ---\n");
            result.append("ID: ").append(viewModel.updatedUser.id).append("\n");
            result.append("Username: ").append(viewModel.updatedUser.username).append("\n");
            result.append("Họ Tên: ").append(viewModel.updatedUser.fullName).append("\n");
            result.append("Email: ").append(viewModel.updatedUser.email).append("\n");
            result.append("Số ĐT: ").append(viewModel.updatedUser.phone).append("\n");
            result.append("Địa Chỉ: ").append(viewModel.updatedUser.address).append("\n");
            result.append("Trạng Thái: ").append(viewModel.updatedUser.status).append("\n");
        }

        txtResult.setText(result.toString());
        
        // Hiển thị dialog thông báo
        if (viewModel.success) {
            JOptionPane.showMessageDialog(this, 
                viewModel.message, 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UpdateUserGUI gui = new UpdateUserGUI();
            gui.setVisible(true);
        });
    }
}