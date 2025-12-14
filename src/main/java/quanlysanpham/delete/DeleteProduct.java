package quanlysanpham.delete;

import quanlysanpham.Product;

import java.math.BigDecimal;
import java.util.Date;

public class DeleteProduct extends Product {
    public DeleteProduct() {
    }

    public DeleteProduct(String productId) {
        super(productId, null, null, null, null, null, null, null, null, 0, null, null, null);
    }

    @Override
    public void validate() {
        checkId(this.productId);
    }

    private void checkId(String id) {
        if(id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng chọn Sản Phẩm để xóa!");
    }


}
