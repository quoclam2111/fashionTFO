package adapters.payment;

import repository.DTO.OrderDetailDTO;
//import payment.strategy.CodPayment;


import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

import Pay.CreateQRForm;

public class PaymentController {
    private final Map<String, CreateQRForm> strategies = new HashMap<>();

//    public PaymentController() {
//        strategies.put("cod", new CodPayment());
//        strategies.put("bankingqr", new BankingQrPayment());
//    }

    public void processOrder(OrderDetailDTO orderDetail) {
        if (orderDetail == null) {
            JOptionPane.showMessageDialog(null, " Không có đơn hàng để xử lý!");
            return;
        }

        String method = orderDetail.getPaymentMethod().toLowerCase();
        CreateQRForm strategy = strategies.get(method);

        if (strategy != null) {
            strategy.pay(orderDetail);
        } else {
            JOptionPane.showMessageDialog(null, " Phương thức thanh toán không hợp lệ: " + method);
        }
    }
}
