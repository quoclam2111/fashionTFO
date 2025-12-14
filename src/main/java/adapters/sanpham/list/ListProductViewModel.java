package adapters.sanpham.list;

import quanlysanpham.list.ProductViewItem;


import java.util.List;

public class ListProductViewModel extends ListProductPublisher {
    public String message;
    public String timestamp;
    public boolean success;
    public List<ProductViewItem> products;
    public int totalCount;
    public int filteredCount;
}