package adapters.quanlynguoidung.delete;

import quanlynguoidung.*;
import quanlynguoidung.delete.DeleteUserUseCase;

public class DeleteUserController implements QuanLyNguoiDungInputBoundary {
    private final DeleteUserUseCase useCase;
    
    public DeleteUserController(DeleteUserUseCase useCase) {
        this.useCase = useCase;
    }
    
    // Method cho GUI layer - nhận DTO
    public void executeWithDTO(DeleteUserInputDTO input) {
        QuanLyNguoiDungRequestData request = convertToRequestData(input);
        execute(request);
    }
    
    // Method implement từ interface - nhận RequestData
    @Override
    public void execute(QuanLyNguoiDungRequestData request) {
        useCase.control(request);
    }
    
    // Helper method để convert
    private QuanLyNguoiDungRequestData convertToRequestData(DeleteUserInputDTO input) {
        QuanLyNguoiDungRequestData request = new QuanLyNguoiDungRequestData();
        request.userId = input.userId;
        return request;
    }
}