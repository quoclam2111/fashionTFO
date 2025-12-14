package quanlynguoidung.dangnhap;

import quanlynguoidung.*;

public class OpenLoginFormUseCase extends QuanLyNguoiDungControl {
    
    public OpenLoginFormUseCase(QuanLyNguoiDungOutputBoundary presenter) {
        super(presenter);
        this.response = new ResponseDataOpenLoginForm();
    }
    
    @Override
    protected void execute(QuanLyNguoiDungRequestData request) {
        try {
            // Use case này chỉ chuẩn bị dữ liệu để hiển thị form login
            ResponseDataOpenLoginForm openFormResponse = (ResponseDataOpenLoginForm) response;
            
            openFormResponse.success = true;
            openFormResponse.message = "Form đăng nhập đã sẵn sàng";
            openFormResponse.formTitle = "Đăng nhập hệ thống";
            openFormResponse.welcomeMessage = "Chào mừng bạn đến với Fashion Store";
            
        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi khi mở form: " + ex.getMessage();
        }
    }
}
