package quanlynguoidung.list;

import java.util.List;

public class ListUsersOutput {
    public boolean success;
    public String message;
    public List<UserInfoData> users;
    public int totalCount;      // Tổng số user
    public int filteredCount;   // Số user sau khi filter	
}
