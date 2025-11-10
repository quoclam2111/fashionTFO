package quanlydonhang;

import java.util.Date;
import java.util.List;
import repository.DTO.OrderDTO;

/**
 * Response Data chung cho tất cả use case
 */
public class QuanLyDonHangResponseData {
    public boolean success;
    public String message;
    public Date timestamp;

    // Data cho "get" action
    public OrderDTO order;

    // Data cho "list" action
    public List<OrderDTO> orders;
    public int totalCount;
    public int filteredCount;
}