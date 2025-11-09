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
    private JButton btnGet;
    private JTextArea txtResult;

    public GetUserGUI() {
        setTitle("Lấy thông tin người dùng");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblUserId = new JLabel("User ID:");
        txtUserId = new JTextField(20);
        btnGet = new JButton("🔍 Lấy thông tin");
        btnGet.setBackground(new Color(72, 163, 255));
        btnGet.setForeground(Color.WHITE);
        btnGet.setFocusPainted(false);
        btnGet.setFont(new Font("Arial", Font.BOLD, 14));

        inputPanel.add(lblUserId);
        inputPanel.add(txtUserId);
        inputPanel.add(btnGet);

        txtResult = new JTextArea(10, 40);
        txtResult.setEditable(false);
        txtResult.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtResult);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        // Xử lý sự kiện khi click nút
        btnGet.addActionListener(e -> {
            String userId = txtUserId.getText().trim();
            if (userId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập User ID", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                GetUserInputDTO dto = new GetUserInputDTO();
                dto.searchBy = "id";        // ✅ chỉ định tìm theo ID
                dto.searchValue = userId;   // ✅ giá trị nhập từ UI

                GetUserViewModel viewModel = new GetUserViewModel();
                GetUserPresenter presenter = new GetUserPresenter(viewModel);

                UserRepoImpl repo = new UserRepoImpl();
                GetUserUseCase useCase = new GetUserUseCase(repo, presenter);

                GetUserController controller = new GetUserController(useCase);
                controller.execute(dto);

                if (viewModel.success) {
                    UserViewItem user = viewModel.user;
                    txtResult.setText(
                            "Tên đăng nhập: " + user.username + "\n" +
                            "Họ tên: " + user.fullName + "\n" +
                            "Email: " + user.email + "\n" +
                            "SĐT: " + user.phone + "\n" +
                            "Địa chỉ: " + user.address + "\n" +
                            "Thời gian: " + viewModel.timestamp
                    );
                } else {
                    JOptionPane.showMessageDialog(this, viewModel.message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        new GetUserGUI();
    }
}
