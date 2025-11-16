package frameworks;

import javax.swing.*;

import adapters.payment.OpenFormPaymentController;
import repository.DTO.OrderDetailDTO;

import java.awt.*;
import java.awt.event.ActionEvent;

public class PaymentMethodFormGUI extends JFrame {
    public PaymentMethodFormGUI(OrderDetailDTO orderDetail) {
        setTitle("Chọn phương thức thanh toán");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel label = new JLabel("Vui lòng chọn phương thức thanh toán:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        add(label);

        JButton btnCOD = new JButton("Thanh toán khi nhận hàng (COD)");
        JButton btnBanking = new JButton("Thanh toán qua Banking QR");

        add(btnCOD);
        add(btnBanking);

        
        btnCOD.addActionListener((ActionEvent e) -> {
            OpenFormPaymentController.forwardToPayment(orderDetail, "cod");
            JOptionPane.showMessageDialog(this, "Đã chọn thanh toán COD!");
            dispose(); 
        });

        
        btnBanking.addActionListener((ActionEvent e) -> {
            OpenFormPaymentController.forwardToPayment(orderDetail, "bankingqr");
            JOptionPane.showMessageDialog(this, "Đã chọn thanh toán Banking QR!");
            dispose();
        });
    }
}