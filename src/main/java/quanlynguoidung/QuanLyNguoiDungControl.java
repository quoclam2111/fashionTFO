package quanlynguoidung;

import java.util.Date;

public abstract class QuanLyNguoiDungControl implements QuanLyNguoiDungInputBoundary {
    protected QuanLyNguoiDungOutputBoundary out;
    protected QuanLyNguoiDungResponseData response;
    
    public QuanLyNguoiDungResponseData getRes() {
        return response;
    }
    
    protected QuanLyNguoiDungControl(QuanLyNguoiDungOutputBoundary out) {
        this.out = out;
    }
    
    public abstract void execute(QuanLyNguoiDungRequestData req);
    
    /**
     * Template Method - định nghĩa luồng chung
     * GIỐNG: control() trong Tinh2SoControl
     */
    public void control(QuanLyNguoiDungRequestData req) {
        response.timestamp = User.getCurrentTimestamp();
        execute(req);
        present();  // ⭐ Luôn gọi present() để cập nhật ViewModel
    }
    
    private void present() {
        if (out != null) {
            out.present(response);
        }
    }

}
