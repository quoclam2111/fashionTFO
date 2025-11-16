package adapters.payment;

import javax.swing.JOptionPane;

import repository.DTO.OrderDetailDTO;


public class OpenFormPaymentController {
    public static void forwardToPayment(OrderDetailDTO orderDetail, String method) {
        if ("cod".equalsIgnoreCase(method)) {
            JOptionPane.showMessageDialog(null, "Xử lý thanh toán COD cho đơn: " + orderDetail.getOrderId());
        } else if ("bankingqr".equalsIgnoreCase(method)) {
//            new frameworks.InfoQRFormGUI(orderDetail).setVisible(true);
        }
    }
}