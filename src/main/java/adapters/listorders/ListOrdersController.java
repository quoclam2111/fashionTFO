package adapters.listorders;

import quanlydonhang.QuanLyDonHangRequestData;
import quanlydonhang.list.ListOrdersUseCase;

public class ListOrdersController {
    private final ListOrdersUseCase control;

    public ListOrdersController(ListOrdersUseCase control) {
        this.control = control;
    }

    public void execute(ListOrdersInputDTO dto) {
        QuanLyDonHangRequestData req = new QuanLyDonHangRequestData();
        req.statusFilter = dto.statusFilter != null ? dto.statusFilter : "all";
        req.sortBy = dto.sortBy != null ? dto.sortBy : "orderDate";
        req.ascending = dto.ascending;
        req.userIdFilter = dto.userIdFilter;

        control.control(req);
    }
}
