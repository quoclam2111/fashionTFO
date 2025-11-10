package adapters.list;

import quanlynguoidung.QuanLyNguoiDungInputBoundary;
import quanlynguoidung.QuanLyNguoiDungRequestData;
import quanlynguoidung.list.ListUsersUseCase;

public class ListUsersController implements QuanLyNguoiDungInputBoundary {
    private final ListUsersUseCase control;
    
    public ListUsersController(ListUsersUseCase control) {
        this.control = control;
    }
    
    // Method cho GUI layer - nhận DTO
    public void executeWithDTO(ListUsersInputDTO dto) {
        QuanLyNguoiDungRequestData req = convertToRequestData(dto);
        execute(req);
    }
    
    // Method implement từ interface - nhận RequestData
    @Override
    public void execute(QuanLyNguoiDungRequestData req) {
        control.control(req);
    }
    
    // Helper method để convert
    private QuanLyNguoiDungRequestData convertToRequestData(ListUsersInputDTO dto) {
        QuanLyNguoiDungRequestData req = new QuanLyNguoiDungRequestData();
        req.statusFilter = dto.statusFilter != null ? dto.statusFilter : "all";
        req.sortBy = dto.sortBy != null ? dto.sortBy : "fullName";
        req.ascending = dto.ascending;
        return req;
    }
}