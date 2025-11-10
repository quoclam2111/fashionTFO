package quanlydonhang.list;

import quanlydonhang.*;
import repository.ListOrdersRepositoryGateway;
import repository.DTO.OrderDTO;
import java.util.List;
import java.util.stream.Collectors;

public class ListOrdersUseCase extends QuanLyDonHangControl {
    private final ListOrdersRepositoryGateway repository;

    public ListOrdersUseCase(ListOrdersRepositoryGateway repository,
                             QuanLyDonHangOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new ResponseDataListOrders();
    }

    @Override
    protected void execute(QuanLyDonHangRequestData request) {
        try {
            // 1. Lấy tất cả orders
            List<OrderDTO> allOrders = repository.findAll();

            // 2. Lọc theo userId nếu có
            if (request.userIdFilter != null && !request.userIdFilter.trim().isEmpty()) {
                allOrders = allOrders.stream()
                        .filter(o -> o.userId.equals(request.userIdFilter))
                        .collect(Collectors.toList());
            }

            // 3. Lọc theo status
            List<OrderDTO> filteredOrders = filterByStatus(allOrders, request.statusFilter);

            // 4. Sắp xếp
            sortOrders(filteredOrders, request.sortBy, request.ascending);

            // 5. Set response
            response.success = true;
            response.message = "Lấy danh sách đơn hàng thành công!";
            response.orders = filteredOrders;
            response.totalCount = allOrders.size();
            response.filteredCount = filteredOrders.size();

        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
    }

    private List<OrderDTO> filterByStatus(List<OrderDTO> orders, String statusFilter) {
        if (statusFilter == null || statusFilter.equals("all")) {
            return orders;
        }
        return orders.stream()
                .filter(o -> o.status.equals(statusFilter))
                .collect(Collectors.toList());
    }

    private void sortOrders(List<OrderDTO> orders, String sortBy, boolean ascending) {
        if (sortBy == null) return;

        orders.sort((a, b) -> {
            int result = 0;
            switch (sortBy) {
                case "orderDate":
                    result = a.orderDate.compareTo(b.orderDate);
                    break;
                case "totalAmount":
                    result = Double.compare(a.totalAmount, b.totalAmount);
                    break;
                case "customerName":
                    result = a.customerName.compareTo(b.customerName);
                    break;
            }
            return ascending ? result : -result;
        });
    }
}