package frameworks;

import Pay.CreateQRForm;
import repository.DTO.OrderDetailDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLEncoder;
import javax.imageio.ImageIO;

public class InfoQRFormGUI implements CreateQRForm {

    @Override
    public void pay(OrderDetailDTO orderDetail) {
        JFrame frame = new JFrame("Thanh toán qua Banking QR");
        frame.setSize(400, 540);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.add(mainPanel);

        JLabel title = new JLabel("Quét mã để thanh toán", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(title, BorderLayout.NORTH);

        try {
            String bank = "STB";
            String account = "070133379612";
            String name = "TRAN QUOC AN";
            double amount = orderDetail.getTotalAmount();
            String info = "Thanh toán đơn " + orderDetail.getOrderId();

            String qrUrl = String.format(
                "https://img.vietqr.io/image/%s-%s-compact2.jpg?amount=%.0f&addInfo=%s&accountName=%s",
                bank, account, amount,
                URLEncoder.encode(info, "UTF-8"),
                URLEncoder.encode(name, "UTF-8")
            );

            BufferedImage originalImage = ImageIO.read(new URL(qrUrl));
            Image scaledImage = originalImage.getScaledInstance(
                    originalImage.getWidth()/2,
                    originalImage.getHeight()/2,
                    Image.SCALE_SMOOTH
            );

            JLabel qrLabel = new JLabel(new ImageIcon(scaledImage));
            qrLabel.setHorizontalAlignment(JLabel.CENTER);
            qrLabel.setVerticalAlignment(JLabel.CENTER);

            JPanel qrPanel = new JPanel(new GridBagLayout());
            qrPanel.setBackground(Color.WHITE);
            qrPanel.add(qrLabel);
            mainPanel.add(qrPanel, BorderLayout.CENTER);

            JLabel infoLabel = new JLabel(
                "<html><center>" +
                "Ngân hàng: <b>" + bank + "</b><br>" +
                "Số tài khoản: <b>" + account + "</b><br>" +
                "Chủ tài khoản: <b>" + name + "</b><br>" +
                "Số tiền: <b>" + String.format("%.0f", amount) + " VND</b>" +
                "</center></html>",
                JLabel.CENTER
            );
            infoLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            mainPanel.add(infoLabel, BorderLayout.SOUTH);

            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
