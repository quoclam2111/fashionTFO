package quanlydonhang;

public class QuanLyDonHangRequestData {
    public String action; // "add", "get", "list", "update", "cancel"

    // Data cho action "add"
    public String userId;
    public String customerName;
    public String customerPhone;
    public String customerAddress;
    public double totalAmount;
    public String note;

    // Data cho action "get"
    public String searchBy;    // "id", "userId", "phone"
    public String searchValue;

    // Data cho action "list"
    public String statusFilter; // "all", "pending", "confirmed", "shipping", "completed", "cancelled"
    public String sortBy;       // "orderDate", "totalAmount", "customerName"
    public boolean ascending;
    public String userIdFilter; // Lọc theo user cụ thể
}