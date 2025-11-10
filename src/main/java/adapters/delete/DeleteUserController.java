package adapters.delete;

import quanlynguoidung.*;
import quanlynguoidung.delete.DeleteUserUseCase;

public class DeleteUserController {
    private final DeleteUserUseCase useCase;

    public DeleteUserController(DeleteUserUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     * Nhận input từ View, đóng gói thành RequestData và gọi UseCase.
     */
    public void execute(DeleteUserInputDTO input) {
        QuanLyNguoiDungRequestData request = new QuanLyNguoiDungRequestData();
        request.userId = input.userId;

        // 👉 Gọi control() để tự động thêm timestamp + present()
        useCase.control(request);
    }
}