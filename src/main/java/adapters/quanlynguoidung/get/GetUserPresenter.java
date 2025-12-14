package adapters.quanlynguoidung.get;

import java.time.LocalDateTime;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung.get.ResponseDataGetUser;


public class GetUserPresenter implements QuanLyNguoiDungOutputBoundary {
    private final GetUserViewModel model;

    public GetUserPresenter(GetUserViewModel model) {
        this.model = model;
    }

    @Override
    public void present(QuanLyNguoiDungResponseData response) {
        ResponseDataGetUser res = (ResponseDataGetUser) response;

        model.success = res.success;
        model.message = res.message;
        model.timestamp = LocalDateTime.now().toString();
        model.user = res.user; // user đã là UserViewItem rồi
    }
}
