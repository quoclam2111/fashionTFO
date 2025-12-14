
package adapters.quanlydonhang.addorder;

import quanlydonhang.QuanLyDonHangRequestData;
import quanlydonhang.add.AddOrderUseCase;


public class AddOrderController {
    private final AddOrderUseCase control;

    public AddOrderController(AddOrderUseCase control) {
        this.control = control;
    }

    public void execute(AddOrderInputDTO dto) {
        // Convert GUI DTO → Request Data
        QuanLyDonHangRequestData req = new QuanLyDonHangRequestData();
        req.userId = dto.userId;
        req.customerName = dto.customerName;
        req.customerPhone = dto.customerPhone;
        req.customerAddress = dto.customerAddress;
        req.totalAmount = dto.totalAmount;
        req.note = dto.note;

        // Gọi control (Template Method)
        control.control(req);
    }
}
