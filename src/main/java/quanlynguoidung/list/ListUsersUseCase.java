package quanlynguoidung.list;

import quanlynguoidung.*;
import repository.ListUsersRepositoryGateway;
import repository.DTO.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ListUsersUseCase extends QuanLyNguoiDungControl {
    private final ListUsersRepositoryGateway repository;

    public ListUsersUseCase(ListUsersRepositoryGateway repository,
                            QuanLyNguoiDungOutputBoundary presenter) {
        super(presenter);
        this.response = new ResponseDataListUsers(); // Quan trọng: response phải là ResponseDataListUsers
        this.repository = repository;
    }

    @Override
    protected void execute(QuanLyNguoiDungRequestData request) {
        try {
            // 1. Lấy tất cả users (từ DB → DTO)
            List<UserDTO> allUsers = repository.findAll();

            // 2. Lọc theo trạng thái
            List<UserDTO> filteredUsers = filterByStatus(allUsers, request.statusFilter);

            // 3. Sắp xếp
            sortUsers(filteredUsers, request.sortBy, request.ascending);

            // 4. Chuyển UserDTO → UserListItem (Use Case model)
            List<UserListItem> items = filteredUsers.stream().map(dto -> {
                UserListItem item = new UserListItem();
                item.id = dto.id;
                item.username = dto.username;
                item.fullName = dto.fullName;
                item.email = dto.email;
                item.phone = dto.phone;
                item.status = dto.status;
                return item;
            }).collect(Collectors.toList());

            // 5. Set response
            ResponseDataListUsers res = (ResponseDataListUsers) this.response;
            res.success = true;
            res.message = "Lấy danh sách thành công!";
            res.users = items;
            res.totalCount = allUsers.size();
            res.filteredCount = items.size();

        } catch (Exception ex) {
            response.success = false;
            response.message = "Lỗi hệ thống: " + ex.getMessage();
        }
    }

    private List<UserDTO> filterByStatus(List<UserDTO> users, String statusFilter) {
        if (statusFilter == null || statusFilter.equals("all")) {
            return users;
        }
        return users.stream()
                .filter(u -> u.status.equals(statusFilter))
                .collect(Collectors.toList());
    }

    private void sortUsers(List<UserDTO> users, String sortBy, boolean ascending) {
        if (sortBy == null) return;

        users.sort((a, b) -> {
            int result = 0;
            switch (sortBy) {
                case "fullName":
                    result = a.fullName.compareTo(b.fullName);
                    break;
                case "email":
                    result = a.email.compareTo(b.email);
                    break;
                case "username":
                    result = a.username.compareTo(b.username);
                    break;
            }
            return ascending ? result : -result;
        });
    }
}
