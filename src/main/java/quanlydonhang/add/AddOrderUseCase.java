package quanlydonhang.add;

import quanlydonhang.*;
import repository.hoadon.AddOrderRepoGateway;
import repository.DTO.OrderDTO;

/**
 * Control cụ thể cho Add Order
 */
public class AddOrderUseCase extends QuanLyDonHangControl {
    private final AddOrderRepoGateway repository;

    public AddOrderUseCase(AddOrderRepoGateway repository,
                           QuanLyDonHangOutputBoundary presenter) {
        super(presenter);
        this.repository = repository;
        this.response = new ResponseDataAddOrder();
    }

    @Override
    protected void execute(QuanLyDonHangRequestData request) {
        try {
            // 1. Validate input
            Order.checkUserId(request.userId);
            Order.checkCustomerName(request.customerName);
            Order.checkCustomerPhone(request.customerPhone);
            Order.checkCustomerAddress(request.customerAddress);
            Order.checkTotalAmount(request.totalAmount);

            // 2. Convert input → Order entity (Business Object)
            Order order = convertToBusinessObject(request);

            // 3. Convert Order → OrderDTO
            OrderDTO dto = convertToDTO(order);

            // 4. Save vào repository
            repository.save(dto);

            // 5. Set response
            ResponseDataAddOrder addResponse = (ResponseDataAddOrder) response;

            response.success = true;
            response.message = "Tạo đơn hàng thành công!";
            addResponse.addedOrderId = order.getId();
            addResponse.orderStatus = order.getStatus();

        } catch (IllegalArgumentException ex) {
            response.success = false;
            response.message = ex.getMessage();
        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
    }

    private Order convertToBusinessObject(QuanLyDonHangRequestData request) {
        return new Order(
                null,                   // ID sẽ tự sinh UUID
                request.userId,
                request.customerName,
                request.customerPhone,
                request.customerAddress,
                request.totalAmount,
                "pending",              // default status
                null,                   // orderDate sẽ tự sinh
                request.note
        );
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.id = order.getId();
        dto.userId = order.getUserId();
        dto.customerName = order.getCustomerName();
        dto.customerPhone = order.getCustomerPhone();
        dto.customerAddress = order.getCustomerAddress();
        dto.totalAmount = order.getTotalAmount();
        dto.status = order.getStatus();
        dto.orderDate = order.getOrderDate();
        dto.note = order.getNote();
        return dto;
    }
}