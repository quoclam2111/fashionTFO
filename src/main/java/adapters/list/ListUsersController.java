package adapters.list;

import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.list.ListUsersUseCase;

public class ListUsersController {
    private final ListUsersUseCase control;

    public ListUsersController(ListUsersUseCase control) {
        this.control = control;
    }

    public void execute(ListUsersInputDTO dto) {
        QuanLyNguoiDungRequestData req = new QuanLyNguoiDungRequestData();
        req.statusFilter = dto.statusFilter != null ? dto.statusFilter : "all";
        req.sortBy = dto.sortBy != null ? dto.sortBy : "fullName";
        req.ascending = dto.ascending;

        control.control(req);
    }
}