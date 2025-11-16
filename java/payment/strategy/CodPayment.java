package payment.strategy;


import model.OrderDetail;


public class CodPayment implements PaymentStrategy {
    @Override
    public void pay(OrderDetail orderDetail) {
        System.out.println("Thanh toán khi nhận hàng (COD).");
    }
}