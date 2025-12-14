package adapters.quanlydonhang.listorders;

import java.util.List;
import repository.DTO.OrderDTO;

public class ListOrdersViewModel extends ListOrdersPublisher {
    public String message;
    public String timestamp;
    public boolean success;
    public List<OrderDTO> orders;
    public int totalCount;
    public int filteredCount;
}