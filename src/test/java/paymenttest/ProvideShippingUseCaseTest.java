package paymenttest;

import entity.Order;
import entity.OrderItem;
import payment.provideshipping.ProvideShippingOutputModel;
import payment.provideshipping.ProvideShippingPresenter;
import payment.provideshipping.ProvideShippingUseCase;
import payment.provideshipping.ShippingInfoInput;
import repository.DTO.ResultPaymentDTO;
import repository.payment.OrderPaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UC2: Nhập thông tin giao hàng")
class ProvideShippingUseCaseTest {

    @Mock 
    private OrderPaymentRepository orderRepo;
    
    @Mock 
    private ProvideShippingPresenter presenter;

    private ProvideShippingUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ProvideShippingUseCase(orderRepo);
    }

    private Order createTestOrder(UUID orderId) {
        List<OrderItem> items = Arrays.asList(
            new OrderItem("p1", "Product", 1, 100000)
        );
        return new Order(orderId, items);
    }

    @Test
    @DisplayName("Kịch bản 1: Nhập thông tin giao hàng thành công")
    void shouldSaveShippingInfo_WhenInputIsValid() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        ShippingInfoInput input = new ShippingInfoInput(
            orderId.toString(),
            "0123456789",
            "123 Nguyễn Văn Linh, Q7, TP.HCM"
        );
        Order order = createTestOrder(orderId);
        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        useCase.execute(input, presenter);

        // Assert
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        
        assertEquals("0123456789", savedOrder.getPhone());
        assertEquals("123 Nguyễn Văn Linh, Q7, TP.HCM", savedOrder.getAddress());

        ArgumentCaptor<ProvideShippingOutputModel> outputCaptor = 
            ArgumentCaptor.forClass(ProvideShippingOutputModel.class);
        verify(presenter).present(outputCaptor.capture());
        ProvideShippingOutputModel output = outputCaptor.getValue();
        
        assertEquals(orderId.toString(), output.orderId);
        assertEquals("0123456789", output.phone);
        assertEquals("123 Nguyễn Văn Linh, Q7, TP.HCM", output.address);
    }

    @Test
    @DisplayName("Kịch bản 1: Extension 2.2 - User nhập thiếu chi tiết trong giao hàng")
    void shouldPresentValidationErrors_WhenPhoneIsInvalid() {
        // Arrange - Phone không hợp lệ (< 10 số)
        UUID orderId = UUID.randomUUID();
        ShippingInfoInput input = new ShippingInfoInput(
            orderId.toString(),
            "123",
            "123 Address"
        );

        // Act
        useCase.execute(input, presenter);

        // Assert
        ArgumentCaptor<List<ResultPaymentDTO.FieldError>> captor = ArgumentCaptor.forClass(List.class);
        verify(presenter).presentValidationErrors(captor.capture());
        List<ResultPaymentDTO.FieldError> errors = captor.getValue();
        
        assertEquals(1, errors.size());
        assertEquals("phone", errors.get(0).field);
        assertTrue(errors.get(0).error.contains("không hợp lệ"));
        verify(orderRepo, never()).save(any());
    }

    @Test
    @DisplayName("Kịch bản 1: Extension 3 - User nhập nhiều lỗi đồng thời")
    void shouldPresentValidationErrors_WhenMultipleFieldsInvalid() {
        // Arrange - Cả phone và address đều lỗi
        UUID orderId = UUID.randomUUID();
        ShippingInfoInput input = new ShippingInfoInput(
            orderId.toString(),
            "abc",  // Phone không hợp lệ
            ""      // Address rỗng
        );

        // Act
        useCase.execute(input, presenter);

        // Assert
        ArgumentCaptor<List<ResultPaymentDTO.FieldError>> captor = ArgumentCaptor.forClass(List.class);
        verify(presenter).presentValidationErrors(captor.capture());
        List<ResultPaymentDTO.FieldError> errors = captor.getValue();
        
        assertEquals(2, errors.size());
        
        // Kiểm tra có lỗi phone
        assertTrue(errors.stream().anyMatch(e -> e.field.equals("phone")));
        
        // Kiểm tra có lỗi address
        assertTrue(errors.stream().anyMatch(e -> e.field.equals("address")));
        
        verify(orderRepo, never()).save(any());
    }

    @Test
    @DisplayName("Kịch bản 2: User nhập address rỗng")
    void shouldPresentValidationErrors_WhenAddressIsBlank() {
        UUID orderId = UUID.randomUUID();
        ShippingInfoInput input = new ShippingInfoInput(
            orderId.toString(),
            "0123456789",
            "   " // Address chỉ có khoảng trắng
        );

        useCase.execute(input, presenter);

        ArgumentCaptor<List<ResultPaymentDTO.FieldError>> captor = ArgumentCaptor.forClass(List.class);
        verify(presenter).presentValidationErrors(captor.capture());
        List<ResultPaymentDTO.FieldError> errors = captor.getValue();
        
        assertTrue(errors.stream().anyMatch(e -> 
            e.field.equals("address") && e.error.contains("không được để trống")
        ));
    }

    @Test
    @DisplayName("Kịch bản 3: User nhập orderId không tồn tại")
    void shouldPresentError_WhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        ShippingInfoInput input = new ShippingInfoInput(
            orderId.toString(),
            "0123456789",
            "Address"
        );
        when(orderRepo.findById(orderId)).thenReturn(Optional.empty());

        useCase.execute(input, presenter);

        verify(presenter).presentError("Order not found");
        verify(orderRepo, never()).save(any());
    }

    @Test
    @DisplayName("Kịch bản 4: User nhập orderId format sai")
    void shouldPresentError_WhenOrderIdFormatInvalid() {
        ShippingInfoInput input = new ShippingInfoInput(
            "invalid-uuid-format",
            "0123456789",
            "Address"
        );

        useCase.execute(input, presenter);

        verify(presenter).presentError("OrderId format invalid");
        verify(orderRepo, never()).save(any());
    }

    @Test
    @DisplayName("Kịch bản 5: Input null")
    void shouldPresentError_WhenInputIsNull() {
        useCase.execute(null, presenter);
        verify(presenter).presentError("Input missing");
    }

    @Test
    @DisplayName("Kịch bản 6: Phone hợp lệ 10-11 số")
    void shouldAcceptValidPhoneNumbers() {
        UUID orderId = UUID.randomUUID();
        Order order = createTestOrder(orderId);
        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));

        // Test phone 10 số
        ShippingInfoInput input10 = new ShippingInfoInput(
            orderId.toString(), "0987654321", "Address"
        );
        useCase.execute(input10, presenter);
        verify(presenter).present(any());

        // Reset và test phone 11 số
        reset(presenter);
        ShippingInfoInput input11 = new ShippingInfoInput(
            orderId.toString(), "01234567890", "Address"
        );
        useCase.execute(input11, presenter);
        verify(presenter).present(any());
    }
}