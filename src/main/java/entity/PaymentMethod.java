package pay.entity;

public class PaymentMethod {
    private final String code;

    public PaymentMethod(String code) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("PaymentMethod empty");
        if (!code.equals("COD") && !code.equals("BANKING_QR"))
            throw new IllegalArgumentException("Invalid method");
        this.code = code;
    }

    public boolean isBanking() { return "BANKING_QR".equals(code); }
    public boolean isCOD() { return "COD".equals(code); }
    public String getCode() { return code; }
}