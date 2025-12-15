package paymenttest;

import entity.Order;
import entity.OrderItem;
import payment.createorder.CreateOrderInput;
import payment.createorder.CreateOrderOutputModel;
import payment.createorder.CreateOrderPaymentUseCase;
import payment.createorder.CreateOrderPresenter;
import payment.provideshipping.ShippingFeeCalculator;
import repository.cart.CartRepository;
import repository.payment.OrderPaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;



@ExtendWith(MockitoExtension.class)
@DisplayName("UC1: Tạo hóa đơn thanh toán")
class CreateOrderUseCaseTest {

    @Mock private CartRepository cartRepo;
    @Mock private OrderPaymentRepository orderRepo;
    @Mock private ShippingFeeCalculator shippingCalc;
    @Mock private CreateOrderPresenter presenter;

    private CreateOrderPaymentUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateOrderPaymentUseCase(cartRepo, orderRepo, shippingCalc);
    }

    // Kịch bản 1: 1000% chi tiết phần mở rộng trong cả hàng - 1, 2, 3
    @Test
    @DisplayName("Kịch bản 1: Tạo hóa đơn thành công - giỏ hàng có sản phẩm")
    void shouldCreateOrderSuccessfully_WhenCartHasProducts() {
        // Arrange - Chuẩn bị dữ liệu test
        CreateOrderInput input = new CreateOrderInput("user123", 10);
        List<OrderItem> items = Arrays.asList(
            new OrderItem("p1", "Áo thun", 2, 150000),
            new OrderItem("p2", "Quần jean", 1, 350000)
        );
        when(cartRepo.getCartForUser("user123")).thenReturn(items);
        when(shippingCalc.calculate(10)).thenReturn(25000);

        // Act - Thực thi use case
        useCase.execute(input, presenter);

        // Assert - Kiểm tra kết quả
        // 2.1: Tổng giỏ hàng và chi phí phân phối
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        
        assertEquals(650000, savedOrder.getItemTotal()); // 2*150000 + 1*350000
        assertEquals(25000, savedOrder.getShippingFee());
        
        // 2.2: Hệ thống lưu giỏ hàng vào giao hàng
        assertNotNull(savedOrder.getId());
        assertEquals(2, savedOrder.getItems().size());
        
        // 3: Thông báo cho User "Giỏ hàng đang chờ"
        ArgumentCaptor<CreateOrderOutputModel> outputCaptor = 
            ArgumentCaptor.forClass(CreateOrderOutputModel.class);
        verify(presenter).present(outputCaptor.capture());
        CreateOrderOutputModel output = outputCaptor.getValue();
        
        assertNotNull(output.orderId);
        assertEquals(650000, output.itemTotal);
        assertEquals(25000, output.shippingFee);
        assertEquals(0, output.vat); // Chưa chọn payment method
        assertEquals(675000, output.finalAmount);
        assertEquals(2, output.itemsSummary.size());
        assertTrue(output.itemsSummary.get(0).contains("Áo thun"));
    }

    @Test
    @DisplayName("Kịch bản 1: Extension 3.1 - Phần mở rộng chi phí giao hàng kém đơn chỉ định")
    void shouldCalculateShippingFeeCorrectly_BasedOnDistance() {
        // Test với khoảng cách khác nhau
        CreateOrderInput input = new CreateOrderInput("user123", 50); // 50km
        List<OrderItem> items = Arrays.asList(
            new OrderItem("p1", "Product", 1, 100000)
        );
        when(cartRepo.getCartForUser("user123")).thenReturn(items);
        when(shippingCalc.calculate(50)).thenReturn(75000); // Phí cao hơn

        useCase.execute(input, presenter);

        ArgumentCaptor<CreateOrderOutputModel> captor = 
            ArgumentCaptor.forClass(CreateOrderOutputModel.class);
        verify(presenter).present(captor.capture());
        
        assertEquals(75000, captor.getValue().shippingFee);
    }

    @Test
    @DisplayName("Kịch bản 2: Giỏ hàng rỗng - User chưa thêm sản phẩm")
    void shouldPresentError_WhenCartIsEmpty() {
        // Arrange
        CreateOrderInput input = new CreateOrderInput("user123", 10);
        when(cartRepo.getCartForUser("user123")).thenReturn(Collections.emptyList());

        // Act
        useCase.execute(input, presenter);

        // Assert
        verify(presenter).presentError("Cart is empty");
        verify(orderRepo, never()).save(any());
    }

    @Test
    @DisplayName("Kịch bản 2: UserId null hoặc rỗng")
    void shouldPresentError_WhenUserIdIsInvalid() {
        // Test với userId null
        CreateOrderInput inputNull = new CreateOrderInput(null, 10);
        useCase.execute(inputNull, presenter);
        verify(presenter).presentError("UserId required");

        // Test với userId rỗng
        reset(presenter);
        CreateOrderInput inputBlank = new CreateOrderInput("", 10);
        useCase.execute(inputBlank, presenter);
        verify(presenter).presentError("UserId required");
    }

    @Test
    @DisplayName("Kịch bản 2: Input null")
    void shouldPresentError_WhenInputIsNull() {
        useCase.execute(null, presenter);
        verify(presenter).presentError("Input missing");
        verify(orderRepo, never()).save(any());
    }
}