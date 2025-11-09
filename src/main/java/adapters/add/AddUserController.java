package adapters.add;

import quanlynguoidung.QuanLyNguoiDungRequestData;

import quanlynguoidung.them.AddUserUseCase;

public class AddUserController {
    private final AddUserUseCase control;

    public AddUserController(AddUserUseCase control) {
        this.control = control;
    }

    public void execute(AddUserInputDTO dto) {
        // Convert GUI DTO → Request Data
        QuanLyNguoiDungRequestData req = new QuanLyNguoiDungRequestData();
        req.username = dto.username;
        req.password = dto.password;
        req.fullName = dto.fullName;
        req.email = dto.email;
        req.phone = dto.phone;
        req.address = dto.address;
        
        // Gọi control (Template Method)
        control.control(req);

    }
}