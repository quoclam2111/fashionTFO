package Pay;

import repository.DTO.OrderDetailDTO;
import repository.jdbc.PaymentDAO;
import frameworks.InfoQRFormGUI;
import Pay.entity.Payment;

public class PaymentControl implements PaymentInputBoundary {

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final PaymentOutputBoundary output;

    public PaymentControl(PaymentOutputBoundary output) {
        this.output = output;
    }

    @Override
    public void input(OrderDetailDTO orderDetail, String paymentMethod) {
        try {
            // 1. Set payment method vào orderDetail
            orderDetail.setPaymentMethod(paymentMethod);

            // 2. Tính tổng tiền - Payment tự xử lý dựa vào paymentMethod
            double tongTien = Payment.tinhTongTien(orderDetail);
            orderDetail.setTotalAmount(tongTien);

            // 3. Hiển thị GUI (nếu cần)
            CreateQRForm strategy = new InfoQRFormGUI();
            strategy.pay(orderDetail);

            // 4. Lưu DB
            paymentDAO.saveOrder(orderDetail);

            // 5. Output
            output.output(orderDetail);

        } catch (IllegalArgumentException e) {
            // Payment.tinhTongTien() sẽ throw exception nếu paymentMethod không hợp lệ
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public void handlePayment(OrderDetailDTO orderDetail, Payment payment, CreateQRForm strategy) {
        try {
            // Tính tổng tiền
            double tongTien = payment.tinhTongTien();
            orderDetail.setTotalAmount(tongTien);

            // Hiển thị GUI
            strategy.pay(orderDetail);

            // Lưu DB
            paymentDAO.saveOrder(orderDetail);

            // Output
            output.output(orderDetail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
