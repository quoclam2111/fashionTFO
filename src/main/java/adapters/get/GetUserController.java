package adapters.get;

import quanlynguoidung.*;
import quanlynguoidung.get.GetUserUseCase;

public class GetUserController implements QuanLyNguoiDungInputBoundary {
    private final GetUserUseCase useCase;
    
    public GetUserController(GetUserUseCase useCase) {
        this.useCase = useCase;
    }
    
    // Method cho GUI layer - nhận DTO
    public void executeWithDTO(GetUserInputDTO input) {
        QuanLyNguoiDungRequestData request = convertToRequestData(input);
        execute(request);
    }
    
    // Method implement từ interface - nhận RequestData
    @Override
    public void execute(QuanLyNguoiDungRequestData request) {
        useCase.control(request);
    }
    
    // Helper method để convert
    private QuanLyNguoiDungRequestData convertToRequestData(GetUserInputDTO input) {
        QuanLyNguoiDungRequestData request = new QuanLyNguoiDungRequestData();
        request.searchBy = input.searchBy;
        request.searchValue = input.searchValue;
        return request;
    }
}