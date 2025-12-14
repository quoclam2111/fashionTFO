package adapters.quanlynguoidung.dangnhap;

import quanlynguoidung.QuanLyNguoiDungInputBoundary;
import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.dangnhap.LoginUseCase;

public class LoginController implements QuanLyNguoiDungInputBoundary {
    private final LoginUseCase control;
    
    public LoginController(LoginUseCase control) {
        this.control = control;
    }
    
    public void executeWithDTO(LoginInputDTO dto) {
        QuanLyNguoiDungRequestData req = convertToRequestData(dto);
        execute(req);
    }
    
    @Override
    public void execute(QuanLyNguoiDungRequestData req) {
        control.control(req);
    }
    
    private QuanLyNguoiDungRequestData convertToRequestData(LoginInputDTO dto) {
        QuanLyNguoiDungRequestData req = new QuanLyNguoiDungRequestData();
        req.username = dto.username;
        req.password = dto.password;
        return req;
    }
}