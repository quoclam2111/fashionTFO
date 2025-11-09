package adapters.get;

import quanlynguoidung.*;
import quanlynguoidung.get.GetUserUseCase;

public class GetUserController {
    private final GetUserUseCase useCase;

    public GetUserController(GetUserUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     * Nhận input từ View, đóng gói thành RequestData và gọi UseCase.
     */
    public void execute(GetUserInputDTO input) {
        QuanLyNguoiDungRequestData request = new QuanLyNguoiDungRequestData();
        request.searchBy = input.searchBy;
        request.searchValue = input.searchValue;

        // 👉 Gọi control() để tự động thêm timestamp + present()
        useCase.control(request);
    }
}
