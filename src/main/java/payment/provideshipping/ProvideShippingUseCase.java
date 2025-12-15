package payment.provideshipping;

import entity.Order;
import repository.DTO.ResultPaymentDTO;
import repository.payment.OrderPaymentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProvideShippingUseCase {
    private final OrderPaymentRepository orderRepo;
    private final GHNAddressValidationService ghnService;
    private final Integer shopDistrictId; // Quận của shop để check có ship được không
    private final Integer shopId; // Shop ID từ GHN

    public ProvideShippingUseCase(
            OrderPaymentRepository orderRepo, 
            GHNAddressValidationService ghnService,
            Integer shopDistrictId,
            Integer shopId) {
        this.orderRepo = orderRepo;
        this.ghnService = ghnService;
        this.shopDistrictId = shopDistrictId;
        this.shopId = shopId;
    }

    public void execute(ShippingInfoInput input, ProvideShippingPresenter presenter) {
        // Step 1: Validate input không null
        if (input == null) {
            presenter.presentError("Input missing");
            return;
        }

        // Step 2: Collect validation errors
        List<ResultPaymentDTO.FieldError> errors = validateInput(input);
        
        if (!errors.isEmpty()) {
            presenter.presentValidationErrors(errors);
            return;
        }

        // Step 3: Validate address với GHN API
        GHNAddressValidationService.GHNAddressValidationResult addressValidation = 
            ghnService.validateAddress(input.wardCode, input.districtId);
        
        if (!addressValidation.isValid()) {
            errors.add(new ResultPaymentDTO.FieldError("wardCode", addressValidation.getErrorMessage()));
            presenter.presentValidationErrors(errors);
            return;
        }

        // Step 4: Check service availability (optional nhưng nên có)
        if (shopDistrictId != null && shopId != null) {
            GHNAddressValidationService.GHNAddressValidationResult serviceCheck = 
                ghnService.checkServiceAvailability(shopDistrictId, input.districtId, shopId);
            
            if (!serviceCheck.isValid()) {
                errors.add(new ResultPaymentDTO.FieldError("districtId", serviceCheck.getErrorMessage()));
                presenter.presentValidationErrors(errors);
                return;
            }
        }

        // Step 5: Parse và validate orderId
        UUID orderId;
        try {
            orderId = UUID.fromString(input.orderId);
        } catch (Exception ex) {
            presenter.presentError("OrderId format invalid");
            return;
        }

        // Step 6: Find order
        Optional<Order> maybeOrder = orderRepo.findById(orderId);
        if (!maybeOrder.isPresent()) {
            presenter.presentError("Order not found");
            return;
        }

        // Step 7: Update order với shipping info
        Order order = maybeOrder.get();
        updateOrderWithShippingInfo(order, input);
        orderRepo.save(order);

        // Step 8: Present success
        ProvideShippingOutputModel output = new ProvideShippingOutputModel(
            order.getId().toString(), 
            order.getPhone(), 
            order.getAddress()
        );
        presenter.present(output);
    }

  
    private List<ResultPaymentDTO.FieldError> validateInput(ShippingInfoInput input) {
        List<ResultPaymentDTO.FieldError> errors = new ArrayList<>();

        // Validate phone
        if (input.phone == null || input.phone.isBlank()) {
            errors.add(new ResultPaymentDTO.FieldError("phone", "Số điện thoại không được để trống"));
        } else if (!input.phone.matches("^[0-9]{10,11}$")) {
            errors.add(new ResultPaymentDTO.FieldError("phone", "Số điện thoại phải có 10-11 chữ số"));
        }

        // Validate address detail
        if (input.address == null || input.address.isBlank()) {
            errors.add(new ResultPaymentDTO.FieldError("address", "Địa chỉ chi tiết không được để trống"));
        } else if (input.address.length() < 5) {
            errors.add(new ResultPaymentDTO.FieldError("address", "Địa chỉ chi tiết quá ngắn"));
        }

        // Validate GHN address components
        if (input.provinceId == null) {
            errors.add(new ResultPaymentDTO.FieldError("provinceId", "Vui lòng chọn Tỉnh/Thành phố"));
        }
        
        if (input.districtId == null) {
            errors.add(new ResultPaymentDTO.FieldError("districtId", "Vui lòng chọn Quận/Huyện"));
        }
        
        if (input.wardCode == null || input.wardCode.isBlank()) {
            errors.add(new ResultPaymentDTO.FieldError("wardCode", "Vui lòng chọn Phường/Xã"));
        }

        // Validate orderId format
        if (input.orderId == null || input.orderId.isBlank()) {
            errors.add(new ResultPaymentDTO.FieldError("orderId", "Order ID không được để trống"));
        }

        return errors;
    }

    private void updateOrderWithShippingInfo(Order order, ShippingInfoInput input) {
        order.setPhone(input.phone);
        

        String fullAddress = buildFullAddress(input);
        order.setAddress(fullAddress);
  
    }


    private String buildFullAddress(ShippingInfoInput input) {
        
        return input.address;
    }
}