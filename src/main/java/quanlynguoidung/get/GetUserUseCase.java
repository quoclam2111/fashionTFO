package quanlynguoidung.get;

import java.util.Optional;

import quanlynguoidung.list.UserInfoData;
import repository.GetUserRepositoryGateway;
import repository.DTO.UserDTO;

public class GetUserUseCase implements GetUserInputBoundary {
    private final GetUserRepositoryGateway repository;
    private final GetUserOutputBoundary presenter;
    
    public GetUserUseCase(GetUserRepositoryGateway repository, GetUserOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }
    
    @Override
    public void execute(GetUserInputData input) {
        GetUserOutputData output = new GetUserOutputData();
        
        try {
            // 1. Validate input
            if (input.searchValue == null || input.searchValue.trim().isEmpty()) {
                output.success = false;
                output.message = "SEARCH_VALUE_EMPTY";
                presenter.present(output);
                return;
            }
            
            if (!isValidSearchType(input.searchBy)) {
                output.success = false;
                output.message = "INVALID_SEARCH_TYPE";
                presenter.present(output);
                return;
            }
            
            // 2. Tìm user theo searchBy
            Optional<UserDTO> result = findUser(input.searchBy, input.searchValue);
            
            if (!result.isPresent()) {
                output.success = false;
                output.message = "USER_NOT_FOUND";
                presenter.present(output);
                return;
            }
            
            // 3. Convert DTO → UserInfoData
            output.user = convertToUserInfoData(result.get());
            output.success = true;
            output.message = "Lấy thông tin người dùng thành công!";
            presenter.present(output);
            
        } catch (Exception ex) {
            output.success = false;
            output.message = ex.getMessage();
            presenter.present(output);
        }
    }
    
    private Optional<UserDTO> findUser(String searchBy, String searchValue) {
        switch (searchBy.toLowerCase()) {
            case "id":
                return repository.findById(searchValue);
            case "username":
                return repository.findByUsername(searchValue);
            case "email":
                return repository.findByEmail(searchValue);
            case "phone":
                return repository.findByPhone(searchValue);
            default:
                return Optional.empty();
        }
    }
    
    private boolean isValidSearchType(String type) {
        return type != null && 
               (type.equalsIgnoreCase("id") || 
                type.equalsIgnoreCase("username") || 
                type.equalsIgnoreCase("email") || 
                type.equalsIgnoreCase("phone"));
    }
    
    private GetUserInfoData convertToUserInfoData(UserDTO dto) {
        GetUserInfoData info = new GetUserInfoData();
        info.id = dto.id;
        info.username = dto.username;
        info.fullName = dto.fullName;
        info.email = dto.email;
        info.phone = dto.phone;
        info.address = dto.address;
        info.status = dto.status;
        return info;
    }
}
