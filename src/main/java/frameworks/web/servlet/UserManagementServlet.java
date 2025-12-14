package frameworks.web.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import adapters.quanlynguoidung.list.*;
import adapters.quanlynguoidung.get.*;
import adapters.quanlynguoidung.update.*;
import adapters.quanlynguoidung.delete.*;
import quanlynguoidung.list.*;
import quanlynguoidung.get.*;
import quanlynguoidung.update.*;
import quanlynguoidung.delete.*;
import repository.jdbc.UserRepoImpl;

@WebServlet("/admin/users")
public class UserManagementServlet extends HttpServlet {
    private ListUsersController listController;
    private ListUsersViewModel listViewModel;
    
    private GetUserController getUserController;
    private GetUserViewModel getUserViewModel;
    
    private UpdateUserController updateController;
    private UpdateUserViewModel updateViewModel;
    
    private DeleteUserController deleteController;
    private DeleteUserViewModel deleteViewModel;
    
    @Override
    public void init() throws ServletException {
        // Sử dụng UserRepoImpl của bạn
        UserRepoImpl repository = new UserRepoImpl();
        
        // List users
        listViewModel = new ListUsersViewModel();
        ListUsersPresenter listPresenter = new ListUsersPresenter(listViewModel);
        ListUsersUseCase listUseCase = new ListUsersUseCase(repository, listPresenter);
        listController = new ListUsersController(listUseCase);
        
        // Get user
        getUserViewModel = new GetUserViewModel();
        GetUserPresenter getUserPresenter = new GetUserPresenter(getUserViewModel);
        GetUserUseCase getUserUseCase = new GetUserUseCase(repository, getUserPresenter);
        getUserController = new GetUserController(getUserUseCase);
        
        // Update user
        updateViewModel = new UpdateUserViewModel();
        UpdateUserPresenter updatePresenter = new UpdateUserPresenter(updateViewModel);
        UpdateUserUseCase updateUseCase = new UpdateUserUseCase(repository, updatePresenter);
        updateController = new UpdateUserController(updateUseCase);
        
        // Delete user
        deleteViewModel = new DeleteUserViewModel();
        DeleteUserPresenter deletePresenter = new DeleteUserPresenter(deleteViewModel);
        DeleteUserUseCase deleteUseCase = new DeleteUserUseCase(repository, deletePresenter);
        deleteController = new DeleteUserController(deleteUseCase);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String role = (String) session.getAttribute("role");
        if ("CUSTOMER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/customer/dashboard");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("view".equals(action)) {
            handleViewUser(request, response);
        } else if ("edit".equals(action)) {
            handleEditForm(request, response);
        } else {
            handleListUsers(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        
        if ("update".equals(action)) {
            handleUpdateUser(request, response);
        } else if ("delete".equals(action)) {
            handleDeleteUser(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
    
    private void handleListUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        ListUsersInputDTO dto = new ListUsersInputDTO();
        dto.statusFilter = request.getParameter("status") != null ? 
                          request.getParameter("status") : "all";
        dto.sortBy = request.getParameter("sortBy") != null ? 
                    request.getParameter("sortBy") : "fullName";
        dto.ascending = !"desc".equals(request.getParameter("order"));
        
        listController.executeWithDTO(dto);
        
        request.setAttribute("viewModel", listViewModel);
        request.getRequestDispatcher("/WEB-INF/views/admin-users.jsp").forward(request, response);
    }
    
    private void handleViewUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String userId = request.getParameter("id");
        
        GetUserInputDTO dto = new GetUserInputDTO();
        dto.searchBy = "id";
        dto.searchValue = userId;
        
        getUserController.executeWithDTO(dto);
        
        request.setAttribute("viewModel", getUserViewModel);
        request.getRequestDispatcher("/WEB-INF/views/admin-user-detail.jsp").forward(request, response);
    }
    
    private void handleEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String userId = request.getParameter("id");
        
        GetUserInputDTO dto = new GetUserInputDTO();
        dto.searchBy = "id";
        dto.searchValue = userId;
        
        getUserController.executeWithDTO(dto);
        
        request.setAttribute("viewModel", getUserViewModel);
        request.getRequestDispatcher("/WEB-INF/views/admin-user-edit.jsp").forward(request, response);
    }
    
    private void handleUpdateUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        UpdateUserInputDTO dto = new UpdateUserInputDTO();
        dto.userId = request.getParameter("userId");
        dto.fullName = request.getParameter("fullName");
        dto.email = request.getParameter("email");
        dto.phone = request.getParameter("phone");
        dto.address = request.getParameter("address");
        dto.status = request.getParameter("status");
        
        // Chỉ cập nhật password nếu user nhập
        String password = request.getParameter("password");
        if (password != null && !password.trim().isEmpty()) {
            dto.password = password;
        }
        
        updateController.execute(dto);
        
        if (updateViewModel.success) {
            response.sendRedirect(request.getContextPath() + "/admin/users?action=view&id=" + dto.userId + "&success=true");
        } else {
            request.setAttribute("error", updateViewModel.message);
            request.setAttribute("viewModel", getUserViewModel);
            request.getRequestDispatcher("/WEB-INF/views/admin-user-edit.jsp").forward(request, response);
        }
    }
    
    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        DeleteUserInputDTO dto = new DeleteUserInputDTO();
        dto.userId = request.getParameter("userId");
        
        deleteController.executeWithDTO(dto);
        
        if (deleteViewModel.success) {
            response.sendRedirect(request.getContextPath() + "/admin/users?deleted=true");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=" + deleteViewModel.message);
        }
    }
}