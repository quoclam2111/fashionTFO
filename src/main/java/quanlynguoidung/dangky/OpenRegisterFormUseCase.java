package quanlynguoidung.dangky;

import quanlynguoidung.*;

public class OpenRegisterFormUseCase extends QuanLyNguoiDungControl {
    
    public OpenRegisterFormUseCase(QuanLyNguoiDungOutputBoundary presenter) {
        super(presenter);
        this.response = new ResponseDataOpenRegisterForm();
    }
    
    @Override
    protected void execute(QuanLyNguoiDungRequestData request) {
        try {
            // Use case này chỉ chuẩn bị dữ liệu để hiển thị form
            ResponseDataOpenRegisterForm openFormResponse = (ResponseDataOpenRegisterForm) response;
            
            openFormResponse.success = true;
            openFormResponse.message = "Form đăng ký đã sẵn sàng";
            openFormResponse.formTitle = "Đăng ký tài khoản";
            
        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi khi mở form: " + ex.getMessage();
        }
    }
}