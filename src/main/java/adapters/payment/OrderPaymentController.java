package adapters.payment;

import payment.createorder.CreateOrderInput;
import payment.createorder.CreateOrderPaymentUseCase;
import payment.createorder.CreateOrderPresenter;
import payment.processpayment.ProcessPaymentInput;
import payment.processpayment.ProcessPaymentPresenter;
import payment.processpayment.ProcessPaymentUseCase;
import payment.provideshipping.ProvideShippingPresenter;
import payment.provideshipping.ProvideShippingUseCase;
import payment.provideshipping.ShippingInfoInput;
import payment.selectpayment.SelectPaymentInput;
import payment.selectpayment.SelectPaymentPresenter;
import payment.selectpayment.SelectPaymentUseCase;

/**
 * Adapter layer between UI and UseCases.
 * The UI will create presenters and publisher and register callbacks.
 */
public class OrderPaymentController {
    private final CreateOrderPaymentUseCase createOrderUseCase;
    private final ProvideShippingUseCase provideShippingUseCase;
    private final SelectPaymentUseCase selectPaymentUseCase;
    private final ProcessPaymentUseCase processPaymentUseCase;

    public OrderPaymentController(CreateOrderPaymentUseCase c, ProvideShippingUseCase p, SelectPaymentUseCase s, ProcessPaymentUseCase pr) {
        this.createOrderUseCase = c;
        this.provideShippingUseCase = p;
        this.selectPaymentUseCase = s;
        this.processPaymentUseCase = pr;
    }

    public void createOrder(CreateOrderInput input, CreateOrderPresenter presenter) {
        createOrderUseCase.execute(input, presenter);
    }

    public void provideShipping(ShippingInfoInput input, ProvideShippingPresenter presenter) {
        provideShippingUseCase.execute(input, presenter);
    }

    public void selectPayment(SelectPaymentInput input, SelectPaymentPresenter presenter) {
        selectPaymentUseCase.execute(input, presenter);
    }

    public void processPayment(ProcessPaymentInput input, ProcessPaymentPresenter presenter) {
        processPaymentUseCase.execute(input);
    }
}