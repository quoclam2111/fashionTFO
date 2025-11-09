package quanlynguoidung;

import java.util.Date;
import java.util.List;

import quanlynguoidung.list.UserInfoData;

/**
 * Response Data chung cho tất cả use case
 */
public class QuanLyNguoiDungResponseData {
    public boolean success;
    public String message;
    public Date timestamp;
    
    // Data cho "get" action
    public UserInfoData user;
    
    // Data cho "list" action
    public List<UserInfoData> users;
    public int totalCount;
    public int filteredCount;
}
