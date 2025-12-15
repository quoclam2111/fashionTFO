package paymenttest;

import pay.entity.Order;
import pay.entity.OrderItem;
import pay.entity.PaymentMethod;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import payment.provideshipping.ShippingFeeCalculator;
import payment.selectpayment.SelectPaymentInput;
import payment.selectpayment.SelectPaymentOutputModel;
import payment.selectpayment.SelectPaymentPresenter;
import payment.selectpayment.SelectPaymentUseCase;
import repository.DTO.ResultPaymentDTO;
import repository.payment.OrderPaymentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UC3: Chọn phương thức thanh toán")
class SelectPaymentUseCaseTest {

    @Mock private OrderPaymentRepository orderRepo;
    @Mock private SelectPaymentPresenter presenter;

    private SelectPaymentUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SelectPaymentUseCase(orderRepo);
    }

    private Order createTestOrderWithTotals(UUID orderId) {
        List<OrderItem> items = Arrays.asList(
            new OrderItem("p1", "Product", 2, 100000)
        );
        Order order = new Order(orderId, items);
        order.calculateItemTotal(); // 200000
        // Giả sử shipping fee = 25000
        return order;
    }

    // Kịch bản 1: User chọn phương thức thanh toán "COD"
    @Test
    @DisplayName("Kịch bản 1: Chọn COD thành công - không tính VAT")
    void shouldSelectCOD_WithoutVAT() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        SelectPaymentInput input = new SelectPaymentInput(
            orderId.toString(), "COD", 10
        );
        Order order = createTestOrderWithTotals(orderId);
        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        useCase.execute(input, presenter);

        // Assert
        // 2: PM xác nhận Phương thức thanh toán
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        
        assertNotNull(savedOrder.getPaymentMethod());
        assertTrue(savedOrder.getPaymentMethod().isCOD());
        assertEquals(0, savedOrder.getVat()); // COD không có VAT

        // 3: Phương thức thanh toán là "COD"
        ArgumentCaptor<SelectPaymentOutputModel> outputCaptor = 
            ArgumentCaptor.forClass(SelectPaymentOutputModel.class);
        verify(presenter).present(outputCaptor.capture());
        SelectPaymentOutputModel output = outputCaptor.getValue();
        
        assertEquals("COD", output.paymentMethod);
        assertEquals(0, output.vat);
    }

    // Kịch bản 2: User chọn phương thức thanh toán "BANKING_QR"
    @Test
    @DisplayName("Kịch bản 2: Chọn BANKING_QR - tính VAT 5%")
    void shouldSelectBankingQR_WithVAT() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        SelectPaymentInput input = new SelectPaymentInput(
            orderId.toString(), "BANKING_QR", 10
        );

        // Tạo order và SPY để control dữ liệu
        Order order = spy(createTestOrderWithTotals(orderId));

        // ÉP shipping fee cho test = 25_000
        doReturn(25000).when(order).getShippingFee();

        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        useCase.execute(input, presenter);

        // Assert: Order được lưu
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();

        assertNotNull(savedOrder.getPaymentMethod());
        assertTrue(savedOrder.getPaymentMethod().isBanking());

        // VAT = (itemTotal + shippingFee) * 5%
        int expectedVAT = (int) Math.round((200000 + 25000) * 0.05);
        assertEquals(expectedVAT, savedOrder.getVat());

        // Assert output
        ArgumentCaptor<SelectPaymentOutputModel> outputCaptor =
            ArgumentCaptor.forClass(SelectPaymentOutputModel.class);
        verify(presenter).present(outputCaptor.capture());
        SelectPaymentOutputModel output = outputCaptor.getValue();

        assertEquals("BANKING_QR", output.paymentMethod);
        assertEquals(expectedVAT, output.vat);

        // Tổng tiền cuối
        assertEquals(200000 + 25000 + expectedVAT, output.finalAmount);
    }

    @Test
    @DisplayName("Kịch bản 2: Extension - User nhập phương thức thanh toán không hợp lệ")
    void shouldPresentError_WhenPaymentMethodInvalid() {
        UUID orderId = UUID.randomUUID();
        SelectPaymentInput input = new SelectPaymentInput(
            orderId.toString(), "INVALID_METHOD", 10
        );
        Order order = createTestOrderWithTotals(orderId);
        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));

        useCase.execute(input, presenter);

        verify(presenter).presentError("Invalid payment method");
        verify(orderRepo, never()).save(any());
    }

    @Test
    @DisplayName("Kịch bản 3: Phương thức thanh toán rỗng")
    void shouldPresentValidationError_WhenMethodIsBlank() {
        UUID orderId = UUID.randomUUID();
        SelectPaymentInput input = new SelectPaymentInput(
            orderId.toString(), "", 10
        );

        useCase.execute(input, presenter);

        ArgumentCaptor<List<ResultPaymentDTO.FieldError>> captor = ArgumentCaptor.forClass(List.class);
        verify(presenter).presentValidationErrors(captor.capture());
        List<ResultPaymentDTO.FieldError> errors = captor.getValue();
        
        assertEquals(1, errors.size());
        assertEquals("method", errors.get(0).field);
        assertTrue(errors.get(0).error.contains("bắt buộc"));
    }

    @Test
    @DisplayName("Kịch bản 4: Order không tồn tại")
    void shouldPresentError_WhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        SelectPaymentInput input = new SelectPaymentInput(
            orderId.toString(), "COD", 10
        );
        when(orderRepo.findById(orderId)).thenReturn(Optional.empty());

        useCase.execute(input, presenter);

        verify(presenter).presentError("Order not found");
    }

    @Test
    @DisplayName("Kịch bản 5: Input null")
    void shouldPresentError_WhenInputIsNull() {
        useCase.execute(null, presenter);
        verify(presenter).presentError("Input missing");
    }
}