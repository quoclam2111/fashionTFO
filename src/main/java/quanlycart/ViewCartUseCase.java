package quanlycart;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import repository.DTO.CartDTO;
import repository.cart.CartRepository;

public class ViewCartUseCase implements ViewCartInputBoundary {
    private final CartRepository repository;
    private final ViewCartOutputBoundary presenter;

    public ViewCartUseCase(CartRepository repository, ViewCartOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewCartInputData inputData) {
        if (!isValidInput(inputData)) {
            return;
        }
        
        List<CartDTO> cartItems = fetchCartItems(inputData.getUserId());
        if (cartItems == null) {
            return;
        }
        
        if (cartItems.isEmpty()) {
            presenter.presentEmptyCart("Giỏ hàng trống. Hãy thêm sản phẩm vào giỏ!");
            return;
        }
        
        List<ViewCartOutputData> outputData = convertToOutputData(cartItems);
        presenter.present(outputData);
    }
    
    private boolean isValidInput(ViewCartInputData inputData) {
        if (inputData == null) {
            presenter.presentError("INVALID_INPUT", "Input data không được null");
            return false;
        }
        
        String userId = inputData.getUserId();
        if (userId == null || userId.trim().isEmpty()) {
            presenter.presentError("INVALID_INPUT", "User ID không được để trống");
            return false;
        }
        
        return true;
    }
    
    private List<CartDTO> fetchCartItems(String userId) {
        try {
            return repository.findCartItemsByUserId(userId);
        } catch (SQLException e) {
            handleDatabaseError(e);
            return null;
        }
    }
    
    private List<ViewCartOutputData> convertToOutputData(List<CartDTO> dtoList) {
        List<ViewCartOutputData> result = dtoList.stream()
            .map(this::mapToOutputData)
            .collect(Collectors.toList());
        
        if (hasIncompleteData(dtoList)) {
            logIncompleteDataWarning();
        }
        
        return result;
    }
    
    private ViewCartOutputData mapToOutputData(CartDTO dto) {
        String productName = (dto.getProductName() != null && !dto.getProductName().isEmpty()) 
            ? dto.getProductName() 
            : "Unknown Product";
        
        double price = dto.getPrice() >= 0 ? dto.getPrice() : 0.0;
        
        return new ViewCartOutputData(
            productName,
            price,
            dto.getQuantity(),
            dto.getVariantId()
        );
    }
    
    private boolean hasIncompleteData(List<CartDTO> dtoList) {
        return dtoList.stream().anyMatch(dto -> 
            dto.getProductName() == null || 
            dto.getProductName().isEmpty() || 
            dto.getPrice() < 0
        );
}
    
    private void handleDatabaseError(SQLException e) {
        String errorMessage = e.getMessage();
        
        if (isConnectionError(errorMessage)) {
            presenter.presentError("DATABASE_OFFLINE", 
                "Không thể kết nối đến cơ sở dữ liệu. Vui lòng kiểm tra kết nối.");
        } else if (e instanceof java.sql.SQLSyntaxErrorException) {
            presenter.presentError("SQL_ERROR", 
                "Lỗi truy vấn cơ sở dữ liệu. Vui lòng liên hệ quản trị viên.");
        } else {
            presenter.presentError("DATABASE_ERROR", 
                "Lỗi cơ sở dữ liệu: " + errorMessage);
        }
        
        logDatabaseError(e);
    }
    
    private boolean isConnectionError(String errorMessage) {
        return errorMessage != null && (
            errorMessage.contains("Communications link failure") || 
            errorMessage.contains("Connection refused")
        );
    }
    
    private void logIncompleteDataWarning() {
        System.out.println("⚠️  Warning: Một số sản phẩm có thông tin không đầy đủ");
    }
    
    private void logDatabaseError(SQLException e) {
        System.err.println("❌ Database Error: " + e.getMessage());
        e.printStackTrace();
    }
}