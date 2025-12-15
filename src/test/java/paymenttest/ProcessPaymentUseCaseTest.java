package paymenttest;

import repository.payment.PaymentGateway;
import entity.Order;
import entity.OrderItem;
import entity.PaymentMethod;
import payment.processpayment.ProcessPaymentInput;
import payment.processpayment.ProcessPaymentOutputModel;
import payment.processpayment.ProcessPaymentPresenter;
import payment.processpayment.ProcessPaymentUseCase;
import payment.provideshipping.ShippingFeeCalculator;
import repository.payment.OrderPaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC4: Thanh toán")
class ProcessPaymentUseCaseTest {

    private FakeOrderPaymentRepository orderRepo;
    private FakePaymentGateway gateway;
    private TestProcessPaymentPresenter presenter;
    private ProcessPaymentUseCase useCase;

    @BeforeEach
    void setUp() {
        orderRepo = new FakeOrderPaymentRepository();
        gateway = new FakePaymentGateway();
        presenter = new TestProcessPaymentPresenter();
        useCase = new ProcessPaymentUseCase(orderRepo, gateway, presenter);
    }

    private Order createTestOrder(UUID orderId) {
        List<OrderItem> items = Arrays.asList(
            new OrderItem("p1", "Product", 1, 100000)
        );

        Order order = new Order(orderId, items);
        order.calculateItemTotal();
        order.calculateShippingFee(
            new ShippingFeeCalculator(25000, 0),
            10
        );
        order.calculateFinalAmount();
        return order;
    }

    @Test
    @DisplayName("Kịch bản 1: Thanh toán COD thành công - không gọi gateway")
    void shouldProcessCODPayment_WithoutCallingGateway() {
        UUID orderId = UUID.randomUUID();
        ProcessPaymentInput input = new ProcessPaymentInput(orderId.toString());
        Order order = createTestOrder(orderId);
        order.setPaymentMethod(new PaymentMethod("COD"));
        orderRepo.addOrder(orderId, order);

        useCase.execute(input);

        ProcessPaymentOutputModel output = presenter.getLastOutput();
        assertNotNull(output);
        assertTrue(output.needGatewayRedirect);
        assertNull(output.gatewayInfo);
        assertTrue(output.message.contains("COD"));
        assertEquals(orderId.toString(), output.orderId);

        assertFalse(gateway.wasRequestPaymentCalled());
    }

    @Test
    @DisplayName("Kịch bản 2: Thanh toán BANKING_QR - gọi payment gateway")
    void shouldProcessBankingPayment_WithGatewayCall() {
        UUID orderId = UUID.randomUUID();
        ProcessPaymentInput input = new ProcessPaymentInput(orderId.toString());
        Order order = createTestOrder(orderId);
        order.setPaymentMethod(new PaymentMethod("BANKING_QR"));
        orderRepo.addOrder(orderId, order);
        gateway.setReturnValue("QR_CODE_STRING_12345");

        useCase.execute(input);

        assertTrue(gateway.wasRequestPaymentCalled());
        assertEquals(orderId.toString(), gateway.getLastOrderId());
        assertEquals(order.getFinalAmount(), gateway.getLastAmount());

        ProcessPaymentOutputModel output = presenter.getLastOutput();
        assertNotNull(output);
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
        orderRepo.addOrder(orderId, order);
        gateway.setShouldFail(true);

        useCase.execute(input);

        String error = presenter.getLastError();
        assertNotNull(error);
        assertTrue(error.contains("Payment gateway failed"));
    }

    @Test
    @DisplayName("Kịch bản 3: OrderId null")
    void shouldPresentError_WhenOrderIdIsNull() {
        ProcessPaymentInput input = new ProcessPaymentInput(null);
        useCase.execute(input);
        assertEquals("orderId required", presenter.getLastError());
    }

    @Test
    @DisplayName("Kịch bản 4: OrderId format không hợp lệ")
    void shouldPresentError_WhenOrderIdFormatInvalid() {
        ProcessPaymentInput input = new ProcessPaymentInput("invalid-uuid");
        useCase.execute(input);
        assertEquals("orderId invalid", presenter.getLastError());
    }

    @Test
    @DisplayName("Kịch bản 5: Order không tồn tại")
    void shouldPresentError_WhenOrderNotFound() {
        UUID orderId = UUID.randomUUID();
        ProcessPaymentInput input = new ProcessPaymentInput(orderId.toString());

        useCase.execute(input);

        assertEquals("Order not found", presenter.getLastError());
    }

    @Test
    @DisplayName("Kịch bản 6: Chưa chọn phương thức thanh toán")
    void shouldPresentError_WhenPaymentMethodNotSelected() {
        UUID orderId = UUID.randomUUID();
        ProcessPaymentInput input = new ProcessPaymentInput(orderId.toString());
        Order order = createTestOrder(orderId);
        orderRepo.addOrder(orderId, order);

        useCase.execute(input);

        assertEquals("Payment method not selected", presenter.getLastError());
    }

    // Fake OrderPaymentRepository
    static class FakeOrderPaymentRepository implements OrderPaymentRepository {
        private java.util.Map<UUID, Order> orders = new java.util.HashMap<>();

        void addOrder(UUID id, Order order) {
            orders.put(id, order);
        }

        @Override
        public void save(Order order) {
            orders.put(order.getId(), order);
        }

        @Override
        public Optional<Order> findById(UUID id) {
            return Optional.ofNullable(orders.get(id));
        }
    }

    // Fake PaymentGateway
    static class FakePaymentGateway implements PaymentGateway {
        private boolean requestPaymentCalled = false;
        private String lastOrderId;
        private int lastAmount;
        private String returnValue;
        private boolean shouldFail = false;

        void setReturnValue(String value) {
            this.returnValue = value;
        }

        void setShouldFail(boolean shouldFail) {
            this.shouldFail = shouldFail;
        }

        @Override
        public String requestPayment(String orderId, int amount) {
            if (shouldFail) {
                throw new RuntimeException("Gateway connection failed");
            }
            this.requestPaymentCalled = true;
            this.lastOrderId = orderId;
            this.lastAmount = amount;
            return returnValue;
        }

        boolean wasRequestPaymentCalled() {
            return requestPaymentCalled;
        }

        String getLastOrderId() {
            return lastOrderId;
        }

        int getLastAmount() {
            return lastAmount;
        }
    }

    // Test Presenter
    static class TestProcessPaymentPresenter implements ProcessPaymentPresenter {
        private ProcessPaymentOutputModel lastOutput;
        private String lastError;

        @Override
        public void present(ProcessPaymentOutputModel output) {
            this.lastOutput = output;
        }

        @Override
        public void presentError(String error) {
            this.lastError = error;
        }

        ProcessPaymentOutputModel getLastOutput() {
            return lastOutput;
        }

        String getLastError() {
            return lastError;
        }
    }
}