package frameworks.desktop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import adapters.deleteorders.DeleteOrderController;
import adapters.deleteorders.DeleteOrderInputDTO;
import adapters.deleteorders.DeleteOrderPresenter;
import adapters.deleteorders.DeleteOrderViewModel;
import adapters.listorders.*;
import quanlydonhang.list.ListOrdersUseCase;
import quanlydonhang.delete.DeleteOrderUseCase;
import repository.jdbc.OrderRepoImpl;
import java.awt.*;

public class ListOrdersGUI extends JFrame implements Subscriber {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbStatus;
    private JComboBox<String> cmbSort;
    private JCheckBox chkAscending;
    private JTextField txtUserIdFilter;
    private JButton btnLoad;
    private JButton btnAdd;    // ⭐ NÚT THÊM
    private JButton btnEdit;   // ⭐ NÚT SỬA
    private JButton btnDelete; // ⭐ NÚT XÓA
    private ListOrdersViewModel model;

    public ListOrdersGUI() {
        setTitle("Danh sách đơn hàng");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ============ FILTER PANEL ============
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        filterPanel.add(new JLabel("Trạng thái:"));
        cmbStatus = new JComboBox<>(new String[]{"all", "pending", "confirmed", "shipping", "completed", "cancelled"});
        filterPanel.add(cmbStatus);

        filterPanel.add(new JLabel("Sắp xếp theo:"));
        cmbSort = new JComboBox<>(new String[]{"orderDate", "totalAmount", "customerName"});
        filterPanel.add(cmbSort);

        chkAscending = new JCheckBox("Tăng dần", true);
        filterPanel.add(chkAscending);

        filterPanel.add(new JLabel("Lọc User ID:"));
        txtUserIdFilter = new JTextField(10);
        filterPanel.add(txtUserIdFilter);

        btnLoad = new JButton("🔄 Tải lại");
        btnLoad.setBackground(new Color(52, 199, 89));
        btnLoad.setForeground(Color.WHITE);
        btnLoad.setFocusPainted(false);
        btnLoad.setFont(new Font("Arial", Font.BOLD, 12));
        filterPanel.add(btnLoad);

        // ============ ACTION BUTTONS PANEL ============
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // ⭐ NÚT THÊM
        btnAdd = new JButton("➕ Thêm đơn hàng");
        btnAdd.setBackground(new Color(0, 122, 255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 12));
        actionPanel.add(btnAdd);

        // ⭐ NÚT SỬA
        btnEdit = new JButton("✏️ Sửa đơn hàng");
        btnEdit.setBackground(new Color(255, 149, 0));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setFont(new Font("Arial", Font.BOLD, 12));
        actionPanel.add(btnEdit);

        // ⭐ NÚT XÓA
        btnDelete = new JButton("🗑️ Xóa đơn hàng");
        btnDelete.setBackground(new Color(255, 59, 48));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setFont(new Font("Arial", Font.BOLD, 12));
        actionPanel.add(btnDelete);

        // ============ COMBINE PANELS ============
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(filterPanel, BorderLayout.NORTH);
        topPanel.add(actionPanel, BorderLayout.SOUTH);

        // ============ TABLE ============
        String[] columns = {"Order ID", "User ID", "Khách hàng", "SĐT", "Địa chỉ", "Tổng tiền", "Trạng thái", "Ngày đặt", "Ghi chú"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(150);
        table.getColumnModel().getColumn(8).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);

        // ============ EVENTS ============
        btnLoad.addActionListener(e -> loadOrders());
        btnAdd.addActionListener(e -> openAddOrderGUI());      // ⭐ MỞ FORM THÊM
        btnEdit.addActionListener(e -> openEditOrderGUI());    // ⭐ MỞ FORM SỬA
        btnDelete.addActionListener(e -> deleteSelectedOrder()); // ⭐ XÓA

        setVisible(true);
        loadOrders();
    }

    private void loadOrders() {
        ListOrdersInputDTO dto = new ListOrdersInputDTO();
        dto.statusFilter = (String) cmbStatus.getSelectedItem();
        dto.sortBy = (String) cmbSort.getSelectedItem();
        dto.ascending = chkAscending.isSelected();

        String userIdFilter = txtUserIdFilter.getText().trim();
        dto.userIdFilter = userIdFilter.isEmpty() ? null : userIdFilter;

        model = new ListOrdersViewModel();
        model.addSubscriber(this);
        ListOrdersPresenter presenter = new ListOrdersPresenter(model);

        OrderRepoImpl repo = new OrderRepoImpl();
        ListOrdersUseCase uc = new ListOrdersUseCase(repo, presenter);
        ListOrdersController controller = new ListOrdersController(uc);

        try {
            controller.execute(dto);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ⭐ MỞ FORM THÊM ĐƠN HÀNG
    private void openAddOrderGUI() {
        new AddOrderGUI(); // Mở form AddOrderGUI (bạn đã có sẵn)
    }

    // ⭐ MỞ FORM SỬA ĐƠN HÀNG
    private void openEditOrderGUI() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn đơn hàng cần sửa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String orderId = (String) tableModel.getValueAt(selectedRow, 0);

        // Mở form UpdateOrderGUI
        new UpdateOrderGUI(orderId);
    }

    // ⭐ XÓA ĐƠN HÀNG
    private void deleteSelectedOrder() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn đơn hàng cần xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String orderId = (String) tableModel.getValueAt(selectedRow, 0);
        String customerName = (String) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa đơn hàng:\n" +
                        "ID: " + orderId + "\n" +
                        "Khách hàng: " + customerName + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        DeleteOrderViewModel deleteViewModel = new DeleteOrderViewModel();
        deleteViewModel.addSubscriber(() -> {
            if (deleteViewModel.success) {
                JOptionPane.showMessageDialog(this,
                        deleteViewModel.message,
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                loadOrders();
            } else {
                JOptionPane.showMessageDialog(this,
                        deleteViewModel.message,
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        DeleteOrderPresenter deletePresenter = new DeleteOrderPresenter(deleteViewModel);
        OrderRepoImpl repo = new OrderRepoImpl();
        DeleteOrderUseCase deleteUseCase = new DeleteOrderUseCase(repo, deletePresenter);
        DeleteOrderController deleteController = new DeleteOrderController(deleteUseCase);

        DeleteOrderInputDTO input = new DeleteOrderInputDTO();
        input.orderId = orderId;
        deleteController.execute(input);
    }

    @Override
    public void update() {
        if (model.success) {
            tableModel.setRowCount(0);

            if (model.orders != null) {
                for (var order : model.orders) {
                    tableModel.addRow(new Object[]{
                            order.id,
                            order.userId,
                            order.customerName,
                            order.customerPhone,
                            order.customerAddress,
                            String.format("%.2f", order.totalAmount),
                            order.status,
                            order.orderDate,
                            order.note != null ? order.note : ""
                    });
                }
            }

            setTitle("Danh sách đơn hàng (" + model.filteredCount + "/" + model.totalCount + ")");
        } else {
            JOptionPane.showMessageDialog(this,
                    model.message,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ListOrdersGUI());
    }
}