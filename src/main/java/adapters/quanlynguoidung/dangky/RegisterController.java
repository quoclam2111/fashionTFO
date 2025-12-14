package adapters.quanlynguoidung.dangky;

import quanlynguoidung.QuanLyNguoiDungInputBoundary;
import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.dangky.RegisterUseCase;

public class RegisterController implements QuanLyNguoiDungInputBoundary {
    private final RegisterUseCase control;
    
    public RegisterController(RegisterUseCase control) {
        this.control = control;
    }
    
    // Method cho GUI layer - nhận DTO
    public void executeWithDTO(RegisterInputDTO dto) {
        QuanLyNguoiDungRequestData req = convertToRequestData(dto);
        execute(req);
    }
    
    // Method implement từ interface - nhận RequestData
    @Override
    public void execute(QuanLyNguoiDungRequestData req) {
        control.control(req);
    }
    
    // Helper method để convert
    private QuanLyNguoiDungRequestData convertToRequestData(RegisterInputDTO dto) {
        QuanLyNguoiDungRequestData req = new QuanLyNguoiDungRequestData();
        req.username = dto.username;
        req.password = dto.password;
        req.fullName = dto.fullName;
        req.email = dto.email;
        req.phone = dto.phone;
        req.address = dto.address;
        return req;
    }
}