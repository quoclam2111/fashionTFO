package adapters.quanlynguoidung.update;

import java.time.LocalDateTime;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung.update.ResponseDataUpdateUser;

public class UpdateUserPresenter implements QuanLyNguoiDungOutputBoundary {
    private final UpdateUserViewModel model;

    public UpdateUserPresenter(UpdateUserViewModel model) {
        this.model = model;
    }

    @Override
    public void present(QuanLyNguoiDungResponseData response) {
        ResponseDataUpdateUser res = (ResponseDataUpdateUser) response;

        model.success = res.success;
        model.message = res.message;
        model.timestamp = LocalDateTime.now().toString();
        model.updatedUser = res.updatedUser; // ✅ Đã là ViewItem rồi
    }

}
