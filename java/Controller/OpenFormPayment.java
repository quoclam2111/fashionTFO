package Controller;

import javax.swing.JOptionPane;

import model.OrderDetail;


public class OpenFormPayment {
    public static void forwardToPayment(OrderDetail orderDetail, String method) {
        // Dựa vào method để mở form tương ứng
        if ("cod".equalsIgnoreCase(method)) {
            // Gọi form COD
            JOptionPane.showMessageDialog(null, "Xử lý thanh toán COD cho đơn: " + orderDetail.getOrderId());
        } else if ("bankingqr".equalsIgnoreCase(method)) {
            // Gọi form hiển thị mã QR ngân hàng
            new view.InfoQRForm(orderDetail).setVisible(true);
        }
    }
}