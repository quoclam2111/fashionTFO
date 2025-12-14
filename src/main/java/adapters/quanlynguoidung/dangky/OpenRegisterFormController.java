package adapters.quanlynguoidung.dangky;

import quanlynguoidung.QuanLyNguoiDungInputBoundary;
import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.dangky.OpenRegisterFormUseCase;

public class OpenRegisterFormController implements QuanLyNguoiDungInputBoundary {
    private final OpenRegisterFormUseCase control;
    
    public OpenRegisterFormController(OpenRegisterFormUseCase control) {
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