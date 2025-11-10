package quanlydonhang;

public abstract class QuanLyDonHangControl {
    protected QuanLyDonHangOutputBoundary out;
    protected QuanLyDonHangResponseData response;

    public QuanLyDonHangResponseData getRes() {
        return response;
    }

    protected QuanLyDonHangControl(QuanLyDonHangOutputBoundary out) {
        this.out = out;
    }

    protected abstract void execute(QuanLyDonHangRequestData req);

    /**
     * Template Method - định nghĩa luồng chung
     * GIỐNG: control() trong QuanLyNguoiDungControl
     */
    public void control(QuanLyDonHangRequestData req) {
        response.timestamp = Order.getCurrentTimestamp();
        execute(req);
        present();  // ⭐ Luôn gọi present() để cập nhật ViewModel
    }

    private void present() {
        if (out != null) {
            out.present(response);
        }
    }
}