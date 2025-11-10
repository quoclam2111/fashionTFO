package quanlydonhang.get;

import java.util.Optional;
import java.text.SimpleDateFormat;
import repository.GetOrderRepositoryGateway;
import repository.DTO.OrderDTO;

public class GetOrderUseCase implements GetOrderInputBoundary {
    private final GetOrderRepositoryGateway repository;
    private final GetOrderOutputBoundary presenter;

    public GetOrderUseCase(GetOrderRepositoryGateway repository, GetOrderOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void execute(GetOrderInputData input) {
        GetOrderOutputData output = new GetOrderOutputData();

        try {
            // 1. Validate input
            if (input.searchValue == null || input.searchValue.trim().isEmpty()) {
                output.success = false;
                output.message = "SEARCH_VALUE_EMPTY";
                presenter.present(output);
                return;
            }

            if (!isValidSearchType(input.searchBy)) {
                output.success = false;
                output.message = "INVALID_SEARCH_TYPE";
                presenter.present(output);
                return;
            }

            // 2. Tìm order theo searchBy
            Optional<OrderDTO> result = findOrder(input.searchBy, input.searchValue);

            if (!result.isPresent()) {
                output.success = false;
                output.message = "ORDER_NOT_FOUND";
                presenter.present(output);
                return;
            }

            // 3. Convert DTO → OrderInfoData
            output.order = convertToOrderInfoData(result.get());
            output.success = true;
            output.message = "Lấy thông tin đơn hàng thành công!";
            presenter.present(output);

        } catch (Exception ex) {
            output.success = false;
            output.message = ex.getMessage();
            presenter.present(output);
        }
    }

    private Optional<OrderDTO> findOrder(String searchBy, String searchValue) {
        switch (searchBy.toLowerCase()) {
            case "id":
                return repository.findById(searchValue);
            case "userid":
                return repository.findByUserId(searchValue);
            case "phone":
                return repository.findByPhone(searchValue);
            default:
                return Optional.empty();
        }
    }

    private boolean isValidSearchType(String type) {
        return type != null &&
                (type.equalsIgnoreCase("id") ||
                        type.equalsIgnoreCase("userId") ||
                        type.equalsIgnoreCase("phone"));
    }

    private GetOrderInfoData convertToOrderInfoData(OrderDTO dto) {
        GetOrderInfoData info = new GetOrderInfoData();
        info.id = dto.id;
        info.userId = dto.userId;
        info.customerName = dto.customerName;
        info.customerPhone = dto.customerPhone;
        info.customerAddress = dto.customerAddress;
        info.totalAmount = dto.totalAmount;
        info.status = dto.status;
        info.orderDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dto.orderDate);
        info.note = dto.note;
        return info;
    }
}