package payment.processpayment;

import repository.payment.PaymentGateway;
import entity.Order;
import entity.PaymentMethod;
import repository.payment.OrderPaymentRepository;

import java.util.Optional;
import java.util.UUID;

public class ProcessPaymentUseCase {
    private final OrderPaymentRepository orderRepo;
    private final PaymentGateway gateway;
    private final ProcessPaymentPresenter presenter;

    // FIX: Thêm presenter vào constructor
    public ProcessPaymentUseCase(
        OrderPaymentRepository orderRepo, 
        PaymentGateway gateway,
        ProcessPaymentPresenter presenter
    ) {
        this.orderRepo = orderRepo;
        this.gateway = gateway;
        this.presenter = presenter;
    }

    // FIX: Bỏ presenter khỏi tham số method
    public void execute(ProcessPaymentInput input) {
        if (input == null || input.orderId == null) { 
            presenter.presentError("orderId required"); 
            return; 
        }
        
        UUID id;
        try { 
            id = UUID.fromString(input.orderId); 
        } catch (Exception ex) { 
            presenter.presentError("orderId invalid"); 
            return; 
        }
        
        Optional<Order> maybe = orderRepo.findById(id);
        if (!maybe.isPresent()) { 
            presenter.presentError("Order not found"); 
            return; 
        }
        
        Order order = maybe.get();
        PaymentMethod pm = order.getPaymentMethod();
        
        if (pm == null) { 
            presenter.presentError("Payment method not selected"); 
            return; 
        }
        
        if (pm.isCOD()) {
            // no gateway — simply success
            presenter.present(new ProcessPaymentOutputModel(
                order.getId().toString(), 
                true, 
                "COD selected. Order will be paid on delivery", 
                null
            ));
            return;
        }
        
        // Banking/QR: call gateway
        try {
            String qr = gateway.requestPayment(order.getId().toString(), order.getFinalAmount());
            presenter.present(new ProcessPaymentOutputModel(
                order.getId().toString(), 
                true, 
                "Payment initiated", 
                qr
            ));
        } catch (Exception ex) {
            presenter.presentError("Payment gateway failed: " + ex.getMessage());
        }
    }
}