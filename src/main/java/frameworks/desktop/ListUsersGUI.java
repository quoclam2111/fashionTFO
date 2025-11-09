package frameworks.desktop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import adapters.list.*;
import quanlynguoidung.list.ListUsersUseCase;
import repository.jdbc.UserRepoImpl;
import java.awt.*;

public class ListUsersGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbStatus;
    private JComboBox<String> cmbSort;
    private JCheckBox chkAscending;
    private JButton btnLoad;

    public ListUsersGUI() {
        setTitle("Danh sách người dùng");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Trạng thái:"));
        cmbStatus = new JComboBox<>(new String[]{"all", "active", "inactive"});
        filterPanel.add(cmbStatus);

        filterPanel.add(new JLabel("Sắp xếp theo:"));
        cmbSort = new JComboBox<>(new String[]{"fullName", "email", "username"});
        filterPanel.add(cmbSort);

        chkAscending = new JCheckBox("Tăng dần", true);
        filterPanel.add(chkAscending);

        btnLoad = new JButton("🔄 Tải danh sách");
        btnLoad.setBackground(new Color(72, 163, 255));
        btnLoad.setForeground(Color.WHITE);
        btnLoad.setFocusPainted(false);
        filterPanel.add(btnLoad);

        // Table
        String[] columns = {"ID", "Username", "Họ tên", "Email", "SĐT", "Địa chỉ", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);

        // Event
        btnLoad.addActionListener(e -> loadUsers());

        setVisible(true);
        loadUsers(); // Load ngay khi mở
    }

    private void loadUsers() {
        ListUsersInputDTO dto = new ListUsersInputDTO();
        dto.statusFilter = (String) cmbStatus.getSelectedItem();
        dto.sortBy = (String) cmbSort.getSelectedItem();
        dto.ascending = chkAscending.isSelected();

        ListUsersViewModel model = new ListUsersViewModel();
        ListUsersPresenter presenter = new ListUsersPresenter(model);

        UserRepoImpl repo = new UserRepoImpl();
        ListUsersUseCase uc = new ListUsersUseCase(repo, presenter);

        ListUsersController controller = new ListUsersController(uc);

        try {
            controller.execute(dto);

            if (model.success) {
                // Xóa dữ liệu cũ
                tableModel.setRowCount(0);

                // Thêm dữ liệu mới
                if (model.users != null) {
                    for (var user : model.users) {
                        tableModel.addRow(new Object[]{
                            user.id,
                            user.username,
                            user.fullName,
                            user.email,
                            user.phone,
                            user.address,
                            user.status
                        });
                    }
                }

                setTitle("Danh sách người dùng (" + model.filteredCount + "/" + model.totalCount + ")");
            } else {
                JOptionPane.showMessageDialog(this, model.message, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ListUsersGUI();
    }
}