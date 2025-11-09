package quanlynguoidung;

import java.util.Date;
import java.util.List;

import quanlynguoidung.get.UserViewItem;
import repository.DTO.UserDTO;

/**
 * Response Data chung cho tất cả use case
 */
public class QuanLyNguoiDungResponseData {
    public boolean success;
    public String message;
    public Date timestamp;
    
    // Data cho "get" action
    public UserViewItem user;
    
    // Data cho "list" action
    public List<UserDTO> users;
    public int totalCount;
    public int filteredCount;
}
