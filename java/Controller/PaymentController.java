package Controller;

import model.OrderDetail;
import payment.strategy.PaymentStrategy;
import payment.strategy.CodPayment;
import payment.strategy.BankingQrPayment;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class PaymentController {
    private final Map<String, PaymentStrategy> strategies = new HashMap<>();

    public PaymentController() {
        strategies.put("cod", new CodPayment());
        strategies.put("bankingqr", new BankingQrPayment());
    }

    public void processOrder(OrderDetail orderDetail) {
        if (orderDetail == null) {
            JOptionPane.showMessageDialog(null, " Không có đơn hàng để xử lý!");
            return;
        }

        String method = orderDetail.getPaymentMethod().toLowerCase();
        PaymentStrategy strategy = strategies.get(method);

        if (strategy != null) {
            strategy.pay(orderDetail);
        } else {
            JOptionPane.showMessageDialog(null, " Phương thức thanh toán không hợp lệ: " + method);
        }
    }
}
