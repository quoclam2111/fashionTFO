package frameworks.desktop;

import adapters.sanpham.list.*;
import quanlysanpham.list.ProductListUseCase;
import repository.jdbc.SanPhamRepositoryImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ListProductsGUI extends JFrame implements ProductUpdateListener {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbStatus;
    private JComboBox<String> cmbSort;
    private JCheckBox chkAscending;
    private JButton btnLoad;
    private JButton btnAdd;
    private JButton btnView;
    private JButton btnEdit;
    private JButton btnDelete;
    private JComboBox<String> cmbSearchField;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnClearSearch;

    public ListProductsGUI() {
        setTitle("Quản Lý Sản Phẩm (0/0 sản phẩm)");
        setSize(1400, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initializeComponents();
        loadProducts();
        
        setVisible(true);
    }

    private void initializeComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title Panel
        JPanel titlePanel = createTitlePanel();
        
        // Filter and Search Container
        JPanel filterSearchPanel = createFilterSearchPanel();

        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Action Buttons Panel
        JPanel actionPanel = createActionPanel();

        // Assemble layout
        JPanel topSection = new JPanel(new BorderLayout(0, 10));
        topSection.setBackground(new Color(245, 245, 245));
        topSection.add(titlePanel, BorderLayout.NORTH);
        topSection.add(filterSearchPanel, BorderLayout.CENTER);

        mainPanel.add(topSection, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Events
        btnLoad.addActionListener(e -> loadProducts());
        btnAdd.addActionListener(e -> addNewProduct());
        btnEdit.addActionListener(e -> editSelectedProduct());
        btnDelete.addActionListener(e -> deleteSelectedProduct());
        btnView.addActionListener(e -> viewSelectedProduct());
        btnSearch.addActionListener(e -> searchProducts());
        btnClearSearch.addActionListener(e -> clearSearch());
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(new Color(245, 245, 245));
        
        JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(50, 50, 50));
        panel.add(lblTitle);
        
        return panel;
    }

    private JPanel createFilterSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Left Panel - Filter & Sort
        JPanel leftPanel = createFilterPanel();

        // Right Panel - Search
        JPanel rightPanel = createSearchPanel();

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel lblFilter = new JLabel("Bộ Lọc & Sắp Xếp");
        lblFilter.setFont(new Font("Arial", Font.BOLD, 13));
        lblFilter.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel filterControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        filterControls.setBackground(Color.WHITE);
        filterControls.setAlignmentX(Component.LEFT_ALIGNMENT);

        filterControls.add(new JLabel("Trạng thái:"));
        cmbStatus = new JComboBox<>(new String[]{"all", "Published", "archived", "Draft"});
        cmbStatus.setPreferredSize(new Dimension(140, 28));
        filterControls.add(cmbStatus);

        filterControls.add(new JLabel("Sắp xếp theo:"));
        cmbSort = new JComboBox<>(new String[]{
                "productName", "price", "discountPrice", "stockQuantity", "createdAt", "updatedAt"
        });
        cmbSort.setPreferredSize(new Dimension(140, 28));
        filterControls.add(cmbSort);

        chkAscending = new JCheckBox("Tăng dần", true);
        chkAscending.setBackground(Color.WHITE);
        filterControls.add(chkAscending);

        btnLoad = new JButton("☐ Làm Mới");
        btnLoad.setBackground(new Color(23, 162, 184));
        btnLoad.setForeground(Color.WHITE);
        btnLoad.setFont(new Font("Arial", Font.BOLD, 12));
        btnLoad.setFocusPainted(false);
        btnLoad.setPreferredSize(new Dimension(110, 32));
        btnLoad.setBorderPainted(false);
        filterControls.add(btnLoad);

        panel.add(lblFilter);
        panel.add(Box.createVerticalStrut(8));
        panel.add(filterControls);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel lblSearch = new JLabel("Tìm Kiếm");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 13));
        lblSearch.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchControls.setBackground(Color.WHITE);
        searchControls.setAlignmentX(Component.LEFT_ALIGNMENT);

        cmbSearchField = new JComboBox<>(new String[]{"productName", "productId", "slug", "description"});
        cmbSearchField.setPreferredSize(new Dimension(140, 28));
        searchControls.add(cmbSearchField);

        txtSearch = new JTextField(22);
        txtSearch.setPreferredSize(new Dimension(260, 28));
        searchControls.add(txtSearch);

        btnSearch = new JButton("🔍");
        btnSearch.setBackground(new Color(40, 167, 69));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Arial", Font.BOLD, 14));
        btnSearch.setFocusPainted(false);
        btnSearch.setPreferredSize(new Dimension(40, 32));
        btnSearch.setBorderPainted(false);
        searchControls.add(btnSearch);

        btnClearSearch = new JButton("✖");
        btnClearSearch.setBackground(new Color(108, 117, 125));
        btnClearSearch.setForeground(Color.WHITE);
        btnClearSearch.setFont(new Font("Arial", Font.BOLD, 14));
        btnClearSearch.setFocusPainted(false);
        btnClearSearch.setPreferredSize(new Dimension(40, 32));
        btnClearSearch.setBorderPainted(false);
        searchControls.add(btnClearSearch);

        panel.add(lblSearch);
        panel.add(Box.createVerticalStrut(8));
        panel.add(searchControls);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        String[] columns = {
                "ID", "Tên Sản Phẩm", "Slug", "Mô Tả", "Brand ID", "Category ID",
                "Hình Ảnh", "Giá", "Giá Giảm", "Số Lượng", "Trạng Thái", "Ngày Tạo", "Ngày Cập Nhật"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(52, 58, 64));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(header.getWidth(), 38));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblActions = new JLabel("Chức Năng");
        lblActions.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(lblActions, BorderLayout.NORTH);

        JPanel buttonsContainer = new JPanel(new GridLayout(1, 4, 15, 0));
        buttonsContainer.setBackground(Color.WHITE);
        buttonsContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnAdd = createActionButton("➕ Thêm Mới", new Color(40, 167, 69));
        btnView = createActionButton("👁️ Xem Chi Tiết", new Color(0, 123, 255));
        btnEdit = createActionButton("✏️ Cập Nhật", new Color(255, 193, 7));
        btnDelete = createActionButton("🗑️ Xóa", new Color(220, 53, 69));

        buttonsContainer.add(btnAdd);
        buttonsContainer.add(btnView);
        buttonsContainer.add(btnEdit);
        buttonsContainer.add(btnDelete);

        panel.add(buttonsContainer, BorderLayout.CENTER);

        return panel;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 45));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    private void loadProducts() {
        try {
            System.out.println("=== BẮT ĐẦU LOAD PRODUCTS ===");

            ListProductInputDTO dto = new ListProductInputDTO();
            dto.statusFilter = (String) cmbStatus.getSelectedItem();
            dto.sortBy = (String) cmbSort.getSelectedItem();
            dto.ascending = chkAscending.isSelected();

            ListProductViewModel model = new ListProductViewModel();
            ListProductPresenter presenter = new ListProductPresenter(model);
            SanPhamRepositoryImpl repo = new SanPhamRepositoryImpl();
            ProductListUseCase useCase = new ProductListUseCase(repo, presenter);
            ListProductController controller = new ListProductController(useCase);

            controller.execute(dto);

            if (model.success) {
                tableModel.setRowCount(0);
                if (model.products != null && !model.products.isEmpty()) {
                    for (var p : model.products) {
                        tableModel.addRow(new Object[]{
                                p.productId, p.productName, p.slug, p.description,
                                p.brandId, p.categoryId, p.defaultImage,
                                p.price, p.discountPrice, p.stockQuantity,
                                p.status, p.createdAt, p.updatedAt
                        });
                    }
                    setTitle("Quản Lý Sản Phẩm (" + model.filteredCount + "/" + model.totalCount + " sản phẩm)");
                    System.out.println("✓ Đã load " + model.products.size() + " sản phẩm thành công!");
                } else {
                    setTitle("Quản Lý Sản Phẩm (0/0 sản phẩm)");
                    JOptionPane.showMessageDialog(this,
                            "Không có sản phẩm nào trong hệ thống!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Lỗi: " + model.message,
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải danh sách sản phẩm:\n" + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("=== KẾT THÚC LOAD PRODUCTS ===\n");
    }

    private void searchProducts() {
        String searchValue = txtSearch.getText().trim();
        if (searchValue.isEmpty()) {
            loadProducts();
            return;
        }

        String searchField = (String) cmbSearchField.getSelectedItem();
        int columnIndex;
        switch (searchField) {
            case "productName": columnIndex = 1; break;
            case "productId": columnIndex = 0; break;
            case "slug": columnIndex = 2; break;
            case "description": columnIndex = 3; break;
            default: columnIndex = 1;
        }

        java.util.List<Object[]> allData = new java.util.ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object[] row = new Object[13];
            for (int j = 0; j < 13; j++) {
                row[j] = tableModel.getValueAt(i, j);
            }
            allData.add(row);
        }

        tableModel.setRowCount(0);
        int count = 0;

        for (Object[] row : allData) {
            String cellValue = row[columnIndex] != null ? row[columnIndex].toString().toLowerCase() : "";
            if (cellValue.contains(searchValue.toLowerCase())) {
                tableModel.addRow(row);
                count++;
            }
        }

        setTitle("Quản Lý Sản Phẩm (Tìm thấy: " + count + " kết quả)");

        if (count == 0) {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy kết quả nào!",
                    "Thông Báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearSearch() {
        txtSearch.setText("");
        cmbSearchField.setSelectedIndex(0);
        loadProducts();
    }

    private void addNewProduct() {
        new AddProductUi(this);
    }

    private void editSelectedProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một sản phẩm để sửa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String productId = (String) tableModel.getValueAt(selectedRow, 0);
        new EditProductGUI(productId, this);
    }

    private void viewSelectedProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một sản phẩm để xem!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String productId = (String) tableModel.getValueAt(selectedRow, 0);
        new ViewProductGUI(productId);
    }

    private void deleteSelectedProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một sản phẩm để xóa!",
                    "Thông Báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String productId = (String) tableModel.getValueAt(selectedRow, 0);
        String productName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa sản phẩm?\n\n" +
                        "Mã: " + productId + "\n" +
                        "Tên: " + productName + "\n\n" +
                        "Hành động này không thể hoàn tác!",
                "Xác Nhận Xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        DeleteProductGUI deleteGUI = new DeleteProductGUI(this);
        deleteGUI.setProductId(productId);
        deleteGUI.setVisible(true);
    }

    @Override
    public void onProductUpdated() {
        loadProducts();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ListProductsGUI());
    }
}