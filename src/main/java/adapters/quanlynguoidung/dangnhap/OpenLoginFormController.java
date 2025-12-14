package adapters.quanlynguoidung.dangnhap;

import quanlynguoidung.QuanLyNguoiDungInputBoundary;
import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.dangnhap.OpenLoginFormUseCase;

public class OpenLoginFormController implements QuanLyNguoiDungInputBoundary {
    private final OpenLoginFormUseCase control;
    
    public OpenLoginFormController(OpenLoginFormUseCase control) {
        this.control = control;
    }
    
    public void openForm() {
        QuanLyNguoiDungRequestData req = new QuanLyNguoiDungRequestData();
        execute(req);
    }
    
    @Override
    public void execute(QuanLyNguoiDungRequestData req) {
        control.control(req);
    }
}
