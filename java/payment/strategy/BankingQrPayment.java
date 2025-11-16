package payment.strategy;

import model.OrderDetail;
import view.InfoQRForm;

public class BankingQrPayment implements PaymentStrategy {
    @Override
    public void pay(OrderDetail orderDetail) {
        new InfoQRForm(orderDetail).setVisible(true);
    }
}