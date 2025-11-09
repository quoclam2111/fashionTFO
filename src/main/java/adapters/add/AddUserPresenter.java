package adapters.add;

import java.text.SimpleDateFormat;
import java.util.Date;

import quanlynguoidung.QuanLyNguoiDungOutputBoundary;
import quanlynguoidung.QuanLyNguoiDungResponseData;
import quanlynguoidung.them.AddUserOutput;
import quanlynguoidung.them.AddUserOutputBoundary;
import quanlynguoidung.them.ResponseDataAddUser;

public class AddUserPresenter implements QuanLyNguoiDungOutputBoundary {
    private AddUserViewModel model;
    
    public AddUserPresenter(AddUserViewModel model) {
        this.model = model;
    }
    
    @Override
    public void present(QuanLyNguoiDungResponseData res) {
        ResponseDataAddUser resAdd = (ResponseDataAddUser) res;
        
        if (resAdd.message != null) {
            model.message = resAdd.message;
        }
        
        model.success = resAdd.success;
        
        if (resAdd.addedUserId != null) {
            model.userId = resAdd.addedUserId;
        }
        
        model.timestamp = converter(res.timestamp);
    }
    
    private String converter(Date timestamp) {
        SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return converter.format(timestamp);
    }
}
