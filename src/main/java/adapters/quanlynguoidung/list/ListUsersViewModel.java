package adapters.quanlynguoidung.list;

import java.util.List;

import quanlynguoidung.list.UserListItem;


public class ListUsersViewModel extends ListUsersPublisher {
    public String message;
    public String timestamp;
    public boolean success;
    public List<UserListItem> users;
    public int totalCount;
    public int filteredCount;
}