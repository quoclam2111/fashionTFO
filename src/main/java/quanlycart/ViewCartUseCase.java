package quanlycart;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import repository.cart.CartRepository;
import repository.DTO.CartDTO;

public class ViewCartUseCase implements ViewCartInputBoundary {
    private final CartRepository repository;
    private final ViewCartOutputBoundary presenter;

    public ViewCartUseCase(CartRepository repository, ViewCartOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewCartInputData inputData) {
        try {
            // Step 3: Lấy userId
            String userId = inputData.getUserId();
            
            // Step 4: Validate userId
            if (userId == null || userId.trim().isEmpty()) {
                // Step 5.4.1: Không có userId
                presenter.presentError("INVALID_INPUT", "User ID không được để trống");
                return;
            }
            
            // Step 5: Kết nối database và lấy danh sách sản phẩm
            List<CartDTO> dtoList;
            
            try {
                // Step 6-10: Query database
                dtoList = repository.findCartItemsByUserId(userId);
                
            } catch (SQLException e) {
                // Step 5.5.1 - 5.5.7: Lỗi database
                handleDatabaseError(e);
                return;
            }
            
            // Step 11: Check kết quả
            if (dtoList == null || dtoList.isEmpty()) {
                // Step 7.1 hoặc 6.1: Giỏ hàng trống hoặc user chưa có cart
                presenter.presentEmptyCart("Giỏ hàng trống. Hãy thêm sản phẩm vào giỏ!");
                return;
            }
            
            // Step 12-13: Convert CartDTO → ViewCartOutputData
            List<ViewCartOutputData> outputList = new ArrayList<>();
            boolean hasIncompleteData = false;
            
            for (CartDTO dto : dtoList) {
                // Step 7.2: Check data integrity
                if (dto.getProductName() == null || dto.getProductName().isEmpty()) {
                    // Step 7.2.1-7.2.4: Data thiếu thông tin
                    dto.setProductName("Unknown Product");
                    hasIncompleteData = true;
                }
                
                if (dto.getPrice() < 0) {
                    dto.setPrice(0.0);
                    hasIncompleteData = true;
                }
                
                outputList.add(new ViewCartOutputData(
                        dto.getProductName(),
                        dto.getPrice(),
                        dto.getQuantity(),
                        dto.getVariantId()
                ));
            }
            
            // Step 14-17: Present kết quả
            presenter.present(outputList);
            
            // Log warning nếu có data không đầy đủ
            if (hasIncompleteData) {
                System.out.println("⚠️  Warning: Một số sản phẩm có thông tin không đầy đủ");
            }
            
        } catch (Exception e) {
            // Catch-all for unexpected errors
            presenter.presentError("UNKNOWN_ERROR", "Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleDatabaseError(SQLException e) {
        String errorMessage = e.getMessage();
        
        if (errorMessage.contains("Communications link failure") || 
            errorMessage.contains("Connection refused")) {
            // Step 5.5.2: Không kết nối được database
            presenter.presentError("DATABASE_OFFLINE", 
                "Không thể kết nối đến cơ sở dữ liệu. Vui lòng kiểm tra kết nối.");
        } else if (e instanceof java.sql.SQLSyntaxErrorException) {
            // Step 5.5.3: Lỗi SQL syntax
            presenter.presentError("SQL_ERROR", 
                "Lỗi truy vấn cơ sở dữ liệu. Vui lòng liên hệ quản trị viên.");
        } else {
            // Step 5.5.4: Lỗi database khác
            presenter.presentError("DATABASE_ERROR", 
                "Lỗi cơ sở dữ liệu: " + errorMessage);
        }
        
        // Step 5.5.6: Log error
        System.err.println("❌ Database Error: " + errorMessage);
        e.printStackTrace();
    }
}