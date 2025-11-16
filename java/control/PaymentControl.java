package control;

import DAO.PaymentDAO;
import entity.Payment;
import entity.COD;
import entity.BankingQR;
import model.OrderDetail;
import payment.strategy.PaymentStrategy;
import payment.strategy.CodPayment;
import payment.strategy.BankingQrPayment;
import view.interfaces.InputInterface;
import view.interfaces.OutputInterface;

public class PaymentControl implements InputInterface {

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final OutputInterface output;

    public PaymentControl(OutputInterface output) {
        this.output = output;
    }

    public void handlePayment(OrderDetail orderDetail, Payment payment, PaymentStrategy strategy) {
        try {
            double tongTien = payment.tinhTongTien();
            orderDetail.setTotalAmount(tongTien);
            strategy.pay(orderDetail);
            paymentDAO.saveOrder(orderDetail);
            output.output(orderDetail);

        } catch (Exception e) {
            System.err.println("Lỗi khi xử lý thanh toán: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void input(OrderDetail orderDetail, String paymentMethod) {
        try {

            orderDetail.setPaymentMethod(paymentMethod);
            Payment payment;
            PaymentStrategy strategy;
            switch (paymentMethod.toUpperCase()) {
                case "COD":
                    payment = new COD(orderDetail);
                    strategy = new CodPayment();
                    break;
                case "BANKINGQR":
                    payment = new BankingQR(orderDetail);
                    strategy = new BankingQrPayment();
                    break;
                default:
                    throw new IllegalArgumentException(
                        "Phương thức thanh toán không hợp lệ: " + paymentMethod
                    );
            }
            handlePayment(orderDetail, payment, strategy);

        } catch (Exception e) {
            System.err.println("Lỗi trong InputInterface: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
