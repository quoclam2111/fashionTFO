package view;

import javax.swing.JOptionPane;

public class PaymentSuccessViewModel {
    private final String message;

    public PaymentSuccessViewModel(String message) {
        this.message = message;
    }

    public void showSuccessMessage() {
        JOptionPane.showMessageDialog(
            null,
            message,
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void destroy() {
        System.out.println("Đã huỷ ViewModel sau khi hiển thị kết quả.");
    }
}