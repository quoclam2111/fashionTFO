package frameworks.desktop;

import adapters.get.GetUserController;
import adapters.get.GetUserInputDTO;
import adapters.get.GetUserPresenter;
import adapters.get.GetUserViewModel;
import quanlynguoidung.get.GetUserUseCase;
import repository.jdbc.UserRepoImpl;
import quanlynguoidung.get.UserViewItem;

import javax.swing.*;
import java.awt.*;

public class GetUserGUI extends JFrame {
    private JTextField txtUserId;
    private JTextArea txtResult;

    public GetUserGUI() {
        setTitle("Xem Thông Tin Người Dùng");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout(10, 10));

        // ========== PANEL HEADER (chỉ hiển thị User ID) ==========
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin"));
        
        JLabel lblUserId = new JLabel("User ID:");
        txtUserId = new JTextField(40);
        txtUserId.setEditable(false); // Không cho edit
        txtUserId.setBackground(new Color(233, 236, 239));

        inputPanel.add(lblUserId);
        inputPanel.add(txtUserId);

        // ========== PANEL RESULT ==========
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Chi Tiết"));
        
        txtResult = new JTextArea(18, 40);
        txtResult.setEditable(false);
        txtResult.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(txtResult);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(resultPanel, BorderLayout.CENTER);

        add(panel);
        
        setVisible(true);
    }
    
    // ✅ Method để set User ID từ bên ngoài
    public void setUserId(String userId) {
        txtUserId.setText(userId);
        handleGet(); // Tự động load thông tin
    }
    
    private void handleGet() {
        String userId = txtUserId.getText().trim();
        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập User ID", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            GetUserInputDTO dto = new GetUserInputDTO();
            dto.searchBy = "id";
            dto.searchValue = userId;

            GetUserViewModel viewModel = new GetUserViewModel();
            GetUserPresenter presenter = new GetUserPresenter(viewModel);

            UserRepoImpl repo = new UserRepoImpl();
            GetUserUseCase useCase = new GetUserUseCase(repo, presenter);

            GetUserController controller = new GetUserController(useCase);
            controller.executeWithDTO(dto);

            if (viewModel.success) {
                UserViewItem user = viewModel.user;
                StringBuilder result = new StringBuilder();
                result.append("=== THÔNG TIN NGƯỜI DÙNG ===\n\n");
                result.append("ID: ").append(user.id).append("\n");
                result.append("Tên đăng nhập: ").append(user.username).append("\n");
                result.append("Họ tên: ").append(user.fullName).append("\n");
                result.append("Email: ").append(user.email).append("\n");
                result.append("Số điện thoại: ").append(user.phone).append("\n");
                result.append("Địa chỉ: ").append(user.address).append("\n");
                result.append("Trạng thái: ").append(user.status).append("\n");
                result.append("\nThời gian: ").append(viewModel.timestamp);
                
                txtResult.setText(result.toString());
            } else {
                JOptionPane.showMessageDialog(this, viewModel.message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtResult.setText("");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GetUserGUI());
    }
}