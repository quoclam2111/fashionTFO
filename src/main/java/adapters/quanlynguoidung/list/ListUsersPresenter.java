package adapters.quanlynguoidung.list;

import java.text.SimpleDateFormat;
import java.util.Date;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung.list.ResponseDataListUsers;

public class ListUsersPresenter implements QuanLyNguoiDungOutputBoundary {
    private ListUsersViewModel model;

    public ListUsersPresenter(ListUsersViewModel model) {
        this.model = model;
    }

    @Override
    public void present(QuanLyNguoiDungResponseData res) {
        ResponseDataListUsers listRes = (ResponseDataListUsers) res;

        model.message = listRes.message;
        model.success = listRes.success;
        model.timestamp = converter(res.timestamp);
        model.users = listRes.users;
        model.totalCount = listRes.totalCount;
        model.filteredCount = listRes.filteredCount;
    }

    private String converter(Date timestamp) {
        SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return converter.format(timestamp);
    }
}