package frameworks.desktop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import adapters.listorders.*;
import quanlydonhang.list.ListOrdersUseCase;
import repository.jdbc.OrderRepoImpl;
import java.lang.String;
import java.awt.*;

public class ListOrdersGUI extends JFrame implements Subscriber {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbStatus;
    private JComboBox<String> cmbSort;
    private JCheckBox chkAscending;
    private JTextField txtUserIdFilter;
    private JButton btnLoad;
    private ListOrdersViewModel model;

    public ListOrdersGUI() {
        setTitle("Danh sách đơn hàng");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Filter Panel
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

        btnLoad = new JButton("🔄 Tải danh sách");
        btnLoad.setBackground(new Color(52, 199, 89));
        btnLoad.setForeground(Color.WHITE);
        btnLoad.setFocusPainted(false);
        btnLoad.setFont(new Font("Arial", Font.BOLD, 12));
        filterPanel.add(btnLoad);

        // Table
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
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Order ID
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  // User ID
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Khách hàng
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // SĐT
        table.getColumnModel().getColumn(4).setPreferredWidth(200); // Địa chỉ
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Tổng tiền
        table.getColumnModel().getColumn(6).setPreferredWidth(100); // Trạng thái
        table.getColumnModel().getColumn(7).setPreferredWidth(150); // Ngày đặt
        table.getColumnModel().getColumn(8).setPreferredWidth(150); // Ghi chú

        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);

        // Event
        btnLoad.addActionListener(e -> loadOrders());

        setVisible(true);
        loadOrders(); // Load ngay khi mở
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

    @Override
    public void update() {
        if (model.success) {
            // Xóa dữ liệu cũ
            tableModel.setRowCount(0);

            // Thêm dữ liệu mới
            if (model.orders != null) {
                for (var order : model.orders) {
                    tableModel.addRow(new Object[]{
                            order.id,
                            order.userId,
                            order.customerName,
                            order.customerPhone,
                            order.customerAddress,
                            String.format("%.2f", Double.valueOf(order.totalAmount))
                            ,
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