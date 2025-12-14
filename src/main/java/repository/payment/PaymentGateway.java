package repository.payment;

public interface PaymentGateway {
	 
    String requestPayment(String orderId, int amount);
}