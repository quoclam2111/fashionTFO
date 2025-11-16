package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import control.PaymentControl;
import entity.COD;
import entity.BankingQR;
import entity.Payment;
import model.OrderDetail;
import model.ProductVariantDTO;
import payment.strategy.CodPayment;
import payment.strategy.BankingQrPayment;
import payment.strategy.PaymentStrategy;
import view.interfaces.OutputInterface;

public class PaymentControlTest {

    @Test
    public void testCODPayment() {
        // 1. Tạo giỏ hàng
        List<ProductVariantDTO> items = new ArrayList<>();
        items.add(new ProductVariantDTO("VAR001", "Áo", "Trắng", "L", 1000, 2));
        items.add(new ProductVariantDTO("VAR002", "Quần", "Xanh", "M", 700, 1));

        OrderDetail order = new OrderDetail("ORD001", "KH001", 0, "cod", items);

        // 2. Tạo Payment + Strategy
        Payment payment = new CODPayment(order);
        PaymentStrategy strategy = new CodPayment();

        // 3. Tạo OutputInterface giả
        final double[] capturedAmount = new double[1];
        OutputInterface output = new OutputInterface() {
            @Override
            public void output(OrderDetail orderDetail) {
                capturedAmount[0] = orderDetail.getTotalAmount();
            }
        };

        // 4. Tạo PaymentControl và thực hiện thanh toán
        PaymentControl control = new PaymentControl(output);
        control.handlePayment(order, payment, strategy);

        // 5. Kiểm tra tổng tiền
        double expectedTotal = 1000*2 + 700*1 + 30000; // COD + phí
        assertEquals(expectedTotal, capturedAmount[0], 0.01);
    }

    @Test
    public void testBankingQRPayment() {
        List<ProductVariantDTO> items = new ArrayList<>();
        items.add(new ProductVariantDTO("VAR001", "Áo", "Trắng", "L", 1000, 2));
        items.add(new ProductVariantDTO("VAR002", "Quần", "Xanh", "M", 700, 1));

        OrderDetail order = new OrderDetail("ORD002", "KH002", 0, "bankingqr", items);

        Payment payment = new BankingQRPayment(order);
        PaymentStrategy strategy = new BankingQrPayment();

        final double[] capturedAmount = new double[1];
        OutputInterface output = new OutputInterface() {
            @Override
            public void output(OrderDetail orderDetail) {
                capturedAmount[0] = orderDetail.getTotalAmount();
            }
        };

        PaymentControl control = new PaymentControl(output);
        control.handlePayment(order, payment, strategy);

        double baseTotal = 1000*2 + 700*1;
        double expectedTotal = baseTotal * 1.05; 
        assertEquals(expectedTotal, capturedAmount[0], 0.01);
    }
}
