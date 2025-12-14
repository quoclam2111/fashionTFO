package adapters.quanlynguoidung.delete;

import java.time.LocalDateTime;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung.delete.ResponseDataDeleteUser;

public class DeleteUserPresenter implements QuanLyNguoiDungOutputBoundary{
    private final DeleteUserViewModel model;

    public DeleteUserPresenter(DeleteUserViewModel model) {
        this.model = model;
    }

    @Override
    public void present(QuanLyNguoiDungResponseData response) {
        ResponseDataDeleteUser res = (ResponseDataDeleteUser) response;

        model.success = res.success;
        model.message = res.message;
        model.timestamp = LocalDateTime.now().toString();
        model.deletedUserId = res.deletedUserId;
        model.deletedUsername = res.deletedUsername;
    }
}
