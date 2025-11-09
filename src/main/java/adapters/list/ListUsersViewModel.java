package adapters.list;

import java.util.List;

import quanlynguoidung.list.UserListItem;
import repository.DTO.UserDTO;

public class ListUsersViewModel extends ListUsersPublisher {
    public String message;
    public String timestamp;
    public boolean success;
    public List<UserListItem> users;
    public int totalCount;
    public int filteredCount;
}