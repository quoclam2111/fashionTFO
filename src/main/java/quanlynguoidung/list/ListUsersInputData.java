package quanlynguoidung.list;

public class ListUsersInputData {
    public String statusFilter;  // "active", "inactive", "deleted", null = lấy tất cả
    public String sortBy;        // "fullName", "email", "username", null = không sort
    public boolean ascending;    // true = A-Z, false = Z-A
    
    public ListUsersInputData() {
        this.statusFilter = null;
        this.sortBy = null;
        this.ascending = true;
    }
    
    public ListUsersInputData(String statusFilter, String sortBy, boolean ascending) {
        this.statusFilter = statusFilter;
        this.sortBy = sortBy;
        this.ascending = ascending;
    }
    
}
