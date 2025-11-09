package quanlynguoidung.list;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import repository.ListUsersRepositoryGateway;
import repository.DTO.UserDTO;

public class ListUsersUseCase implements ListUsersInputBoundary {
    private final ListUsersRepositoryGateway repository;
    private final ListUsersOutputBoundary presenter;
    
    public ListUsersUseCase(ListUsersRepositoryGateway repository, ListUsersOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }
    
    @Override
    public void execute(ListUsersInputData input) {
        ListUsersOutput output = new ListUsersOutput();
        
        try {
            // 1. Lấy TẤT CẢ users từ repository
            List<UserDTO> allUsers = repository.findAll();
            output.totalCount = allUsers.size();
            
            // 2. Apply FILTER nếu có
            List<UserDTO> filteredUsers = filterUsers(allUsers, input.statusFilter);
            output.filteredCount = filteredUsers.size();
            
            // 3. Apply SORT nếu có
            List<UserDTO> sortedUsers = sortUsers(filteredUsers, input.sortBy, input.ascending);
            
            // 4. Convert DTO → UserInfoData
            output.users = convertToUserInfoDataList(sortedUsers);
            
            output.success = true;
            output.message = String.format(
                "Lấy danh sách thành công! Hiển thị %d/%d người dùng", 
                output.filteredCount, 
                output.totalCount
            );
            
            presenter.present(output);
            
        } catch (Exception e) {
            output.success = false;
            output.message = e.getMessage();
            presenter.present(output);
        }
    }
    
    // ============================================
    // FILTER LOGIC
    // ============================================
    private List<UserDTO> filterUsers(List<UserDTO> users, String statusFilter) {
        // Nếu không có filter → trả về tất cả
        if (statusFilter == null || statusFilter.isEmpty() || statusFilter.equals("all")) {
            return users;
        }
        
        // Filter theo status
        return users.stream()
            .filter(user -> statusFilter.equalsIgnoreCase(user.status))
            .collect(Collectors.toList());
    }
    
    // ============================================
    // SORT LOGIC
    // ============================================
    private List<UserDTO> sortUsers(List<UserDTO> users, String sortBy, boolean ascending) {
        // Nếu không có sortBy → trả về nguyên thứ tự
        if (sortBy == null || sortBy.isEmpty()) {
            return users;
        }
        
        // Tạo Comparator dựa trên sortBy
        Comparator<UserDTO> comparator = getComparator(sortBy);
        
        // Reverse nếu descending
        if (!ascending) {
            comparator = comparator.reversed();
        }
        
        return users.stream()
            .sorted(comparator)
            .collect(Collectors.toList());
    }
    
    private Comparator<UserDTO> getComparator(String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "fullname":
                return Comparator.comparing(u -> u.fullName);
            case "email":
                return Comparator.comparing(u -> u.email);
            case "username":
                return Comparator.comparing(u -> u.username);
            default:
                return Comparator.comparing(u -> u.fullName); // Default sort by fullName
        }
    }
    
    // ============================================
    // CONVERT DTO → UserInfoData
    // ============================================
    private UserInfoData convertToUserInfoData(UserDTO dto) {
        UserInfoData info = new UserInfoData();
        info.id = dto.id;
        info.username = dto.username;
        info.fullName = dto.fullName;
        info.email = dto.email;
        info.phone = dto.phone;
        info.address = dto.address;
        info.status = dto.status;
        return info;
    }
    
    private List<UserInfoData> convertToUserInfoDataList(List<UserDTO> dtos) {
        return dtos.stream()
            .map(this::convertToUserInfoData)
            .collect(Collectors.toList());
    }
}
