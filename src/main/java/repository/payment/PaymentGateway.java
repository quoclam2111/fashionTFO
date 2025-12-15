package repository.payment;

public interface PaymentGateway {
    
    String requestPayment(String orderId, int amount);
    
    String checkPaymentStatus(String transactionId);

    String cancelPayment(String transactionId);
    
    String refundPayment(String transactionId, int amount);
}