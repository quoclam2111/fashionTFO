package quanlysanpham;

import repository.DTO.ProductDTO;

import java.util.Date;
import java.util.List;


public class ProductResponseData {
    public boolean success;
    public String message;
    public Date timestamp;
    
    public List<ProductDTO> products;
    public int totalCount;
    public int filteredCount;
}
