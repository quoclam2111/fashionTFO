package paymenttest;

import repository.cart.CartRepository;
import entity.Order;
import entity.OrderItem;
import payment.createorder.CreateOrderInput;
import payment.createorder.CreateOrderOutputModel;
import payment.createorder.CreateOrderPaymentUseCase;
import payment.createorder.CreateOrderPresenter;
import payment.provideshipping.ShippingFeeCalculator;
import repository.payment.OrderPaymentRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC1: Tạo hóa đơn thanh toán")
class CreateOrderUseCaseTest {

    @Test
    @DisplayName("Kịch bản 1: Tạo hóa đơn thành công - giỏ hàng có sản phẩm")
    void shouldCreateOrderSuccessfully_WhenCartHasProducts() {
        // Given
        List<OrderItem> cartItems = Arrays.asList(
            new OrderItem("p1", "Áo thun", 2, 150000),
            new OrderItem("p2", "Quần jean", 1, 350000)
        );
        
        CreateOrderInput input = new CreateOrderInput("user123", 10);
        
        // Captured results
        Order[] savedOrder = {null};
        CreateOrderOutputModel[] outputModel = {null};
        String[] errorMsg = {null};
        
        // Stub dependencies
        CartRepository cartRepo = userId -> cartItems;
        OrderPaymentRepository orderRepo = order -> savedOrder[0] = order;
        ShippingFeeCalculator shippingCalc = new ShippingFeeCalculator(25000, 0);
        CreateOrderPresenter presenter = new CreateOrderPresenter() {
            public void present(CreateOrderOutputModel output) { outputModel[0] = output; }
            public void presentError(String error) { errorMsg[0] = error; }
        };
        
        // When
        CreateOrderPaymentUseCase useCase = new CreateOrderPaymentUseCase(cartRepo, orderRepo, shippingCalc);
        useCase.execute(input, presenter);
        
        // Then
        assertNull(errorMsg[0], "Should not have error");
        assertNotNull(savedOrder[0], "Order should be saved");
        assertEquals(650000, savedOrder[0].getItemTotal());
        assertEquals(25000, savedOrder[0].getShippingFee());
        assertEquals(2, savedOrder[0].getItems().size());
        
        assertNotNull(outputModel[0], "Output model should be presented");
        assertNotNull(outputModel[0].orderId);
        assertEquals(650000, outputModel[0].itemTotal);
        assertEquals(25000, outputModel[0].shippingFee);
        assertEquals(0, outputModel[0].vat);
        assertEquals(675000, outputModel[0].finalAmount);
        assertEquals(2, outputModel[0].itemsSummary.size());
        assertTrue(outputModel[0].itemsSummary.get(0).contains("Áo thun"));
    }

    @Test
    @DisplayName("Kịch bản 1: Extension 3.1 - Chi phí giao hàng theo khoảng cách")
    void shouldCalculateShippingFeeCorrectly_BasedOnDistance() {
        // Given
        List<OrderItem> cartItems = Arrays.asList(
            new OrderItem("p1", "Product", 1, 100000)
        );
        CreateOrderInput input = new CreateOrderInput("user123", 50);
        
        CreateOrderOutputModel[] outputModel = {null};
        
        // Stub với phí ship cao hơn cho khoảng cách 50km
        CartRepository cartRepo = userId -> cartItems;
        OrderPaymentRepository orderRepo = order -> {};
        ShippingFeeCalculator shippingCalc = new ShippingFeeCalculator(25000, 1000); // 25k base + 1k/km
        CreateOrderPresenter presenter = new CreateOrderPresenter() {
            public void present(CreateOrderOutputModel output) { outputModel[0] = output; }
            public void presentError(String error) {}
        };
        
        // When
        new CreateOrderPaymentUseCase(cartRepo, orderRepo, shippingCalc).execute(input, presenter);
        
        // Then
        assertNotNull(outputModel[0]);
        assertEquals(75000, outputModel[0].shippingFee); // 25000 + 50*1000
    }

    @Test
    @DisplayName("Kịch bản 2: Giỏ hàng rỗng - User chưa thêm sản phẩm")
    void shouldPresentError_WhenCartIsEmpty() {
        // Given
        CreateOrderInput input = new CreateOrderInput("user123", 10);
        String[] errorMsg = {null};
        boolean[] orderSaved = {false};
        
        // Stub trả về giỏ hàng rỗng
        CartRepository cartRepo = userId -> Collections.emptyList();
        OrderPaymentRepository orderRepo = order -> orderSaved[0] = true;
        ShippingFeeCalculator shippingCalc = new ShippingFeeCalculator(25000, 0);
        CreateOrderPresenter presenter = new CreateOrderPresenter() {
            public void present(CreateOrderOutputModel output) {}
            public void presentError(String error) { errorMsg[0] = error; }
        };
        
        // When
        new CreateOrderPaymentUseCase(cartRepo, orderRepo, shippingCalc).execute(input, presenter);
        
        // Then
        assertEquals("Cart is empty", errorMsg[0]);
        assertFalse(orderSaved[0], "Order should not be saved");
    }

    @Test
    @DisplayName("Kịch bản 2: UserId null hoặc rỗng")
    void shouldPresentError_WhenUserIdIsInvalid() {
        // Given
        String[] errorMsg = {null};
        CartRepository cartRepo = userId -> Collections.emptyList();
        OrderPaymentRepository orderRepo = order -> {};
        ShippingFeeCalculator shippingCalc = new ShippingFeeCalculator(0, 0);
        CreateOrderPresenter presenter = new CreateOrderPresenter() {
            public void present(CreateOrderOutputModel output) {}
            public void presentError(String error) { errorMsg[0] = error; }
        };
        CreateOrderPaymentUseCase useCase = new CreateOrderPaymentUseCase(cartRepo, orderRepo, shippingCalc);
        
        // When - Test với userId null
        useCase.execute(new CreateOrderInput(null, 10), presenter);
        
        // Then
        assertEquals("UserId required", errorMsg[0]);
        
        // When - Test với userId rỗng
        errorMsg[0] = null;
        useCase.execute(new CreateOrderInput("", 10), presenter);
        
        // Then
        assertEquals("UserId required", errorMsg[0]);
    }

    @Test
    @DisplayName("Kịch bản 2: Input null")
    void shouldPresentError_WhenInputIsNull() {
        // Given
        String[] errorMsg = {null};
        boolean[] orderSaved = {false};
        
        CartRepository cartRepo = userId -> Collections.emptyList();
        OrderPaymentRepository orderRepo = order -> orderSaved[0] = true;
        ShippingFeeCalculator shippingCalc = new ShippingFeeCalculator(0, 0);
        CreateOrderPresenter presenter = new CreateOrderPresenter() {
            public void present(CreateOrderOutputModel output) {}
            public void presentError(String error) { errorMsg[0] = error; }
        };
        
        // When
        new CreateOrderPaymentUseCase(cartRepo, orderRepo, shippingCalc).execute(null, presenter);
        
        // Then
        assertEquals("Input missing", errorMsg[0]);
        assertFalse(orderSaved[0]);
    }
}