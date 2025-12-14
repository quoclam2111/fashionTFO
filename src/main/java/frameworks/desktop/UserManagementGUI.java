package frameworks.desktop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import adapters.quanlynguoidung.list.ListUsersController;
import adapters.quanlynguoidung.list.ListUsersInputDTO;
import adapters.quanlynguoidung.list.ListUsersPresenter;
import adapters.quanlynguoidung.list.ListUsersViewModel;
import quanlynguoidung.list.ListUsersUseCase;
import repository.jdbc.UserRepoImpl;
import java.awt.*;

public class UserManagementGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbStatus;
    private JComboBox<String> cmbSort;
    private JCheckBox chkAscending;
    private JButton btnRefresh;
    private JButton btnAdd;
    private JButton btnGet;
    private JButton btnUpdate;
    private JButton btnDelete;
    
    // ✅ Thêm các component tìm kiếm
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchBy;
    private JButton btnSearch;
    private JButton btnClearSearch;

    public UserManagementGUI() {
        initComponents();
        loadUsers();
    }

    private void initComponents() {
        setTitle("Quản Lý Người Dùng");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ========== MAIN PANEL ==========
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ========== HEADER PANEL ==========
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("📋 QUẢN LÝ NGƯỜI DÙNG");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(52, 58, 64));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        headerPanel.add(lblTitle, BorderLayout.WEST);

        // ========== FILTER PANEL (LEFT) ==========
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Bộ Lọc & Sắp Xếp"));
        
        filterPanel.add(new JLabel("Trạng thái:"));
        cmbStatus = new JComboBox<>(new String[]{"all", "active", "inactive"});
        filterPanel.add(cmbStatus);

        filterPanel.add(new JLabel("   Sắp xếp theo:"));
        cmbSort = new JComboBox<>(new String[]{"fullName", "email", "username"});
        filterPanel.add(cmbSort);

        chkAscending = new JCheckBox("Tăng dần", true);
        filterPanel.add(chkAscending);

        btnRefresh = new JButton("🔄 Làm Mới");
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(23, 162, 184));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setPreferredSize(new Dimension(120, 30));
        btnRefresh.addActionListener(e -> loadUsers());
        filterPanel.add(btnRefresh);

        // ========== SEARCH PANEL (RIGHT) ==========
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm Kiếm"));
        
        cmbSearchBy = new JComboBox<>(new String[]{"username", "email", "fullName", "phone"});
        cmbSearchBy.setPreferredSize(new Dimension(100, 25));
        searchPanel.add(cmbSearchBy);
        
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 25));
        // ✅ Enter để tìm kiếm
        txtSearch.addActionListener(e -> performSearch());
        searchPanel.add(txtSearch);
        
        btnSearch = new JButton("🔍");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 14));
        btnSearch.setBackground(new Color(40, 167, 69));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setPreferredSize(new Dimension(45, 28));
        btnSearch.setToolTipText("Tìm kiếm");
        btnSearch.addActionListener(e -> performSearch());
        searchPanel.add(btnSearch);
        
        // ✅ Nút Xóa Tìm Kiếm
        btnClearSearch = new JButton("✖");
        btnClearSearch.setFont(new Font("Arial", Font.BOLD, 12));
        btnClearSearch.setBackground(new Color(108, 117, 125));
        btnClearSearch.setForeground(Color.WHITE);
        btnClearSearch.setFocusPainted(false);
        btnClearSearch.setPreferredSize(new Dimension(45, 28));
        btnClearSearch.setToolTipText("Xóa bộ lọc");
        btnClearSearch.addActionListener(e -> clearSearch());
        searchPanel.add(btnClearSearch);

        // ========== COMBINED TOP PANEL ==========
        JPanel topFilterPanel = new JPanel(new BorderLayout(10, 0));
        topFilterPanel.add(filterPanel, BorderLayout.WEST);
        topFilterPanel.add(searchPanel, BorderLayout.EAST);

        // ========== TABLE ==========
        String[] columns = {"ID", "Username", "Họ Tên", "Email", "Số ĐT", "Địa Chỉ", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(52, 58, 64));
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218)));

        // ========== ACTION BUTTONS PANEL ==========
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Chức Năng"));
        
        btnAdd = createActionButton("➕ Thêm Mới", new Color(40, 167, 69));
        btnAdd.addActionListener(e -> openAddUser());
        
        btnGet = createActionButton("🔍 Xem Chi Tiết", new Color(0, 123, 255));
        btnGet.addActionListener(e -> openGetUser());
        
        btnUpdate = createActionButton("✏️ Cập Nhật", new Color(255, 193, 7));
        btnUpdate.addActionListener(e -> openUpdateUser());
        
        btnDelete = createActionButton("🗑️ Xóa", new Color(220, 53, 69));
        btnDelete.addActionListener(e -> handleDelete());
        
        actionPanel.add(btnAdd);
        actionPanel.add(btnGet);
        actionPanel.add(btnUpdate);
        actionPanel.add(btnDelete);

        // ========== LAYOUT ==========
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(topFilterPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 45));
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
        
        return button;
    }

    // ✅ Method xóa tìm kiếm và hiển thị lại tất cả
    private void clearSearch() {
        txtSearch.setText("");
        loadUsers();
    }

    // ✅ Method tìm kiếm mới
    private void performSearch() {
        String searchValue = txtSearch.getText().trim();
        
        if (searchValue.isEmpty()) {
            // Nếu ô tìm kiếm rỗng → load tất cả
            loadUsers();
            return;
        }
        
        String searchBy = (String) cmbSearchBy.getSelectedItem();
        
        // ✅ XÓA DỮ LIỆU CŨ VÀ LỌC TRỰC TIẾP TRÊN tableModel
        int columnIndex;
        switch (searchBy) {
            case "username": columnIndex = 1; break;
            case "fullName": columnIndex = 2; break;
            case "email": columnIndex = 3; break;
            case "phone": columnIndex = 4; break;
            default: columnIndex = 1;
        }
        
        // Lưu tất cả dữ liệu trước khi lọc
        java.util.List<Object[]> allData = new java.util.ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            allData.add(new Object[]{
                tableModel.getValueAt(i, 0),
                tableModel.getValueAt(i, 1),
                tableModel.getValueAt(i, 2),
                tableModel.getValueAt(i, 3),
                tableModel.getValueAt(i, 4),
                tableModel.getValueAt(i, 5),
                tableModel.getValueAt(i, 6)
            });
        }
        
        // Xóa bảng và thêm lại dữ liệu đã lọc
        tableModel.setRowCount(0);
        int count = 0;
        
        for (Object[] row : allData) {
            String cellValue = row[columnIndex].toString().toLowerCase();
            if (cellValue.contains(searchValue.toLowerCase())) {
                tableModel.addRow(row);
                count++;
            }
        }
        
        setTitle("Quản Lý Người Dùng (Tìm thấy: " + count + " kết quả)");
        
        if (count == 0) {
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy kết quả nào!", 
                "Thông Báo", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadUsers() {
        // ✅ Reset ô tìm kiếm khi load lại
        txtSearch.setText("");
        
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
            controller.executeWithDTO(dto);

            if (model.success) {
                tableModel.setRowCount(0);

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

                setTitle("Quản Lý Người Dùng (" + model.filteredCount + "/" + model.totalCount + " người dùng)");
            } else {
                JOptionPane.showMessageDialog(this, model.message, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddUser() {
        RegisterGUI addGUI = new RegisterGUI();
        addGUI.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadUsers();
            }
        });
    }

    private void openGetUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn một người dùng từ danh sách!", 
                "Thông Báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        GetUserGUI getUserGUI = new GetUserGUI();
        getUserGUI.setUserId(userId);
    }
    
    private void openUpdateUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn một người dùng từ danh sách để cập nhật!", 
                "Thông Báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        UpdateUserGUI updateGUI = new UpdateUserGUI();
        updateGUI.setUserId(userId);
        updateGUI.setVisible(true);
        updateGUI.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadUsers();
            }
        });
    }

    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn một người dùng từ danh sách để xóa!", 
                "Thông Báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        String fullName = (String) tableModel.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa người dùng?\n\n" +
            "ID: " + userId + "\n" +
            "Username: " + username + "\n" +
            "Họ tên: " + fullName + "\n\n" +
            "Hành động này không thể hoàn tác!", 
            "Xác Nhận Xóa", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            adapters.quanlynguoidung.delete.DeleteUserInputDTO input = new adapters.quanlynguoidung.delete.DeleteUserInputDTO();
            input.userId = userId;
            
            adapters.quanlynguoidung.delete.DeleteUserViewModel viewModel = new adapters.quanlynguoidung.delete.DeleteUserViewModel();
            adapters.quanlynguoidung.delete.DeleteUserPresenter presenter = new adapters.quanlynguoidung.delete.DeleteUserPresenter(viewModel);
            
            UserRepoImpl repository = new UserRepoImpl();
            quanlynguoidung.delete.DeleteUserUseCase useCase = 
                new quanlynguoidung.delete.DeleteUserUseCase(repository, presenter);
            
            adapters.quanlynguoidung.delete.DeleteUserController controller = 
                new adapters.quanlynguoidung.delete.DeleteUserController(useCase);
            
            controller.executeWithDTO(input);
            
            if (viewModel.success) {
                JOptionPane.showMessageDialog(this, 
                    "Xóa người dùng thành công!\n" +
                    "User: " + viewModel.deletedUsername, 
                    "Thành Công", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, 
                    viewModel.message, 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi xóa: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManagementGUI gui = new UserManagementGUI();
            gui.setVisible(true);
        });
    }
}