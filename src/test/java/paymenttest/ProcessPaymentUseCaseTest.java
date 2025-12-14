package paymenttest;

import application.port.PaymentGateway;
import pay.entity.Order;
import pay.entity.OrderItem;
import pay.entity.PaymentMethod;
import payment.processpayment.ProcessPaymentInput;
import payment.processpayment.ProcessPaymentOutputModel;
import payment.processpayment.ProcessPaymentPresenter;
import payment.processpayment.ProcessPaymentUseCase;
import payment.provideshipping.ShippingFeeCalculator;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UC4: Thanh toán")
class ProcessPaymentUseCaseTest {

    @Mock private OrderPaymentRepository orderRepo;
    @Mock private PaymentGateway gateway;
    @Mock private ProcessPaymentPresenter presenter;

    private ProcessPaymentUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ProcessPaymentUseCase(orderRepo, gateway, presenter);
    }

    private Order createTestOrder(UUID orderId) {
        List<OrderItem> items = Arrays.asList(
            new OrderItem("p1", "Product", 1, 100000)
        );

        Order order = new Order(orderId, items);
        order.calculateItemTotal();

        // ✅ FIX CHUẨN: dùng constructor thật của domain
        // baseFee = 25000, perKmFee = 0  → luôn ra 25.000
        order.calculateShippingFee(
            new ShippingFeeCalculator(25000, 0),
            10
        );

        order.calculateFinalAmount();
        return order;
    }
    // Kịch bản 1: User chọn thanh toán bằng "COD"
    @Test
    @DisplayName("Kịch bản 1: Thanh toán COD thành công - không gọi gateway")
    void shouldProcessCODPayment_WithoutCallingGateway() {
        UUID orderId = UUID.randomUUID();
        ProcessPaymentInput input = new ProcessPaymentInput(orderId.toString());
        Order order = createTestOrder(orderId);
        order.setPaymentMethod(new PaymentMethod("COD"));
        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));

        useCase.execute(input);

        ArgumentCaptor<ProcessPaymentOutputModel> captor =
            ArgumentCaptor.forClass(ProcessPaymentOutputModel.class);
        verify(presenter).present(captor.capture());
        ProcessPaymentOutputModel output = captor.getValue();

        assertTrue(output.needGatewayRedirect);
        assertNull(output.gatewayInfo);
        assertTrue(output.message.contains("COD"));
        assertEquals(orderId.toString(), output.orderId);

        verify(gateway, never()).requestPayment(any(), anyInt());
    }

    // Kịch bản 2: User chọn thanh toán bằng "BANKING_QR"
    @Test
    @DisplayName("Kịch bản 2: Thanh toán BANKING_QR - gọi payment gateway")
    void shouldProcessBankingPayment_WithGatewayCall() {
        UUID orderId = UUID.randomUUID();
        ProcessPaymentInput input = new ProcessPaymentInput(orderId.toString());
        Order order = createTestOrder(orderId);
        order.setPaymentMethod(new PaymentMethod("BANKING_QR"));
        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));
        when(gateway.requestPayment(orderId.toString(), order.getFinalAmount()))
            .thenReturn("QR_CODE_STRING_12345");

        useCase.execute(input);

        verify(gateway).requestPayment(orderId.toString(), order.getFinalAmount());

        ArgumentCaptor<ProcessPaymentOutputModel> captor =
            ArgumentCaptor.forClass(ProcessPaymentOutputModel.class);
        verify(presenter).present(captor.capture());
        ProcessPaymentOutputModel output = captor.getValue();

        assertTrue(output.needGatewayRedirect);
        assertEquals("QR_CODE_STRING_12345", output.gatewayInfo);
        assertTrue(output.message.contains("Payment initiated"));
        assertEquals(orderId.toString(), output.orderId);
    }

    @Test
    @DisplayName("Kịch bản 2: Extension - Payment gateway thất bại")
    void shouldPresentError_WhenGatewayFails() {
        UUID orderId = UUID.randomUUID();
        ProcessPaymentInput input = new ProcessPaymentInput(orderId.toString());
        Order order = createTestOrder(orderId);
        order.setPaymentMethod(new PaymentMethod("BANKING_QR"));
        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));
        when(gateway.requestPayment(anyString(), anyInt()))
            .thenThrow(new RuntimeException("Gateway connection failed"));

        useCase.execute(input);

        verify(presenter).presentError(contains("Payment gateway failed"));
    }

    @Test
    @DisplayName("Kịch bản 3: OrderId null")
    void shouldPresentError_WhenOrderIdIsNull() {
        ProcessPaymentInput input = new ProcessPaymentInput(null);
        useCase.execute(input);
        verify(presenter).presentError("orderId required");
    }

    @Test
    @DisplayName("Kịch bản 4: OrderId format không hợp lệ")
    void shouldPresentError_WhenOrderIdFormatInvalid() {
        ProcessPaymentInput input = new ProcessPaymentInput("invalid-uuid");
        useCase.execute(input);
        verify(presenter).presentError("orderId invalid");
    }

    @Test
    @DisplayName("Kịch bản 5: Order không tồn tại")
    void shouldPresentError_WhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        ProcessPaymentInput input = new ProcessPaymentInput(orderId.toString());
        when(orderRepo.findById(orderId)).thenReturn(Optional.empty());

        useCase.execute(input);

        verify(presenter).presentError("Order not found");
    }

    @Test
    @DisplayName("Kịch bản 6: Chưa chọn phương thức thanh toán")
    void shouldPresentError_WhenPaymentMethodNotSelected() {
        UUID orderId = UUID.randomUUID();
        ProcessPaymentInput input = new ProcessPaymentInput(orderId.toString());
        Order order = createTestOrder(orderId);
        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));

        useCase.execute(input);

        verify(presenter).presentError("Payment method not selected");
    }
}
