package adapters.update;

import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.update.UpdateUserUseCase;

public class UpdateUserController {
    private final UpdateUserUseCase useCase;

    public UpdateUserController(UpdateUserUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     * Nhận input từ View, đóng gói thành RequestData và gọi UseCase.
     */
    public void execute(UpdateUserInputDTO input) {
        QuanLyNguoiDungRequestData request = new QuanLyNguoiDungRequestData();
        request.userId = input.userId;
        request.fullName = input.fullName;
        request.email = input.email;
        request.phone = input.phone;
        request.address = input.address;
        request.status = input.status;
        request.password = input.password; // optional

        // 👉 Gọi control() để tự động thêm timestamp + present()
        useCase.control(request);
    }
}

