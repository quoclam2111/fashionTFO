package quanlysanpham;


import quanlynguoidung.User;

public abstract class ProductManageControl {
    protected ProductOutputBoundary out;
    protected ProductResponseData response;
    
    public ProductResponseData getRes() {
        return response;
    }
    
    protected ProductManageControl(ProductOutputBoundary out) {
        this.out = out;
    }
    
    protected abstract void execute(ProductRequestData req);

    public void control(ProductRequestData req) {
        response.timestamp = User.getCurrentTimestamp();
        execute(req);
        present();  // ⭐ Luôn gọi present() để cập nhật ViewModel
    }
    
    private void present() {
        if (out != null) {
            out.present(response);
        }
    }

}
